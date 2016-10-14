package com.gnet.app.customerTrack;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.app.clerkWeekly.ClerkWeekly;
import com.gnet.app.clerkWeekly.ClerkWeeklyMapper;
import com.gnet.app.customerTrack.CustomerTrackMapper;
import com.gnet.utils.date.DateUtil;
import com.gnet.utils.page.PageUtil;
import com.gnet.utils.page.PageUtil.Callback;

@Service
@Transactional(readOnly = true)
public class CustomerTrackService {
	
	@Autowired
	private CustomerTrackMapper customerTrackMapper;
	@Autowired
	private ClerkWeeklyMapper clerkWeeklyMapper;
	
	/**
	 * 返回客户最新的跟踪记录
	 * @param customerId
	 * @return
	 */
	public CustomerTrack findLatestTrack(String customerId){
		return customerTrackMapper.findLatestTrack(customerId);
	}


	public Page<CustomerTrack> pagination(Pageable pageable, List<String> orderList, String customerId) {
		return PageUtil.pagination(pageable, orderList, new Callback<CustomerTrack>() {
			
			@Override
			public List<CustomerTrack> getPageContent(){
				return customerTrackMapper.selectAllByCustomerId(customerId);
				
			}
		});
	}

	
	/**
	 * 新增客户跟进记录
	 * @param customerTrack
	 * @return
	 */
	//以后下次跟进时间不为空的话，将会加到一张表中用于短信提醒
	@Transactional(readOnly = false)
	public Boolean create(CustomerTrack customerTrack) {
		Date date = new Date();
		Boolean result;
		Date nextTrackTime = customerTrack.getNextTrackTime();
		String clerkId = customerTrack.getCustomerResponsibleId();
		
		result = customerTrackMapper.insertSelective(customerTrack) == 1;
		if(!result){
			return false;
		}
		
		return result;
	}

	
	/**
	 * 更新客户跟进记录
	 * @param customerTrack
	 * @return
	 */
	//下次跟进时间发生变化的话(如果当前时间没有超过提醒时间)，需要修改短信提醒表的内容
	@Transactional(readOnly = false)
	public Boolean update(CustomerTrack customerTrack) {
		Boolean result;
		//判断下次跟进有没有改变，并且改变的时间在工作周表中是否已经有数据存在
		Date nextTrackTime = customerTrack.getNextTrackTime();
		result = customerTrackMapper.updateByPrimaryKeySelective(customerTrack) == 1;
		if(!result){
			return false;
		}
		return result;
	}
	
	
	/**
	 * 返回客户某条跟踪记录
	 * @param customerTrackId
	 * @return
	 */
	public CustomerTrack findById(String customerTrackId) {
		return customerTrackMapper.findByCustomerTrackId(customerTrackId);
	}

	
	
    /**
     * 删除客户跟进记录
     * @param customerTrackId
     * @return
     */
	//如果短信提醒表中也有数据的话，(如果当前时间没有超过提醒时间)一起删除
    @Transactional(readOnly = false)
	public boolean delete(String customerTrackId) {
    	Boolean result;
    	CustomerTrack oldCustomerTrack = customerTrackMapper.selectByPrimaryKey(customerTrackId);
    	String attachmentRoot = oldCustomerTrack.getAttachmentRoot(); 
    	
    	result = customerTrackMapper.deleteByPrimaryKey(customerTrackId) == 1;
    	if(!result){
    		return false;
    	}
    	//如果附件不为空则同时删除附件
/*    	if(StringUtils.isNotBlank(attachmentRoot)){
    		//到时候通过configutils方法获取路径加上attachmentRoot即可得到文件
    		File file = new file();
    		result = file.delete();
    	}*/
		return result;
	}

}
