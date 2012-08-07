/*******************************************************************************
 *  Copyright (c) 2007, 2008 aQute and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 * 
 *  Contributors:
 *  aQute - initial implementation and ideas 
 *  IBM Corporation - initial adaptation to Equinox provisioning use
 *******************************************************************************/
package org.python.pydev.core.path_watch;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.python.pydev.core.ListenerList;
import org.python.pydev.core.log.Log;

class DirectoryWatcher {

    private class WatcherThread extends Thread {

        private final long pollFrequency;
        private boolean done = false;

        private WatcherThread(long frequency) {
            super("Directory Watcher"); //$NON-NLS-1$
            this.pollFrequency = frequency;
        }

        public void run() {
            do {
                try {
                    poll();
                    synchronized (this) {
                        wait(pollFrequency);
                    }
                } catch (InterruptedException e) {
                    // ignore
                } catch (Throwable e) {
                    Log.log(e);
                    done = true;
                }
            } while (!done);
        }

        private synchronized void done() {
            done = true;
            notify();
        }
    }

    /**
     * 10 seconds
     */
    private static final long DEFAULT_POLL_FREQUENCY = 10000;

    private final File[] directories;
    private ListenerList<DirectoryChangeListener> listeners = new ListenerList<DirectoryChangeListener>(
            DirectoryChangeListener.class);
    private Set<File> scannedFiles = new HashSet<File>();
    private Set<File> removals;
    private WatcherThread watcher;
    private boolean watchSubdirs;

    DirectoryWatcher(File directory, boolean watchSubtree) {
        if (directory == null) {
            throw new IllegalArgumentException("Null folder");
        }

        this.directories = new File[] { directory };
        this.watchSubdirs = watchSubtree;
    }

    synchronized void addListener(DirectoryChangeListener listener) {
        listeners.add(listener);
    }

    synchronized void removeListener(DirectoryChangeListener listener) {
        listeners.remove(listener);
    }

    void start() {
        start(DEFAULT_POLL_FREQUENCY);
    }

    private synchronized void poll() {
        startPoll();
        scanDirectories();
        stopPoll();
    }

    private synchronized void start(final long pollFrequency) {
        if (watcher != null) {
            throw new IllegalStateException("Thread already started");
        }

        watcher = new WatcherThread(pollFrequency);
        watcher.start();
    }

    synchronized void stop() {
        if (watcher == null) {
            throw new IllegalStateException("Unable to stop (thread not started)");
        }

        watcher.done();
        watcher = null;
    }

    synchronized void dispose() {
        if (watcher != null) {
            stop();
        }
        if (listeners != null) {
            for (DirectoryChangeListener listener : listeners.getListeners()) {
                removeListener(listener);
            }
        }
    }

    private void startPoll() {
        removals = scannedFiles;
        scannedFiles = new HashSet<File>();
        for (DirectoryChangeListener l : listeners.getListeners()) {
            l.startPoll();
        }
    }

    private void scanDirectories() {
        for (int index = 0; index < directories.length; index++) {
            File directory = directories[index];
            scanDirectoryRecursively(directory);
        }
    }

    private void scanDirectoryRecursively(File directory) {
        if (directory == null) {
            return;
        }
        File list[] = directory.listFiles();
        if (list == null) {
            return;
        }
        for (int i = 0; i < list.length; i++) {
            File file = list[i];

            // remember that we saw the file and remove it from this list of files to be
            // removed at the end. Then notify all the listeners as needed.
            scannedFiles.add(file);
            removals.remove(file);
            for (DirectoryChangeListener listener : listeners.getListeners()) {
                if (isInterested(listener, file))
                    processFile(file, listener);
            }
            if (watchSubdirs && file.isDirectory()) {
                scanDirectoryRecursively(file);
            }
        }
    }

    private void stopPoll() {
        notifyRemovals();
        removals = scannedFiles;
        for (DirectoryChangeListener l : listeners.getListeners()) {
            l.stopPoll();
        }
    }

    private boolean isInterested(DirectoryChangeListener listener, File file) {
        return listener.isInterested(file);
    }

    /**
     * Notify the listeners of the files that have been deleted or marked for deletion.
     */
    private void notifyRemovals() {
        Set<File> removed = removals;
        for (DirectoryChangeListener listener : listeners.getListeners()) {
            for (Iterator<File> j = removed.iterator(); j.hasNext();) {
                File file = j.next();
                if (isInterested(listener, file)) {
                    listener.removed(file);
                }
            }
        }
    }

    private void processFile(File file, DirectoryChangeListener listener) {
        try {
            Long oldTimestamp = listener.getSeenFile(file);
            if (oldTimestamp == null) {
                // The file is new
                listener.added(file);
            } else {
                // The file is not new but may have changed
                long lastModified = file.lastModified();
                if (oldTimestamp.longValue() != lastModified) {
                    listener.changed(file);
                }
            }
        } catch (Exception e) {
            Log.log(e);
        }
    }

}
