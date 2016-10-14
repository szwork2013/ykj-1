package com.gnet.app.business;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.app.clerk.ClerkService;
import com.gnet.app.office.OfficeService;
import com.gnet.utils.spring.SpringContextHolder;

public class BusinessValidator {
	
	private BusinessValidator(){}
	
	static Map<String, Object> validateBeforeCreateBusiness(Business business) {
		Map<String, Object> map = new HashMap<>();
		BusinessService businessService = SpringContextHolder.getBean(BusinessService.class);
		
		// 联系地址不能为空过滤
		if (StringUtils.isBlank(business.getAddress())) {
			map.put("code", BusinessErrorBuilder.ERROR_ADDRESS_NULL);
			map.put("msg", "联系地址不能为空");
			return map;
		}

		// 供货商名称不能为空过滤
		if (StringUtils.isBlank(business.getName())) {
			map.put("code", BusinessErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "供货商名称不能为空");
			return map;
		}

		// 联系电话不能为空过滤
		if (StringUtils.isBlank(business.getContactNumber())) {
			map.put("code", BusinessErrorBuilder.ERROR_CONTACTNUMBER_NULL);
			map.put("msg", "联系电话不能为空");
			return map;
		}

		// 联系人不能为空过滤
		if (StringUtils.isBlank(business.getContactPerson())) {
			map.put("code", BusinessErrorBuilder.ERROR_CONTACTPERSON_NULL);
			map.put("msg", "联系人不能为空");
			return map;
		}

		if (StringUtils.isNotBlank(business.getSaleBrands()) && business.getSaleBrands().length() > 100 ) {
			map.put("code", BusinessErrorBuilder.ERROR_SALEBRANDS_TOOLONG);
			map.put("msg", "长度不能超过100");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getAddress()) && business.getAddress().length() > 255 ) {
			map.put("code", BusinessErrorBuilder.ERROR_ADDRESS_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getContactPerson()) && business.getContactPerson().length() > 50 ) {
			map.put("code", BusinessErrorBuilder.ERROR_CONTACTPERSON_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getServiceCall()) && business.getServiceCall().length() > 20 ) {
			map.put("code", BusinessErrorBuilder.ERROR_SERVICECALL_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getPostcode()) && business.getPostcode().length() > 20 ) {
			map.put("code", BusinessErrorBuilder.ERROR_POSTCODE_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getContactNumber()) && business.getContactNumber().length() > 20 ) {
			map.put("code", BusinessErrorBuilder.ERROR_CONTACTNUMBER_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getName()) && business.getName().length() > 50 ) {
			map.put("code", BusinessErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getLogo()) && business.getLogo().length() > 255 ) {
			map.put("code", BusinessErrorBuilder.ERROR_LOGO_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getLocation()) && business.getLocation().length() > 50 ) {
			map.put("code", BusinessErrorBuilder.ERROR_LOCATION_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getId()) && business.getId().length() > 36 ) {
			map.put("code", BusinessErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if(businessService.isCreateExist(business.getName())){
			map.put("code", BusinessErrorBuilder.ERROR_NAME_REPEAT);
			map.put("msg", "商家名称重复");
			return map;
		}
		
		return null;
	}
	
	
	static Map<String, Object> validateBeforeUpdateBusiness(Business business) {
		Map<String, Object> map = new HashMap<>();
		BusinessService businessService = SpringContextHolder.getBean(BusinessService.class);
		if (StringUtils.isBlank(business.getId())) {
			map.put("code", BusinessErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "商家编号为空");
			return map;
		}
		Business oldBusiness = businessService.findById(business.getId());
		if(oldBusiness == null){
			map.put("code", BusinessErrorBuilder.ERROR_BUSINESS_NULL);
			map.put("msg", "找不到该商家");
			return map;
		}
		
		// 联系地址不能为空过滤
		if (StringUtils.isBlank(business.getAddress())) {
			map.put("code", BusinessErrorBuilder.ERROR_ADDRESS_NULL);
			map.put("msg", "联系地址不能为空");
			return map;
		}

		// 供货商名称不能为空过滤
		if (StringUtils.isBlank(business.getName())) {
			map.put("code", BusinessErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "供货商名称不能为空");
			return map;
		}

		// 联系电话不能为空过滤
		if (StringUtils.isBlank(business.getContactNumber())) {
			map.put("code", BusinessErrorBuilder.ERROR_CONTACTNUMBER_NULL);
			map.put("msg", "联系电话不能为空");
			return map;
		}

		// 联系人不能为空过滤
		if (StringUtils.isBlank(business.getContactPerson())) {
			map.put("code", BusinessErrorBuilder.ERROR_CONTACTPERSON_NULL);
			map.put("msg", "联系人不能为空");
			return map;
		}

		if (StringUtils.isNotBlank(business.getSaleBrands()) && business.getSaleBrands().length() > 100 ) {
			map.put("code", BusinessErrorBuilder.ERROR_SALEBRANDS_TOOLONG);
			map.put("msg", "长度不能超过100");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getAddress()) && business.getAddress().length() > 255 ) {
			map.put("code", BusinessErrorBuilder.ERROR_ADDRESS_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getContactPerson()) && business.getContactPerson().length() > 50 ) {
			map.put("code", BusinessErrorBuilder.ERROR_CONTACTPERSON_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getServiceCall()) && business.getServiceCall().length() > 20 ) {
			map.put("code", BusinessErrorBuilder.ERROR_SERVICECALL_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getPostcode()) && business.getPostcode().length() > 20 ) {
			map.put("code", BusinessErrorBuilder.ERROR_POSTCODE_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getContactNumber()) && business.getContactNumber().length() > 20 ) {
			map.put("code", BusinessErrorBuilder.ERROR_CONTACTNUMBER_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getName()) && business.getName().length() > 50 ) {
			map.put("code", BusinessErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getLogo()) && business.getLogo().length() > 255 ) {
			map.put("code", BusinessErrorBuilder.ERROR_LOGO_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getLocation()) && business.getLocation().length() > 50 ) {
			map.put("code", BusinessErrorBuilder.ERROR_LOCATION_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(business.getId()) && business.getId().length() > 36 ) {
			map.put("code", BusinessErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if(businessService.isModifyExist(business.getName(), oldBusiness.getName())){
			map.put("code", BusinessErrorBuilder.ERROR_NAME_REPEAT);
			map.put("msg", "商家名称重复");
			return map;
		}
		return null;
	}
	
	static Map<String, Object> validateBeforeDeleteBusiness(String id) {
		Map<String, Object> map = new HashMap<>();
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		ClerkService clerkService = SpringContextHolder.getBean(ClerkService.class);
		if(officeService.existRelation(id)){
			map.put("code", BusinessErrorBuilder.ERROR_EXIST_RELATION);
			map.put("msg", "商家下存在部门不允许删除");
			return map;
		}
		
		if(clerkService.businessExistClerk(id)){
			map.put("code", BusinessErrorBuilder.ERROR_EXIST_RELATION);
			map.put("msg", "商家下存在职员不允许删除");
			return map;
		}
		if(StringUtils.isBlank(id)){
			map.put("code", BusinessErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "商家编号为空");
			return map;
		}
		return null;
	}	
}