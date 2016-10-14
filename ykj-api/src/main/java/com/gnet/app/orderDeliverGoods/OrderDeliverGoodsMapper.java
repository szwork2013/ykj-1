package com.gnet.app.orderDeliverGoods;

import tk.mybatis.mapper.common.Mapper;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface OrderDeliverGoodsMapper extends Mapper<OrderDeliverGoods> {
	
	public int deleteById(@Param("id") String id, @Param("date") Date date);

	public Integer batchSave(@Param("serviceGoods") List<OrderDeliverGoods> serviceGoods, @Param("date") Date date, @Param("serviceId") String serviceId);

	public List<OrderDeliverGoods> findAllList(@Param("orderList") List<String> orderList, @Param("serviceId") String serviceId);

	public List<OrderDeliverGoods> selectAllList(@Param("serviceId") String serviceId);

	public Integer deleteAll(@Param("orderDeliverGoods") List<OrderDeliverGoods> orderDeliverGoods);
	
}
