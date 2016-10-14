package com.gnet.app.clerk;

import java.util.List;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface ClerkMapper extends Mapper<Clerk>{

	public List<Clerk> findAll();

	public Clerk findOneById(@Param("id") String id);
	
	public int softDeleteById(@Param("id") String id, @Param("date") Date date);
	
	public List<Clerk> selectClerksUnderBusiness(@Param("name") String name, @Param("businessId") String businessId);
	
	public List<Clerk> selectClerksListUnderBusiness(@Param("orderList") List<String> orderList, @Param("name") String name, @Param("businessId") String businessId);
	
	public List<Clerk> selectClerksUnderStore(@Param("name") String name, @Param("storeId") String storeId);
	
	public List<Clerk> selectClerksListUnderStore(@Param("orderList") List<String> orderList, @Param("name") String name, @Param("storeId") String storeId);
	
	public List<Clerk> selectClerksListUnderOffice(@Param("officeId") String officeId);

	public Integer businessExistClerk(@Param("id") String id);

	public Integer storeExistClerk(@Param("id") String id);
	
}
