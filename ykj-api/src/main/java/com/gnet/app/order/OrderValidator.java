package com.gnet.app.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.gnet.app.orderGood.OrderGood;
import com.gnet.app.orderGood.OrderGoodErrorBuilder;
import com.gnet.app.orderService.OrderServiceErrorBuilder;
import com.gnet.utils.math.PriceUtils;
import com.gnet.utils.spring.SpringContextHolder;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;

public class OrderValidator {
	
	private OrderValidator(){}
	
	static Map<String, Object> validateBeforeCreateOrder(Order order) {
		Map<String, Object> map = new HashMap<>();
		
		// 订单来源不能为空过滤
		if (order.getOrderSource() == null) {
			map.put("code", OrderErrorBuilder.ERROR_ORDERSOURCE_NULL);
			map.put("msg", "订单来源不能为空");
			return map;
		}

		// 送货地址不能为空过滤
		if (order.getAddress() == null) {
			map.put("code", OrderErrorBuilder.ERROR_ADDRESS_NULL);
			map.put("msg", "送货地址不能为空");
			return map;
		}

		// 客户编号不能为空过滤
		if (order.getCustomerId() == null) {
			map.put("code", OrderErrorBuilder.ERROR_CUSTOMERID_NULL);
			map.put("msg", "客户编号不能为空");
			return map;
		}

		// 跟单人编号不能为空过滤
		if (order.getOrderResponsibleId() == null) {
			map.put("code", OrderErrorBuilder.ERROR_ORDERRESPONSIBLEID_NULL);
			map.put("msg", "跟单人编号不能为空");
			return map;
		}

		// 订单下单日期不能为空过滤
		if (order.getOrderDate() == null) {
			map.put("code", OrderErrorBuilder.ERROR_ORDERDATE_NULL);
			map.put("msg", "订单下单日期不能为空");
			return map;
		}
		
		// 数据长度校验
		Map<String, Object> lengthErrorMap = validateLength(order);
		if (lengthErrorMap != null) {
			return lengthErrorMap;
		}
		
		// 订单商品不为空时进行数据校验
		if (order.getOrderGoods() != null && !order.getOrderGoods().isEmpty()) {
			Set<String> storageGoodIds = Sets.newHashSet();
			for (OrderGood orderGood : order.getOrderGoods()) {
				if (orderGood.getStorageGoodsId() == null) {
					map.put("code", OrderGoodErrorBuilder.ERROR_STORAGEGOOD_NULL);
					map.put("msg", "商品编号不能为空");
					return map;
				}
				
				if (storageGoodIds.contains(orderGood.getStorageGoodsId())) {
					map.put("code", OrderGoodErrorBuilder.ERROR_CREATED);
					map.put("msg", "商品编号在一个订单中重复");
					return map;
				} else {
					storageGoodIds.add(orderGood.getStorageGoodsId());
				}
				
				if (orderGood.getOrderGoodsNum() == null || orderGood.getOrderGoodsNum() <= 0) {
					map.put("code", OrderGoodErrorBuilder.ERROR_GOODSNUM_INVALID);
					map.put("msg", "商品数目不能为空且必须大于0");
					return map;
				}
				
				if (orderGood.getReservedGoods() != null && orderGood.getReservedGoods() > orderGood.getOrderGoodsNum()) {
					map.put("code", OrderGoodErrorBuilder.ERROR_RESERVEDGOODS_NULL);
					map.put("msg", "商品预留库存数不得大于商品数目");
					return map;
				}
				
				if (orderGood.getDiscountRate() == null) {
					map.put("code", OrderGoodErrorBuilder.ERROR_DISCOUNTRATE_NULL);
					map.put("msg", "商品折扣率不能为空");
					return map;
				}
				
				if (orderGood.getStrikeUnitPrice() == null) {
					map.put("code", OrderGoodErrorBuilder.ERROR_UNITPRICE_NULL);
					map.put("msg", "折后单价不能为空");
					return map;
				}
				
				if (StringUtils.isNotBlank(orderGood.getInitPosition()) && orderGood.getInitPosition().length() > 255 ) {
					map.put("code", OrderGoodErrorBuilder.ERROR_INITPOSITION_TOOLONG);
					map.put("msg", "长度不能超过255");
					return map;
				}
			}
		}

		return null;
	}
	
