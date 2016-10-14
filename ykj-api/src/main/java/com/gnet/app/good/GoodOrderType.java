package com.gnet.app.good;

import com.gnet.utils.sort.OrderType;

public enum GoodOrderType implements OrderType{

	NAME("name", "name");
	
	private String key;
	private String value;

	private GoodOrderType(String key, String value) {
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