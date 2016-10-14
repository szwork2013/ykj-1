package com.gnet.app.customerTrack;

import com.gnet.utils.sort.OrderType;

public enum CustomerTrackOrderType implements OrderType{
	
    WAY("way", "way"),
    TIME("time", "time");

	private String key;
	private String value;
	
	
	private CustomerTrackOrderType(String key, String value){
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
