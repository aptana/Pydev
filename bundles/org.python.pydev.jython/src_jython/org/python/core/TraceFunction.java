// Copyright (c) Corporation for National Research Initiatives
package org.python.core;

public abstract class TraceFunction {
    public abstract TraceFunction traceCall(PyFrame frame);

    public abstract TraceFunction traceReturn(PyFrame frame, PyObject ret);

    public abstract TraceFunction traceLine(PyFrame frame, int line);

    public abstract TraceFunction traceException(PyFrame frame, PyException exc);
}
