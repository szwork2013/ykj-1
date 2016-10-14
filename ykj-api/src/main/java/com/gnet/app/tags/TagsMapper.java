package com.gnet.app.tags;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface TagsMapper extends Mapper<Tags> {

	public Integer insertTagsList(@Param("clerkId") String clerkId, @Param("date") Date date, @Param("newTagsNameList") List<String> newTagsNameList);

	public List<Tags> selectTagsbyName(@Param("addList") List<String> addList);

	public List<Tags> findCustomerTags(@Param("customerId") String customerId);


}
