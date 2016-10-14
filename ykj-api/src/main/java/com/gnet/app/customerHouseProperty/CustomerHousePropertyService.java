package com.gnet.app.customerHouseProperty;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomerHousePropertyService {

	@Autowired
	private CustomerHousePropertyMapper customerHousePropertyMapper;
	
	/**
	 * 返回客户已有的房产
	 * @param customerId
	 * @return
	 */
	public List<CustomerHouseProperty> findCustomerHouses(String customerId) {
		return customerHousePropertyMapper.findCustomerHouses(customerId);
	}
    
	/**
	 * 返回客户某处房产的详细信息
	 * @param customerHouseId
	 * @return
	 */
	public CustomerHouseProperty findById(String customerHouseId) {
		return customerHousePropertyMapper.selectByPrimaryKey(customerHouseId);
	}

	
	@Transactional(readOnly = false)
	public Boolean create(CustomerHouseProperty customerHouseProperty) {
		return customerHousePropertyMapper.insertSelective(customerHouseProperty) == 1;
	}

	@Transactional(readOnly = false)
	public Boolean update(CustomerHouseProperty customerHouseProperty) {
		return customerHousePropertyMapper.updateByPrimaryKeySelective(customerHouseProperty) == 1;
	}

	@Transactional(readOnly = false)
	public boolean delete(String customerHouseId) {
		return customerHousePropertyMapper.deleteByPrimaryKey(customerHouseId) == 1;
	}
	
	
}
