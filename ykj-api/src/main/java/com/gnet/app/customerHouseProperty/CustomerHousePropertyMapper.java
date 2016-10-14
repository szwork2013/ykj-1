package com.gnet.app.customerHouseProperty;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface CustomerHousePropertyMapper extends Mapper<CustomerHouseProperty>{

  public List<CustomerHouseProperty> findCustomerHouses(@Param("customerId") String customerId);


}
