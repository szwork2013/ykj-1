package com.gnet.app.business;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.app.office.Office;
import com.gnet.app.office.OfficeMapper;
import com.gnet.utils.page.PageUtil;

@Service
@Transactional(readOnly = true)
public class BusinessService {
	
	@Autowired
	private BusinessMapper businessMapper;
	@Autowired
	private OfficeMapper officeMapper;
	
	public Business findById(String id) {
		return businessMapper.findById(id);
	}
	
	public Business find(Business business) {
		return businessMapper.selectOne(business);
	}
	
	public List<Business> findAll(List<String> orderList) {
		return findAll(null, null, null, orderList);
	}
	
	public List<Business> findAll(String name, String saleBrands, String contactPerson, List<String> orderList) {
		return businessMapper.selectBusinessesAllList(orderList, name, saleBrands, contactPerson);
	}
	
	public Page<Business> pagination(Pageable pageable, List<String> orderList) {
		return pagination(pageable, null, null, null, orderList);
	}
	
	public Page<Business> pagination(Pageable pageable, String name, String saleBrands, String contactPerson, List<String> orderList) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<Business>() {
			
			@Override
			public List<Business> getPageContent() {
				return businessMapper.selectBusinessesAll(name, saleBrands, contactPerson);
			}
			
		});
	}
	
	/**
	 * 新增商家
	 * @param business
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean create(Business business) {
		Boolean result;
		result = businessMapper.insertSelective(business) == 1;
		if(!result){
			return false;
		}
		result = officeMapper.insertOffice(business.getId(), business.getCreateDate(), business.getModifyDate(), business.getName(), Office.BUSINESS_PARENTID_ID, 0, Office.TYPE_BUSINESS, business.getIsDel()) == 1;
		if(!result){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}
	
	
	/**
	 * 更新商家
	 * @param business
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean update(Business business) {
		Boolean result;
		result = businessMapper.updateByPrimaryKeySelective(business) == 1;
		if(!result){
			return false;
		}
		Office office = officeMapper.findById(business.getId());
		office.setModifyDate(business.getModifyDate());
		office.setName(business.getName());
		result = officeMapper.updateByPrimaryKeySelective(office) == 1;
		if(!result){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}
	
	
	/**
	 * 删除商家同时删除对应的部门
	 * @param id
	 * @param date
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean delete(String id, Date date) {
		Boolean result;
		result = businessMapper.deleteById(id, date) == 1;
        if(!result){
        	return false;
        }
        result = officeMapper.deleteById(id, date) == 1;
		if(!result){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}

	
	/**
	 * 新增时验证商家名称是否重复
	 * @param name
	 * @return
	 */
	public boolean isCreateExist(String name) {
		return businessMapper.isCreateExist(name) > 0;
	}

	public boolean isModifyExist(String name, String oldName) {
		return businessMapper.isModifyExist(name, oldName)  > 0;
	}
		
}