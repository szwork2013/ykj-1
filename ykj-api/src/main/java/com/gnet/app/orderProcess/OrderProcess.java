package com.gnet.app.orderProcess;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ykj_order_process")
public class OrderProcess extends BaseEntity {
	
	public static final Integer STATUS_ADUIT = 1;
	
	public static final Integer STATUS_SUBSCRIPTION = 2;
	
	public static final Integer STATUS_MEASURE = 3;
	
	public static final Integer STATUS_DESIGN = 4;
	
	public static final Integer STATUS_DELIVERY = 5;
	
	public static final Integer STATUS_INSTALL = 6;
	
	public static final Integer STATUS_PAYMENT = 7;
	
	public static final Integer STATUS_FINISH = 8;

	private static final long serialVersionUID = 1487223204156914748L;

	/** 创建日期 **/
	private Date createDate;
	
	/** 修改日期 **/
	private Date modifyDate;
	
	/** 类型,1:审核,2:订金,3:测量,4:设计,5:送货,6:安装,7:付款,8:完成 **/
	private Integer type;
	
	/** 订单编号 **/
	private String orderId;
	
	/** 是否完成 **/
	private Boolean isFinish;
	
	/** 订单号 **/
	private @Transient String orderNo;
	
	/** 订单地址 **/
	private @Transient String address;
	
}