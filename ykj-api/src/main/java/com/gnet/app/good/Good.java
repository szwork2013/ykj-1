package com.gnet.app.good;

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
@Table(name = "ykj_storage_goods")
public class Good extends BaseEntity {
	
	/**
	 * 0: 在售
	 */
	public static final Integer ONSALE_STATUS = 0;
	
	/**
	 * 1: 下架
	 */
	public static final Integer SOLD_OUT = 1;
	
	/**
	 * 2: 停产
	 */
	public static final Integer STOP_PRODUCT = 2;

	private static final long serialVersionUID = 562481088438233614L;

	/** 创建日期 **/
	private Date createDate;
	
	/** 修改日期 **/
	private Date modifyDate;
	
	/** 商品供货商编号 **/
	private String supplierId;
	
	/** 商品名称 **/
	private String name;
	
	/** 商品型号 **/
	private String model;
	
	/** 商品类别（行业） **/
	private String type;
	
	/** 商品颜色 **/
	private String color;
	
	/** 商品规格 **/
	private String specification;
	
	/** 状态类型 **/
	private Integer statusType;
	
	/** 单位 **/
	private Integer unit;
	
	/** 最小单位 **/
	private Integer atomUnit;
	
	/** 售价 **/
	private BigDecimal price;
	
	/** 长 **/
	private BigDecimal length;
	
	/** 宽 **/
	private BigDecimal width;
	
	/** 高/厚 **/
	private BigDecimal height;
	
	/** 重 **/
	private BigDecimal weight;
	
	/** 在售状态,0:在售,1:下架,2:停产 **/
	private Integer onsaleStatus;
	
	/** 商家编号 **/
	private String businessId;
	
	/** 外置参数：原值 **/
	private @Transient String originValue;
	
}
