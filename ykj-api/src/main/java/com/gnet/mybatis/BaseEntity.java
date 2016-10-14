package com.gnet.mybatis;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 基础Entity类<br/>
 * 
 * <b>提供UUID及其GETTER和SETTER方法</b><br/>
 * 
 * <font color="red"><b>注意： 如果主键类型非UUID策略，则BaseEntity不适用，请自行构建Entity</b></font>
 * 
 * @author xuq
 * @date 2016年9月10日
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 未删除
	 */
	public static final Boolean DEL_FALSE = Boolean.FALSE;
	
	/**
	 * 已删除
	 */
	public static final Boolean DEL_TRUE = Boolean.TRUE;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) private String id;
	
}
