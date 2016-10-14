package com.gnet.app.codeword;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.app.businessCodeword.BusinessCodeword;
import com.gnet.app.businessCodeword.BusinessCodewordMapper;
import com.gnet.utils.page.PageUtil;

@Service
@Transactional(readOnly = true)
public class CodewordService {
	
	@Autowired
	private CodewordMapper codewordMapper;
	@Autowired
	private BusinessCodewordMapper businessCodewordMapper;
	
	public Codeword findById(String id) {
		Codeword codeword = new Codeword();
		codeword.setDynamicTableName("sc_codeword");
		codeword.setId(id);
		return find(codeword);
	}
	
	public Codeword find(Codeword codeword) {
		return codewordMapper.selectOne(codeword);
	}
	
	public List<Codeword> findAll(List<String> orderList) {
		return findCodewords(orderList, null, null);
	}
	
	public List<Codeword> findCodewords(List<String> orderList, String businessId, String typeValue) {
		return codewordMapper.selectCodewordsByBusinessList(orderList, businessId, typeValue);
	}
	
	public Page<Codeword> pagination(Pageable pageable, List<String> orderList) {
		return pagination(pageable, null, null, orderList);
	}
	
	public Page<Codeword> pagination(Pageable pageable, final String businessId, final String typeValue, List<String> orderList) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<Codeword>() {
			@Override
			public List<Codeword> getPageContent() {
				return codewordMapper.selectCodewordsByBusiness(businessId, typeValue);
			}
			
		});
	}
	
	public Codeword findByTypeAndValue(String typeId, String value) {
		Codeword codeword = new Codeword();
		codeword.setCodewordTypeId(typeId);
		codeword.setValue(value);
		codeword.setDynamicTableName("sc_codeword");
		return codewordMapper.selectOne(codeword);
	}
	
	@Transactional(readOnly = false)
	public Boolean create(Codeword codeword, String businessId) {
		Codeword maxcodeword = codewordMapper.findMaxCode(codeword.getCodewordTypeId());
		
		Date date = new Date();
		codeword.setCode(maxcodeword == null ? "0" : String.valueOf(Integer.valueOf(maxcodeword.getCode()) + 1));
		codeword.setSuperid(Codeword.NO_PARENT_ID);
		codeword.setUpdateTime(date);
		codeword.setDynamicTableName("sc_codeword");
		codeword.setIsSystem(Boolean.FALSE);
		
		if (codewordMapper.insert(codeword) != 1) {
			return false;
		}
		
		BusinessCodeword businessCodeword = new BusinessCodeword();
		businessCodeword.setCreateDate(date);
		businessCodeword.setModifyDate(date);
		businessCodeword.setCodewordId(codeword.getId());
		businessCodeword.setBusinessId(businessId);
		
		if (businessCodewordMapper.insert(businessCodeword) != 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}
	
	@Transactional(readOnly = false)
	public Boolean delete(String id, String businessId) {
		BusinessCodeword businessCodeword = new BusinessCodeword();
		businessCodeword.setBusinessId(businessId);
		businessCodeword.setCodewordId(id);
		businessCodeword = businessCodewordMapper.selectOne(businessCodeword);
		if (businessCodeword == null) {
			return false;
		}
		
		if (businessCodewordMapper.delete(businessCodeword) != 1) {
			return false;
		}
		
		boolean result = true;
		if (businessCodewordMapper.selectCount(businessCodeword) == 0) {
			Codeword codeword = new Codeword();
			codeword.setDynamicTableName("sc_codeword");
			codeword.setId(id);
			result = codewordMapper.delete(codeword) == 1;
		}
		
		if (!result) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return result;
	}
	
}
