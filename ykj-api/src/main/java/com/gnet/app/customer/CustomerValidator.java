package com.gnet.app.customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import com.gnet.utils.spring.SpringContextHolder;

public class CustomerValidator {
	
	private CustomerValidator(){}

	
	/**
	 * 保存客户之前需要验证
	 * @param customer
	 * @param tags 
	 * @return
	 */
	public static Map<String, Object> validateBeforeCreateCustomer(Customer customer, String[] tags) {
		CustomerService customerService = SpringContextHolder.getBean(CustomerService.class);
		Map<String, Object> map = new HashMap<>();
		String name = customer.getName();
		String phone = customer.getPhone();
		String businessId = customer.getBusinessId();
		
		if(StringUtils.isBlank(name)){
			map.put("code", CustomerErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "客户姓名为空");
			return map;
		}
		
		if(StringUtils.isBlank(phone)){
			map.put("code", CustomerErrorBuilder.ERROR_PHONE_NULL);
			map.put("msg", "手机号码为空");
			return map;
		}
		if(StringUtils.isBlank(customer.getCustomerResponsibleId())){
			map.put("code", CustomerErrorBuilder.ERROR_CUSTOMER_RESPONSIBLE_NULL);
			map.put("msg", "客户负责人编号为空");
			return map;
		}
		
		if(customer.getSex() == null){
			map.put("code", CustomerErrorBuilder.ERROR_SEX_NULL);
			map.put("msg", "性别不能为空");
			return map;
		}
		
		if(customer.getNeedTime() == null){
			map.put("code", CustomerErrorBuilder.ERROR_NEED_TIME_NULL);
			map.put("msg", "购物需求不能为空");
			return map;
		}
		
		if(StringUtils.isBlank(businessId)){
			map.put("code", CustomerErrorBuilder.ERROR_BUSINESS_ID_NULL);
			map.put("msg", "商家编号不能为空");
			return map;
		}
		
		if(customer.getOriginType() == null){
			map.put("code", CustomerErrorBuilder.ERROR_ORIGIN_TYPE_NULL);
			map.put("msg", "客户来源不能为空");
			return map;
		}
		
		//同一商家下的用户的手机号码和名字不同重复
		if(customerService.isExist(name, phone, businessId)){
			map.put("code", CustomerErrorBuilder.ERROR_REPEAT);
			map.put("msg", "该客户已存在");
			return map;
		}
		
		if(tags != null && tags.length > 0 && isRepeat(tags)){
			map.put("code", CustomerErrorBuilder.ERROR_TAGS_REPEAT);
			map.put("msg", "标签重复了");
			return map;
		}
		
		return null;
	}
	
	
     /**
      * 判断是否有标签重复
     * @param tags
     * @return
     */
    public static boolean isRepeat(String[] tags){
    	//需要保存的标签
    	List<String> tagsList = Arrays.asList(tags);
    	//不重复标签
    	List<String> unRepeatList = new ArrayList<>();
    	//重复标签
    	List<String> repeatList = new ArrayList<>();
    	
    	for(String tag : tagsList){
    		String tagName = StringUtils.trim(tag);
    		if(!unRepeatList.contains(tagName)){
    			unRepeatList.add(tagName);
    		}else{
    			repeatList.add(tagName);
    		}
    	}
  
    	return repeatList.size() > 0;
    	 
     }

	/**
	 * 更改之前的客户信息验证
	 * @param customer
	 * @param oldCustomer
	 * @return
	 */
	public static Map<String, Object> validateBeforeUpdateCustomer(Customer customer, Customer oldCustomer) {
		CustomerService customerService = SpringContextHolder.getBean(CustomerService.class);
		Map<String, Object> map = new HashMap<>();
		String name = customer.getName();
		String phone = customer.getPhone();
		String oldPhone = oldCustomer.getPhone();
		String oldName = oldCustomer.getName();
		String businessId = oldCustomer.getBusinessId();
		
		if(StringUtils.isBlank(customer.getId())) {
			map.put("code", CustomerErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "客户编号为空");
			return map;
		}
			
		if(StringUtils.isBlank(name)){
			map.put("code", CustomerErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "客户姓名为空");
			return map;
		}
		
		if(StringUtils.isBlank(phone)){
			map.put("code", CustomerErrorBuilder.ERROR_PHONE_NULL);
			map.put("msg", "手机号码为空");
			return map;
		}
		
		if(StringUtils.isBlank(customer.getCustomerResponsibleId())){
			map.put("code", CustomerErrorBuilder.ERROR_CUSTOMER_RESPONSIBLE_NULL);
			map.put("msg", "客户负责人编号为空");
			return map;
		}
		
		if(customer.getSex() == null){
			map.put("code", CustomerErrorBuilder.ERROR_SEX_NULL);
			map.put("msg", "性别不能为空");
			return map;
		}
		
		if(customer.getNeedTime() == null){
			map.put("code", CustomerErrorBuilder.ERROR_NEED_TIME_NULL);
			map.put("msg", "购物需求不能为空");
			return map;
		}
		
		if(StringUtils.isBlank(businessId)){
			map.put("code", CustomerErrorBuilder.ERROR_BUSINESS_ID_NULL);
			map.put("msg", "商家编号不能为空");
			return map;
		}
		
		if(customer.getOriginType() == null){
			map.put("code", CustomerErrorBuilder.ERROR_ORIGIN_TYPE_NULL);
			map.put("msg", "客户来源不能为空");
			return map;
		}
		
		//同一商家下的用户的手机号码和名字不同重复
		if(customerService.isExist(name, phone, oldName, oldPhone, businessId)){
			map.put("code", CustomerErrorBuilder.ERROR_REPEAT);
			map.put("msg", "该客户已存在");
			return map;
		}
		
		return null;
	}


	/**
	 * 删除之前的信息验证
	 * @param id
	 * @return
	 */
	public static Map<String, Object> validateBeforeDeleteCustomer(String id) {
		Map<String, Object> map = new HashMap<>();
		
		if(StringUtils.isBlank(id)) {
			map.put("code", CustomerErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "客户编号为空");
			return map;
		}
		return null;
	}

   
	/**
	 * 设置客户有效前的验证
	 * @param oldCustomer
	 * @return
	 */
	public static Map<String, Object> validateBeforeSetCustomerEffectivity(Customer oldCustomer) {
		Map<String, Object> map = new HashMap<>();
		if(oldCustomer.getIsEffectivity()){
			map.put("code", CustomerErrorBuilder.ERROR_IS_EFFECTIVITY);
			map.put("msg", "客户已是有效无须再置为有效");
			return map;
		}
		return null;
	}
	

	/**
	 * 设置客户无效前的验证
	 * @param oldCustomer
	 * @return
	 */
	public static Map<String, Object> validateBeforeSetCustomerUnEffectivity(Customer oldCustomer) {
		Map<String, Object> map = new HashMap<>();
		if(!oldCustomer.getIsEffectivity()){
			map.put("code", CustomerErrorBuilder.ERROR_NOT_EFFECTIVITY);
			map.put("msg", "客户已是无效无须再置为无效");
			return map;
		}
		return null;
	}
	
}
