package com.gnet.app.customer;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface CustomerMapper extends Mapper<Customer>{

	public List<Customer> selectAllByOfficeId(@Param("officeId") String officeId, @Param("roleType") Integer roleType, @Param("name") String name, @Param("phone") String phone, @Param("buildingName") String buildingName);

	public List<Customer> selectAllById(@Param("id") String id, @Param("name") String name, @Param("phone") String phone, @Param("buildingName") String buildingName);

	public Customer selectOneById(@Param("id") String id);

	public Integer isCreateExist(@Param("name") String name, @Param("phone") String phone, @Param("businessId") String businessId);

	public Integer isUpdateExist(@Param("name") String name, @Param("phone") String phone, @Param("oldName") String oldName, @Param("oldPhone") String oldPhone, @Param("businessId") String businessId);

	public Integer deleteById(@Param("id") String id, @Param("date") Date date);
    
}
