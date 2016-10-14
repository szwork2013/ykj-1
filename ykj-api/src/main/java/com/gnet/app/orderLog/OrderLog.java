package com.gnet.app.orderLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ykj_order_log")
public class OrderLog extends BaseEntity {

	private static final long serialVersionUID = 8176972002601922871L;

	/** 创建日期 **/
	private Date createDate;
	
	/** 修改日期 **/
	private Date modifyDate;
	
	/** 订单编号 **/
	private String orderId;
	
	/** 操作人编号 **/
	private String clerkId;
	
	/** 操作日志 **/
	private String content;
	
}
