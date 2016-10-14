package com.gnet.app.orderInstallGoods;

import com.gnet.utils.sort.OrderType;

public enum OrderInstallGoodsOrderType implements OrderType{

	Model("goodModel", "ysg.model");
	
	private String key;
	private String value;

	private OrderInstallGoodsOrderType(String key, String value) {
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