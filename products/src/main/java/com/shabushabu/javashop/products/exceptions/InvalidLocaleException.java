package com.shabushabu.javashop.products.exceptions;





public class InvalidLocaleException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidLocaleException() {
	}

	public InvalidLocaleException(String message) {
		super(message);
	}

	public InvalidLocaleException(Throwable cause) {
		super(cause);
	}

	public InvalidLocaleException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidLocaleException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
