package com.gnet.app.order;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.gnet.app.business.Business;
import com.gnet.app.business.BusinessMapper;
import com.gnet.utils.date.DateUtil;
import com.gnet.utils.pinyin.PinYinUtil;
import com.gnet.utils.spring.SpringContextHolder;

public class OrderKit {
	
	// 默认后三位
	private static final Integer valueLength = 2;
	
	// 订单生成默认后三位
	private static final Format valueFormat = new DecimalFormat("#000");
	
	private OrderKit() {};
	
	public static String generateOrderNo(String businessId) {
		Business business = SpringContextHolder.getBean(BusinessMapper.class).selectByPrimaryKey(businessId);
		String abbrNamePy = PinYinUtil.getFirstSpell(StringUtils.isBlank(business.getAbbrName()) ? business.getName() : business.getAbbrName()).toUpperCase();
		Order order = SpringContextHolder.getBean(OrderMapper.class).selectTodayLastCreateOrder(DateUtil.getCurrDate("yyyy-MM-dd"));
		StringBuilder orderNo = new StringBuilder(abbrNamePy + DateUtil.getCurrDate("yyyyMMdd"));
		if (order == null) {
			orderNo.append(valueFormat.format(0));
		} else {
			String prevNo = order.getOrderNo();
			Integer newNo = Integer.valueOf(prevNo.substring(prevNo.length() - valueLength, prevNo.length())) + 1;
			
			orderNo.append(valueFormat.format(newNo));
		}
		
		return orderNo.toString();
	}
	
	
}
