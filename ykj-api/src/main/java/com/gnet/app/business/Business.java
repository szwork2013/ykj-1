package com.gnet.app.business;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "ykj_business")
public class Business extends BaseEntity {

	private static final long serialVersionUID = -6028221133393967053L;

	/** 创建日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date createDate;
	
	/** 修改日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date modifyDate;
	
	/** 商家名称 **/
	private String name;
	
	/** 缩略名称 **/
	private String abbrName;
	
	/** 门店数量 **/
	private @Transient Integer storeNum;
	
	/** 主营品牌 **/
	private String saleBrands;
	
	/** 联系电话 **/
	private String contactNumber;
	
	/** 售后电话 **/
	private String serviceCall;
	
	/** 联系人 **/
	private String contactPerson;
	
	/** logo图片 **/
	private String logo;
	
	/** 所在地 **/
	private String location;
	
	/** 邮编 **/
	private String postcode;
	
	/** 联系地址 **/
	private String address;
	
	/** 是否已经删除 **/
	private @JsonIgnore Boolean isDel;
	
}
