package com.gnet.app.store;

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
public class StoreService {
	
	@Autowired
	private StoreMapper storeMapper;
	@Autowired
	private OfficeMapper officeMapper;
	
	public Store findById(String id) {
		return storeMapper.findById(id);
	}
			
	/**
	 * 某个商家下的所有门店
	 * @param orderList
	 * @param businessId
	 * @return
	 */
	public List<Store> findAllByBusinessId(List<String> orderList, String businessId) {
		return storeMapper.findAllByBusinessId(orderList, businessId);
	}
	
	public Page<Store> pagination(Pageable pageable, List<String> orderList, String businessId) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<Store>() {
			
			@Override
			public List<Store> getPageContent() {
				return storeMapper.selectAllByBusinessId(businessId);
			}
			
		});
	}
	
	/**
	 * 新增门店
	 * @param store
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean create(Store store) {
		Boolean result;
		result = storeMapper.insertSelective(store) == 1;
		if(!result){
			return false;
		}
		result = officeMapper.insertOffice(store.getId(), store.getCreateDate(), store.getModifyDate(), store.getName(), store.getBusinessId(), 1, Office.TYPE_BUSINESS, store.getIsDel()) == 1;
		if(!result){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}
	
	
	/**
	 * 更新门店
	 * @param store
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean update(Store store) {
		Boolean result;
		result = storeMapper.updateByPrimaryKeySelective(store) == 1;
		if(!result){
			return false;
		}
		Office office = officeMapper.findById(store.getId());
		office.setModifyDate(store.getModifyDate());
		office.setName(store.getName());
		result = officeMapper.updateByPrimaryKeySelective(office) == 1;
		if(!result){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		return true;
	}
	
	
	/**
	 * 删除门店
	 * @param id
	 * @param date
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean delete(String id, Date date) {
		Boolean result;
		result = storeMapper.deleteById(id, date) == 1;
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

	
	public boolean isCreateExist(String name, String businessId) {
		return storeMapper.isCreateExist(name, businessId) > 0;
	}

	public boolean isModifyExist(String name, String oldName, String businessId) {
		return storeMapper.isModifyExist(name, oldName, businessId) > 0;
	}


		
}