package com.gnet.app.business;

import com.gnet.utils.sort.OrderType;

public enum BusinessOrderType implements OrderType{

	NAME("name", "name"),
	STORENUM("storeNum", "storeNum");
	
	private String key;
	private String value;

	private BusinessOrderType(String key, String value) {
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