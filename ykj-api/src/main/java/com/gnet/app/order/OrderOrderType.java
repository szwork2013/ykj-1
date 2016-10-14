package com.gnet.app.order;

import com.gnet.utils.sort.OrderType;

public enum OrderOrderType implements OrderType{

	ORDERNO("orderNo", "order_no"),
	TYPE("type", "type"),
	ORDERDATE("orderDate", "order_date"),
	ORDERSOURCE("orderSource", "order_source");
	
	private String key;
	private String value;

	private OrderOrderType(String key, String value) {
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