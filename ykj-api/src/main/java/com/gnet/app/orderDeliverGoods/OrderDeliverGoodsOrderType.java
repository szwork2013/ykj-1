package com.gnet.app.orderDeliverGoods;

import com.gnet.utils.sort.OrderType;

public enum OrderDeliverGoodsOrderType implements OrderType{

	Model("goodModel", "ysg.model");
	
	private String key;
	private String value;

	private OrderDeliverGoodsOrderType(String key, String value) {
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