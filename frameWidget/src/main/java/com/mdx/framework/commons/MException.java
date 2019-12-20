package com.mdx.framework.commons;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


public class MException extends Exception {
    private static final long serialVersionUID = 1L;
    private int code = 0;

    public MException(int errorcode, String msg) {
        super(msg);
        this.code = errorcode;
    }

    public MException(Exception e) {
        super(e);
    }

    public MException(int errorcode) {
        this.code = errorcode;
    }

    public MException(int errorcode, Exception e) {
        super(e);
        this.code = errorcode;
    }

    public int getCode() {
        return this.code;
    }
}
