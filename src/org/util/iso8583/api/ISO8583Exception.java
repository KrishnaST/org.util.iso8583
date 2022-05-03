package org.util.iso8583.api;

public final class ISO8583Exception extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public final ISO8583ExceptionCause cause;

	public ISO8583Exception(final ISO8583ExceptionCause cause) {
		this.cause = cause;
	}

	public ISO8583Exception(final ISO8583ExceptionCause cause, String message) {
		super(message);
		this.cause = cause;
	}

	public ISO8583Exception(final ISO8583ExceptionCause cause, Throwable throwable) {
		super(throwable);
		this.cause = cause;
	}

	public ISO8583Exception(final ISO8583ExceptionCause cause, String message, Throwable throwable) {
		super(message, throwable);
		this.cause = cause;
	}

	public ISO8583Exception(final ISO8583ExceptionCause cause, String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
		this.cause = cause;
	}

}
