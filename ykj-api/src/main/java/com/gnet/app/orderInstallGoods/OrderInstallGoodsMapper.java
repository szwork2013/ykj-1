package com.gnet.app.orderInstallGoods;

import tk.mybatis.mapper.common.Mapper;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderInstallGoodsMapper extends Mapper<OrderInstallGoods> {
	
	public int deleteById(@Param("id") String id, @Param("date") Date date);

	public Integer batchSave(@Param("serviceGoods") List<OrderInstallGoods> serviceGoods, @Param("date") Date date, @Param("serviceId") String serviceId);

	public List<OrderInstallGoods> findAllList(@Param("orderList") List<String> orderList, @Param("serviceId") String serviceId);

	public List<OrderInstallGoods> selectAllList(@Param("serviceId") String serviceId);

	public Integer deleteAll(@Param("orderInstallGoods") List<OrderInstallGoods> orderInstallGoods);
}
