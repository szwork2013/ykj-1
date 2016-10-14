package com.gnet.app.orderGood;

import java.math.BigDecimal;
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
@Table(name = "ykj_order_goods")
public class OrderGood extends BaseEntity {

	private static final long serialVersionUID = 3336388410512652207L;

	/** 创建日期 **/
	private Date createDate;
	
	/** 修改日期 **/
	private Date modifyDate;
	
	/** 订单编号 **/
	private String orderId;
	
	/** 仓库商品编号 **/
	private String storageGoodsId;
	
	/** 订单数量 **/
	private Integer orderGoodsNum;
	
	/** 默认1.00 **/
	private BigDecimal discountRate;
	
	/** 默认原价 **/
	private BigDecimal strikeUnitPrice;
	
	/** 预留库存 **/
	private Integer reservedGoods;
	
	/** 预留到期时间 **/
	private Date reservedDate;
	
	/** 位置 **/
	private String initPosition;
	
	/** 剩余需送货数 **/
	private Integer needDeliverNum;
	
	/** 剩余需安装数 **/
	private Integer needInstallNum;
	
	/** 订单商品出库数 **/
	private Integer outGoodsNum;
	
	/** 备注 **/
	private String remark;
	
	/** 商品名称 **/
	private @Transient String goodName;
	
	/** 商品型号 **/
	private @Transient String goodModel;
	
	/** 商品供货商名称 **/
	private @Transient String supplierName;
}
