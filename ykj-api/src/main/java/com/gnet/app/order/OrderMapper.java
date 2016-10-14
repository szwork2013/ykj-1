package com.gnet.app.order;

import tk.mybatis.mapper.common.Mapper;
import java.util.Date;


import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OrderMapper extends Mapper<Order> {
	
	public int deleteById(@Param("id") String id, @Param("date") Date date);
	
	public int deleteByIds(@Param("ids") String ids[], @Param("date") Date date);
	
	public List<Order> selectOrdersPersonal(@Param("orderList") List<String> orderList, @Param("type") Integer type, @Param("orderSource") Integer orderSource, @Param("orderResponsibleName") String orderResponsibleName,
			@Param("customerName") String customerName, @Param("startOrderDate") String startOrderDate, @Param("endOrderDate") String endOrderDate, 
			@Param("mutiSearchColumn") String mutiSearchColumn, @Param("clerkId") String clerkId);
	
	public List<Order> selectOrdersUnderStore(@Param("orderList") List<String> orderList, @Param("type") Integer type, @Param("orderSource") Integer orderSource, @Param("orderResponsibleName") String orderResponsibleName,
			@Param("customerName") String customerName, @Param("startOrderDate") String startOrderDate, @Param("endOrderDate") String endOrderDate, 
			@Param("mutiSearchColumn") String mutiSearchColumn, @Param("storeId") String storeId);
	
	public List<Order> selectOrdersUnderBusiness(@Param("orderList") List<String> orderList, @Param("type") Integer type, @Param("orderSource") Integer orderSource, @Param("orderResponsibleName") String orderResponsibleName,
			@Param("customerName") String customerName, @Param("startOrderDate") String startOrderDate, @Param("endOrderDate") String endOrderDate, 
			@Param("mutiSearchColumn") String mutiSearchColumn, @Param("businessId") String businessId);
	
	public Order selectTodayLastCreateOrder(@Param("today") String date);
}