	static Map<String, Object> validateBeforeUpdateOrder(Order order) {
		Map<String, Object> map = new HashMap<>();
		
		if (order.getId() == null) {
			map.put("code", OrderErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "订单编号为空");
			return map;
		}
		
		// 订单来源不能为空过滤
		if (order.getOrderSource() == null) {
			map.put("code", OrderErrorBuilder.ERROR_ORDERSOURCE_NULL);
			map.put("msg", "订单来源不能为空");
			return map;
		}

		// 送货地址不能为空过滤
		if (order.getAddress() == null) {
			map.put("code", OrderErrorBuilder.ERROR_ADDRESS_NULL);
			map.put("msg", "送货地址不能为空");
			return map;
		}

		// 客户编号不能为空过滤
		if (order.getCustomerId() == null) {
			map.put("code", OrderErrorBuilder.ERROR_CUSTOMERID_NULL);
			map.put("msg", "客户编号不能为空");
			return map;
		}

		// 跟单人编号不能为空过滤
		if (order.getOrderResponsibleId() == null) {
			map.put("code", OrderErrorBuilder.ERROR_ORDERRESPONSIBLEID_NULL);
			map.put("msg", "跟单人编号不能为空");
			return map;
		}

		// 订单下单日期不能为空过滤
		if (order.getOrderDate() == null) {
			map.put("code", OrderErrorBuilder.ERROR_ORDERDATE_NULL);
			map.put("msg", "订单下单日期不能为空");
			return map;
		}
		
		Map<String, Object> lengthErrorMap = validateLength(order);
		if (lengthErrorMap != null) {
			return lengthErrorMap;
		}
		
		OrderMapper orderMapper = SpringContextHolder.getBean(OrderMapper.class);
		Order oldOrder = orderMapper.selectByPrimaryKey(order.getId());
		// 订单已退单或是已完成不能编辑
		if (Order.TYPE_FINISH_ORDER.equals(oldOrder.getType()) || Order.TYPE_BACK_ORDER.equals(oldOrder.getType())) {
			map.put("code", OrderErrorBuilder.ERROR_EDITED);
			map.put("msg", "订单已结束不能进行编辑");
			return map;
		}
		
		// 非预定单不能进行报价审核状态的修改
		if (!Order.TYPE_PRE_ORDER.equals(oldOrder.getType()) && !oldOrder.getIsNeedCostAduit().equals(order.getIsNeedCostAduit())) {
			map.put("code", OrderErrorBuilder.ERROR_EDITED);
			map.put("msg", "非预定单报价审核状态不能修改");
			return map;
		}
		
		// 客户编号不允许编辑
		if (StringUtils.isNoneBlank(order.getCustomerId()) && !oldOrder.getCustomerId().equals(order.getCustomerId())) {
			map.put("code", OrderErrorBuilder.ERROR_EDITED);
			map.put("msg", "客户编号不能修改");
			return map;
		}
		
		// 订单号不能为空
		if (StringUtils.isNoneBlank(order.getOrderNo()) && !oldOrder.getOrderNo().equals(order.getOrderNo())) {
			map.put("code", OrderErrorBuilder.ERROR_EDITED);
			map.put("msg", "订单号不能修改");
			return map;
		}
		
		return null;
	}
	
