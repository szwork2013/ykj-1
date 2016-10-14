package com.gnet.app.store;

import com.gnet.utils.sort.OrderType;

public enum StoreOrderType implements OrderType{

	NAME("name", "name");
	
	private String key;
	private String value;

	private StoreOrderType(String key, String value) {
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