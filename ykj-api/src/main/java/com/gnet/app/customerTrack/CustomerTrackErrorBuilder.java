package com.gnet.app.customerTrack;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class CustomerTrackErrorBuilder extends BaseErrorBuilder{

	/** 跟踪记录编号为空 **/
	public static final Integer ERROR_ID_NULL = 10701;
	
	/** 跟进时间为空 **/
	public static final Integer ERROR_TIME_NULL = 10702;
	
	/** 跟进方式为空 **/
	public static final Integer ERROR_WAY_NULL = 10703;
	
	/** 客户跟进记录为空  **/
	public static final Integer ERROR_CUSTOMER_TRACK_NULL = 10704;
	
	/** 跟进方式不符合要求 **/
	public static final Integer ERROR_WAY = 10705;
	
	
	public CustomerTrackErrorBuilder(Integer code, String msg) {
		super(code, msg);
		
	   }

}
