from pydevd_comm import * #@UnusedWildImport
from pydevd_constants import * #@UnusedWildImport
import traceback #@Reimport
import os.path
basename = os.path.basename

#=======================================================================================================================
# PyDBFrame
#=======================================================================================================================
class PyDBFrame:
    '''This makes the tracing for a given frame, so, the trace_dispatch
    is used initially when we enter into a new context ('call') and then
    is reused for the entire context.
    '''
    
    def __init__(self, args):
        #args = mainDebugger, filename, base, info, t, frame
        #yeap, much faster than putting in self and then getting it from self later on
        self._args = args[:-1]
    
    def setSuspend(self, *args, **kwargs):
        self._args[0].setSuspend(*args, **kwargs)
        
    def doWaitSuspend(self, *args, **kwargs):
        self._args[0].doWaitSuspend(*args, **kwargs)
    
    def trace_exception(self, frame, event, arg):
        if event == 'exception':
            mainDebugger = self._args[0]
            if not mainDebugger.break_on_caught:
                return None
            
            handle_exceptions = mainDebugger.handle_exceptions
            if handle_exceptions is not None and issubclass(arg[0], handle_exceptions):
                self.handle_exception(frame, event, arg)
                mainDebugger.SetTraceForFrameAndParents(frame)
                return self.trace_dispatch
        return self.trace_exception
    
    
    def handle_exception(self, frame, event, arg):
        thread = self._args[3]
        self.setSuspend(thread, CMD_STEP_INTO)
        self.doWaitSuspend(thread, frame, event, arg)
    
    
    def trace_dispatch(self, frame, event, arg):
        if event not in ('line', 'call', 'return'):
            if event == 'exception':
                mainDebugger = self._args[0]
                if mainDebugger.break_on_caught and issubclass(arg[0], mainDebugger.handle_exceptions):
                    self.handle_exception(frame, event, arg)
                    return self.trace_dispatch
            else:
                #I believe this can only happen in jython on some frontiers on jython and java code, which we don't want to trace.
                return None 
            
        mainDebugger, filename, info, thread = self._args
        
        breakpoint = mainDebugger.breakpoints.get(filename)
        
        
        if info.pydev_state == STATE_RUN:
            #we can skip if:
            #- we have no stop marked
            #- we should make a step return/step over and we're not in the current frame
            can_skip = (info.pydev_step_cmd is None and info.pydev_step_stop is None)\
            or (info.pydev_step_cmd in (CMD_STEP_RETURN, CMD_STEP_OVER) and info.pydev_step_stop is not frame) 
        else:
            can_skip = False
            
        # Let's check to see if we are in a function that has a breakpoint. If we don't have a breakpoint, 
        # we will return nothing for the next trace
        #also, after we hit a breakpoint and go to some other debugging state, we have to force the set trace anyway,
        #so, that's why the additional checks are there.
        if not breakpoint:
            if can_skip:
                if mainDebugger.break_on_caught:
                    return self.trace_exception
                else:
                    return None

        else:
            #checks the breakpoint to see if there is a context match in some function
            curr_func_name = frame.f_code.co_name
            
            #global context is set with an empty name
            if curr_func_name in ('?', '<module>'):
                curr_func_name = ''
                
            for _b, condition, func_name in breakpoint.values(): #jython does not support itervalues()
                #will match either global or some function
                if func_name in ('None', curr_func_name):
                    break
                
            else: # if we had some break, it won't get here (so, that's a context that we want to skip)
                if can_skip:
                    #print 'skipping', frame.f_lineno, info.pydev_state, info.pydev_step_stop, info.pydev_step_cmd
                    if mainDebugger.break_on_caught:
                        return self.trace_exception
                    else:
                        return None
                
        #We may have hit a breakpoint or we are already in step mode. Either way, let's check what we should do in this frame
        #print 'NOT skipped', frame.f_lineno, frame.f_code.co_name

        
        try:
            line = frame.f_lineno
            
            #return is not taken into account for breakpoint hit because we'd have a double-hit in this case
            #(one for the line and the other for the return).
            if event != 'return' and info.pydev_state != STATE_SUSPEND and breakpoint is not None \
                and DictContains(breakpoint, line):
                
                #ok, hit breakpoint, now, we have to discover if it is a conditional breakpoint
                # lets do the conditional stuff here
                condition = breakpoint[line][1]

                if condition is not None:
                    try:
                        val = eval(condition, frame.f_globals, frame.f_locals)
                        if not val:
                            return self.trace_dispatch
                            
                    except:
                        sys.stderr.write('Error while evaluating expression\n')
                        traceback.print_exc()
                        return self.trace_dispatch
                
                self.setSuspend(thread, CMD_SET_BREAK)
                
            # if thread has a suspend flag, we suspend with a busy wait
            if info.pydev_state == STATE_SUSPEND:
                self.doWaitSuspend(thread, frame, event, arg)
                return self.trace_dispatch
            
        except:
            traceback.print_exc()
            raise
        
        #step handling. We stop when we hit the right frame
        try:
            
            if info.pydev_step_cmd == CMD_STEP_INTO:
                
                stop = event in ('line', 'return')
                    
            elif info.pydev_step_cmd == CMD_STEP_OVER:
                
                stop = info.pydev_step_stop is frame and event in ('line', 'return')
            
            elif info.pydev_step_cmd == CMD_STEP_RETURN:
                
                stop = event == 'return' and info.pydev_step_stop is frame
            
            elif info.pydev_step_cmd == CMD_RUN_TO_LINE or info.pydev_step_cmd == CMD_SET_NEXT_STATEMENT:
                stop = False
                if event == 'line' or event == 'exception':
                    #Yes, we can only act on line events (weird hum?)
                    #Note: This code is duplicated at pydevd.py
                    #Acting on exception events after debugger breaks with exception
                    curr_func_name = frame.f_code.co_name
                    
                    #global context is set with an empty name
                    if curr_func_name in ('?', '<module>'):
                        curr_func_name = ''
                    
                    if curr_func_name == info.pydev_func_name:
                        line = info.pydev_next_line
                        if frame.f_lineno == line:
                            stop = True
                        else:
                            if frame.f_trace is None:
                                frame.f_trace = self.trace_dispatch
                            frame.f_lineno = line
                            frame.f_trace = None
                            stop = True
            
            else:
                stop = False

            if stop:
                #event is always == line or return at this point
                if event == 'line':
                    self.setSuspend(thread, info.pydev_step_cmd)
                    self.doWaitSuspend(thread, frame, event, arg)
                else: #return event
                    back = frame.f_back
                    if back is not None:
                        
                        #When we get to the pydevd run function, the debugging has actually finished for the main thread
                        #(note that it can still go on for other threads, but for this one, we just make it finish)
                        #So, just setting it to None should be OK
                        base = basename(back.f_code.co_filename)
                        if base == 'pydevd.py' and back.f_code.co_name == 'run':
                            back = None
                            
                        elif base == 'pydevd_traceproperty.py':
                            # We dont want to trace the return event of pydevd_traceproperty (custom property for debugging)
                            #if we're in a return, we want it to appear to the user in the previous frame!
                            return None

                    if back is not None:
                        self.setSuspend(thread, info.pydev_step_cmd)
                        self.doWaitSuspend(thread, back, event, arg)
                    else:
                        #in jython we may not have a back frame
                        info.pydev_step_stop = None
                        info.pydev_step_cmd = None
                        info.pydev_state = STATE_RUN
                
                    
        except:
            traceback.print_exc()
            info.pydev_step_cmd = None
        
        #if we are quitting, let's stop the tracing
        retVal = None
        if not mainDebugger.quitting:
            retVal = self.trace_dispatch

        return retVal
    
    if USE_PSYCO_OPTIMIZATION:
        try:
            import psyco
            trace_dispatch = psyco.proxy(trace_dispatch)
        except ImportError:
            if hasattr(sys, 'exc_clear'): #jython does not have it
                sys.exc_clear() #don't keep the traceback
            pass #ok, psyco not available
