package com.gnet.app.customer;

import com.gnet.utils.sort.OrderType;

public enum CustomerOrderType implements OrderType{
	
	NAME("name", "name"),
	PHONE("phone", "phone");

	private String key;
	private String value;
	
	
	private CustomerOrderType(String key, String value){
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
