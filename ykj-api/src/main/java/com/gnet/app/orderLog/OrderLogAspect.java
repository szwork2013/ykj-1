package com.gnet.app.orderLog;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gnet.app.Constant;
import com.gnet.app.order.Order;
import com.gnet.app.order.OrderMapper;
import com.gnet.app.orderGood.OrderGood;
import com.gnet.app.orderGood.OrderGoodMapper;
import com.gnet.codeword.CodewordGetter;
import com.gnet.utils.date.DateUtil;

import tk.mybatis.mapper.entity.Example;

/**
 * 实现订单相关操作的日志记录
 *
 * @ClassName OrderLogAspect
 * @Description TODO
 * @author wct
 * @date 2016年10月5日
 */
@Aspect
@Component
public class OrderLogAspect {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderGoodMapper orderGoodMapper;
	@Autowired
	private OrderLogMapper orderLogMapper;
	@Autowired
	private CodewordGetter codewordGetter;
	
	/**
	 * 创建订单日志记录
	 * @param joinPoint
	 * @param returnValue
	 */
	@AfterReturning(returning="returnValue", pointcut = "execution(* com.gnet.app.order.OrderService.create(..))")
	public void createOrderLog(JoinPoint joinPoint, Boolean returnValue) {
		if (returnValue) {
			Date date = new Date();
			Object[] args = joinPoint.getArgs();
			Order order = (Order) args[0];
			String operatorId = (String) args[1];
			OrderLog orderLog = new OrderLog();
			Order newOrder = orderMapper.selectByPrimaryKey(order.getId());
			
			orderLog.setClerkId(operatorId);
			orderLog.setOrderId(newOrder.getId());
			orderLog.setCreateDate(date);
			orderLog.setModifyDate(date);
			orderLog.setContent("创建订单，订单基本信息:【" + orderToString(newOrder) + "】");
			orderLogMapper.insertSelective(orderLog);
		}
	}
	
	/**
	 * 修改订单日志记录
	 * @param joinPoint
	 * @param returnValue
	 */
	@AfterReturning(returning="returnValue", pointcut = "execution(* com.gnet.app.order.OrderService.update(..))")
	public void updateOrderLog(JoinPoint joinPoint, Boolean returnValue) {
		if (returnValue) {
			Date date = new Date();
			Object[] args = joinPoint.getArgs();
			Order order = (Order) args[0];
			String operatorId = (String) args[1];
			OrderLog orderLog = new OrderLog();
			Order newOrder = orderMapper.selectByPrimaryKey(order.getId());
			
			orderLog.setClerkId(operatorId);
			orderLog.setOrderId(newOrder.getId());
			orderLog.setCreateDate(date);
			orderLog.setModifyDate(date);
			orderLog.setContent("修改订单，订单基本信息:【" + orderToString(newOrder) + "】");
			orderLogMapper.insertSelective(orderLog);
		}
	}
	
	/**
	 * 删除订单日志记录
	 * @param joinPoint
	 * @param returnValue
	 */
	@AfterReturning(returning="returnValue", pointcut = "execution(* com.gnet.app.order.OrderService.delete(..))")
	public void deleteOrderLog(JoinPoint joinPoint, Boolean returnValue) {
		if (returnValue) {
			Date date = new Date();
			Object[] args = joinPoint.getArgs();
			String orderId = (String) args[0];
			String operatorId = (String) args[2];
			OrderLog orderLog = new OrderLog();
			
			Example example = new Example(Order.class);
			example.createCriteria().andEqualTo("id", orderId).andEqualTo("isDel", Order.DEL_TRUE);
			orderLog.setClerkId(operatorId);
			orderLog.setOrderId(orderId);
			orderLog.setCreateDate(date);
			orderLog.setModifyDate(date);
			orderLog.setContent("删除订单，订单号:" + orderMapper.selectByExample(example).get(0).getOrderNo());
			orderLogMapper.insertSelective(orderLog);
		}
	}
	
	/**
	 * 设置订单完成日志记录
	 * @param joinPoint
	 * @param returnValue
	 */
	@AfterReturning(returning="returnValue", pointcut = "execution(* com.gnet.app.order.OrderService.finishOrder(..))")
	public void finishOrderLog(JoinPoint joinPoint, Boolean returnValue) {
		if (returnValue) {
			Date date = new Date();
			Object[] args = joinPoint.getArgs();
			String orderId = (String) args[0];
			String operatorId = (String) args[1];
			OrderLog orderLog = new OrderLog();
			Order order = orderMapper.selectByPrimaryKey(orderId);
			
			orderLog.setClerkId(operatorId);
			orderLog.setOrderId(order.getId());
			orderLog.setCreateDate(date);
			orderLog.setModifyDate(date);
			orderLog.setContent("标记完成订单，订单号:" + order.getOrderNo());
			orderLogMapper.insertSelective(orderLog);
		}
	}
	
