package com.gnet.app.businessCodeword;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BusinessCodewordService {
	
	@Autowired
	private BusinessCodewordMapper businessCodewordMapper;
	
	public BusinessCodeword findByWordAndBusiness(String codewordId, String businessId) {
		BusinessCodeword businessCodeword = new BusinessCodeword();
		businessCodeword.setCodewordId(codewordId);
		businessCodeword.setBusinessId(businessId);
		return businessCodewordMapper.selectOne(businessCodeword);
	}
	
	@Transactional(readOnly = false)
	public boolean createBusinessCodeword(String codewordId, String businessId) {
		Date date = new Date();
		BusinessCodeword businessCodeword = new BusinessCodeword();
		businessCodeword.setCreateDate(date);
		businessCodeword.setModifyDate(date);
		businessCodeword.setCodewordId(codewordId);
		businessCodeword.setBusinessId(businessId);
		return businessCodewordMapper.insert(businessCodeword) == 1;
	}
}
