package com.gnet.app.delivery;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.app.noticeMsg.NoticeMsg;
import com.gnet.app.noticeMsg.NoticeMsgService;
import com.gnet.app.orderDeliverGoods.OrderDeliverGoods;
import com.gnet.app.orderDeliverGoods.OrderDeliverGoodsMapper;
import com.gnet.app.orderGood.OrderGood;
import com.gnet.app.orderGood.OrderGoodMapper;
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
public class DeliveryService {
	
	@Autowired
	private OrderServiceMapper orderServiceMapper;
	@Autowired
	private OrderGoodMapper orderGoodMapper;
	@Autowired
	private OrderDeliverGoodsMapper orderDeliverGoodsMapper;
	@Autowired
	private OrderProcessMapper orderProcessMapper;
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	public OrderSer findById(String id) {
		return orderServiceMapper.findById(id);
	}

	public List<OrderSer> findAll(List<String> orderList, String orderId, Integer type) {
		List<OrderSer> deliverys = orderServiceMapper.findAll(orderList, orderId, type);
		Date date =  new Date();
		if(!deliverys.isEmpty()){
			for(OrderSer delivery : deliverys){
				if(StringUtils.isBlank(delivery.getClerkId()) && (DateUtil.dayDiff(delivery.getNeedTime(), date) > 0)){
					delivery.setStatus(OrderSer.STATUS_OVERDUE);
				}else if(delivery.getIsFinish()){
					delivery.setStatus(OrderSer.STATUS_COMPLETE);
				}else if(StringUtils.isNotBlank(delivery.getServicePosition())){
					delivery.setStatus(OrderSer.STATUS_SIGN);
				}else if(StringUtils.isNotBlank(delivery.getClerkId())){
					delivery.setStatus(OrderSer.STATUS_ARRANGE);
				}else{
					delivery.setStatus(OrderSer.STATUS_UNARRANGE);
				}
			}
		}
		
		return deliverys;
	}
	
	
	public Page<OrderSer> pagination(Pageable pageable, List<String> orderList, String orderId, Integer type) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<OrderSer>() {
			
			@Override
			public List<OrderSer> getPageContent() {
				List<OrderSer> deliverys = orderServiceMapper.selectAllList(orderId, type);
				Date date =  new Date();
				if(!deliverys.isEmpty()){
					for(OrderSer delivery : deliverys){
						if(StringUtils.isBlank(delivery.getClerkId()) && (DateUtil.dayDiff(delivery.getNeedTime(), date) > 0)){
							delivery.setStatus(OrderSer.STATUS_OVERDUE);
						}else if(delivery.getIsFinish()){
							delivery.setStatus(OrderSer.STATUS_COMPLETE);
						}else if(StringUtils.isNotBlank(delivery.getServicePosition())){
							delivery.setStatus(OrderSer.STATUS_SIGN);
						}else if(StringUtils.isNotBlank(delivery.getClerkId())){
							delivery.setStatus(OrderSer.STATUS_ARRANGE);
						}else{
							delivery.setStatus(OrderSer.STATUS_UNARRANGE);
						}
					}
				}
				
				return deliverys;
			}
			
		});
	}
	
	//因为预警期表还未完成，到时候还得增加消息提醒表记录
	@Transactional(readOnly = false)
	public Boolean create(OrderSer orderService) {
		Boolean result;
		result = orderServiceMapper.insertSelective(orderService) == 1;
		if(!result){
			return false;
		}
		Date date = new Date();
		String orderId = orderService.getOrderId();
		List<OrderDeliverGoods> serviceGoods = orderService.getServiceGoods();
		//订单商品列表
		List<OrderGood> ordersGoods = orderGoodMapper.selectAllUnderOrder(orderId);
		if(!serviceGoods.isEmpty() && !ordersGoods.isEmpty()){
			Map<String, Integer> goodMap = new HashMap<>();
			for(OrderDeliverGoods seviceGood : serviceGoods){
				goodMap.put(seviceGood.getOrderGoodsId(), seviceGood.getDeliverNum());
			}
	        for(OrderGood orderGood : ordersGoods){
	        	orderGood.setModifyDate(date);
	        	orderGood.setNeedDeliverNum(orderGood.getNeedDeliverNum() - goodMap.get(orderGood.getId()));
	        }
	        
	        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, Boolean.FALSE);
	        OrderGoodMapper batchUpdateOrderGoodMapper =  sqlSession.getMapper(OrderGoodMapper.class);
			try {
				for (OrderGood orderGood : ordersGoods) {
					batchUpdateOrderGoodMapper.updateByPrimaryKeySelective(orderGood);
				}	
				sqlSession.commit();
				sqlSession.clearCache();
		
			} catch (Exception e) {
				sqlSession.rollback();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			} finally {
				sqlSession.close();
			}
	        
	        //批量保存这次服务的商品信息
	        result = orderDeliverGoodsMapper.batchSave(serviceGoods, date, orderService.getId()) == serviceGoods.size();
	        if(!result){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
	        }
		}
		
		//如果服务未完成状态，并且订单存在商品，且未送货商品为0的数量为0，则修改订单状态为已完成送货
		if(orderService.getIsFinish() && orderGoodMapper.goodsNum(orderId) > 0 && orderGoodMapper.needServiceNum(orderId, OrderSer.TYPE_DELIVERY) <= 0){
			OrderProcess orderProcess = orderProcessMapper.findByOrderId(orderId, OrderProcess.STATUS_DELIVERY);
			orderProcess.setModifyDate(date);
			orderProcess.setIsFinish(Boolean.TRUE);
			result = orderProcessMapper.updateByPrimaryKeySelective(orderProcess) == 1;
			if(!result){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
  
		return true;
	}
	
	//因为预警期表还未完成，如果业主要求时间改变的话更改消息提醒表记录
	@Transactional(readOnly = false)
	public Boolean update(OrderSer orderService) {
		NoticeMsgService noticeMsgService = SpringContextHolder.getBean(NoticeMsgService.class);
		OrderServiceAttachmentService orderServiceAttachmentService = SpringContextHolder.getBean(OrderServiceAttachmentService.class);
		Boolean result;
		OrderSer oldOrderService = findById(orderService.getId());
		String oldAttachmentId = oldOrderService.getAttachmentId();
		String newAttachmentId = orderService.getAttachmentId();
		String orderId = orderService.getOrderId();
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
		
		//如果服务未完成状态，并且订单存在商品，且未送货商品为0的数量为0，则修改订单状态为已完成送货
		if(orderService.getIsFinish() && orderGoodMapper.goodsNum(orderId) > 0 && orderGoodMapper.needServiceNum(orderId, OrderSer.TYPE_DELIVERY) <= 0){
			OrderProcess orderProcess = orderProcessMapper.findByOrderId(orderId, OrderProcess.STATUS_DELIVERY);
			orderProcess.setModifyDate(orderService.getModifyDate());
			orderProcess.setIsFinish(Boolean.TRUE);
			result = orderProcessMapper.updateByPrimaryKeySelective(orderProcess) == 1;
			if(!result){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
		return true;
	}
	
	/**
	 * 批量更新送货商品信息
	 * @param delivery
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean updateGoods(OrderSer delivery) {
	    Date date = new Date();
	    //订单商品数据
	    List<OrderGood> orderGoods = orderGoodMapper.selectAllUnderOrder(delivery.getOrderId());
	    //需要更新的数据
	    List<OrderDeliverGoods> newOrderDeliverGoods = delivery.getServiceGoods();
	    //原本的商品送货数据
	    List<OrderDeliverGoods> oldOrderDeliverGoods = orderDeliverGoodsMapper.selectAllList(delivery.getId());
		//送货商品map
	    Map<String, Integer> deliverMap = new HashMap<>();
	    //记录每个订单商品的送货差值map
	    Map<String, Integer> differentMap = new HashMap<>();
	    for(OrderDeliverGoods good : oldOrderDeliverGoods){
	    	deliverMap.put(good.getOrderGoodsId(), good.getDeliverNum());
	    }
	    for(OrderDeliverGoods good : newOrderDeliverGoods){
	    	String orderGoodsId = good.getOrderGoodsId();
	    	differentMap.put(orderGoodsId, deliverMap.get(orderGoodsId) - good.getDeliverNum());
	    }
	    //每个订单商品的剩余需要安装数为送货商品更改的差值加原本的值
	    for(OrderDeliverGoods deliverGood : newOrderDeliverGoods){
	    	deliverGood.setModifyDate(date);
	    }
	    for(OrderGood orderGood : orderGoods){
	    	orderGood.setModifyDate(date);
	    	orderGood.setNeedDeliverNum(orderGood.getNeedDeliverNum() + differentMap.get(orderGood.getId()));
	    }
	    	    
        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, Boolean.FALSE);
        OrderGoodMapper batchUpdateOrderGoodMapper =  sqlSession.getMapper(OrderGoodMapper.class);
        OrderDeliverGoodsMapper batchUpdateDeliverGoodsMapper = sqlSession.getMapper(OrderDeliverGoodsMapper.class);
		try {
			for (OrderGood orderGood : orderGoods) {
				batchUpdateOrderGoodMapper.updateByPrimaryKeySelective(orderGood);
			}	
			sqlSession.commit();
			sqlSession.clearCache();
	
		} catch (Exception e) {
			sqlSession.rollback();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		} 
	    
		try {
			for (OrderDeliverGoods orderDeliverGood : newOrderDeliverGoods) {
				batchUpdateDeliverGoodsMapper.updateByPrimaryKeySelective(orderDeliverGood);
			}	
			sqlSession.commit();
			sqlSession.clearCache();
	
		} catch (Exception e) {
			sqlSession.rollback();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		} finally {
			sqlSession.close();
		}
		
		return true;
	}
	
	//因为预警期表还未完成，如果删除的日期早于消息提醒表的时间的话，删除消息提醒表的数据
	@Transactional(readOnly = false)
	public Boolean delete(String id, Date date) {
		NoticeMsgService noticeMsgService = SpringContextHolder.getBean(NoticeMsgService.class);
		OrderSer orderService = orderServiceMapper.findById(id);
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
		
	    //订单商品数据
	    List<OrderGood> orderGoods = orderGoodMapper.selectAllUnderOrder(orderService.getOrderId());
	    //这次服务的订单送货数据
	    List<OrderDeliverGoods> orderDeliverGoods = orderDeliverGoodsMapper.selectAllList(id);
	    if(!orderGoods.isEmpty() && !orderDeliverGoods.isEmpty()){
		    Map<String, Integer> deliverMap = new HashMap<>();
		    for(OrderDeliverGoods good : orderDeliverGoods){
		    	deliverMap.put(good.getOrderGoodsId(), good.getDeliverNum());
		    }
		    for(OrderGood good : orderGoods){
		    	good.setModifyDate(date);
		    	good.setNeedDeliverNum(good.getNeedDeliverNum() + deliverMap.get(good.getId()));
		    }
		    result = orderDeliverGoodsMapper.deleteAll(orderDeliverGoods) == orderDeliverGoods.size();
			if(!result){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
			
	        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, Boolean.FALSE);
	        OrderGoodMapper batchUpdateOrderGoodMapper =  sqlSession.getMapper(OrderGoodMapper.class);
			try {
				for (OrderGood orderGood : orderGoods) {
					batchUpdateOrderGoodMapper.updateByPrimaryKeySelective(orderGood);
				}	
				sqlSession.commit();
				sqlSession.clearCache();
		
			} catch (Exception e) {
				sqlSession.rollback();
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			} finally {
				sqlSession.close();
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