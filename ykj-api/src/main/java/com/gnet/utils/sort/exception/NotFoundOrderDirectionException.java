package com.gnet.utils.sort.exception;

public class NotFoundOrderDirectionException extends RuntimeException{

	private static final long serialVersionUID = 5131155854414559220L;
	
	public NotFoundOrderDirectionException() {
	}
	
	public NotFoundOrderDirectionException(String msg) {
		super(msg);
	}
	
	public NotFoundOrderDirectionException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public NotFoundOrderDirectionException(Throwable t) {
		super(t);
	}
}
