package com.gnet.app.noticeMsg;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface NoticeMsgMapper extends Mapper<NoticeMsg> {

	public Integer deleteById(@Param("id") String id, @Param("date") Date date);

	public NoticeMsg findByFromId(@Param("id") String id);
	
}
