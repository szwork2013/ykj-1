package com.gnet.app.clerk;

import com.gnet.utils.sort.OrderType;

public enum ClerkOrderType implements OrderType{

	NAME("name", "name");
	
	private String key;
	private String value;

	private ClerkOrderType(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
}