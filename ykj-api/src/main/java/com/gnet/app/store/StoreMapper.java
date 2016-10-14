package com.gnet.app.store;

import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface StoreMapper extends Mapper<Store> {

	public List<Store> findAllByBusinessId(@Param("orderList") List<String> orderList, @Param("businessId") String businessId);

	public List<Store> selectAllByBusinessId(@Param("businessId") String businessId);

	public Store findById(@Param("id") String id);

	public Integer deleteById(@Param("id") String id, @Param("date") Date date);
	
	public Integer isCreateExist(@Param("name") String name, @Param("businessId") String businessId);

	public Integer isModifyExist(@Param("name") String name, @Param("oldName") String oldName, @Param("businessId") String businessId);

}
