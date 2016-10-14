package com.gnet.app.clerk;

import org.springframework.security.core.AuthenticationException;

public class ClerkNotFoundException extends AuthenticationException {

	private static final long serialVersionUID = 8052088450463136449L;

	public ClerkNotFoundException(String msg) {
		super(msg);
	}
	
	public ClerkNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}

}
