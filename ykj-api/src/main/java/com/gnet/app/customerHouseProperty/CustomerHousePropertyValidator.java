package com.gnet.app.customerHouseProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.app.codeword.Codeword;
import com.gnet.app.customer.CustomerErrorBuilder;
import com.gnet.codeword.CodewordGetter;
import com.gnet.utils.spring.SpringContextHolder;


public class CustomerHousePropertyValidator {
	
	private CustomerHousePropertyValidator(){}
			
	public static Map<String, Object> validateBeforeCreateCustomerHouseProperty(CustomerHouseProperty customerHouseProperty){
		
		Map<String, Object> map = new HashMap<>();
		CustomerHousePropertyValidator customerHousePropertyValidator = new CustomerHousePropertyValidator();
		
		
		if(StringUtils.isBlank(customerHouseProperty.getCustomerId())){
			map.put("code", CustomerErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "客户编号为空");
			return map;
		}
		
		if(StringUtils.isBlank(customerHouseProperty.getBuildingName())){
			map.put("code", CustomerHousePropertyErrorBuilder.ERROR_BUILDING_NAME_NULL);
			map.put("msg", "楼盘名称为空");
			return map;
		}
	    
		if(customerHouseProperty.getRoomStyle() == null){
			map.put("code", CustomerHousePropertyErrorBuilder.ERROR_ROOM_STYLE_NULL);
			map.put("msg", "装修风格为空");
			return map;
		}
		
		if(customerHouseProperty.getDecorateProcess() == null){
			map.put("code", CustomerHousePropertyErrorBuilder.ERROR_DECORATE_PROCESS_NULL);
			map.put("msg", "装修进度为空");
			return map;
		}
		
		if(customerHouseProperty.getDecorateType() == null){
			map.put("code", CustomerHousePropertyErrorBuilder.ERROR_DECORATE_TYPE_NULL);
			map.put("msg", "装修类型为空");
			return map;
		}
		
		if(customerHouseProperty.getRoomStyle() == null){
			map.put("code", CustomerHousePropertyErrorBuilder.ERROR_ROOM_STYLE_NULL);
			map.put("msg", "户型为空");
			return map;
		}
			
		if(!customerHousePropertyValidator.isContain("DECORATE_STYLE", customerHouseProperty.getRoomStyle())){
			map.put("code", CustomerHousePropertyErrorBuilder.ERROR_ROOM_STYLE);
		    map.put("msg", "没有这种装修风格");
			return map;
		}
		
		if(!customerHousePropertyValidator.isContain("DECORATE_PROCESS", customerHouseProperty.getDecorateProcess())){
			map.put("code", CustomerHousePropertyErrorBuilder.ERROR_DECORATE_PROCESS);
			map.put("msg", "没有这种装修进度");
			return map;
		}
		
		if(!customerHousePropertyValidator.isContain("DECORATE_TYPE", customerHouseProperty.getDecorateType())){
			map.put("code", CustomerHousePropertyErrorBuilder.ERROR_DECORATE_TYPE);
			map.put("msg", "没有这种装修类型");
			return map;
		}
		
		if(!customerHousePropertyValidator.isContain("HOUSE_TYPE", customerHouseProperty.getRoomModel())){
			map.put("code", CustomerHousePropertyErrorBuilder.ERROR_ROOM_MODEL);
			map.put("msg", "没有这种户型");
			return map;
		}
		
		return null;
		
	}

	
	/**
	 * 用于判断新增的装修类型等是否符合要求
	 * @param codewords
	 * @param code
	 * @return
	 */	
	public boolean isContain(String codeWord, Integer code){ 
		CodewordGetter getter = SpringContextHolder.getBean(CodewordGetter.class);
		List<Codeword> codewords = getter.getCodewordsByTypeCode(codeWord);
		if(!codewords.isEmpty()){
			List<String> codes = new ArrayList<>();
			for(Codeword codeword : codewords){
				codes.add(codeword.getCode());
			}
			if(!codes.contains(String.valueOf(code))){
				return false;
			}
		}
		return true;
	}

}
