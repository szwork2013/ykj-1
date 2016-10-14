package com.gnet.app.codeword;

import com.gnet.utils.sort.OrderType;

public enum CodewordOrderType implements OrderType {
	
	ID("id", "id"),
	
	UPDATETIME("update_time", "updateTime");
	
	private String key;
	private String value;

	private CodewordOrderType(String key, String value) {
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
