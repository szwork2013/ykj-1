package com.gnet.app.supplier;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ykj_supplier")
public class Supplier extends BaseEntity {

	private static final long serialVersionUID = 5322720757403613984L;

	/** 创建日期 **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date createDate;
	
	/** 修改日期 **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date modifyDate;
	
	/** 供货商名称 **/
	private String supplierName;
	
	/** 联系人 **/
	private String contactName;
	
	/** 联系电话 **/
	private String contactPhone;
	
	/** 电子邮箱 **/
	private String contactEmail;
	
	/** 联系地址 **/
	private String contactAddress;
	
	/** 备注 **/
	private String remark;
	
	/** 商家编号 **/
	private String businessId;
	
}