	// 数据长度校验
	static Map<String, Object> validateLength(Order order) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotBlank(order.getOrderNo()) && order.getOrderNo().length() > 20 ) {
			map.put("code", OrderErrorBuilder.ERROR_ORDERNO_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(order.getAddress()) && order.getAddress().length() > 255 ) {
			map.put("code", OrderErrorBuilder.ERROR_ADDRESS_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(order.getOrderCreatorId()) && order.getOrderCreatorId().length() > 36 ) {
			map.put("code", OrderErrorBuilder.ERROR_ORDERCREATORID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(order.getPhoneSec()) && order.getPhoneSec().length() > 20 ) {
			map.put("code", OrderErrorBuilder.ERROR_PHONESEC_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(order.getOrderResponsibleId()) && order.getOrderResponsibleId().length() > 36 ) {
			map.put("code", OrderErrorBuilder.ERROR_ORDERRESPONSIBLEID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(order.getId()) && order.getId().length() > 36 ) {
			map.put("code", OrderErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(order.getCustomerId()) && order.getCustomerId().length() > 36 ) {
			map.put("code", OrderErrorBuilder.ERROR_CUSTOMERID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(order.getBusinessId()) && order.getBusinessId().length() > 36 ) {
			map.put("code", OrderErrorBuilder.ERROR_BUSINESSID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		return null;
	}
	
	
	/**
	 * 订单商品更新检验
	 * 
	 * @param orderGoods
	 * @return
	 */
	static Map<String, Object> validateUpdateOrderGoods(List<OrderGood> orderGoods) {
		Map<String, Object> map = new HashMap<>();
		
		Set<String> storageGoodIds = Sets.newHashSet();
		for (OrderGood orderGood : orderGoods) {
			if (orderGood.getStorageGoodsId() == null) {
				map.put("code", OrderGoodErrorBuilder.ERROR_STORAGEGOOD_NULL);
				map.put("msg", "商品编号不能为空");
				return map;
			}
			
			if (storageGoodIds.contains(orderGood.getStorageGoodsId())) {
				map.put("code", OrderGoodErrorBuilder.ERROR_CREATED);
				map.put("msg", "商品编号在一个订单中重复");
				return map;
			} else {
				storageGoodIds.add(orderGood.getStorageGoodsId());
			}
			
			if (orderGood.getOrderGoodsNum() == null || orderGood.getOrderGoodsNum() <= 0) {
				map.put("code", OrderGoodErrorBuilder.ERROR_GOODSNUM_INVALID);
				map.put("msg", "商品数目不能为空且必须大于0");
				return map;
			}
			
			if (orderGood.getReservedGoods() != null && orderGood.getReservedGoods() > orderGood.getOrderGoodsNum()) {
				map.put("code", OrderGoodErrorBuilder.ERROR_RESERVEDGOODS_NULL);
				map.put("msg", "商品预留库存数不得大于商品数目");
				return map;
			}
			
			if (orderGood.getDiscountRate() == null) {
				map.put("code", OrderGoodErrorBuilder.ERROR_DISCOUNTRATE_NULL);
				map.put("msg", "商品折扣率不能为空");
				return map;
			}
			
			if (orderGood.getStrikeUnitPrice() == null) {
				map.put("code", OrderGoodErrorBuilder.ERROR_UNITPRICE_NULL);
				map.put("msg", "折后单价不能为空");
				return map;
			}
			
			if (StringUtils.isNotBlank(orderGood.getInitPosition()) && orderGood.getInitPosition().length() > 255 ) {
				map.put("code", OrderGoodErrorBuilder.ERROR_INITPOSITION_TOOLONG);
				map.put("msg", "长度不能超过255");
				return map;
			}
			
		}
		
		return null;
	}

	public static Map<String, Object> validateBeforeDelivery(Order order) {
		Map<String, Object> map = Maps.newHashMap();
		if(isPayoff(order)){
			map.put("code", OrderServiceErrorBuilder.ERROR_DELIVER);
			map.put("msg", "订单还未付清不能进行送货服务");
			return map;
		}	
		return null;
	}
	
	public static Map<String, Object> validateBeforeInstallation(Order order){
		Map<String, Object> map = Maps.newHashMap();
		if(isPayoff(order)){
			map.put("code", OrderServiceErrorBuilder.ERROR_INSTALLATION);
			map.put("msg", "订单还未付清不能进行安装服务");
			return map;
		}	
		return null;
	}
	
	
	/**
	 * 判断订单是否已经付清
	 * @param order
	 * @return
	 */
    static boolean isPayoff(Order order){
		if(order.getStrikePrice() != null && order.getReceiptPrice() != null){
			return PriceUtils.greaterEqThan(order.getReceiptPrice(), order.getStrikePrice());
		}
		return false;
	}
	
}