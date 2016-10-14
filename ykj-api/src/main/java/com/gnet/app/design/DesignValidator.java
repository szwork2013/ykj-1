package com.gnet.app.design;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.app.orderService.OrderSer;
import com.gnet.app.orderService.OrderServiceErrorBuilder;
import com.gnet.utils.spring.SpringContextHolder;

public class DesignValidator {
	
	private DesignValidator(){}
	
	static Map<String, Object> validateBeforeCreateDesign(OrderSer design, String businessId) {
		DesignService designService = SpringContextHolder.getBean(DesignService.class);
		Map<String, Object> map = new HashMap<>();
		
		// 费用不能为空过滤
		if (design.getCost() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_COST_NULL);
			map.put("msg", "费用不能为空");
			return map;
		}

		// 订单编号不能为空过滤
		if (StringUtils.isBlank(design.getOrderId())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ORDERID_NULL);
			map.put("msg", "订单编号不能为空");
			return map;
		}

		// 测量单号不能为空过滤
		if (StringUtils.isBlank(design.getServiceCode())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_NULL);
			map.put("msg", "测量单号不能为空");
			return map;
		}

		// 测量名称不能为空过滤
		if (StringUtils.isBlank(design.getName())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "测量名称不能为空");
			return map;
		}
		
		// 要求时间不能为空过滤
		if (design.getNeedTime() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NEEDTIME_NULL);
			map.put("msg", "要求时间不能为空");
			return map;
		}

		// 是否已经完成不能为空过滤
		if (design.getIsFinish() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ISFINISH_NULL);
			map.put("msg", "是否已经完成不能为空");
			return map;
		}

		// 类型不能为空过滤
		if (design.getType() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_TYPE_NULL);
			map.put("msg", "类型不能为空");
			return map;
		}

		// 是否已经结算不能为空过滤
		if (design.getIsClear() == null) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ISCLEAR_NULL);
			map.put("msg", "是否已经结算不能为空");
			return map;
		}
				
		if (StringUtils.isNotBlank(design.getClerkId()) && design.getClerkId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_CLERKID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getServicePosition()) && design.getServicePosition().length() > 255 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICEPOSITION_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getServiceCode()) && design.getServiceCode().length() > 20 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getName()) && design.getName().length() > 50 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getAttachmentId()) && design.getAttachmentId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ATTACHMENTID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getId()) && design.getId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getFinanceExpendId()) && design.getFinanceExpendId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_FINANCEEXPENDID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getOrderId()) && design.getOrderId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ORDERID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if(designService.isCreateExist(design.getServiceCode(), businessId)){
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_REPEAT);
			map.put("msg", "设计单号重复");
			return map;
		}

		return null;
	}
	
	static Map<String, Object> validateBeforeUpdateDesign(OrderSer design, String businessId) {
		Map<String, Object> map = new HashMap<>();
		DesignService designService = SpringContextHolder.getBean(DesignService.class);
		
		if (StringUtils.isBlank(design.getId())) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "设计服务编号为空");
			return map;
		}
		OrderSer oldDesign = designService.findById(design.getId());
		
		if (StringUtils.isNotBlank(design.getServiceCode()) && design.getServiceCode().length() > 20 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getClerkId()) && design.getClerkId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_CLERKID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getServicePosition()) && design.getServicePosition().length() > 255 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICEPOSITION_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
				
		if (StringUtils.isNotBlank(design.getName()) && design.getName().length() > 50 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getAttachmentId()) && design.getAttachmentId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ATTACHMENTID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getId()) && design.getId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getFinanceExpendId()) && design.getFinanceExpendId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_FINANCEEXPENDID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(design.getOrderId()) && design.getOrderId().length() > 36 ) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ORDERID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if(designService.isModifyExist(design.getServiceCode(), oldDesign.getServiceCode(), businessId)){
			map.put("code", OrderServiceErrorBuilder.ERROR_SERVICECODE_REPEAT);
			map.put("msg", "设计单号重复");
			return map;
		}

		return null;
	}
	
	
	static Map<String, Object> validateBeforeDeleteOrderService(String id) {
		DesignService designService = SpringContextHolder.getBean(DesignService.class);
		
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(id)) {
			map.put("code", OrderServiceErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "设计服务编号为空");
			return map;
		}
		OrderSer design = designService.findById(id);
		if(design.getIsFinish()){
			map.put("code", OrderServiceErrorBuilder.ERROR_DELETED);
			map.put("msg", "已完成的不允许删除");
			return map;
		}
		if(design.getIsClear()){
			map.put("code", OrderServiceErrorBuilder.ERROR_DELETED);
			map.put("msg", "已结算的不允许删除");
			return map;
		}
		
		return null;
	}

	public static Map<String, Object> validateBeforeUpdateState(OrderSer design) {
		Map<String, Object> map = new HashMap<>();
		if(design.getIsClear()){
			map.put("code", OrderServiceErrorBuilder.ERROR_STATE);
			map.put("msg", "已结算的无法再结算");
			return map;
		}
		
		if(StringUtils.isBlank(design.getClerkId())){
			map.put("code", OrderServiceErrorBuilder.ERROR_STATE);
			map.put("msg", "没有服务人员无法结算");
			return map;
		}
		return null;
	}
		
	
	public static Map<String, Object> validateBeforeUpdateCancelState(OrderSer measure) {
		Map<String, Object> map = new HashMap<>();
		if(measure.getIsClear()){
			map.put("code", OrderServiceErrorBuilder.ERROR_CANCELSTATE);
			map.put("msg", "还未结算的无法取消结算");
			return map;
		}
		
		return null;
	}
}