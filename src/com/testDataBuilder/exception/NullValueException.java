package com.testDataBuilder.exception;

public class NullValueException extends BaseException {

    public NullValueException() {
        super();
    }

    public NullValueException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullValueException(String message) {
        super(message);
    }

    public NullValueException(Throwable cause) {
        super(cause);
    }

}