	/**
	 * 订单退单日志记录
	 * @param joinPoint
	 * @param returnValue
	 */
	@AfterReturning(returning="returnValue", pointcut = "execution(* com.gnet.app.order.OrderService.returnOrder(..))")
	public void returnOrderLog(JoinPoint joinPoint, Boolean returnValue) {
		if (returnValue) {
			Date date = new Date();
			Object[] args = joinPoint.getArgs();
			String orderId = (String) args[0];
			String operatorId = (String) args[1];
			OrderLog orderLog = new OrderLog();
			Order order = orderMapper.selectByPrimaryKey(orderId);
			
			orderLog.setClerkId(operatorId);
			orderLog.setOrderId(order.getId());
			orderLog.setCreateDate(date);
			orderLog.setModifyDate(date);
			orderLog.setContent("标记订单退单，订单号:" + order.getOrderNo());
			orderLogMapper.insertSelective(orderLog);
		}
	}
	
	/**
	 * 订单商品更新日志记录
	 * @param joinPoint
	 * @param returnValue
	 */
	@AfterReturning(returning="returnValue", pointcut = "execution(* com.gnet.app.orderGood.OrderGoodService.batchSaveOrUpdate(..))")
	public void batchSaveOrUpdateLog(JoinPoint joinPoint, Boolean returnValue) {
		if (returnValue) {
			Date date = new Date();
			Object[] args = joinPoint.getArgs();
			String orderId = (String) args[1];
			String operatorId = (String) args[2];
			Order order = orderMapper.selectByPrimaryKey(orderId);
			OrderLog orderLog = new OrderLog();
			
			orderLog.setClerkId(operatorId);
			orderLog.setOrderId(order.getId());
			orderLog.setCreateDate(date);
			orderLog.setModifyDate(date);
			orderLog.setContent("订单商品更新，订单号:" + order.getOrderNo());
			orderLogMapper.insertSelective(orderLog);
		}
	}
	
	/**
	 * 订单商品删除日志记录
	 * @param joinPoint
	 * @param returnValue
	 * @throws Throwable 
	 */
	@Around("execution(* com.gnet.app.orderGood.OrderGoodService.delete(..))")
	public Boolean deleteGoodLog(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		String orderGoodId = (String) args[0];
		String operatorId = (String) args[1];
		OrderGood orderGood = orderGoodMapper.selectByPrimaryKey(orderGoodId);
		Boolean returnValue = (Boolean) joinPoint.proceed();
		
		if (returnValue) {
			Date date = new Date();
			
			Order order = orderMapper.selectByPrimaryKey(orderGood.getOrderId());
			OrderLog orderLog = new OrderLog();
			orderLog.setClerkId(operatorId);
			orderLog.setOrderId(order.getId());
			orderLog.setCreateDate(date);
			orderLog.setModifyDate(date);
			orderLog.setContent("订单商品删除，订单号:" + order.getOrderNo());
			orderLogMapper.insertSelective(orderLog);
		}
		
		return returnValue;
	}
	
	private String orderToString(Order order) {
		StringBuilder stringBuilder = new StringBuilder("订单号：").append(order.getOrderNo());
		if (order.getOrderDate() != null) {
			stringBuilder.append(",").append("订单日期：").append(DateUtil.dateToString(order.getOrderDate(), "yyyy-MM-dd"));
		}
		
		if (StringUtils.isNoneBlank(order.getPhoneSec())) {
			stringBuilder.append(",").append("备用联系电话：").append(order.getPhoneSec());
		}
		
		if (order.getOrderSource() != null) {
			stringBuilder.append(",").append("订单来源：").append(codewordGetter.getCodewordByKey(Constant.ORDER_SOURCE, order.getOrderSource().toString()).getValue());
		}
		
		if (StringUtils.isNoneBlank(order.getAddress())) {
			stringBuilder.append(",").append("送货地址：").append(order.getAddress());
		}
		
		if (StringUtils.isNoneBlank(order.getCustomerRemark())) {
			stringBuilder.append(",").append("客户备注：").append(order.getCustomerRemark());
		}
		
		if (StringUtils.isNoneBlank(order.getPrivateRemark())) {
			stringBuilder.append(",").append("内部备注：").append(order.getPrivateRemark());
		}
		
		if (order.getPriceBeforeDiscount() != null) {
			stringBuilder.append(",").append("折前单价：").append(order.getPriceBeforeDiscount());
		}
		
		if (order.getPriceAfterDiscount() != null) {
			stringBuilder.append(",").append("折后单价：").append(order.getPriceAfterDiscount());
		}
		
		if (order.getStrikePrice() != null) {
			stringBuilder.append(",").append("成交价：").append(order.getStrikePrice());
		}
		
		if (order.getReceiptPrice() != null) {
			stringBuilder.append(",").append("已付款：").append(order.getReceiptPrice());
		}
		
		return stringBuilder.toString();
	}
	
}
