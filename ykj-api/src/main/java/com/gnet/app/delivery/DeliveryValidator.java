package com.gnet.app.delivery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.app.orderDeliverGoods.OrderDeliverGoods;
import com.gnet.app.orderDeliverGoods.OrderDeliverGoodsErrorBuilder;
import com.gnet.app.orderDeliverGoods.OrderDeliverGoodsMapper;
import com.gnet.app.orderGood.OrderGood;
import com.gnet.app.orderGood.OrderGoodErrorBuilder;
import com.gnet.app.orderGood.OrderGoodMapper;
import com.gnet.app.orderService.OrderSer;
import com.gnet.app.orderService.OrderServiceErrorBuilder;
import com.gnet.utils.spring.SpringContextHolder;

public class DeliveryValidator {
	
	private DeliveryValidator(){}
	
	static Map<String, Object> validateBeforeCreateDelivery(OrderSer delivery, String businessId) {
		DeliveryService deliveryService = SpringContextHolder.getBean(DeliveryService.class);
		Map<String, Object> map = new HashMap<>();
		List<OrderDeliverGoods> serviceGoods = delivery.getServiceGoods();
		
		if(serviceGoods != null && !serviceGoods.isEmpty()){
			for(OrderDeliverGoods serviceGood : serviceGoods){
				if(StringUtils.isBlank(serviceGood.getOrderGoodsId())){
					map.put("code", OrderGoodErrorBuilder.ERROR_ORDERGOODID_NULL);
					map.put("msg", "订单商品编号为空");
					return map;
				}
				if(serviceGood.getDeliverNum() == null){
					map.put("code", OrderServiceErrorBuilder.ERROR_DELIVER_NUM_NULL);
					map.put("msg", "订单商品送货数量为空");
					return map;
				}
			}
		}
		
		// 费用不能为空过滤
		if (delivery.getCost() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_COST_NULL);
			map.put("msg", "费用不能为空");
			return map;
		}

