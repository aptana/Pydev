/**
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Eclipse Public License (EPL).
 * Please see the license.txt included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
/*
 * Created on Sep 14, 2005
 * 
 * @author Fabio Zadrozny
 */
package org.python.pydev.parser;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Schedules Python parsing events. Each ParserScheduler instance has a timer
 * thread on which all parsing is performed.
 * 
 * The public methods of this class are thread-safe.
 */
public class ParserScheduler {
    // The scheduler thread can perform the following transitions:
    //     WAITING => PARSE_SCHEDULED
    //     PARSING => PARSING_WITH_RESCHEDULE
    //
    // The parsing task timer thread can perform the following transitions:
    //     WAITING => PARSING
    //     PARSE_SCHEDULED => PARSING
    //     PARSING => WAITING
    //     PARSING_WITH_RESCHEDULE => PARSE_SCHEDULED
    private enum State {
        WAITING,                  // Waiting for a parse directive
        PARSE_SCHEDULED,          // A parse has been scheduled for the future
        PARSING,                  // Currently in a parse
        PARSING_WITH_RESCHEDULE,  // In a parse, with auto-reschedule
    }
    
    private PyParser parser;
    private Timer timer;
    private State state;
    private long scheduleInterval;

    /**
     * @param parser
     *            The main Python parser for this scheduler. Must not be null.
     */
    public ParserScheduler(PyParser parser) {
        super();
        assert parser != null;
        this.parser = parser;
        this.timer = new Timer(true);
        this.state = State.WAITING;
        this.scheduleInterval = PyParserManager.getPyParserManager(null)
                .getElapseMillisBeforeAnalysis();
    }
    
    /**
     * The same as parseNow(false) .
     */
    public void parseNow() {
        parseNow(false);
    }
    
    /**
     * Initiate a parse. If no parse is currently happening, and none is
     * scheduled, a parse is performed immediately.
     * 
     * @param force
     *            If true, and a parse is currently happening, simply returns
     *            false. Otherwise, begins a parse immediately, even if a parse
     *            is already scheduled. If false, and a parse is currently
     *            happening, schedules another parse after it is done.
     *            Otherwise, if a parse is scheduled, does nothing (i.e. waits
     *            for that scheduled parse).
     * @param argsToReparse
     *            The arguments passed to the PyParser's reparseDocument().
     * @return false if we asked a forced parse and it will not be scheduled
     *         because a parse is already in action.
     */
    public synchronized boolean parseNow(boolean force, Object ... argsToReparse) {
        // This method does not modify state, although calls to scheduleParse()
        // may do so.
        if (state == State.WAITING) {
            scheduleParse(0, argsToReparse);
        } else if (!force) {
            if (state == State.PARSING) {
                scheduleParse(scheduleInterval, argsToReparse);
            } else {
                // PARSE_SCHEDULED or PARSING_WITH_RESCHEDULE; just wait for the
                // next scheduled event.
            }
        } else {
            if (state == State.PARSING
                    || state == State.PARSING_WITH_RESCHEDULE) {
                return false;
            } else {
                // PARSE_SCHEDULED; kick of an "out of band" parse immediately.
                // The previously scheduled parse becomes a "dangling" parse,
                // which nevertheless should be well-behaved.
                scheduleParse(0, argsToReparse);
            }
        }
        return true;
    }
    
    public synchronized void parseLater() {
        if (state != State.PARSE_SCHEDULED && state != State.PARSING_WITH_RESCHEDULE) {
            scheduleParse(scheduleInterval);
        }
    }
    
    private synchronized void scheduleParse(long delay, Object... args) {
        // State transitions:
        //     WAITING => PARSE_SCHEDULED
        //     PARSING => PARSING_WITH_RESCHEDULE
        if (state == State.WAITING) {
            state = State.PARSE_SCHEDULED;
        } else if (state == State.PARSING){
            state = State.PARSING_WITH_RESCHEDULE;
        }
        
        // A shared PyParser instance is passed into the task's constructor.
        // This should be ok, because the tasks are serialized on the Timer
        // thread.
        this.timer.schedule(new ParseTask(this, this.parser, args), delay);
    }
    
    /**
     * To be called back by the parsing task that wants to start parsing. The
     * return value indicates whether the parse should proceed.
     */
    private synchronized boolean notifyParseIntent() {
        // State transitions:
        //     WAITING => PARSING
        //     PARSE_SCHEDULED => PARSING
        if (state == State.WAITING || state == State.PARSE_SCHEDULED) {
            state = State.PARSING;
            return true;
        }
        return false;
    }
    
    /**
     * To be called back by the parsing task when a parse has been completed.
     */
    private synchronized void notifyParseComplete() {
        // State transitions:
        //     PARSING => WAITING
        //     PARSING_WITH_RESCHEDULE => PARSE_SCHEDULED
        if (state == State.PARSING) {
            state = State.WAITING;
        } else if (state == State.PARSING_WITH_RESCHEDULE) {
            state = State.PARSE_SCHEDULED;
        }
    }
    
    /**
     * A parsing task that notifies the scheduler of parsing events.
     */
    private static class ParseTask extends TimerTask {
        private ParserScheduler scheduler;
        private PyParser parser;
        private Object[] args;

        public ParseTask(ParserScheduler scheduler, PyParser parser,
                Object[] args) {
            this.scheduler = scheduler;
            this.parser = parser;
            this.args = args;
        }
        
        public void run() {
            if (this.scheduler.notifyParseIntent()) {
                this.parser.reparseDocument(args);
                this.scheduler.notifyParseComplete();
            }
        }
    }
    
    public void dispose() {
        this.parser = null;
        this.timer.cancel();
    }
}
