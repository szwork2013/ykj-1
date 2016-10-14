package com.gnet.app.supplier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.utils.page.PageUtil;

@Service
@Transactional(readOnly = true)
public class SupplierService {
	
	@Autowired
	private SupplierMapper supplierMapper;
	
	public Supplier findById(String id) {
		return supplierMapper.selectByPrimaryKey(id);
	}
	
	public Supplier find(Supplier supplier) {
		return supplierMapper.selectOne(supplier);
	}
	
	public List<Supplier> findAll(List<String> orderList) {
		return findAll(null, null, orderList);
	}
	
	public List<Supplier> findAll(String supplierName, String contactName, List<String> orderList) {
		return supplierMapper.selectSuppliersAllList(orderList, supplierName, contactName);
	}
	
	public Page<Supplier> pagination(Pageable pageable, List<String> orderList) {
		return pagination(pageable, null, null, orderList);
	}
	
	public Page<Supplier> pagination(Pageable pageable, String supplierName, String contactName, List<String> orderList) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<Supplier>() {
			
			@Override
			public List<Supplier> getPageContent() {
				return supplierMapper.selectSuppliersAll(supplierName, contactName);
			}
			
		});
	}
	
	@Transactional(readOnly = false)
	public Boolean create(Supplier supplier) {
		return supplierMapper.insertSelective(supplier) == 1;
	}
	
	@Transactional(readOnly = false)
	public Boolean update(Supplier supplier) {
		return supplierMapper.updateByPrimaryKeySelective(supplier) == 1;
	}
	
	
	@Transactional(readOnly = false)
	public Boolean delete(String id) {
		return supplierMapper.deleteByPrimaryKey(id) == 1;
	}
	
	@Transactional(readOnly = false)
	public Boolean deleteAll(String[] ids) {
		if(ids == null) {
			return true;
		}
		
		return supplierMapper.deleteByIds(ids) == ids.length;
	}

	
	public boolean isCreateExist(String supplierName, String businessId) {
		return supplierMapper.isCreateExist(supplierName, businessId) > 0;
	}

	public boolean isModifyExist(String supplierName, String oldSupplierName, String businessId) {
		return supplierMapper.isModifyExist(supplierName, oldSupplierName, businessId) > 0;
	}
	
}