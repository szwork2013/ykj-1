package com.gnet.app.supplier;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.utils.spring.SpringContextHolder;

public class SupplierValidator {
	
	private SupplierValidator(){}
	
	static Map<String, Object> validateBeforeCreateSupplier(Supplier supplier) {
		Map<String, Object> map = new HashMap<>();
		SupplierService supplierService = SpringContextHolder.getBean(SupplierService.class);
		
		// 供货商名称不能为空过滤
		if (StringUtils.isBlank(supplier.getSupplierName())) {
			map.put("code", SupplierErrorBuilder.ERROR_SUPPLIERNAME_NULL);
			map.put("msg", "供货商名称不能为空");
			return map;
		}

		// 电子邮箱不能为空过滤
		if (StringUtils.isBlank(supplier.getContactEmail())) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTEMAIL_NULL);
			map.put("msg", "电子邮箱不能为空");
			return map;
		}

		// 联系人不能为空过滤
		if (StringUtils.isBlank(supplier.getContactName())) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTNAME_NULL);
			map.put("msg", "联系人不能为空");
			return map;
		}

		// 商家编号不能为空过滤
		if (StringUtils.isBlank(supplier.getBusinessId())) {
			map.put("code", SupplierErrorBuilder.ERROR_BUSINESSID_NULL);
			map.put("msg", "商家编号不能为空");
			return map;
		}

		// 联系电话不能为空过滤
		if (StringUtils.isBlank(supplier.getContactPhone())) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTPHONE_NULL);
			map.put("msg", "联系电话不能为空");
			return map;
		}

		if (StringUtils.isNotBlank(supplier.getContactName()) && supplier.getContactName().length() > 50 ) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTNAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(supplier.getContactPhone()) && supplier.getContactPhone().length() > 20 ) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTPHONE_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
				
		if (StringUtils.isNotBlank(supplier.getId()) && supplier.getId().length() > 36 ) {
			map.put("code", SupplierErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(supplier.getSupplierName()) && supplier.getSupplierName().length() > 50 ) {
			map.put("code", SupplierErrorBuilder.ERROR_SUPPLIERNAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(supplier.getContactAddress()) && supplier.getContactAddress().length() > 255 ) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTADDRESS_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(supplier.getBusinessId()) && supplier.getBusinessId().length() > 36 ) {
			map.put("code", SupplierErrorBuilder.ERROR_BUSINESSID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(supplier.getContactEmail()) && supplier.getContactEmail().length() > 50 ) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTEMAIL_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if(supplierService.isCreateExist(supplier.getSupplierName(), supplier.getBusinessId())){
			map.put("code", SupplierErrorBuilder.ERROR_SUPPLIERNAME_REPEAT);
			map.put("msg", "品牌供货商名称重复");
			return map;
		}
		
		return null;
	}
	
	
	static Map<String, Object> validateBeforeUpdateSupplier(Supplier supplier) {
		Map<String, Object> map = new HashMap<>();
		SupplierService supplierService = SpringContextHolder.getBean(SupplierService.class);
		if (supplier.getId() == null) {
			map.put("code", SupplierErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "品牌供货商编号为空");
			return map;
		}
		
		Supplier oldSupplier = supplierService.findById(supplier.getId());
		supplier.setBusinessId(oldSupplier.getBusinessId());
		// 供货商名称不能为空过滤
		if (StringUtils.isBlank(supplier.getSupplierName())) {
			map.put("code", SupplierErrorBuilder.ERROR_SUPPLIERNAME_NULL);
			map.put("msg", "供货商名称不能为空");
			return map;
		}

		// 电子邮箱不能为空过滤
		if (StringUtils.isBlank(supplier.getContactEmail())) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTEMAIL_NULL);
			map.put("msg", "电子邮箱不能为空");
			return map;
		}

		// 联系人不能为空过滤
		if (StringUtils.isBlank(supplier.getContactName())) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTNAME_NULL);
			map.put("msg", "联系人不能为空");
			return map;
		}

		// 商家编号不能为空过滤
		if (StringUtils.isBlank(supplier.getBusinessId())) {
			map.put("code", SupplierErrorBuilder.ERROR_BUSINESSID_NULL);
			map.put("msg", "商家编号不能为空");
			return map;
		}

		// 联系电话不能为空过滤
		if (StringUtils.isBlank(supplier.getContactPhone())) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTPHONE_NULL);
			map.put("msg", "联系电话不能为空");
			return map;
		}

		if (StringUtils.isNotBlank(supplier.getContactName()) && supplier.getContactName().length() > 50 ) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTNAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(supplier.getContactPhone()) && supplier.getContactPhone().length() > 20 ) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTPHONE_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
				
		if (StringUtils.isNotBlank(supplier.getId()) && supplier.getId().length() > 36 ) {
			map.put("code", SupplierErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(supplier.getSupplierName()) && supplier.getSupplierName().length() > 50 ) {
			map.put("code", SupplierErrorBuilder.ERROR_SUPPLIERNAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(supplier.getContactAddress()) && supplier.getContactAddress().length() > 255 ) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTADDRESS_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(supplier.getBusinessId()) && supplier.getBusinessId().length() > 36 ) {
			map.put("code", SupplierErrorBuilder.ERROR_BUSINESSID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(supplier.getContactEmail()) && supplier.getContactEmail().length() > 50 ) {
			map.put("code", SupplierErrorBuilder.ERROR_CONTACTEMAIL_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if(supplierService.isModifyExist(supplier.getSupplierName(), oldSupplier.getSupplierName(), supplier.getBusinessId())){
			map.put("code", SupplierErrorBuilder.ERROR_SUPPLIERNAME_REPEAT);
			map.put("msg", "品牌供货商名称重复");
			return map;
		}
		
		return null;
	}
	
	
	static Map<String, Object> validateBeforeDeleteSupplier(String id) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isBlank(id)) {
			map.put("code", SupplierErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "品牌供货商编号为空");
			return map;
		}
		return null;
	}
	
}