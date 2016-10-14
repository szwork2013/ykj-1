package com.gnet.app.orderService;

import com.gnet.utils.sort.OrderType;

public enum OrderServiceOrderType implements OrderType{

	NEEDTIME("needTime", "need_time"),
	SERVICETIME("serviceTime", "service_time"),
	NAME("name", "name");
	
	private String key;
	private String value;

	private OrderServiceOrderType(String key, String value) {
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