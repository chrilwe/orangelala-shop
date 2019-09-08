package com.orangelala.framework.common.base;

public class BaseException extends RuntimeException {
	public BaseException() {
		super();
	}
	
	public BaseException(String message) {
		super(message);
	}
	
	public BaseException(Throwable cause) {
		super(cause);
	}
	
	public BaseException(String message,Throwable cause) {
		super(message,cause);
	}
}
