package com.gnet.app.customer;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ykj_customer")
public class Customer extends BaseEntity{
	
	private static final long serialVersionUID = -4584865205360689505L;
	
	/** 潜在客户  **/
	public static Integer TYPE_POTENTIAL_CUSTOMER = 0;
	
	/** 成交客户 **/
	public static Integer TYPE_DEAL_CUSTOMER = 1;
	
	/** 客户负责人 **/
	private String customerResponsibleId;
	
	/** 客户姓名  **/
	private String name;
	
	/** 客户性别  **/
	private Integer sex;
	
	/** 客户类型 **/
	private Integer type;

	/** 手机号码  **/
	private String phone;
	
	/** 备用手机号码  **/
	private String phoneSec;
	
	/** 意向产品  **/
	private String needProduct;
	
	/** 需求时限  **/
	private Integer needTime;
	
	/** 微信号  **/
	private String wechat;
	
	/** qq **/
	private String qq;
	
	/** 邮箱地址 **/
	private String email;
	
	/** 联系地址 **/
	private String address;
	
	/** 单位名称 **/
	private String organization;
	
	/** 录入时间  **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date createDate;
	
	/** 更新时间 **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date modifyDate;
	
	/** 商家编号 **/
	private String businessId;
	
	/** 是否有效 **/
	private Boolean isEffectivity;
	
	/** 来源类型 **/
	private Integer originType;
	
	/** 是否删除 **/
	private @JsonIgnore Boolean isDel;
	
	/** 客户负责人姓名 **/
	private @Transient String customerResponsibleName;
	
	/** 楼盘名称 **/
	private @Transient String buildingName;
	
	/** 最后跟踪时间 **/
	private @Transient @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date time; 
	
	/** 装修进度 **/
	private @Transient Integer decorateType;
	
	/** 标签数组 **/
	private @Transient String[] tags;
}
