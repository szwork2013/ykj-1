package com.gnet.app.hello;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class HelloErrorBuilder extends BaseErrorBuilder {
	
	static final Integer ERROR_CREATED_ERROR = 10001;

	public HelloErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}
