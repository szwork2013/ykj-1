package com.gnet.app.supplier;

import com.gnet.utils.sort.OrderType;

public enum SupplierOrderType implements OrderType{

	SUPPLIER_NAME("supplier_name", "supplierName"),
	CONTACT_NAME("contact_name", "contactName");
	
	private String key;
	private String value;

	private SupplierOrderType(String key, String value) {
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