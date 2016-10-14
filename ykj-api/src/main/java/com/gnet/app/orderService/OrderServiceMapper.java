package com.gnet.app.orderService;

import tk.mybatis.mapper.common.Mapper;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OrderServiceMapper extends Mapper<OrderSer> {
	
	public int deleteById(@Param("id") String id, @Param("date") Date date);

	public List<OrderSer> findAll(@Param("orderList") List<String> orderList, @Param("orderId") String orderId, @Param("type") Integer type);

	public List<OrderSer> selectAllList(@Param("orderId") String orderId, @Param("type") Integer type);

	public OrderSer findById(@Param("id") String id);

	public Integer isCreateExist(@Param("serviceCode") String serviceCode, @Param("businessId") String businessId);

	public Integer isModifyExist(@Param("serviceCode") String serviceCode, @Param("oldServiceCode") String oldServiceCode, @Param("businessId") String businessId);
	
	public OrderSer selectServiceUnFinishWithoutMaintenance(@Param("orderId") String orderId);
	
	public List<OrderSer> finishServiceNum(@Param("orderId") String orderId, @Param("type") Integer type);
}
