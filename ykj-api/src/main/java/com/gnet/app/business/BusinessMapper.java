package com.gnet.app.business;

import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BusinessMapper extends Mapper<Business> {

	
	public List<Business> selectBusinessesAll(@Param("name") String name, @Param("saleBrands") String saleBrands, @Param("contactPerson") String contactPerson);
	
	public List<Business> selectBusinessesAllList(@Param("orderList") List<String> orderList, @Param("name") String name, @Param("saleBrands") String saleBrands, @Param("contactPerson") String contactPerson);

	public Integer isCreateExist(@Param("name") String name);

	public Business findById(@Param("id") String id);

	public Integer isModifyExist(@Param("name") String name, @Param("oldName") String oldName);

	public Integer deleteById(@Param("id") String id, @Param("date") Date date);

}
