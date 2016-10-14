package com.gnet.app.tags;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.app.customerTags.CustomerTags;
import com.gnet.app.customerTags.CustomerTagsMapper;

@Service
@Transactional(readOnly = true)
public class TagsService {
	
	@Autowired
	private TagsMapper tagsMapper;
	@Autowired
	private CustomerTagsMapper customerTagsMapper;

	public List<Tags> findCustomerTags(String customerId) {
		return tagsMapper.findCustomerTags(customerId);
	}

	public Tags findById(String tagsId) {
		return tagsMapper.selectByPrimaryKey(tagsId);
	}
	
	/**
	 * 给客户增加标签
	 * @param tags
	 * @param customerId 
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean createCustomerTags(Tags tags, String customerId) {
		Date date = new Date();
		//数据库已经存在的客户标签
		List<Tags> tagsList = tagsMapper.selectAll();
		String tagsId = null;
		if(!tagsList.isEmpty()){
			for(Tags tag : tagsList){
				if(tag.getName().equals(tags.getName())){
					tagsId = tag.getId();
					break;
				}
			}
		}
		
		//数据库没有的新标签名称
		if(StringUtils.isBlank(tagsId)){
			if(tagsMapper.insertSelective(tags) != 1){
				return false;
			}
			tagsId = tags.getId();
		}
		
		CustomerTags customerTags = new CustomerTags();
		customerTags.setCreateDate(date);
		customerTags.setModifyDate(date);
		customerTags.setCustomerId(customerId);
		customerTags.setTagId(tagsId);
		if(customerTagsMapper.insertSelective(customerTags) != 1){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}

}
