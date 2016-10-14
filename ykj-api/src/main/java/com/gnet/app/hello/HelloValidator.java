package com.gnet.app.hello;

import org.apache.commons.lang3.StringUtils;

public class HelloValidator {

	private HelloValidator(){}
	
	static boolean validateBeforeCreateHello(Hello hello) {
		if (StringUtils.isBlank(hello.getName())) {
			return false;
		}
		return true;
	}
	
}