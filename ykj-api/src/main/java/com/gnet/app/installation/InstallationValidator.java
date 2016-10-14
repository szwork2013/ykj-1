package com.gnet.app.installation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.gnet.app.orderDeliverGoods.OrderDeliverGoodsErrorBuilder;
import com.gnet.app.orderGood.OrderGood;
import com.gnet.app.orderGood.OrderGoodErrorBuilder;
import com.gnet.app.orderGood.OrderGoodMapper;
import com.gnet.app.orderInstallGoods.OrderInstallGoods;
import com.gnet.app.orderInstallGoods.OrderInstallGoodsErrorBuilder;
import com.gnet.app.orderInstallGoods.OrderInstallGoodsMapper;
import com.gnet.app.orderService.OrderSer;
import com.gnet.app.orderService.OrderServiceErrorBuilder;
import com.gnet.utils.spring.SpringContextHolder;

public class InstallationValidator {
	
	private InstallationValidator(){}
	
	static Map<String, Object> validateBeforeCreateInstation(OrderSer instation, String businessId) {
		InstallationService installationService = SpringContextHolder.getBean(InstallationService.class);
		Map<String, Object> map = new HashMap<>();
		List<OrderInstallGoods> serviceGoods = instation.getInstallGoods();
		if(serviceGoods != null && !serviceGoods.isEmpty()){
			for(OrderInstallGoods serviceGood : serviceGoods){
				if(StringUtils.isBlank(serviceGood.getOrderGoodsId())){
					map.put("code", OrderGoodErrorBuilder.ERROR_ORDERGOODID_NULL);
					map.put("msg", "订单商品编号为空");
					return map;
				}
				if(serviceGood.getInstallNum() == null){
					map.put("code", OrderServiceErrorBuilder.ERROR_INSTALL_NUM_NULL);
					map.put("msg", "订单商品安装数量为空");
					return map;
				}
			}	
		}
		
		// 费用不能为空过滤
		if (instation.getCost() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_COST_NULL);
			map.put("msg", "费用不能为空");
			return map;
		}

