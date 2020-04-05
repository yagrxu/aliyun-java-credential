package com.aliyun.credential.exception;

public class CredentialServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6661291164825914730L;

	public CredentialServiceException(String message) {
		super(message);
	}

	public CredentialServiceException(String message, Throwable ex) {
		super(message, ex);
	}

}
