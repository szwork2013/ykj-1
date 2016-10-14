package com.gnet.app.orderGood;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface OrderGoodMapper extends Mapper<OrderGood> {
	
	public int insertAllOrderGoods(@Param("goods") List<OrderGood> orderGoods);

	public Integer batchUpdateDeliverNum(@Param("ordersGoods") List<OrderGood> ordersGoods);
	
	public List<OrderGood> selectAllUnderOrder(@Param("orderId") String orderId);
	
	public List<OrderGood> selectAllUnderOrderWithGoodInfo(@Param("orderId") String orderId);
	
	public int updateAllOrderGoods(@Param("goods") List<OrderGood> orderGoods);
	
	public int deleteByIds(@Param("ids") Collection<String> ids);
	

	public List<OrderGood> findAll(@Param("orderList") List<String> orderList, @Param("orderId") String orderId, @Param("type") Integer type);

	public List<OrderGood> selectAllList(@Param("orderId") String orderId, @Param("type") Integer type);
	
	public Integer needServiceNum(@Param("orderId") String orderId, @Param("type") Integer type);
	
	public Integer goodsNum(@Param("orderId") String orderId);
}
