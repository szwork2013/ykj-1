package com.gnet.app.store;

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
@Table(name = "ykj_store")
public class Store extends BaseEntity {

	private static final long serialVersionUID = -816838536922457527L;

	/** 创建日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date createDate;
	
	/** 修改日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date modifyDate;
	
	/** 门店名称 **/
	private String name;
	
	/** 联系人 **/
	private String contactPerson;
	
	/** 联系电话 **/
	private String contactNumber;
	
	/** 门店地址 **/
	private String address;
	
	/** 商家编号 **/
	private String businessId;
	
	/** 是否已经删除 **/
	private @JsonIgnore Boolean isDel;
	
}
