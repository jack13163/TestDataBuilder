package com.testDataBuilder.exception;

public class BaseException extends Exception {

    public BaseException() {
        super();
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String typeName, String message) {
        super("Role name=" + typeName + " MSG:" + message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

}
