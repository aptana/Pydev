// Copyright (c) Corporation for National Research Initiatives
package org.python.core;

import java.io.Serializable;

/**
 * A builtin python int.
 */
public class PyInteger extends PyObject {
    //~ BEGIN GENERATED REGION -- DO NOT EDIT SEE gexpose.py
    /* type info */

    public static final String exposed_name = "int";

    public static void typeSetup(PyObject dict, PyType.Newstyle marker) {
        class exposed___abs__ extends PyBuiltinMethodNarrow {

            exposed___abs__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___abs__(self, info);
            }

            public PyObject __call__() {
                return ((PyInteger) self).int___abs__();
            }

        }
        dict.__setitem__("__abs__",
                new PyMethodDescr("__abs__", PyInteger.class, 0, 0, new exposed___abs__(null, null)));
        class exposed___float__ extends PyBuiltinMethodNarrow {

            exposed___float__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___float__(self, info);
            }

            public PyObject __call__() {
                return ((PyInteger) self).int___float__();
            }

        }
        dict.__setitem__("__float__", new PyMethodDescr("__float__", PyInteger.class, 0, 0, new exposed___float__(null,
                null)));
        class exposed___hex__ extends PyBuiltinMethodNarrow {

            exposed___hex__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___hex__(self, info);
            }

            public PyObject __call__() {
                return ((PyInteger) self).int___hex__();
            }

        }
        dict.__setitem__("__hex__",
                new PyMethodDescr("__hex__", PyInteger.class, 0, 0, new exposed___hex__(null, null)));
        class exposed___int__ extends PyBuiltinMethodNarrow {

            exposed___int__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___int__(self, info);
            }

            public PyObject __call__() {
                return ((PyInteger) self).int___int__();
            }

        }
        dict.__setitem__("__int__",
                new PyMethodDescr("__int__", PyInteger.class, 0, 0, new exposed___int__(null, null)));
        class exposed___invert__ extends PyBuiltinMethodNarrow {

            exposed___invert__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___invert__(self, info);
            }

            public PyObject __call__() {
                return ((PyInteger) self).int___invert__();
            }

        }
        dict.__setitem__("__invert__", new PyMethodDescr("__invert__", PyInteger.class, 0, 0, new exposed___invert__(
                null, null)));
        class exposed___long__ extends PyBuiltinMethodNarrow {

            exposed___long__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___long__(self, info);
            }

            public PyObject __call__() {
                return ((PyInteger) self).int___long__();
            }

        }
        dict.__setitem__("__long__", new PyMethodDescr("__long__", PyInteger.class, 0, 0, new exposed___long__(null,
                null)));
        class exposed___neg__ extends PyBuiltinMethodNarrow {

            exposed___neg__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___neg__(self, info);
            }

            public PyObject __call__() {
                return ((PyInteger) self).int___neg__();
            }

        }
        dict.__setitem__("__neg__",
                new PyMethodDescr("__neg__", PyInteger.class, 0, 0, new exposed___neg__(null, null)));
        class exposed___oct__ extends PyBuiltinMethodNarrow {

            exposed___oct__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___oct__(self, info);
            }

            public PyObject __call__() {
                return ((PyInteger) self).int___oct__();
            }

        }
        dict.__setitem__("__oct__",
                new PyMethodDescr("__oct__", PyInteger.class, 0, 0, new exposed___oct__(null, null)));
        class exposed___pos__ extends PyBuiltinMethodNarrow {

            exposed___pos__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___pos__(self, info);
            }

            public PyObject __call__() {
                return ((PyInteger) self).int___pos__();
            }

        }
        dict.__setitem__("__pos__",
                new PyMethodDescr("__pos__", PyInteger.class, 0, 0, new exposed___pos__(null, null)));
        class exposed___add__ extends PyBuiltinMethodNarrow {

            exposed___add__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___add__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___add__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__add__",
                new PyMethodDescr("__add__", PyInteger.class, 1, 1, new exposed___add__(null, null)));
        class exposed___and__ extends PyBuiltinMethodNarrow {

            exposed___and__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___and__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___and__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__and__",
                new PyMethodDescr("__and__", PyInteger.class, 1, 1, new exposed___and__(null, null)));
        class exposed___div__ extends PyBuiltinMethodNarrow {

            exposed___div__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___div__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___div__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__div__",
                new PyMethodDescr("__div__", PyInteger.class, 1, 1, new exposed___div__(null, null)));
        class exposed___divmod__ extends PyBuiltinMethodNarrow {

            exposed___divmod__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___divmod__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___divmod__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__divmod__", new PyMethodDescr("__divmod__", PyInteger.class, 1, 1, new exposed___divmod__(
                null, null)));
        class exposed___floordiv__ extends PyBuiltinMethodNarrow {

            exposed___floordiv__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___floordiv__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___floordiv__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__floordiv__", new PyMethodDescr("__floordiv__", PyInteger.class, 1, 1,
                new exposed___floordiv__(null, null)));
        class exposed___lshift__ extends PyBuiltinMethodNarrow {

            exposed___lshift__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___lshift__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___lshift__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__lshift__", new PyMethodDescr("__lshift__", PyInteger.class, 1, 1, new exposed___lshift__(
                null, null)));
        class exposed___mod__ extends PyBuiltinMethodNarrow {

            exposed___mod__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___mod__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___mod__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__mod__",
                new PyMethodDescr("__mod__", PyInteger.class, 1, 1, new exposed___mod__(null, null)));
        class exposed___mul__ extends PyBuiltinMethodNarrow {

            exposed___mul__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___mul__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___mul__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__mul__",
                new PyMethodDescr("__mul__", PyInteger.class, 1, 1, new exposed___mul__(null, null)));
        class exposed___or__ extends PyBuiltinMethodNarrow {

            exposed___or__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___or__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___or__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__or__", new PyMethodDescr("__or__", PyInteger.class, 1, 1, new exposed___or__(null, null)));
        class exposed___radd__ extends PyBuiltinMethodNarrow {

            exposed___radd__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___radd__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___radd__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__radd__", new PyMethodDescr("__radd__", PyInteger.class, 1, 1, new exposed___radd__(null,
                null)));
        class exposed___rdiv__ extends PyBuiltinMethodNarrow {

            exposed___rdiv__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rdiv__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rdiv__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rdiv__", new PyMethodDescr("__rdiv__", PyInteger.class, 1, 1, new exposed___rdiv__(null,
                null)));
        class exposed___rfloordiv__ extends PyBuiltinMethodNarrow {

            exposed___rfloordiv__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rfloordiv__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rfloordiv__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rfloordiv__", new PyMethodDescr("__rfloordiv__", PyInteger.class, 1, 1,
                new exposed___rfloordiv__(null, null)));
        class exposed___rmod__ extends PyBuiltinMethodNarrow {

            exposed___rmod__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rmod__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rmod__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rmod__", new PyMethodDescr("__rmod__", PyInteger.class, 1, 1, new exposed___rmod__(null,
                null)));
        class exposed___rmul__ extends PyBuiltinMethodNarrow {

            exposed___rmul__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rmul__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rmul__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rmul__", new PyMethodDescr("__rmul__", PyInteger.class, 1, 1, new exposed___rmul__(null,
                null)));
        class exposed___rshift__ extends PyBuiltinMethodNarrow {

            exposed___rshift__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rshift__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rshift__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rshift__", new PyMethodDescr("__rshift__", PyInteger.class, 1, 1, new exposed___rshift__(
                null, null)));
        class exposed___rsub__ extends PyBuiltinMethodNarrow {

            exposed___rsub__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rsub__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rsub__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rsub__", new PyMethodDescr("__rsub__", PyInteger.class, 1, 1, new exposed___rsub__(null,
                null)));
        class exposed___rtruediv__ extends PyBuiltinMethodNarrow {

            exposed___rtruediv__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rtruediv__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rtruediv__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rtruediv__", new PyMethodDescr("__rtruediv__", PyInteger.class, 1, 1,
                new exposed___rtruediv__(null, null)));
        class exposed___sub__ extends PyBuiltinMethodNarrow {

            exposed___sub__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___sub__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___sub__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__sub__",
                new PyMethodDescr("__sub__", PyInteger.class, 1, 1, new exposed___sub__(null, null)));
        class exposed___truediv__ extends PyBuiltinMethodNarrow {

            exposed___truediv__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___truediv__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___truediv__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__truediv__", new PyMethodDescr("__truediv__", PyInteger.class, 1, 1,
                new exposed___truediv__(null, null)));
        class exposed___xor__ extends PyBuiltinMethodNarrow {

            exposed___xor__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___xor__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___xor__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__xor__",
                new PyMethodDescr("__xor__", PyInteger.class, 1, 1, new exposed___xor__(null, null)));
        class exposed___rxor__ extends PyBuiltinMethodNarrow {

            exposed___rxor__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rxor__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rxor__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rxor__", new PyMethodDescr("__rxor__", PyInteger.class, 1, 1, new exposed___rxor__(null,
                null)));
        class exposed___rrshift__ extends PyBuiltinMethodNarrow {

            exposed___rrshift__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rrshift__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rrshift__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rrshift__", new PyMethodDescr("__rrshift__", PyInteger.class, 1, 1,
                new exposed___rrshift__(null, null)));
        class exposed___ror__ extends PyBuiltinMethodNarrow {

            exposed___ror__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___ror__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___ror__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__ror__",
                new PyMethodDescr("__ror__", PyInteger.class, 1, 1, new exposed___ror__(null, null)));
        class exposed___rand__ extends PyBuiltinMethodNarrow {

            exposed___rand__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rand__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rand__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rand__", new PyMethodDescr("__rand__", PyInteger.class, 1, 1, new exposed___rand__(null,
                null)));
        class exposed___rpow__ extends PyBuiltinMethodNarrow {

            exposed___rpow__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rpow__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rpow__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rpow__", new PyMethodDescr("__rpow__", PyInteger.class, 1, 1, new exposed___rpow__(null,
                null)));
        class exposed___rlshift__ extends PyBuiltinMethodNarrow {

            exposed___rlshift__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rlshift__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rlshift__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rlshift__", new PyMethodDescr("__rlshift__", PyInteger.class, 1, 1,
                new exposed___rlshift__(null, null)));
        class exposed___rdivmod__ extends PyBuiltinMethodNarrow {

            exposed___rdivmod__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___rdivmod__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___rdivmod__(arg0);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__rdivmod__", new PyMethodDescr("__rdivmod__", PyInteger.class, 1, 1,
                new exposed___rdivmod__(null, null)));
        class exposed___cmp__ extends PyBuiltinMethodNarrow {

            exposed___cmp__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___cmp__(self, info);
            }

            public PyObject __call__(PyObject arg0) {
                int ret = ((PyInteger) self).int___cmp__(arg0);
                if (ret == -2) {
                    throw Py.TypeError("int" + ".__cmp__(x,y) requires y to be '" + "int" + "', not a '"
                            + (arg0).getType().fastGetName() + "'");
                }
                return Py.newInteger(ret);
            }

        }
        dict.__setitem__("__cmp__",
                new PyMethodDescr("__cmp__", PyInteger.class, 1, 1, new exposed___cmp__(null, null)));
        class exposed___pow__ extends PyBuiltinMethodNarrow {

            exposed___pow__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___pow__(self, info);
            }

            public PyObject __call__(PyObject arg0, PyObject arg1) {
                PyObject ret = ((PyInteger) self).int___pow__(arg0, arg1);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

            public PyObject __call__(PyObject arg0) {
                PyObject ret = ((PyInteger) self).int___pow__(arg0, null);
                if (ret == null)
                    return Py.NotImplemented;
                return ret;
            }

        }
        dict.__setitem__("__pow__",
                new PyMethodDescr("__pow__", PyInteger.class, 1, 2, new exposed___pow__(null, null)));
        class exposed___nonzero__ extends PyBuiltinMethodNarrow {

            exposed___nonzero__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___nonzero__(self, info);
            }

            public PyObject __call__() {
                return Py.newBoolean(((PyInteger) self).int___nonzero__());
            }

        }
        dict.__setitem__("__nonzero__", new PyMethodDescr("__nonzero__", PyInteger.class, 0, 0,
                new exposed___nonzero__(null, null)));
        class exposed___reduce__ extends PyBuiltinMethodNarrow {

            exposed___reduce__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___reduce__(self, info);
            }

            public PyObject __call__() {
                return ((PyInteger) self).int___reduce__();
            }

        }
        dict.__setitem__("__reduce__", new PyMethodDescr("__reduce__", PyInteger.class, 0, 0, new exposed___reduce__(
                null, null)));
        class exposed___repr__ extends PyBuiltinMethodNarrow {

            exposed___repr__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___repr__(self, info);
            }

            public PyObject __call__() {
                return new PyString(((PyInteger) self).int_toString());
            }

        }
        dict.__setitem__("__repr__", new PyMethodDescr("__repr__", PyInteger.class, 0, 0, new exposed___repr__(null,
                null)));
        class exposed___str__ extends PyBuiltinMethodNarrow {

            exposed___str__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___str__(self, info);
            }

            public PyObject __call__() {
                return new PyString(((PyInteger) self).int_toString());
            }

        }
        dict.__setitem__("__str__",
                new PyMethodDescr("__str__", PyInteger.class, 0, 0, new exposed___str__(null, null)));
        class exposed___hash__ extends PyBuiltinMethodNarrow {

            exposed___hash__(PyObject self, PyBuiltinFunction.Info info) {
                super(self, info);
            }

            public PyBuiltinFunction bind(PyObject self) {
                return new exposed___hash__(self, info);
            }

            public PyObject __call__() {
                return Py.newInteger(((PyInteger) self).int_hashCode());
            }

        }
        dict.__setitem__("__hash__", new PyMethodDescr("__hash__", PyInteger.class, 0, 0, new exposed___hash__(null,
                null)));
        dict.__setitem__("__new__", new PyNewWrapper(PyInteger.class, "__new__", -1, -1) {

            public PyObject new_impl(boolean init, PyType subtype, PyObject[] args, String[] keywords) {
                return int_new(this, init, subtype, args, keywords);
            }

        });
    }

    //~ END GENERATED REGION -- DO NOT EDIT SEE gexpose.py

    public static PyObject int_new(PyNewWrapper new_, boolean init, PyType subtype, PyObject[] args, String[] keywords) {
        ArgParser ap = new ArgParser("int", args, keywords, new String[] { "x", "base" }, 0);
        PyObject x = ap.getPyObject(0, null);
        int base = ap.getInt(1, -909);
        if (new_.for_type == subtype) {
            if (x == null) {
                return Py.Zero;
            }
            if (base == -909) {
                return asPyInteger(x);
            }
            if (!(x instanceof PyString)) {
                throw Py.TypeError("int: can't convert non-string with explicit base");
            }
            return Py.newInteger(((PyString) x).atoi(base));
        } else {
            if (x == null) {
                return new PyIntegerDerived(subtype, 0);
            }
            if (base == -909) {
                PyObject intOrLong = asPyInteger(x);
                if (intOrLong instanceof PyInteger) {
                    return new PyIntegerDerived(subtype, ((PyInteger) intOrLong).getValue());
                } else {
                    throw Py.OverflowError("long int too large to convert to int");
                }
            }
            if (!(x instanceof PyString)) {
                throw Py.TypeError("int: can't convert non-string with explicit base");
            }
            return new PyIntegerDerived(subtype, ((PyString) x).atoi(base));
        }
    } // xxx

    /**
     * @return the result of x.__int__ 
     * @throws Py.Type error if x.__int__ throws an Py.AttributeError
     */
    private static PyObject asPyInteger(PyObject x) {
        try {
            return x.__int__();
        } catch (PyException pye) {
            if (!Py.matchException(pye, Py.AttributeError))
                throw pye;
            throw Py.TypeError("int() argument must be a string or a number");
        }
    }

    private static final PyType INTTYPE = PyType.fromClass(PyInteger.class);

    private int value;

    public PyInteger(PyType subType, int v) {
        super(subType);
        value = v;
    }

    public PyInteger(int v) {
        this(INTTYPE, v);
    }

    public int getValue() {
        return value;
    }

    public String safeRepr() throws PyIgnoreMethodTag {
        return "'int' object";
    }

    public String toString() {
        return int_toString();
    }

    final String int_toString() {
        return Integer.toString(getValue());
    }

    public int hashCode() {
        return int_hashCode();
    }

    final int int_hashCode() {
        return getValue();
    }

    private static void err_ovf(String msg) {
        try {
            Py.OverflowWarning(msg);
        } catch (PyException exc) {
            if (Py.matchException(exc, Py.OverflowWarning))
                throw Py.OverflowError(msg);
        }
    }

    public boolean __nonzero__() {
        return int___nonzero__();
    }

    final boolean int___nonzero__() {
        return getValue() != 0;
    }

    public Object __tojava__(Class c) {
        if (c == Integer.TYPE || c == Number.class || c == Object.class || c == Integer.class
                || c == Serializable.class) {
            return new Integer(getValue());
        }

        if (c == Boolean.TYPE || c == Boolean.class)
            return new Boolean(getValue() != 0);
        if (c == Byte.TYPE || c == Byte.class)
            return new Byte((byte) getValue());
        if (c == Short.TYPE || c == Short.class)
            return new Short((short) getValue());

        if (c == Long.TYPE || c == Long.class)
            return new Long(getValue());
        if (c == Float.TYPE || c == Float.class)
            return new Float(getValue());
        if (c == Double.TYPE || c == Double.class)
            return new Double(getValue());
        return super.__tojava__(c);
    }

    public int __cmp__(PyObject other) {
        return int___cmp__(other);
    }

    final int int___cmp__(PyObject other) {
        if (!canCoerce(other))
            return -2;
        int v = coerce(other);
        return getValue() < v ? -1 : getValue() > v ? 1 : 0;
    }

    public Object __coerce_ex__(PyObject other) {
        if (other instanceof PyInteger)
            return other;
        else
            return Py.None;
    }

    private static final boolean canCoerce(PyObject other) {
        return other instanceof PyInteger;
    }

    private static final int coerce(PyObject other) {
        if (other instanceof PyInteger)
            return ((PyInteger) other).getValue();
        else
            throw Py.TypeError("xxx");
    }

    public PyObject __add__(PyObject right) {
        return int___add__(right);
    }

    final PyObject int___add__(PyObject right) {
        if (!canCoerce(right))
            return null;
        int rightv = coerce(right);
        int a = getValue();
        int b = rightv;
        int x = a + b;
        if ((x ^ a) >= 0 || (x ^ b) >= 0)
            return Py.newInteger(x);
        err_ovf("integer addition");
        return new PyLong((long) a + (long) b);
    }

    public PyObject __radd__(PyObject left) {
        return int___radd__(left);
    }

    final PyObject int___radd__(PyObject left) {
        return __add__(left);
    }

    private static PyObject _sub(int a, int b) {
        int x = a - b;
        if ((x ^ a) >= 0 || (x ^ ~b) >= 0)
            return Py.newInteger(x);
        err_ovf("integer subtraction");
        return new PyLong((long) a - (long) b);
    }

    public PyObject __sub__(PyObject right) {
        return int___sub__(right);
    }

    final PyObject int___sub__(PyObject right) {
        if (!canCoerce(right))
            return null;
        return _sub(getValue(), coerce(right));
    }

    public PyObject __rsub__(PyObject left) {
        return int___rsub__(left);
    }

    final PyObject int___rsub__(PyObject left) {
        if (!canCoerce(left))
            return null;
        return _sub(coerce(left), getValue());
    }

    public PyObject __mul__(PyObject right) {
        return int___mul__(right);
    }

    final PyObject int___mul__(PyObject right) {
        if (right instanceof PySequence)
            return ((PySequence) right).repeat(getValue());

        if (!canCoerce(right))
            return null;
        int rightv = coerce(right);

        double x = (double) getValue();
        x *= rightv;
        //long x = ((long)getValue())*((PyInteger)right).getValue();
        //System.out.println("mul: "+this+" * "+right+" = "+x);

        if (x <= Integer.MAX_VALUE && x >= Integer.MIN_VALUE)
            return Py.newInteger((int) x);
        err_ovf("integer multiplication");
        return __long__().__mul__(right);
    }

    public PyObject __rmul__(PyObject left) {
        return int___rmul__(left);
    }

    final PyObject int___rmul__(PyObject left) {
        return __mul__(left);
    }

    // Getting signs correct for integer division
    // This convention makes sense when you consider it in tandem with modulo
    private static int divide(int x, int y) {
        if (y == 0)
            throw Py.ZeroDivisionError("integer division or modulo by zero");

        if (y == -1 && x < 0 && x == -x) {
            err_ovf("integer division: " + x + " + " + y);
        }
        int xdivy = x / y;
        int xmody = x - xdivy * y;
        /* If the signs of x and y differ, and the remainder is non-0,
         * C89 doesn't define whether xdivy is now the floor or the
         * ceiling of the infinitely precise quotient.  We want the floor,
         * and we have it iff the remainder's sign matches y's.
         */
        if (xmody != 0 && ((y ^ xmody) < 0) /* i.e. and signs differ */) {
            xmody += y;
            --xdivy;
            //assert(xmody && ((y ^ xmody) >= 0));
        }
        return xdivy;
    }

    public PyObject __div__(PyObject right) {
        return int___div__(right);
    }

    final PyObject int___div__(PyObject right) {
        if (!canCoerce(right))
            return null;
        if (Options.divisionWarning > 0)
            Py.warning(Py.DeprecationWarning, "classic int division");
        return Py.newInteger(divide(getValue(), coerce(right)));
    }

    public PyObject __rdiv__(PyObject left) {
        return int___rdiv__(left);
    }

    final PyObject int___rdiv__(PyObject left) {
        if (!canCoerce(left))
            return null;
        if (Options.divisionWarning > 0)
            Py.warning(Py.DeprecationWarning, "classic int division");
        return Py.newInteger(divide(coerce(left), getValue()));
    }

    public PyObject __floordiv__(PyObject right) {
        return int___floordiv__(right);
    }

    final PyObject int___floordiv__(PyObject right) {
        if (!canCoerce(right))
            return null;
        return Py.newInteger(divide(getValue(), coerce(right)));
    }

    public PyObject __rfloordiv__(PyObject left) {
        return int___rfloordiv__(left);
    }

    final PyObject int___rfloordiv__(PyObject left) {
        if (!canCoerce(left))
            return null;
        return Py.newInteger(divide(coerce(left), getValue()));
    }

    public PyObject __truediv__(PyObject right) {
        return int___truediv__(right);
    }

    final PyObject int___truediv__(PyObject right) {
        if (right instanceof PyInteger)
            return __float__().__truediv__(right);
        else if (right instanceof PyLong)
            return int___long__().__truediv__(right);
        else
            return null;
    }

    public PyObject __rtruediv__(PyObject left) {
        return int___rtruediv__(left);
    }

    final PyObject int___rtruediv__(PyObject left) {
        if (left instanceof PyInteger)
            return left.__float__().__truediv__(this);
        else if (left instanceof PyLong)
            return left.__truediv__(int___long__());
        else
            return null;
    }

    private static int modulo(int x, int y, int xdivy) {
        return x - xdivy * y;
    }

    public PyObject __mod__(PyObject right) {
        return int___mod__(right);
    }

    final PyObject int___mod__(PyObject right) {
        if (!canCoerce(right))
            return null;
        int rightv = coerce(right);
        int v = getValue();
        return Py.newInteger(modulo(v, rightv, divide(v, rightv)));
    }

    public PyObject __rmod__(PyObject left) {
        return int___rmod__(left);
    }

    final PyObject int___rmod__(PyObject left) {
        if (!canCoerce(left))
            return null;
        int leftv = coerce(left);
        int v = getValue();
        return Py.newInteger(modulo(leftv, v, divide(leftv, v)));
    }

    public PyObject __divmod__(PyObject right) {
        return int___divmod__(right);
    }

    final PyObject int___divmod__(PyObject right) {
        if (!canCoerce(right))
            return null;
        int rightv = coerce(right);

        int v = getValue();
        int xdivy = divide(v, rightv);
        return new PyTuple(new PyObject[] { Py.newInteger(xdivy), Py.newInteger(modulo(v, rightv, xdivy)) });
    }

    final PyObject int___rdivmod__(PyObject left) {
        if (!canCoerce(left))
            return null;
        int leftv = coerce(left);

        int v = getValue();
        int xdivy = divide(leftv, v);
        return new PyTuple(new PyObject[] { Py.newInteger(xdivy), Py.newInteger(modulo(leftv, v, xdivy)) });
    }

    public PyObject __pow__(PyObject right, PyObject modulo) {
        return int___pow__(right, modulo);
    }

    final PyObject int___pow__(PyObject right, PyObject modulo) {
        if (!canCoerce(right))
            return null;

        if (modulo != null && !canCoerce(modulo))
            return null;

        return _pow(getValue(), coerce(right), modulo, this, right);
    }

    public PyObject __rpow__(PyObject left, PyObject modulo) {
        if (!canCoerce(left))
            return null;

        if (modulo != null && !canCoerce(modulo))
            return null;

        return _pow(coerce(left), getValue(), modulo, left, this);
    }

    final PyObject int___rpow__(PyObject left) {
        return __rpow__(left, null);
    }

    private static PyObject _pow(int value, int pow, PyObject modulo, PyObject left, PyObject right) {
        int mod = 0;
        long tmp = value;
        boolean neg = false;
        if (tmp < 0) {
            tmp = -tmp;
            neg = (pow & 0x1) != 0;
        }
        long result = 1;

        if (pow < 0) {
            if (value != 0)
                return left.__float__().__pow__(right, modulo);
            else
                throw Py.ZeroDivisionError("cannot raise 0 to a " + "negative power");
        }

        if (modulo != null) {
            mod = coerce(modulo);
            if (mod == 0) {
                throw Py.ValueError("pow(x, y, z) with z==0");
            }
        }

        // Standard O(ln(N)) exponentiation code
        while (pow > 0) {
            if ((pow & 0x1) != 0) {
                result *= tmp;
                if (mod != 0) {
                    result %= (long) mod;
                }

                if (result > Integer.MAX_VALUE) {
                    err_ovf("integer exponentiation");
                    return left.__long__().__pow__(right, modulo);
                }
            }
            pow >>= 1;
            if (pow == 0)
                break;
            tmp *= tmp;

            if (mod != 0) {
                tmp %= (long) mod;
            }

            if (tmp > Integer.MAX_VALUE) {
                err_ovf("integer exponentiation");
                return left.__long__().__pow__(right, modulo);
            }
        }

        int ret = (int) result;
        if (neg)
            ret = -ret;

        // Cleanup result of modulo
        if (mod != 0) {
            ret = modulo(ret, mod, divide(ret, mod));
        }
        return Py.newInteger(ret);
    }

    public PyObject __lshift__(PyObject right) {
        return int___lshift__(right);
    }

    final PyObject int___lshift__(PyObject right) {
        int rightv;
        if (right instanceof PyInteger)
            rightv = ((PyInteger) right).getValue();
        else if (right instanceof PyLong)
            return int___long__().__lshift__(right);
        else
            return null;

        if (rightv > 31)
            return Py.newInteger(0);
        else if (rightv < 0)
            throw Py.ValueError("negative shift count");
        return Py.newInteger(getValue() << rightv);
    }

    final PyObject int___rlshift__(PyObject left) {
        int leftv;
        if (left instanceof PyInteger)
            leftv = ((PyInteger) left).getValue();
        else if (left instanceof PyLong)
            return left.__rlshift__(int___long__());
        else
            return null;

        if (getValue() > 31)
            return Py.newInteger(0);
        else if (getValue() < 0)
            throw Py.ValueError("negative shift count");
        return Py.newInteger(leftv << getValue());
    }

    public PyObject __rshift__(PyObject right) {
        return int___rshift__(right);
    }

    final PyObject int___rshift__(PyObject right) {
        int rightv;
        if (right instanceof PyInteger)
            rightv = ((PyInteger) right).getValue();
        else if (right instanceof PyLong)
            return int___long__().__rshift__(right);
        else
            return null;

        if (rightv < 0)
            throw Py.ValueError("negative shift count");

        return Py.newInteger(getValue() >> rightv);
    }

    final PyObject int___rrshift__(PyObject left) {
        int leftv;
        if (left instanceof PyInteger)
            leftv = ((PyInteger) left).getValue();
        else if (left instanceof PyLong)
            return left.__rshift__(int___long__());
        else
            return null;

        if (getValue() < 0)
            throw Py.ValueError("negative shift count");

        return Py.newInteger(leftv >> getValue());
    }

    public PyObject __and__(PyObject right) {
        return int___and__(right);
    }

    final PyObject int___and__(PyObject right) {
        int rightv;
        if (right instanceof PyInteger)
            rightv = ((PyInteger) right).getValue();
        else if (right instanceof PyLong)
            return int___long__().__and__(right);
        else
            return null;

        return Py.newInteger(getValue() & rightv);
    }

    final PyObject int___rand__(PyObject left) {
        return int___and__(left);
    }

    public PyObject __xor__(PyObject right) {
        return int___xor__(right);
    }

    final PyObject int___xor__(PyObject right) {
        int rightv;
        if (right instanceof PyInteger)
            rightv = ((PyInteger) right).getValue();
        else if (right instanceof PyLong)
            return int___long__().__xor__(right);
        else
            return null;

        return Py.newInteger(getValue() ^ rightv);
    }

    final PyObject int___rxor__(PyObject left) {
        int leftv;
        if (left instanceof PyInteger)
            leftv = ((PyInteger) left).getValue();
        else if (left instanceof PyLong)
            return left.__rxor__(int___long__());
        else
            return null;

        return Py.newInteger(leftv ^ getValue());
    }

    public PyObject __or__(PyObject right) {
        return int___or__(right);
    }

    final PyObject int___or__(PyObject right) {
        int rightv;
        if (right instanceof PyInteger)
            rightv = ((PyInteger) right).getValue();
        else if (right instanceof PyLong)
            return int___long__().__or__(right);
        else
            return null;

        return Py.newInteger(getValue() | rightv);
    }

    final PyObject int___ror__(PyObject left) {
        return int___or__(left);
    }

    public PyObject __neg__() {
        return int___neg__();
    }

    final PyObject int___neg__() {
        int x = -getValue();
        if (getValue() < 0 && x < 0)
            err_ovf("integer negation");
        return Py.newInteger(x);
    }

    public PyObject __pos__() {
        return int___pos__();
    }

    final PyObject int___pos__() {
        return Py.newInteger(getValue());
    }

    public PyObject __abs__() {
        return int___abs__();
    }

    final PyObject int___abs__() {
        if (getValue() >= 0)
            return Py.newInteger(getValue());
        else
            return __neg__();
    }

    public PyObject __invert__() {
        return int___invert__();
    }

    final PyObject int___invert__() {
        return Py.newInteger(~getValue());
    }

    public PyObject __int__() {
        return int___int__();
    }

    final PyInteger int___int__() {
        return Py.newInteger(getValue());
    }

    public PyLong __long__() {
        return int___long__();
    }

    final PyLong int___long__() {
        return new PyLong(getValue());
    }

    public PyFloat __float__() {
        return int___float__();
    }

    final PyFloat int___float__() {
        return new PyFloat((double) getValue());
    }

    public PyComplex __complex__() {
        return new PyComplex((double) getValue(), 0.);
    }

    public PyString __oct__() {
        return int___oct__();
    }

    final PyString int___oct__() {
        if (getValue() < 0) {
            return new PyString("0" + Long.toString(0x100000000l + (long) getValue(), 8));
        } else if (getValue() > 0) {
            return new PyString("0" + Integer.toString(getValue(), 8));
        } else
            return new PyString("0");
    }

    public PyString __hex__() {
        return int___hex__();
    }

    final PyString int___hex__() {
        if (getValue() < 0) {
            return new PyString("0x" + Long.toString(0x100000000l + (long) getValue(), 16));
        } else {
            return new PyString("0x" + Integer.toString(getValue(), 16));
        }
    }

    public boolean isMappingType() {
        return false;
    }

    public boolean isSequenceType() {
        return false;
    }

    public long asLong(int index) throws PyObject.ConversionException {
        return getValue();
    }

    public int asInt(int index) throws PyObject.ConversionException {
        return getValue();
    }

    /**
     * Used for pickling.
     *
     * @return a tuple of (class, (Integer))
     */
    public PyObject __reduce__() {
        return int___reduce__();
    }

    final PyObject int___reduce__() {
        return new PyTuple(new PyObject[] { getType(), new PyTuple(new PyObject[] { Py.newInteger(getValue()) }) });
    }
}
