package org.python.core;

public class PyEnumerate extends PyIterator {

    private long en_index; /* current index of enumeration */
    private PyObject en_sit; /* secondary iterator of enumeration */
    private PyTuple en_result; /* result tuple  */
    protected static PyObject __methods__;

    //~ BEGIN GENERATED REGION -- DO NOT EDIT SEE gexpose.py
    /* type info */

    public static final String exposed_name = "enumerate";

    public static final Class exposed_base = PyObject.class;

    public static void typeSetup(PyObject dict, PyType.Newstyle marker) {
        class exposed_next extends PyBuiltinMethodNarrow {

            exposed_next(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed_next(self, info);
            }

            public PyObject __call__() {
                return ((PyEnumerate) self).enumerate_next();
            }

        }
        dict.__setitem__("next", new PyMethodDescr("next", PyEnumerate.class, 0, 0, new exposed_next(null, null)));
        class exposed___iter__ extends PyBuiltinMethodNarrow {

            exposed___iter__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___iter__(self, info);
            }

            public PyObject __call__() {
                return ((PyEnumerate) self).enumerate___iter__();
            }

        }
        dict.__setitem__("__iter__", new PyMethodDescr("__iter__", PyEnumerate.class, 0, 0, new exposed___iter__(null,
                null)));
        dict.__setitem__("__new__", new PyNewWrapper(PyEnumerate.class, "__new__", -1, -1) {

            public PyObject new_impl(boolean init, PyType subtype, PyObject[] args, String[] keywords) {
                return enumerate_new(this, init, subtype, args, keywords);
            }

        });
    }

    //~ END GENERATED REGION -- DO NOT EDIT SEE gexpose.py

    public PyObject enumerate_next() {
        return next();
    }

    public PyObject enumerate___iter__() {
        return __iter__();
    }

    public static PyEnumerate enumerate_new(PyObject new_, boolean init, PyType subtype, PyObject[] args,
            String[] keywords) {
        if (args.length != 1) {
            throw PyBuiltinFunction.DefaultInfo.unexpectedCall(args.length, false, exposed_name, 0, 1);
        }
        return new PyEnumerate(args[0]);
    }

    public PyEnumerate(PyObject seq) {
        en_index = 0;
        en_sit = seq.__iter__();
    }

    public PyObject __iternext__() {
        PyObject next_item;
        PyObject next_index;

        next_item = en_sit.__iternext__();
        if (next_item == null) {
            if (en_sit instanceof PyIterator && ((PyIterator) en_sit).stopException != null) {
                stopException = ((PyIterator) en_sit).stopException;
            }
            return null;
        }
        next_index = new PyInteger((int) en_index);
        en_index++;

        en_result = new PyTuple(new PyObject[] { next_index, next_item });
        return en_result;
    }
}
