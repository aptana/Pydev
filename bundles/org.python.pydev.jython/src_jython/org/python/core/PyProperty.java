package org.python.core;

public class PyProperty extends PyObject implements PyType.Newstyle {
    //~ BEGIN GENERATED REGION -- DO NOT EDIT SEE gexpose.py
    /* type info */

    public static final String exposed_name = "property";

    public static void typeSetup(PyObject dict, PyType.Newstyle marker) {
        dict.__setitem__("fget", new PyGetSetDescr("fget", PyProperty.class, "getFget", "setFget", null));
        dict.__setitem__("fset", new PyGetSetDescr("fset", PyProperty.class, "getFset", "setFset", null));
        dict.__setitem__("fdel", new PyGetSetDescr("fdel", PyProperty.class, "getFdel", "setFdel", null));
        dict.__setitem__("__doc__", new PyGetSetDescr("__doc__", PyProperty.class, "getDoc", "setDoc", null));
        class exposed___get__ extends PyBuiltinMethodNarrow {

            exposed___get__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___get__(self, info);
            }

            public PyObject __call__(PyObject arg0, PyObject arg1) {
                PyObject obj = (arg0 == Py.None) ? null : arg0;
                PyObject type = (arg1 == null) ? obj : arg1;
                return ((PyProperty) self).property___get__(obj, type);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject obj = (arg0 == Py.None) ? null : arg0;
                PyObject type = ((null) == null) ? obj : (null);
                return ((PyProperty) self).property___get__(obj, type);
            }

        }
        dict.__setitem__("__get__", new PyMethodDescr("__get__", PyProperty.class, 1, 2,
                new exposed___get__(null, null)));
        class exposed___set__ extends PyBuiltinMethodNarrow {

            exposed___set__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___set__(self, info);
            }

            public PyObject __call__(PyObject arg0, PyObject arg1) {
                ((PyProperty) self).property___set__(arg0, arg1);
                return Py.None;
            }

        }
        dict.__setitem__("__set__", new PyMethodDescr("__set__", PyProperty.class, 2, 2,
                new exposed___set__(null, null)));
        class exposed___delete__ extends PyBuiltinMethodNarrow {

            exposed___delete__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___delete__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                ((PyProperty) self).property___delete__(arg0);
                return Py.None;
            }

        }
        dict.__setitem__("__delete__", new PyMethodDescr("__delete__", PyProperty.class, 1, 1, new exposed___delete__(
                null, null)));
        class exposed___init__ extends PyBuiltinMethod {

            exposed___init__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___init__(self, info);
            }

            public PyObject __call__(PyObject[] args) {
                return __call__(args, Py.NoKeywords);
            }

            public PyObject __call__(PyObject[] args, String[] keywords) {
                ((PyProperty) self).property_init(args, keywords);
                return Py.None;
            }

        }
        dict.__setitem__("__init__", new PyMethodDescr("__init__", PyProperty.class, -1, -1, new exposed___init__(null,
                null)));
        dict.__setitem__("__new__", new PyNewWrapper(PyProperty.class, "__new__", -1, -1) {

            public PyObject new_impl(boolean init, PyType subtype, PyObject[] args, String[] keywords) {
                PyProperty newobj;
                if (for_type == subtype) {
                    newobj = new PyProperty();
                    if (init)
                        newobj.property_init(args, keywords);
                } else {
                    newobj = new PyPropertyDerived(subtype);
                }
                return newobj;
            }

        });
    }

    //~ END GENERATED REGION -- DO NOT EDIT SEE gexpose.py

    private static final PyType PROPERTYTYPE = PyType.fromClass(PyProperty.class);

    protected PyObject fget;
    protected PyObject fset;
    protected PyObject fdel;
    protected PyObject doc;

    public PyProperty() {
        this(PROPERTYTYPE);
    }

    public PyProperty(PyType subType) {
        super(subType);
    }

    public PyObject getDoc() {
        return doc;
    }

    public PyObject getFdel() {
        return fdel;
    }

    public PyObject getFset() {
        return fset;
    }

    public PyObject getFget() {
        return fget;
    }

    // These methods are to conform to test_descr.py
    // However I believe that this should be fixed through
    // PyGetSetDescr.java instead
    // Carlos Quiroz: 19.11.2005
    public void setFget(PyObject py) {
        throw Py.TypeError("readonly attribute");
    }

    public void setFset(PyObject py) {
        throw Py.TypeError("readonly attribute");
    }

    public void setFdel(PyObject py) {
        throw Py.TypeError("readonly attribute");
    }

    public void setDoc(PyObject py) {
        throw Py.TypeError("readonly attribute");
    }

    public void property_init(PyObject[] args, String[] keywords) {
        ArgParser argparse = new ArgParser("property", args, keywords, new String[] { "fget", "fset", "fdel", "doc" },
                0);
        fget = argparse.getPyObject(0, null);
        fget = fget == Py.None ? null : fget;
        fset = argparse.getPyObject(1, null);
        fset = fset == Py.None ? null : fset;
        fdel = argparse.getPyObject(2, null);
        fdel = fdel == Py.None ? null : fdel;
        doc = argparse.getPyObject(3, null);
    }

    public PyObject __call__(PyObject arg1, PyObject args[], String keywords[]) {
        return fget.__call__(arg1);
    }

    public PyObject __get__(PyObject obj, PyObject type) {
        return property___get__(obj, type);
    }

    final PyObject property___get__(PyObject obj, PyObject type) {
        if (obj == null || null == Py.None)
            return this;
        if (fget == null)
            throw Py.AttributeError("unreadable attribute");
        return fget.__call__(obj);
    }

    public void __set__(PyObject obj, PyObject value) {
        property___set__(obj, value);
    }

    final void property___set__(PyObject obj, PyObject value) {
        if (fset == null)
            throw Py.AttributeError("can't set attribute");
        fset.__call__(obj, value);
    }

    public void __delete__(PyObject obj) {
        property___delete__(obj);
    }

    final void property___delete__(PyObject obj) {
        if (fdel == null)
            throw Py.AttributeError("can't delete attribute");
        fdel.__call__(obj);
    }

}
