'''
@author Fabio Zadrozny 
'''
import sys
import unittest
import socket
import urllib


IS_JYTHON = sys.platform.find('java') != -1

if IS_JYTHON:
    import os
    
    #make it as if we were executing from the directory above this one (so that we can use jycompletionserver
    #without the need for it being in the pythonpath)
    sys.argv[0] = os.path.dirname(sys.argv[0]) 
    #twice the dirname to get the previous level from this file.
    sys.path.insert(1, os.path.join(os.path.dirname(sys.argv[0])))
    
    import pycompletionserver as jycompletionserver
    
    
    DEBUG = 0

def dbg(s):
    if DEBUG:
        sys.stdout.write('TEST %s\n' % s)

class Test(unittest.TestCase):

    def setUp(self):
        unittest.TestCase.setUp(self)

    def tearDown(self):
        unittest.TestCase.tearDown(self)
    
    def testIt(self):
        dbg('ok')
        
    def testMessage(self):
        t = jycompletionserver.T(0, 0)
        
        l = []
        l.append(('Def', 'description'  , 'args'))
        l.append(('Def1', 'description1', 'args1'))
        l.append(('Def2', 'description2', 'args2'))
        
        msg = t.processor.formatCompletionMessage('test_jyserver.py', l)
        
        self.assertEquals('@@COMPLETIONS(test_jyserver.py,(Def,description,args),(Def1,description1,args1),(Def2,description2,args2))END@@', msg)
        
        l = []
        l.append(('Def', 'desc,,r,,i()ption', ''))
        l.append(('Def(1', 'descriptio(n1', ''))
        l.append(('De,f)2', 'de,s,c,ription2', ''))
        msg = t.processor.formatCompletionMessage(None, l)
        expected = '@@COMPLETIONS(None,(Def,desc%2C%2Cr%2C%2Ci%28%29ption, ),(Def%281,descriptio%28n1, ),(De%2Cf%292,de%2Cs%2Cc%2Cription2, ))END@@'
        
        self.assertEquals(expected, msg)






    def testCompletionSocketsAndMessages(self):
        dbg('testCompletionSocketsAndMessages')
        t, sToWrite, sToRead, self.connToRead, addr = self.createConnections()
        dbg('connections created')
        
        try:
            #now that we have the connections all set up, check the code completion messages.
            msg = urllib.quote_plus('math')

            toWrite = '@@IMPORTS:%sEND@@' % msg
            dbg('writing' + str(toWrite))
            sToWrite.send(toWrite) #math completions
            completions = self.readMsg()
            dbg(urllib.unquote_plus(completions))
            
            start = '@@COMPLETIONS('
            self.assert_(completions.startswith(start), '%s DOESNT START WITH %s' % (completions, start))
            self.assert_(completions.find('@@COMPLETIONS') != -1)
            self.assert_(completions.find('END@@') != -1)


            msg = urllib.quote_plus('__builtin__.str')
            toWrite = '@@IMPORTS:%sEND@@' % msg
            dbg('writing' + str(toWrite))
            sToWrite.send(toWrite) #math completions
            completions = self.readMsg()
            dbg(urllib.unquote_plus(completions))
            
            start = '@@COMPLETIONS('
            self.assert_(completions.startswith(start), '%s DOESNT START WITH %s' % (completions, start))
            self.assert_(completions.find('@@COMPLETIONS') != -1)
            self.assert_(completions.find('END@@') != -1)


        
        finally:
            try:
                self.sendKillMsg(sToWrite)
                
        
                while not hasattr(t, 'ended'):
                    pass #wait until it receives the message and quits.
        
                    
                sToRead.close()
                sToWrite.close()
                self.connToRead.close()
            except:
                pass




    def createConnections(self, p1=50002, p2=50003):
        '''
        Creates the connections needed for testing.
        '''
        t = jycompletionserver.T(p1, p2)
        
        t.start()

        sToWrite = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sToWrite.connect((jycompletionserver.HOST, p1))
        
        sToRead = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sToRead.bind((jycompletionserver.HOST, p2))
        sToRead.listen(1) #socket to receive messages.

        connToRead, addr = sToRead.accept()

        return t, sToWrite, sToRead, connToRead, addr
        

    def readMsg(self):
        msg = '@@PROCESSING_END@@'
        while msg.startswith('@@PROCESSING'):
            msg = self.connToRead.recv(1024)
            if msg.startswith('@@PROCESSING:'):
                dbg('Status msg:' + str(msg))
        
        while msg.find('END@@') == -1:
            msg += self.connToRead.recv(1024)
        
        return msg
        
    def sendKillMsg(self, socket):
        socket.send(jycompletionserver.MSG_KILL_SERVER)
        
    


#"C:\Program Files\Java\jdk1.5.0_04\bin\java.exe" -Dpython.path="C:\bin\jython21\Lib";"C:\bin\jython21";"C:\Program Files\Java\jdk1.5.0_04\jre\lib\rt.jar" -classpath C:/bin/jython21/jython.jar org.python.util.jython D:\eclipse_workspace\org.python.pydev\pysrc\pycompletionserver.py 53795 58659
#
#"C:\Program Files\Java\jdk1.5.0_04\bin\java.exe" -Dpython.path="C:\bin\jython21\Lib";"C:\bin\jython21";"C:\Program Files\Java\jdk1.5.0_04\jre\lib\rt.jar" -classpath C:/bin/jython21/jython.jar org.python.util.jython D:\eclipse_workspace\org.python.pydev\pysrc\tests\test_jyserver.py
#
#"C:\Program Files\Java\jdk1.5.0_04\bin\java.exe" -Dpython.path="C:\bin\jython21\Lib";"C:\bin\jython21";"C:\Program Files\Java\jdk1.5.0_04\jre\lib\rt.jar" -classpath C:/bin/jython21/jython.jar org.python.util.jython d:\runtime-workbench-workspace\jython_test\src\test.py        
if __name__ == '__main__':
    if IS_JYTHON:
        suite = unittest.makeSuite(Test)
        unittest.TextTestRunner(verbosity=1).run(suite)
    else:
        sys.stdout.write('Not running jython tests for non-java platform: %s' % sys.platform)

