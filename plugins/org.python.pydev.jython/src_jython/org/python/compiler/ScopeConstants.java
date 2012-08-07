package org.python.compiler;

public interface ScopeConstants {

    public final static int BOUND = 1;
    public final static int NGLOBAL = 2; // func scope expl global
    public final static int PARAM = 4;
    public final static int FROM_PARAM = 8;
    public final static int CELL = 16;
    public final static int FREE = 32;
    public final static int CLASS_GLOBAL = 64; // class scope expl global
    public final static int GLOBAL = NGLOBAL | CLASS_GLOBAL; // all global

    public final static int TOPSCOPE = 0;
    public final static int FUNCSCOPE = 1;
    public final static int CLASSSCOPE = 2;

}
