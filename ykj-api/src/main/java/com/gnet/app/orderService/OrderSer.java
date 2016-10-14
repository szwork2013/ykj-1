package com.gnet.app.orderService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnet.app.orderDeliverGoods.OrderDeliverGoods;
import com.gnet.app.orderInstallGoods.OrderInstallGoods;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ykj_order_service")
public class OrderSer extends BaseEntity {

	private static final long serialVersionUID = -1738577469686888856L;
	
	/** 已安排 **/
	public static String STATUS_ARRANGE = "已安排";
	
	/** 未安排 **/
	public static String STATUS_UNARRANGE = "未安排";
	
	/** 已签到 **/
	public static String STATUS_SIGN = "已签到";

	/** 已完成 **/
	public static String STATUS_COMPLETE = "已完成";
	
	/** 已过期 **/
	public static String STATUS_OVERDUE = "已过期";
	
	/** 测量 **/
	public static Integer TYPE_MEASURE = 0;
	/** 设计 **/
	public static Integer TYPE_DESIGN = 1;
	/** 送货 **/
	public static Integer TYPE_DELIVERY = 2;
	/** 安装 **/
	public static Integer TYPE_INSTALLATION = 3;
	/** 维护保养 **/
	public static Integer TYPE_MAINTENANCE = 4;

	/** 创建日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date createDate;
	
	/** 修改日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date modifyDate;
	
	/** 类型,0:测量,1:设计,2:送货,3:安装,4:维护保养,99:其它 **/
	private Integer type;
	
	/** 订单编号 **/
	private String orderId;
	
	/** 服务单号 **/
	private String serviceCode;
	
	/** 服务名称，默认服务名称加时间戳 **/
	private String name;
	
	/** 服务内容  **/
	private String remark;
	
	/** 服务人员编号 **/
	private String clerkId;
	
	/** 服务人员名称 **/
	private @Transient String clerkName;
	
	/** 服务人员电话 **/
	private @Transient String clerkPhone;
	
	/** 附件编号 **/
	private String attachmentId;
	
	/** 要求时间 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date needTime;
	
	/** 服务日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date serviceTime;
	
	/** 客户备注 **/
	private String customerRemark;
	
	/** 内部备注 **/
	private String privateRemark;
	
	/** 费用 **/
	private BigDecimal cost;
	
	/** 支出记录编号 **/
	private String financeExpendId;
	
	/** 服务评价星级 **/
	private Integer starLevel;
	
	/** 服务地址 **/
	private String servicePosition;
	
	/** 是否已完成 **/
	private Boolean isFinish;
	
	/** 是否已经结算 **/
	private Boolean isClear;
	
	/** 是否已经删除 **/
	private @JsonIgnore Boolean isDel;
	
	/** 服务状态 **/
	private @Transient String status;
	
	/** 送货商品列表 **/
	private @Transient List<OrderDeliverGoods> serviceGoods;
	
	/** 安装商品列表 **/
	private @Transient List<OrderInstallGoods> installGoods;
	
}