		// 订单编号不能为空过滤
		if (StringUtils.isBlank(delivery.getOrderId())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ORDERID_NULL);
			map.put("msg", "订单编号不能为空");
			return map;
		}

		// 测量单号不能为空过滤
		if (StringUtils.isBlank(delivery.getServiceCode())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_NULL);
			map.put("msg", "送货单号不能为空");
			return map;
		}

		// 测量名称不能为空过滤
		if (StringUtils.isBlank(delivery.getName())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "名称不能为空");
			return map;
		}

		// 要求时间不能为空过滤
		if (delivery.getNeedTime() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NEEDTIME_NULL);
			map.put("msg", "要求时间不能为空");
			return map;
		}

		// 是否已经完成不能为空过滤
		if (delivery.getIsFinish() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ISFINISH_NULL);
			map.put("msg", "是否已经完成不能为空");
			return map;
		}

		// 类型不能为空过滤
		if (delivery.getType() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_TYPE_NULL);
			map.put("msg", "类型不能为空");
			return map;
		}

		// 是否已经结算不能为空过滤
		if (delivery.getIsClear() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ISCLEAR_NULL);
			map.put("msg", "是否已经结算不能为空");
			return map;
		}
				
		if (StringUtils.isNotBlank(delivery.getClerkId()) && delivery.getClerkId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_CLERKID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getServicePosition()) && delivery.getServicePosition().length() > 255 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICEPOSITION_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getServiceCode()) && delivery.getServiceCode().length() > 20 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getName()) && delivery.getName().length() > 50 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getAttachmentId()) && delivery.getAttachmentId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ATTACHMENTID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getId()) && delivery.getId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getFinanceExpendId()) && delivery.getFinanceExpendId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_FINANCEEXPENDID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getOrderId()) && delivery.getOrderId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ORDERID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if(deliveryService.isCreateExist(delivery.getServiceCode(), businessId)){
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_REPEAT);
			map.put("msg", "送货单号重复");
			return map;
		}
		
		validateDeliverGoods(delivery);

		return null;
	}
	
	
	/**
	 * 验证订单商品的送货数量和位置长度是否合理
	 * @return
	 */
	static Map<String, Object> validateDeliverGoods(OrderSer delivery){
		Map<String, Object> map = new HashMap<>();
		OrderGoodMapper orderGoodMapper = SpringContextHolder.getBean(OrderGoodMapper.class);
		OrderDeliverGoodsMapper orderDeliverGoodsMapper = SpringContextHolder.getBean(OrderDeliverGoodsMapper.class);
		List<OrderDeliverGoods> serviceGoods = delivery.getServiceGoods();
		if(serviceGoods != null && !serviceGoods.isEmpty()){
			for(OrderDeliverGoods serviceGood : serviceGoods){
				if(serviceGood.getDeliverNum() == null){
	    			map.put("code", OrderDeliverGoodsErrorBuilder.ERROR_DELIVENUM_NULL);
	    			map.put("msg", "商品送货数量为空");
	    			return map;
				}
				
				if (StringUtils.isNotBlank(serviceGood.getPosition()) && serviceGood.getPosition().length() > 50 ) {
					map.put("code", OrderDeliverGoodsErrorBuilder.ERROR_POSTION_TOOLONG);
					map.put("msg", "长度不能超过50");
					return map;
				}
				
				if (serviceGood.getDeliverNum() != null && String.valueOf(serviceGood.getDeliverNum()).length() > 11 ) {
					map.put("code", OrderDeliverGoodsErrorBuilder.ERROR_DELIVENUM_TOOLONG);
					map.put("msg", "长度不能超过11");
					return map;
				}
				
			}
			
			//订单商品列表
			List<OrderGood> ordersGoods = orderGoodMapper.selectAllList(delivery.getOrderId(), OrderSer.TYPE_DELIVERY);
			if(serviceGoods.size() != ordersGoods.size()){
	   			map.put("code", OrderDeliverGoodsErrorBuilder.ERROR_DELIVERGOODS_LACK);
				map.put("msg", "保存的送货商品数量和订单未送货数量大于0的商品数量不同");
				return map;
			}
			
			Map<String, Integer> goodMap = new HashMap<>();
			for(OrderDeliverGoods seviceGood : serviceGoods){
				goodMap.put(seviceGood.getOrderGoodsId(), seviceGood.getDeliverNum());
			}
			
			if(StringUtils.isBlank(delivery.getId())){
				for(OrderGood orderGood : ordersGoods){
		            if(orderGood.getNeedDeliverNum() < goodMap.get(orderGood.getId())){
		    			map.put("code", OrderServiceErrorBuilder.ERROR_DELIVER_NUM);
		    			map.put("msg", "商品送货数量大于订单商品未送货数量");
		    			return map;
		            }
				}
			}else{		
				//编辑时验证商品送货数是否合理
				Map<String, Integer> differentMap = new HashMap<>();
			    //原本的商品送货数据
			    List<OrderDeliverGoods> oldOrderDeliverGoods = orderDeliverGoodsMapper.selectAllList(delivery.getId());
			    for(OrderDeliverGoods seviceGood : oldOrderDeliverGoods){
			    	String orderGoodId = seviceGood.getOrderGoodsId();
			    	differentMap.put(orderGoodId, seviceGood.getDeliverNum() - goodMap.get(orderGoodId));
			    }
		        for(OrderGood orderGood : ordersGoods){
		            if(orderGood.getNeedDeliverNum() + differentMap.get(orderGood.getId()) < goodMap.get(orderGood.getId())){
		    			map.put("code", OrderServiceErrorBuilder.ERROR_DELIVER_NUM);
		    			map.put("msg", "商品送货数量大于订单未送货数量");
		    			return map;
		            }
		        }
			}
   		
		}
		return null;
	}
	
	/**
	 * 更新送货商品前的验证
	 * @param delivery
	 * @return
	 */
	public static Map<String, Object> validateBeforeUpdateDeliveryGoods(OrderSer delivery) {
		OrderDeliverGoodsMapper orderDeliverGoodsMapper = SpringContextHolder.getBean(OrderDeliverGoodsMapper.class);
		Map<String, Object> map = new HashMap<>();
		List<OrderDeliverGoods> serviceGoods = delivery.getServiceGoods();
	    //原本的商品送货数据
	    List<OrderDeliverGoods> oldOrderDeliverGoods = orderDeliverGoodsMapper.selectAllList(delivery.getId());
		validateDeliverGoods(delivery);
		
		if(serviceGoods.size() != oldOrderDeliverGoods.size()){
   			map.put("code", OrderDeliverGoodsErrorBuilder.ERROR_DELIVERGOODS_LACK);
			map.put("msg", "更新的送货商品数量和原本订单送货商品数量不同");
			return map;
		}
		
		return null;
	}

	
	static Map<String, Object> validateBeforeUpdateDelivery(OrderSer delivery, String businessId) {
		Map<String, Object> map = new HashMap<>();
		DeliveryService deliveryService = SpringContextHolder.getBean(DeliveryService.class);
		
		if (StringUtils.isBlank(delivery.getId())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "送货服务编号为空");
			return map;
		}
		OrderSer oldDelivery = deliveryService.findById(delivery.getId());
		
		if (StringUtils.isNotBlank(delivery.getServiceCode()) && delivery.getServiceCode().length() > 20 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}

		if (StringUtils.isNotBlank(delivery.getClerkId()) && delivery.getClerkId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_CLERKID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getServicePosition()) && delivery.getServicePosition().length() > 255 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICEPOSITION_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
				
		if (StringUtils.isNotBlank(delivery.getName()) && delivery.getName().length() > 50 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getAttachmentId()) && delivery.getAttachmentId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ATTACHMENTID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getId()) && delivery.getId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getFinanceExpendId()) && delivery.getFinanceExpendId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_FINANCEEXPENDID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(delivery.getOrderId()) && delivery.getOrderId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ORDERID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if(deliveryService.isModifyExist(delivery.getServiceCode(), oldDelivery.getServiceCode(), businessId)){
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_REPEAT);
			map.put("msg", "送货单号重复");
			return map;
		}
		
		return null;
	}
	
	
	static Map<String, Object> validateBeforeDeleteOrderService(String id) {
		DeliveryService deliveryService = SpringContextHolder.getBean(DeliveryService.class);
		
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(id)) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "送货服务编号为空");
			return map;
		}
		OrderSer measure = deliveryService.findById(id);
		if(measure.getIsFinish()){
			map.put("code", OrderServiceErrorBuilder.ERROR_DELETED);
			map.put("msg", "已完成的不允许删除");
			return map;
		}
		if(measure.getIsClear()){
			map.put("code", OrderServiceErrorBuilder.ERROR_DELETED);
			map.put("msg", "已结算的不允许删除");
			return map;
		}
		
		return null;
	}

	public static Map<String, Object> validateBeforeUpdateState(OrderSer delivery) {
		Map<String, Object> map = new HashMap<>();
		if(delivery.getIsClear()){
			map.put("code", OrderServiceErrorBuilder.ERROR_STATE);
			map.put("msg", "已结算的无法再结算");
			return map;
		}
		if(StringUtils.isBlank(delivery.getClerkId())){
			map.put("code", OrderServiceErrorBuilder.ERROR_STATE);
			map.put("msg", "没有服务人员无法结算");
			return map;
		}
		return null;
	}
		
	
	public static Map<String, Object> validateBeforeUpdateCancelState(OrderSer delivery) {
		Map<String, Object> map = new HashMap<>();
		if(delivery.getIsClear()){
			map.put("code", OrderServiceErrorBuilder.ERROR_CANCELSTATE);
			map.put("msg", "还未结算的无法取消结算");
			return map;
		}
		
		return null;
	}

}