		// 订单编号不能为空过滤
		if (StringUtils.isBlank(instation.getOrderId())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ORDERID_NULL);
			map.put("msg", "订单编号不能为空");
			return map;
		}

		// 测量单号不能为空过滤
		if (StringUtils.isBlank(instation.getServiceCode())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_NULL);
			map.put("msg", "送货单号不能为空");
			return map;
		}

		// 测量名称不能为空过滤
		if (StringUtils.isBlank(instation.getName())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "名称不能为空");
			return map;
		}

		// 要求时间不能为空过滤
		if (instation.getNeedTime() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NEEDTIME_NULL);
			map.put("msg", "要求时间不能为空");
			return map;
		}

		// 是否已经完成不能为空过滤
		if (instation.getIsFinish() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ISFINISH_NULL);
			map.put("msg", "是否已经完成不能为空");
			return map;
		}

		// 类型不能为空过滤
		if (instation.getType() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_TYPE_NULL);
			map.put("msg", "类型不能为空");
			return map;
		}

		// 是否已经结算不能为空过滤
		if (instation.getIsClear() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ISCLEAR_NULL);
			map.put("msg", "是否已经结算不能为空");
			return map;
		}
				
		if (StringUtils.isNotBlank(instation.getClerkId()) && instation.getClerkId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_CLERKID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(instation.getServicePosition()) && instation.getServicePosition().length() > 255 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICEPOSITION_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(instation.getServiceCode()) && instation.getServiceCode().length() > 20 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(instation.getName()) && instation.getName().length() > 50 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(instation.getAttachmentId()) && instation.getAttachmentId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ATTACHMENTID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(instation.getId()) && instation.getId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(instation.getFinanceExpendId()) && instation.getFinanceExpendId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_FINANCEEXPENDID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(instation.getOrderId()) && instation.getOrderId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ORDERID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if(installationService.isCreateExist(instation.getServiceCode(), businessId)){
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_REPEAT);
			map.put("msg", "送货单号重复");
			return map;
		}
		
		validateInstallationGoods(instation);

		return null;
	}
	
	
	/**
	 * 验证订单商品的安装数量和位置长度是否合理
	 * @return
	 */
	static Map<String, Object> validateInstallationGoods(OrderSer installtion){
		Map<String, Object> map = new HashMap<>();
		OrderGoodMapper orderGoodMapper = SpringContextHolder.getBean(OrderGoodMapper.class);
		OrderInstallGoodsMapper orderInstallGoodsMapper = SpringContextHolder.getBean(OrderInstallGoodsMapper.class);
		List<OrderInstallGoods> serviceGoods = installtion.getInstallGoods();
		
		if(serviceGoods != null && !serviceGoods.isEmpty()){
			for(OrderInstallGoods serviceGood : serviceGoods){
				if(serviceGood.getInstallNum() == null){
	    			map.put("code", OrderInstallGoodsErrorBuilder.ERROR_INSTALLNUM_NULL);
	    			map.put("msg", "商品安装数量为空");
	    			return map;
				}
				
				if (serviceGood.getInstallNum() != null && String.valueOf(serviceGood.getInstallNum()).length() > 11 ) {
					map.put("code", OrderInstallGoodsErrorBuilder.ERROR_INSTALLNUM_TOOLONG);
					map.put("msg", "长度不能超过11");
					return map;
				}
				
			}
			
			//订单商品列表
			List<OrderGood> ordersGoods = orderGoodMapper.selectAllList(installtion.getOrderId(), OrderSer.TYPE_INSTALLATION);
			if(serviceGoods.size() != ordersGoods.size()){
	   			map.put("code", OrderInstallGoodsErrorBuilder.ERROR_INSTALLGOODS_LACK);
				map.put("msg", "保存的安装商品数量和订单未安装数量大于0的商品数量不同");
				return map;
			}
			
			Map<String, Integer> goodMap = new HashMap<>();	
			for(OrderInstallGoods seviceGood : serviceGoods){
				goodMap.put(seviceGood.getOrderGoodsId(), seviceGood.getInstallNum());
			}
			
			if(StringUtils.isBlank(installtion.getId())){
				for(OrderGood orderGood : ordersGoods){
		            if(orderGood.getNeedInstallNum() < goodMap.get(orderGood.getId())){
		    			map.put("code", OrderServiceErrorBuilder.ERROR_INSTALL_NUM);
		    			map.put("msg", "商品安装数量大于订单商品未安装数量");
		    			return map;
		            }
				}
			}else{		
				//编辑时验证商品安装数是否合理
				Map<String, Integer> differentMap = new HashMap<>();
			    //原本的商品安装数据
			    List<OrderInstallGoods> oldOrderInstallGoods = orderInstallGoodsMapper.selectAllList(installtion.getId());
			    for(OrderInstallGoods seviceGood : oldOrderInstallGoods){
			    	String orderGoodId = seviceGood.getOrderGoodsId();
			    	differentMap.put(orderGoodId, seviceGood.getInstallNum() - goodMap.get(orderGoodId));
			    }
		        for(OrderGood orderGood : ordersGoods){
		            if(orderGood.getNeedInstallNum() + differentMap.get(orderGood.getId()) < goodMap.get(orderGood.getId())){
		    			map.put("code", OrderServiceErrorBuilder.ERROR_INSTALL_NUM);
		    			map.put("msg", "商品安装数量大于订单未安装数量");
		    			return map;
		            }
		        }
			}
		}
		return null;
	}
	
	/**
	 * 更新安装商品前的验证
	 * @param installation
	 * @return
	 */
	public static Map<String, Object> validateBeforeUpdateInstallationGoods(OrderSer installation) {
		OrderInstallGoodsMapper orderInstallGoodsMapper = SpringContextHolder.getBean(OrderInstallGoodsMapper.class);
		Map<String, Object> map = new HashMap<>();
		List<OrderInstallGoods> serviceGoods = installation.getInstallGoods();
		
	    //原本的商品安装数据
	    List<OrderInstallGoods> oldOrderInstallationGoods = orderInstallGoodsMapper.selectAllList(installation.getId());
		validateInstallationGoods(installation);
		
		if(serviceGoods.size() != oldOrderInstallationGoods.size()){
   			map.put("code", OrderDeliverGoodsErrorBuilder.ERROR_DELIVERGOODS_LACK);
			map.put("msg", "更新的安装商品数量和原本安装商品数量不同");
			return map;
		}
		
		return null;
	}

	
	static Map<String, Object> validateBeforeUpdateInstallation(OrderSer installation, String businessId) {
		Map<String, Object> map = new HashMap<>();
		InstallationService installationService = SpringContextHolder.getBean(InstallationService.class);
		
		if (StringUtils.isBlank(installation.getId())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "安装服务编号为空");
			return map;
		}
		OrderSer oldInstallation = installationService.findById(installation.getId());
		
		if (StringUtils.isNotBlank(installation.getServiceCode()) && installation.getServiceCode().length() > 20 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}

		if (StringUtils.isNotBlank(installation.getClerkId()) && installation.getClerkId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_CLERKID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(installation.getServicePosition()) && installation.getServicePosition().length() > 255 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICEPOSITION_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
				
		if (StringUtils.isNotBlank(installation.getName()) && installation.getName().length() > 50 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(installation.getAttachmentId()) && installation.getAttachmentId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ATTACHMENTID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(installation.getId()) && installation.getId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(installation.getFinanceExpendId()) && installation.getFinanceExpendId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_FINANCEEXPENDID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(installation.getOrderId()) && installation.getOrderId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ORDERID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if(installationService.isModifyExist(installation.getServiceCode(), oldInstallation.getServiceCode(), businessId)){
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_REPEAT);
			map.put("msg", "安装单号重复");
			return map;
		}
		
		return null;
	}
	
	
	static Map<String, Object> validateBeforeDeleteOrderService(String id) {
		InstallationService deliveryService = SpringContextHolder.getBean(InstallationService.class);
		
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(id)) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "安装服务编号为空");
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

	public static Map<String, Object> validateBeforeUpdateState(OrderSer installation) {
		Map<String, Object> map = new HashMap<>();
		if(installation.getIsClear()){
			map.put("code", OrderServiceErrorBuilder.ERROR_STATE);
			map.put("msg", "已结算的无法再结算");
			return map;
		}
		if(StringUtils.isBlank(installation.getClerkId())){
			map.put("code", OrderServiceErrorBuilder.ERROR_STATE);
			map.put("msg", "没有服务人员无法结算");
			return map;
		}
		return null;
	}
		
	
	public static Map<String, Object> validateBeforeUpdateCancelState(OrderSer installation) {
		Map<String, Object> map = new HashMap<>();
		if(installation.getIsClear()){
			map.put("code", OrderServiceErrorBuilder.ERROR_CANCELSTATE);
			map.put("msg", "还未结算的无法取消结算");
			return map;
		}
		
		return null;
	}

}