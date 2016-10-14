package com.gnet.app.orderServiceAttachment;

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class OrderServiceAttachmentErrorBuilder extends BaseErrorBuilder {
	
	/** 附件信息为空 **/
	public static Integer ERROR_ORDERSERVICEATTACHMENT_NULL = 15000;
	
	/** 附件编号为空 **/
	public static Integer ERROR_ID_NULL = 15001;
	
	public OrderServiceAttachmentErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}
