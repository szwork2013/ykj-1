package com.gnet.app.customerTrack;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import tk.mybatis.mapper.common.Mapper;

public interface CustomerTrackMapper extends Mapper<CustomerTrack>{

	public CustomerTrack findLatestTrack(@Param("customerId") String customerId);

	public List<CustomerTrack> selectAllByCustomerId(@Param("customerId") String customerId);

	public CustomerTrack findByCustomerTrackId(@Param("customerTrackId") String customerTrackId);

}
