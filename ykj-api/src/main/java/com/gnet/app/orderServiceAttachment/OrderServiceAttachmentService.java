package com.gnet.app.orderServiceAttachment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderServiceAttachmentService {
	
	@Autowired
	private OrderServiceAttachmentMapper orderServiceAttachmentMapper;
	
	public OrderServiceAttachment findById(String id) {
		return orderServiceAttachmentMapper.selectByPrimaryKey(id);
	}
	
	@Transactional(readOnly = false)
	public Boolean create(OrderServiceAttachment orderServiceAttachment) {
		return orderServiceAttachmentMapper.insertSelective(orderServiceAttachment) == 1;
	}
	
	@Transactional(readOnly = false)
	public Boolean delete(String id) {
		return orderServiceAttachmentMapper.deleteByPrimaryKey(id) == 1;
	}
		
}