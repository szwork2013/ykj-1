package com.gnet.app.customerTags;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface CustomerTagsMapper extends Mapper<CustomerTags>{

	public Integer insertCustomerTagsList(@Param("customerId") String customerId, @Param("date") Date date, @Param("tagsIds") List<String> tagsIds);

}
