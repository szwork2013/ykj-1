package com.gnet.app.customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.app.customerTags.CustomerTagsMapper;
import com.gnet.app.tags.Tags;
import com.gnet.app.tags.TagsMapper;
import com.gnet.utils.page.PageUtil;
import com.gnet.utils.page.PageUtil.Callback;

@Service
@Transactional(readOnly = true)
public class CustomerService {
	
	@Autowired
	private CustomerMapper customerMapper;
    @Autowired
    private TagsMapper tagsMapper;
    @Autowired
    private CustomerTagsMapper customerTagsMapper;
	
	public Page<Customer> pagination(Pageable pageable, List<String> orderList, Integer roleType, String officeId, String name, String phone, String buildingName) {
		return PageUtil.pagination(pageable, orderList, new Callback<Customer>() {
			
			@Override
			public List<Customer> getPageContent(){
				return customerMapper.selectAllByOfficeId(officeId, roleType, name, phone, buildingName);
				
			}
		});
	}


	public Page<Customer> pagination(Pageable pageable, List<String> orderList, String id, String name, String phone, String buildingName) {
        return PageUtil.pagination(pageable, orderList, new Callback<Customer>() {
			
			@Override
			public List<Customer> getPageContent(){
				return customerMapper.selectAllById(id, name, phone, buildingName);  
				
			}
		});
	}


	/**
	 * 客户信息包含客户负责人姓名
	 * @param id
	 * @return
	 */
	public Customer findDetailById(String id) {
		return customerMapper.selectOneById(id);
	}
	
	public Customer findById(String id) {
		return customerMapper.selectByPrimaryKey(id);
	}


	/**
	 * 同一商家下的客户是否重复
	 * @param name
	 * @param phone
	 * @param businessId
	 * @return
	 */
	public boolean isExist(String name, String phone, String businessId) {
		return customerMapper.isCreateExist(name, phone, businessId) > 0 ;
	}

	public boolean isExist(String name, String phone, String oldName, String oldPhone, String businessId) {
		return customerMapper.isUpdateExist(name, phone, oldName, oldPhone, businessId) > 0 ;
	}
	
	@Transactional(readOnly = false)
	public Boolean update(Customer customer) {
		return customerMapper.updateByPrimaryKeySelective(customer) == 1;
	}
	
    /**
     * 增加客户信息
     * @param customer
     * @param tags
     * @param clerkId
     * @return
     */
    @Transactional(readOnly = false)
	public Boolean create(Customer customer, String[] tags, String clerkId) {
    	Boolean result;
    	
    	result = customerMapper.insertSelective(customer) == 1;
		if(!result){
			return false;
		}
		
		//需要增加的标签不为空的时候
		if(tags != null && tags.length > 0){
			Date date = new Date();
			//客户需要关联的标签编号
			List<String> tagsIds = new ArrayList<>();
			//数据库已经存在的客户标签
			List<Tags> tagsList = tagsMapper.selectAll();
			//需要增加的标签名称集合
			List<String> addList = Arrays.asList(tags);
			//如果有新的标签则保存到数据库
			List<String> newTagsNameList = new ArrayList<>();
			
			if(!tagsList.isEmpty()){
				//已经存在的标签名称集合
				List<String> existList = new ArrayList<>();;
				for(Tags tag : tagsList){
					existList.add(tag.getName());
				}
				
				for(String name : addList){
					if(!existList.contains(name)){
						newTagsNameList.add(name);
					}
				}
			}
			
			if(!newTagsNameList.isEmpty()){
				result = tagsMapper.insertTagsList(clerkId, date, newTagsNameList) == newTagsNameList.size();
			}else if(tagsList.isEmpty()){
				result = tagsMapper.insertTagsList(clerkId, date, addList) == addList.size();
			}
			
			if(!result) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			
			//该客户已有的标签信息
			List<Tags> customerTags = tagsMapper.selectTagsbyName(addList);
			for(Tags tag : customerTags){
				tagsIds.add(tag.getId());
			}
			
			result = customerTagsMapper.insertCustomerTagsList(customer.getId(), date, tagsIds) == tagsIds.size();
			
			if(!result){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			
		}
		
		return true;
	}

    
   @Transactional(readOnly = false)
	public boolean deleteById(String id, Date date) {
		return customerMapper.deleteById(id, date) == 1;
	}

}
