package com.gnet.app.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.app.customer.CustomerErrorBuilder;
import com.gnet.utils.spring.SpringContextHolder;


public class TagsValidator {
	
	private TagsValidator(){}
	
	public static Map<String, Object> validateBeforeCreateTags(Tags tags, String customerId){
		TagsService tagsService = SpringContextHolder.getBean(TagsService.class);
		Map<String, Object> map = new HashMap<>();
		
		if(StringUtils.isBlank(tags.getName())){
			map.put("code", TagsErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "标签名称为空");
			return map;
		}
		
		List<Tags> customerTags = tagsService.findCustomerTags(customerId);
		if(!customerTags.isEmpty()){
			for(Tags tag : customerTags){
				if(tag.getName().equals(StringUtils.trim(tags.getName()))){
					map.put("code", CustomerErrorBuilder.ERROR_TAGS_REPEAT);
					map.put("msg", "标签重复了");
					return map;
				}	
			}
		}	
		return null;
		
	}

	
	/**
	 * 删除标签前的验证
	 * @param tagsId
	 * @return
	 */
	public static Map<String, Object> validateBeforeDeleteTags(String tagsId) {
		Map<String, Object> map = new HashMap<>();
		
		if(StringUtils.isBlank(tagsId)) {
			map.put("code", TagsErrorBuilder.ERROR_CUSTOMER_TAGS_NULL);
			map.put("msg", "客户关联标签编号为空");
			return map;
		}
		return null;
	}

}
