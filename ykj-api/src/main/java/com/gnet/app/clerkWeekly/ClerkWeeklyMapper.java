package com.gnet.app.clerkWeekly;


import java.util.Date;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface ClerkWeeklyMapper extends Mapper<ClerkWeekly>{

 public ClerkWeekly findByDayAndClerkId(@Param("nextTrackTime") Date nextTrackTime, @Param("clerkId") String clerkId);

}
