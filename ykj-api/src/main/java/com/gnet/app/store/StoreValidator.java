package com.gnet.app.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.app.business.BusinessErrorBuilder;
import com.gnet.app.clerk.ClerkService;
import com.gnet.app.office.OfficeService;
import com.gnet.utils.spring.SpringContextHolder;

public class StoreValidator {
	
	private StoreValidator(){}
	
	public static Map<String, Object> validateBeforeCreateStore(Store store) {
		Map<String, Object> map = new HashMap<>();
		StoreService storeService = SpringContextHolder.getBean(StoreService.class);
		
		// 门店名称不能为空过滤
		if (StringUtils.isBlank(store.getName())) {
			map.put("code", StoreErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "门店名称不能为空");
			return map;
		}

		// 联系电话不能为空过滤
		if (StringUtils.isBlank(store.getContactNumber())) {
			map.put("code", StoreErrorBuilder.ERROR_CONTACTNUMBER_NULL);
			map.put("msg", "联系电话不能为空");
			return map;
		}

		// 商家编号不能为空过滤
		if (StringUtils.isBlank(store.getBusinessId())) {
			map.put("code", StoreErrorBuilder.ERROR_BUSINESSID_NULL);
			map.put("msg", "商家编号不能为空");
			return map;
		}

		// 联系人不能为空过滤
		if (StringUtils.isBlank(store.getContactPerson())) {
			map.put("code", StoreErrorBuilder.ERROR_CONTACTPERSON_NULL);
			map.put("msg", "联系人不能为空");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getAddress()) && store.getAddress().length() > 255 ) {
			map.put("code", StoreErrorBuilder.ERROR_ADDRESS_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getContactPerson()) && store.getContactPerson().length() > 50 ) {
			map.put("code", StoreErrorBuilder.ERROR_CONTACTPERSON_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getName()) && store.getName().length() > 50 ) {
			map.put("code", StoreErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getId()) && store.getId().length() > 36 ) {
			map.put("code", StoreErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getBusinessId()) && store.getBusinessId().length() > 36 ) {
			map.put("code", StoreErrorBuilder.ERROR_BUSINESSID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getContactNumber()) && store.getContactNumber().length() > 20 ) {
			map.put("code", StoreErrorBuilder.ERROR_CONTACTNUMBER_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}

		if(storeService.isCreateExist(store.getName(), store.getBusinessId())){
			map.put("code", StoreErrorBuilder.ERROR_NAME_REPEAT);
			map.put("msg", "门店名称重复");
			return map;
		}
		return null;
	}
	
	public	static Map<String, Object> validateBeforeUpdateStore(Store store) {
		Map<String, Object> map = new HashMap<>();
		StoreService storeService = SpringContextHolder.getBean(StoreService.class);
		
		if (StringUtils.isBlank(store.getId())) {
			map.put("code", StoreErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "门店编号为空");
			return map;
		}
		
		Store oldStore = storeService.findById(store.getId());
        if(oldStore == null){
			map.put("code", StoreErrorBuilder.ERROR_STORE_NULL);
			map.put("msg", "门店信息为空");
			return map;
        }

		// 门店名称不能为空过滤
		if (StringUtils.isBlank(store.getName())) {
			map.put("code", StoreErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "门店名称不能为空");
			return map;
		}

		// 联系电话不能为空过滤
		if (StringUtils.isBlank(store.getContactNumber())) {
			map.put("code", StoreErrorBuilder.ERROR_CONTACTNUMBER_NULL);
			map.put("msg", "联系电话不能为空");
			return map;
		}

		// 商家编号不能为空过滤
		if (StringUtils.isBlank(store.getBusinessId())) {
			map.put("code", StoreErrorBuilder.ERROR_BUSINESSID_NULL);
			map.put("msg", "商家编号不能为空");
			return map;
		}

		// 联系人不能为空过滤
		if (StringUtils.isBlank(store.getContactPerson())) {
			map.put("code", StoreErrorBuilder.ERROR_CONTACTPERSON_NULL);
			map.put("msg", "联系人不能为空");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getAddress()) && store.getAddress().length() > 255 ) {
			map.put("code", StoreErrorBuilder.ERROR_ADDRESS_TOOLONG);
			map.put("msg", "长度不能超过255");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getContactPerson()) && store.getContactPerson().length() > 50 ) {
			map.put("code", StoreErrorBuilder.ERROR_CONTACTPERSON_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getName()) && store.getName().length() > 50 ) {
			map.put("code", StoreErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getId()) && store.getId().length() > 36 ) {
			map.put("code", StoreErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getBusinessId()) && store.getBusinessId().length() > 36 ) {
			map.put("code", StoreErrorBuilder.ERROR_BUSINESSID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(store.getContactNumber()) && store.getContactNumber().length() > 20 ) {
			map.put("code", StoreErrorBuilder.ERROR_CONTACTNUMBER_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}

		if(storeService.isModifyExist(store.getName(), oldStore.getName(), store.getBusinessId())){
			map.put("code", StoreErrorBuilder.ERROR_NAME_REPEAT);
			map.put("msg", "商家名称重复");
			return map;
		}
		
		return null;
	}
	
	
	static Map<String, Object> validateBeforeDeleteStore(String id) {
		Map<String, Object> map = new HashMap<>();
		OfficeService officeService = SpringContextHolder.getBean(OfficeService.class);
		ClerkService clerkService = SpringContextHolder.getBean(ClerkService.class);
		if(officeService.existRelation(id)){
			map.put("code", StoreErrorBuilder.ERROR_EXIST_RELATION);
			map.put("msg", "门店下存在部门不允许删除");
			return map;
		}
		
		if(clerkService.storeExistClerk(id)){
			map.put("code", BusinessErrorBuilder.ERROR_EXIST_RELATION);
			map.put("msg", "门店下存在职员不允许删除");
			return map;
		}
		if (StringUtils.isBlank(id)) {
			map.put("code", StoreErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "门店编号为空");
			return map;
		}
		return null;
	}
		
}