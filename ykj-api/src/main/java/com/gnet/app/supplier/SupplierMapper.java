package com.gnet.app.supplier;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SupplierMapper extends Mapper<Supplier> {


	public int deleteByIds(@Param("ids") String ids[]);
	
	public List<Supplier> selectSuppliersAll(@Param("supplierName") String supplierName, @Param("contactName") String contactName);
	
	public List<Supplier> selectSuppliersAllList(@Param("orderList") List<String> orderList, @Param("supplierName") String supplierName, @Param("contactName") String contactName);

	public Integer isCreateExist(@Param("supplierName") String supplierName, @Param("businessId") String businessId);

	public Integer isModifyExist(@Param("supplierName") String supplierName, @Param("oldSupplierName") String oldSupplierName, @Param("businessId") String businessId);

}
