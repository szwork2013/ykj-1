package com.gnet.app.orderGood;

import com.gnet.utils.sort.OrderType;

public enum OrderGoodOrderType implements OrderType{

	Model("goodModel", "ysg.model");
	
	private String key;
	private String value;

	private OrderGoodOrderType(String key, String value) {
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