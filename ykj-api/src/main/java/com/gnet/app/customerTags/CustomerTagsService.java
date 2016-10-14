package com.gnet.app.customerTags;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class CustomerTagsService {
	
	@Autowired
	private CustomerTagsMapper customerTagsMapper;

	
	@Transactional(readOnly = false)
	public boolean delete(String tagsId) {
		return customerTagsMapper.deleteByPrimaryKey(tagsId) == 1;
	}


}
