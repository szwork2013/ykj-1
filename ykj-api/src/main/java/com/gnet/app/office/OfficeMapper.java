package com.gnet.app.office;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface OfficeMapper extends Mapper<Office>{

	public Integer insertOffice(@Param("id") String id, @Param("createDate") Date createDate, @Param("modifyDate") Date modifyDate, @Param("name") String name, @Param("parentId") String parentId, @Param("level") Integer level,
			@Param("type") Integer type, @Param("isDel") Boolean isDel);

	public Office findById(@Param("id") String id);

	public Integer existRelation(@Param("id") String id);

	public Integer deleteById(@Param("id") String id, @Param("date") Date date);

}
