package com.ristoLuik.app.library.exception;

public class ParserException extends Exception {
    public ParserException(String message) {
        super(message);
    }

    public ParserException(String message, Throwable e) {
        super(message, e);
    }
}
