package com.gnet.app.codeword;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface CodewordMapper extends Mapper<Codeword> {
	
	public List<Codeword> selectCodewordsByBusiness(@Param("businessId") String businessId, @Param("typeValue") String typeValue);
	
	public List<Codeword> selectCodewordsByBusinessList(@Param("orderList") List<String> orderList, @Param("businessId") String businessId, @Param("typeValue") String typeValue);
	
	public Codeword findMaxCode(@Param("typeId") String typeId);
}
