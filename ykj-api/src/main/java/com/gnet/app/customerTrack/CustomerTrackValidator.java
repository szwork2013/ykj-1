package com.gnet.app.customerTrack;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.app.customer.CustomerErrorBuilder;

public class CustomerTrackValidator {
	
	private CustomerTrackValidator(){}

	public static Map<String, Object> validateBeforeCreateCustomerTrack(CustomerTrack customerTrack) {
		Map<String, Object> map = new HashMap<>();
		
		if(StringUtils.isBlank(customerTrack.getCustomerId())){
			map.put("code", CustomerErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "客户编号为空");
			return map;
		}
		
		if(StringUtils.isBlank(customerTrack.getCustomerResponsibleId())){
			map.put("code", CustomerErrorBuilder.ERROR_CUSTOMER_RESPONSIBLE_NULL);
			map.put("msg", "客户负责人编号为空");
			return map;
		}
		
		if(customerTrack.getTime() == null){
			map.put("code", CustomerTrackErrorBuilder.ERROR_TIME_NULL);
			map.put("msg", "跟进时间为空");
			return map;
		}
		
		if(customerTrack.getWay() == null){
			map.put("code", CustomerTrackErrorBuilder.ERROR_WAY_NULL);
			map.put("msg", "跟进方式为空");
			return map;
		}
		
		if(!customerTrack.getWay().equals(CustomerTrack.TRACK_WAY_INTERVIEW) && !customerTrack.getWay().equals(CustomerTrack.TRACK_WAY_MESSAGE) && !customerTrack.getWay().equals(CustomerTrack.TRACK_WAY_PHONE) && !customerTrack.getWay().equals(CustomerTrack.TRACK_WAY_WECHAT)){
			map.put("code", CustomerTrackErrorBuilder.ERROR_WAY);
			map.put("msg", "跟进方式不符合要求");
			return map;
		}
		
		return null;
	}

}
