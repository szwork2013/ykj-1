package com.gnet.app.orderDeliverGoods;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ykj_order_deliver_goods")
public class OrderDeliverGoods extends BaseEntity {

	private static final long serialVersionUID = 164389609144375095L;

	/** 创建日期 **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date createDate;
	
	/** 修改日期 **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date modifyDate;
	
	/** 订单商品编号 **/
	private String orderGoodsId;
	
	/** 订单商品名称 **/
	private @Transient String goodName;
	
	/** 商品型号 **/
	private @Transient String goodModel;
	
	/** 商品单位 **/
	private @Transient Integer goodUnit;
	
	/** 商品订单数量 **/
	private @Transient Integer orderNum;
	
	/** 未送货数量 **/
	private @Transient Integer needDeliverNum;
	
	/** 当前库存 **/
	private @Transient Integer storeNow;
	
	/** 订单送货服务记录编号 **/
	private String orderDeliverServiceId;
	
	/** 送货数量 **/
	private Integer deliverNum;
	
	/** 安装位置 **/
	private String position;
	
	/** 备注 **/
	private String remark;
	
}
