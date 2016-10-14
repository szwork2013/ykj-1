package com.gnet.app.orderProcess;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class OrderProcessService {
	
	@Autowired
	private OrderProcessMapper orderProcessMapper;
	
	public OrderProcess findById(String id) {
		return orderProcessMapper.selectByPrimaryKey(id);
	}
	
	
}