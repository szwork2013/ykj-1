package com.gnet.utils.sort;

/**
 * 排序方向枚举
 * @author SY
 * @Date 2016年9月10日16:33:58
 */
public enum OrderDirectionType {
	
	ASC("asc"),
	
	DESC("desc");
	
	private String value;
	
	private OrderDirectionType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
