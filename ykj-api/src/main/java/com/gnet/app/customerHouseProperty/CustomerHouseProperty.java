package com.gnet.app.customerHouseProperty;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ykj_customer_house_property")
public class CustomerHouseProperty extends BaseEntity{
	
	private static final long serialVersionUID = -4584865205360689505L;

	/** 创建时间  **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date createDate;
	
	/** 更新时间 **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date modifyDate;
	
	/** 客户编号 **/
	private String customerId;
	
	/** 楼盘名称  **/
	private String buildingName;
	
	/** 楼幢房号 **/
	private String buildingNo;
	
	/** 风格 **/
	private Integer roomStyle;
	
	/** 装修进度 **/
	private Integer decorateProcess;

	/** 装修类型 **/
	private Integer decorateType;
	
	/** 户型 **/
	private Integer roomModel;
	
	/** 区域  **/
	private String buildingPosition;
	
	/** 面积 **/
	private Integer area;
}
