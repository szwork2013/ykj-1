package com.gnet.app.hello;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;

public interface HelloMapper extends 
	Mapper<Hello> {
	
	public List<Hello> selectHellos();

}
