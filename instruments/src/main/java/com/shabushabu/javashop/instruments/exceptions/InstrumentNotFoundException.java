package com.shabushabu.javashop.instruments.exceptions;

public class InstrumentNotFoundException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public InstrumentNotFoundException() {
    }

    public InstrumentNotFoundException(String message) {
        super(message);
    }

    public InstrumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
