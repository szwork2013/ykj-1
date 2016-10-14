package com.gnet.app.office;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ykj_office")
public class Office extends BaseEntity {
	
	/**
	 * 0 商家
	 */
	public static final Integer TYPE_BUSINESS = 0;
	
	/**
	 * 1 门店
	 */
	public static final Integer TYPE_STORE = 1;
	
	/**
	 * 2 部门
	 */
	public static final Integer TYPE_DEPARTMENT = 2;
	
	/**
	 * 商家的上级部门id同一定义为0
	 */
	public static final String BUSINESS_PARENTID_ID = "0";

	private static final long serialVersionUID = -1804701158013377653L;

	/** 创建日期 **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date createDate;
	
	/** 修改日期 **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date modifyDate;
	
	/** 上级部门编号 **/
	private String parentid;
	
	/** 部门名称 **/
	private String name;
	
	/** 部门代码 **/
	private String code;
	
	/** 部门层级 **/
	private Integer level;
	
	/** 部门类型(0:商家、1:门店、2:部门) **/
	private Integer type;
	
	/** 是否已经删除 **/
	private @JsonIgnore Boolean isDel;
	
	/** 描述 **/
	private String description;
	
}
