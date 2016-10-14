package com.gnet.app.orderGood;

import java.util.HashMap;
import java.util.Map;

import com.gnet.app.order.Order;
import com.gnet.app.order.OrderErrorBuilder;
import com.gnet.app.order.OrderService;
import com.gnet.utils.spring.SpringContextHolder;

public class OrderGoodValidator {

	private OrderGoodValidator() {
	}

	/**
	 * 检验订单商品能删除的条件 
	 * 1. 剩余送货数等于订单商品数目
	 * 2. 剩余安装数等于订单商品数目
	 * 3. 已完成或已退单的订单商品不能删除
	 * 
	 * @param id
	 * @return
	 */
	static Map<String, Object> validateBeforeDeleteOrderGood(String id) {
		OrderGoodService orderGoodService = SpringContextHolder.getBean(OrderGoodService.class);
		OrderGood orderGood = orderGoodService.findById(id);
		Map<String, Object> errorCode = new HashMap<>();
		
		if (orderGood == null) {
			errorCode.put("code", OrderGoodErrorBuilder.ERROR_ORDERGOOD_NULL);
			errorCode.put("msg", "订单商品为空");
			return errorCode;
		}
		
		if (!orderGood.getNeedDeliverNum().equals(orderGood.getOrderGoodsNum())) {
			errorCode.put("code", OrderGoodErrorBuilder.ERROR_DELETED);
			errorCode.put("msg", "订单商品已安排送货不能删除");
			return errorCode;
		}
		
		if (!orderGood.getNeedInstallNum().equals(orderGood.getNeedInstallNum())) {
			errorCode.put("code", OrderGoodErrorBuilder.ERROR_DELETED);
			errorCode.put("msg", "订单商品已安排安装不能删除");
			return errorCode;
		}
		
		OrderService orderService = SpringContextHolder.getBean(OrderService.class);
		Order order = orderService.findById(orderGood.getOrderId());
		if (Order.TYPE_FINISH_ORDER.equals(order.getType()) || Order.TYPE_BACK_ORDER.equals(order.getType())) {
			errorCode.put("code", OrderGoodErrorBuilder.ERROR_DELETED);
			errorCode.put("msg", "订单已结束不能进行修改");
			return errorCode;
		}

		return null;
	}

}