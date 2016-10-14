package com.gnet.resource.errorBuilder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

/**
 * 基础的错误构建器
 * 
 * @author xuq
 * @date 2016年8月18日
 * @version 1.0
 */
public abstract class BaseErrorBuilder {
	
	/**
	 * 新增错误	错误编码:10100
	 */
	public static final Integer ERROR_CREATED = 10100;
	
	/**
	 * 编辑错误	错误编码:10101
	 */
	public static final Integer ERROR_EDITED = 10101;
	
	/**
	 * 编辑错误	错误编码:10102
	 */
	public static final Integer ERROR_DELETED = 10102;
	
	/**
	 * 排序字段不符合要求	错误编码:10103
	 */
	public static final Integer ERROR_SORT_PROPERTY_NOTFOUND = 10103;
	
	/**
	 * 排序方向不符合要求	错误编码:10104
	 */
	public static final Integer ERROR_SORT_DIRECTION_NOTFOUND = 10104;

	private Integer code;
	private String msg;
	private String request;
	
	public BaseErrorBuilder(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public BaseErrorBuilder setRequest(RequestMethod requestMethod, String url) {
		this.request = String.format("%s %s", requestMethod.name(), url);
		return this;
	}
	
	public JSONObject build() {
		JSONObject result = new JSONObject();
		result.put("code", code);
		result.put("message", msg);
		if (StringUtils.isNotBlank(request)) {
			result.put("request", request);
		}
		return result;
	}
	
}
