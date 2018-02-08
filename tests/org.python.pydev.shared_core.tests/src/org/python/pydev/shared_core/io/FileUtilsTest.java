package org.python.pydev.shared_core.io;

import java.io.File;
import java.io.FileFilter;

import junit.framework.TestCase;

public class FileUtilsTest extends TestCase {

    private File baseDir;

    @Override
    protected void setUp() throws Exception {
        baseDir = new File(FileUtils.getFileAbsolutePath(new File("FileUtilsTest.temporary_dir")));
        try {
            FileUtils.deleteDirectoryTree(baseDir);
        } catch (Exception e) {
            //ignore
        }
        if (baseDir.exists()) {
            throw new AssertionError("Not expecting: " + baseDir + " to exist.");
        }
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            FileUtils.deleteDirectoryTree(baseDir);
        } catch (Exception e) {
            //ignore
        }
    }

    public void testGetLastModifiedTime() throws Exception {
        baseDir.mkdir();
        File dir1 = new File(baseDir, "dir1");
        dir1.mkdir();
        File dir2 = new File(baseDir, "dir2");
        dir2.mkdir();

        File f1 = new File(dir1, "f1.py");
        FileUtils.writeStrToFile("test", f1);
        synchronized (this) {
            this.wait(50);
        }
        File f1a = new File(dir1, "f1a.txt");
        FileUtils.writeStrToFile("test", f1);
        synchronized (this) {
            this.wait(50);
        }
        File f2 = new File(dir2, "f2.txt");
        FileUtils.writeStrToFile("test", f2);

        FileFilter acceptAll = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return true;
            }
        };

        FileFilter acceptOnlyDir1 = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().equals("dir1");
            }
        };

        FileFilter acceptOnlyPy = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".py");
            }
        };

        FileFilter acceptOnlyTxt = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".txt");
            }
        };

        assertTrue(f1.lastModified() != f2.lastModified()); //if equal, this would invalidate the test!
        assertTrue(f1a.lastModified() != f1.lastModified()); //if equal, this would invalidate the test!
        assertTrue(f1a.lastModified() != f2.lastModified()); //if equal, this would invalidate the test!

        long lastModifiedTimeFromDir = FileUtils.getLastModifiedTimeFromDir(baseDir, acceptAll, acceptAll, 1000);
        assertEquals(lastModifiedTimeFromDir, f2.lastModified());

        lastModifiedTimeFromDir = FileUtils.getLastModifiedTimeFromDir(baseDir, acceptAll, acceptAll, 1);
        assertEquals(lastModifiedTimeFromDir, 0);

        lastModifiedTimeFromDir = FileUtils.getLastModifiedTimeFromDir(baseDir, acceptAll, acceptAll, 2);
        assertEquals(lastModifiedTimeFromDir, f2.lastModified());

        lastModifiedTimeFromDir = FileUtils.getLastModifiedTimeFromDir(baseDir, acceptAll, acceptOnlyDir1, 2);
        assertEquals(lastModifiedTimeFromDir, f1.lastModified());

        lastModifiedTimeFromDir = FileUtils.getLastModifiedTimeFromDir(baseDir, acceptOnlyPy, acceptAll, 2);
        assertEquals(lastModifiedTimeFromDir, f1.lastModified());

        lastModifiedTimeFromDir = FileUtils.getLastModifiedTimeFromDir(baseDir, acceptOnlyTxt, acceptOnlyDir1, 2);
        assertEquals(lastModifiedTimeFromDir, f1a.lastModified());
    }
}
