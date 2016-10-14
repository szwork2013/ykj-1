package com.gnet.utils.sort.exception;

public class NotFoundOrderPropertyException extends RuntimeException{

	private static final long serialVersionUID = 5713481960634489780L;
	
	public NotFoundOrderPropertyException() {
	}
	
	public NotFoundOrderPropertyException(String msg) {
		super(msg);
	}
	
	public NotFoundOrderPropertyException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public NotFoundOrderPropertyException(Throwable t) {
		super(t);
	}

}
