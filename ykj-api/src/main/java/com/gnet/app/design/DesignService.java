package com.gnet.app.design;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.app.noticeMsg.NoticeMsg;
import com.gnet.app.noticeMsg.NoticeMsgService;
import com.gnet.app.orderProcess.OrderProcess;
import com.gnet.app.orderProcess.OrderProcessMapper;
import com.gnet.app.orderService.OrderSer;
import com.gnet.app.orderService.OrderServiceMapper;
import com.gnet.app.orderServiceAttachment.OrderServiceAttachment;
import com.gnet.app.orderServiceAttachment.OrderServiceAttachmentService;
import com.gnet.upload.FileUploadService;
import com.gnet.utils.date.DateUtil;
import com.gnet.utils.page.PageUtil;
import com.gnet.utils.spring.SpringContextHolder;

@Service
@Transactional(readOnly = true)
public class DesignService {
	
	@Autowired
	private OrderServiceMapper orderServiceMapper;
	
	public OrderSer findById(String id) {
		return orderServiceMapper.findById(id);
	}

	public List<OrderSer> findAll(List<String> orderList, String orderId, Integer type) {
		List<OrderSer> measures = orderServiceMapper.findAll(orderList, orderId, type);
		Date date =  new Date();
		if(!measures.isEmpty()){
			for(OrderSer measure : measures){
				if(StringUtils.isBlank(measure.getClerkId()) && (DateUtil.dayDiff(measure.getNeedTime(), date) > 0)){
					measure.setStatus(OrderSer.STATUS_OVERDUE);
				}else if(measure.getIsFinish()){
					measure.setStatus(OrderSer.STATUS_COMPLETE);
				}else if(StringUtils.isNotBlank(measure.getServicePosition())){
					measure.setStatus(OrderSer.STATUS_SIGN);
				}else if(StringUtils.isNotBlank(measure.getClerkId())){
					measure.setStatus(OrderSer.STATUS_ARRANGE);
				}else{
					measure.setStatus(OrderSer.STATUS_UNARRANGE);
				}
			}
		}
		
		return measures;
	}
	
	
	public Page<OrderSer> pagination(Pageable pageable, List<String> orderList, String orderId, Integer type) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<OrderSer>() {
			
			@Override
			public List<OrderSer> getPageContent() {
				List<OrderSer> measures = orderServiceMapper.selectAllList(orderId, type);
				Date date =  new Date();
				if(!measures.isEmpty()){
					for(OrderSer measure : measures){
						if(StringUtils.isBlank(measure.getClerkId()) && (DateUtil.dayDiff(measure.getNeedTime(), date) > 0)){
							measure.setStatus(OrderSer.STATUS_OVERDUE);
						}else if(measure.getIsFinish()){
							measure.setStatus(OrderSer.STATUS_COMPLETE);
						}else if(StringUtils.isNotBlank(measure.getServicePosition())){
							measure.setStatus(OrderSer.STATUS_SIGN);
						}else if(StringUtils.isNotBlank(measure.getClerkId())){
							measure.setStatus(OrderSer.STATUS_ARRANGE);
						}else{
							measure.setStatus(OrderSer.STATUS_UNARRANGE);
						}
					}
				}
				
				return measures;
			}
			
		});
	}
	
	//因为预警期表还未完成，到时候还得增加消息提醒表记录
	@Transactional(readOnly = false)
	public Boolean create(OrderSer orderService) {
		OrderProcessMapper orderProcessMapper = SpringContextHolder.getBean(OrderProcessMapper.class);
		String orderId = orderService.getOrderId();
		Integer type = orderService.getType();
		Boolean result;
		result = orderServiceMapper.insertSelective(orderService) == 1;
		if(!result){
			return false;
		}
		//判断是否新增的时候已经勾选完成
		if(orderService.getIsFinish() && orderServiceMapper.finishServiceNum(orderId, type).size() < 1){
			OrderProcess orderProcess = orderProcessMapper.findByOrderId(orderId, OrderProcess.STATUS_DESIGN);
			if(orderProcess != null){
				orderProcess.setModifyDate(orderService.getCreateDate());
				orderProcess.setIsFinish(Boolean.TRUE);
				result = orderProcessMapper.updateByPrimaryKeySelective(orderProcess) == 1;
				if(!result){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
		}
		return true;
	}
	
	//因为预警期表还未完成，如果业主要求时间改变的话更改消息提醒表记录
	@Transactional(readOnly = false)
	public Boolean update(OrderSer orderService) {
		OrderProcessMapper orderProcessMapper = SpringContextHolder.getBean(OrderProcessMapper.class);
		NoticeMsgService noticeMsgService = SpringContextHolder.getBean(NoticeMsgService.class);
		OrderServiceAttachmentService orderServiceAttachmentService = SpringContextHolder.getBean(OrderServiceAttachmentService.class);
		String orderId = orderService.getOrderId();
		Integer type = orderService.getType();
		Boolean result;
		OrderSer oldOrderService = findById(orderService.getId());
		String oldAttachmentId = oldOrderService.getAttachmentId();
		String newAttachmentId = orderService.getAttachmentId();
		result = orderServiceMapper.updateByPrimaryKeySelective(orderService) == 1;
		if(!result){
			return false;
		}
		//如果有新的附件上传的话则删除原本的附件
		if(StringUtils.isNotBlank(oldAttachmentId) && StringUtils.isNotBlank(newAttachmentId) && !oldAttachmentId.equals(newAttachmentId)){
			FileUploadService fileUploadService = SpringContextHolder.getBean(FileUploadService.class);
			OrderServiceAttachment attachment = orderServiceAttachmentService.findById(oldAttachmentId);
			result = orderServiceAttachmentService.delete(oldAttachmentId);
			if(!result){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			File file = new File(fileUploadService.getUploadRootPath() + attachment.getAttachmentRoot());
			result = file.delete();
			if(!result){
				return false;
			}
		}		
		//判断是否编辑的时候已经勾选完成
		if(orderService.getIsFinish() && orderServiceMapper.finishServiceNum(orderId, type).size() < 1){
			OrderProcess orderProcess = orderProcessMapper.findByOrderId(orderId, type);
			if(orderProcess != null){
				orderProcess.setModifyDate(orderService.getCreateDate());
				orderProcess.setIsFinish(Boolean.TRUE);
				result = orderProcessMapper.updateByPrimaryKeySelective(orderProcess) == 1;
				if(!result){
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
		}
		
		return true;
	}
	
	//因为预警期表还未完成，如果删除的日期早于消息提醒表的时间的话，删除消息提醒表的数据
	@Transactional(readOnly = false)
	public Boolean delete(String id, Date date) {
		NoticeMsgService noticeMsgService = SpringContextHolder.getBean(NoticeMsgService.class);
		Boolean result;
		result = orderServiceMapper.deleteById(id, date) == 1;
		if(!result){
			return false;
		}
		NoticeMsg noticeMsg = noticeMsgService.findByFromId(id);
		if(noticeMsg != null && DateUtil.dayDiff(noticeMsg.getNoticeDate(), date) > 0){
			result = noticeMsgService.delete(noticeMsg.getId(), date);
			if(!result){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
		
		return true;
	}


	public boolean isCreateExist(String serviceCode, String businessId) {
		return orderServiceMapper.isCreateExist(serviceCode, businessId) > 0 ;
	}

	public boolean isModifyExist(String serviceCode, String oldServiceCode, String businessId) {
		return orderServiceMapper.isModifyExist(serviceCode, oldServiceCode, businessId) > 0;
	}
	
}