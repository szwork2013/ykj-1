package com.gnet.app.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gnet.app.clerk.Clerk;
import com.gnet.app.good.Good;
import com.gnet.app.good.GoodMapper;
import com.gnet.app.orderGood.OrderGood;
import com.gnet.app.orderGood.OrderGoodMapper;
import com.gnet.app.orderProcess.OrderProcess;
import com.gnet.app.orderProcess.OrderProcessMapper;
import com.gnet.app.orderService.OrderSer;
import com.gnet.app.orderService.OrderServiceMapper;
import com.gnet.utils.math.PriceUtils;
import com.gnet.utils.page.PageUtil;
import com.gnet.utils.page.PageUtil.Callback;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import tk.mybatis.mapper.entity.Example;

@Service
@Transactional(readOnly = true)
public class OrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderProcessMapper orderProcessMapper;
	@Autowired
	private OrderGoodMapper orderGoodMapper;
	@Autowired
	private OrderServiceMapper orderServiceMapper;
	
	@Autowired
	private GoodMapper goodMapper;
	
	public Order findById(String id) {
		return orderMapper.selectByPrimaryKey(id);
	}
	
	public Order find(Order order) {
		return orderMapper.selectOne(order);
	}
	
	/**
	 * 通过订单编号判断服务是否全部完成
	 * @param orderId
	 * @return
	 */
	public Boolean orderServiceAllFinish(String orderId) {
		OrderSer orderSer = new OrderSer();
		orderSer.setOrderId(orderId);
		orderSer.setIsFinish(Boolean.FALSE);
		return orderServiceMapper.selectCount(orderSer) == 0;
	}
	
	public OrderSer getServiceWithoutMaintenanceUnFinish(String orderId) {
		return orderServiceMapper.selectServiceUnFinishWithoutMaintenance(orderId);
	}
	
	/**
	 * 根据角色类型获得订单数据
	 * 
	 * @param clerk
	 * @param type
	 * @param orderSource
	 * @param orderResponsibleName
	 * @param customerName
	 * @param startOrderDate
	 * @param endOrderDate
	 * @param mutiSearchColumn
	 * @param orderList
	 * @return
	 */
	public List<Order> findAll(Clerk clerk, Integer type, Integer orderSource, String orderResponsibleName,
			String customerName, String startOrderDate, String endOrderDate, String mutiSearchColumn, List<String> orderList) {
		Integer roleType = clerk.getRoleType();
		List<Order> orders = null;
		if (Clerk.ROLE_TYPE_ADMIN.equals(roleType) || Clerk.ROLE_TYPE_MANAGER.equals(roleType) 
			|| Clerk.ROLE_TYPE_LOGISTIC.equals(roleType) || Clerk.ROLE_TYPE_AFTER_SALES.equals(roleType)
			|| Clerk.ROLE_TYPE_WAREHOUSE.equals(roleType)) {
			
			orders = findUnderBusiness(type, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, clerk.getBusinessId(), orderList);
		
		} else if (Clerk.ROLE_TYPE_STORE_MANAGER.equals(roleType)) {
			
			orders = findUnderStore(roleType, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, clerk.getStoreId(), orderList);
		
		} else if (Clerk.ROLE_TYPE_CLERK.equals(roleType)) {
			
			orders = findPersonal(roleType, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, clerk.getId(), orderList);
		} else {
			orders = new ArrayList<>();
		}
		
		return orders;
	}
	
	public List<Order> findUnderBusiness(Integer type, Integer orderSource, String orderResponsibleName,
			String customerName, String startOrderDate, String endOrderDate, String mutiSearchColumn,
			String businessId, List<String> orderList) {
		
		return orderMapper.selectOrdersUnderBusiness(orderList, type, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, businessId);
		
	}
	
	public List<Order> findUnderStore(Integer type, Integer orderSource, String orderResponsibleName,
			String customerName, String startOrderDate, String endOrderDate, String mutiSearchColumn,
			String storeId, List<String> orderList) {
		
		return orderMapper.selectOrdersUnderStore(orderList, type, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, storeId);
	}
	
	public List<Order> findPersonal(Integer type, Integer orderSource, String orderResponsibleName,
			String customerName, String startOrderDate, String endOrderDate, String mutiSearchColumn,
			String clerkId, List<String> orderList) {
		
		return orderMapper.selectOrdersPersonal(orderList, type, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, clerkId);
	}
	
	/**
	 * 根据角色类型获得分页订单数据
	 * 
	 * @param clerk
	 * @param type
	 * @param orderSource
	 * @param orderResponsibleName
	 * @param customerName
	 * @param startOrderDate
	 * @param endOrderDate
	 * @param mutiSearchColumn
	 * @param orderList
	 * @return
	 */
	public Page<Order> paginationAll(Clerk clerk, Pageable pageable, Integer type, Integer orderSource, String orderResponsibleName,
			String customerName, String startOrderDate, String endOrderDate, String mutiSearchColumn, List<String> orderList) {
		Integer roleType = clerk.getRoleType();
		Page<Order> orders = null;
		
		if (Clerk.ROLE_TYPE_ADMIN.equals(roleType) || Clerk.ROLE_TYPE_MANAGER.equals(roleType) 
			|| Clerk.ROLE_TYPE_LOGISTIC.equals(roleType) || Clerk.ROLE_TYPE_AFTER_SALES.equals(roleType)
			|| Clerk.ROLE_TYPE_WAREHOUSE.equals(roleType)) {
			
			orders = paginationUnderBusiness(pageable, type, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, clerk.getBusinessId(), orderList);
		
		} else if (Clerk.ROLE_TYPE_STORE_MANAGER.equals(roleType)) {
			
			orders = paginationUnderStore(pageable, roleType, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, clerk.getStoreId(), orderList);
		
		} else if (Clerk.ROLE_TYPE_CLERK.equals(roleType)) {
			
			orders = paginationPersonal(pageable, roleType, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, clerk.getId(), orderList);
		} else {
			orders = PageUtil.pagination(pageable, new Callback<Order>() {
				
				public List<Order> getPageContent() {
					return new ArrayList<>();
				}
			});
		}
		
		return orders;
	}
	
	public Page<Order> paginationUnderBusiness(Pageable pageable, Integer type, Integer orderSource, String orderResponsibleName,
								String customerName, String startOrderDate, String endOrderDate, String mutiSearchColumn,
								String businessId, List<String> orderList) {
	
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<Order>() {
		
			@Override
			public List<Order> getPageContent() {
				return orderMapper.selectOrdersUnderBusiness(null, type, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, businessId);
			}
		
		});
	}
	
	public Page<Order> paginationUnderStore(Pageable pageable, Integer type, Integer orderSource, String orderResponsibleName,
								String customerName, String startOrderDate, String endOrderDate, String mutiSearchColumn,
								String storeId, List<String> orderList) {
	
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<Order>() {
		
			@Override
			public List<Order> getPageContent() {
				return orderMapper.selectOrdersUnderStore(null, type, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, storeId);
			}
		
		});
	}
	
	public Page<Order> paginationPersonal(Pageable pageable, Integer type, Integer orderSource, String orderResponsibleName,
								String customerName, String startOrderDate, String endOrderDate, String mutiSearchColumn,
								String clerkId, List<String> orderList) {
		
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<Order>() {
			
			@Override
			public List<Order> getPageContent() {
				return orderMapper.selectOrdersPersonal(null, type, orderSource, orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, clerkId);
			}
			
		});
	}
	
	/**
	 * 添加订单需要额外的数据
	 * 1.付款状态
	 * 
	 * @param orders
	 */
	public void addSearchExtraData(List<Order> orders) {
		if (orders.isEmpty()) {
			return;
		}
		
		// 新增订金状态
		for (Order order : orders) {
			if (order.getSubscriptionIsFinish() == null || !order.getSubscriptionIsFinish()) {
				order.setPayStatus("未付订金");
			} else {
				// 成交价
				BigDecimal strikePrice = order.getStrikePrice() == null ? new BigDecimal(0) : order.getStrikePrice();
				// 已付金额
				BigDecimal receiptPrice = order.getReceiptPrice() == null ? new BigDecimal(0) : order.getReceiptPrice();
				if (PriceUtils.isZero(receiptPrice)) {
					order.setPayStatus("已付订金");
				} else if (PriceUtils.isLessZero(PriceUtils.sub(strikePrice, receiptPrice))) {
					order.setPayStatus("全款付清");
				} else {
					order.setPayStatus("未付清");
				}
			}
		}
	}
	
	/**
	 * 下订单
	 * 
	 * @param order
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean create(Order order, String operatorId) {
		Date date = new Date();
		// 放入订单编号
		order.setOrderNo(OrderKit.generateOrderNo(order.getBusinessId()));
		// 若不需要审核，则进行直接变为进行中订单
		if (order.getIsNeedCostAduit() != null && !order.getIsNeedCostAduit()) {
			order.setType(Order.TYPE_UNDERWAY_ORDER);
		} else {
			order.setType(Order.TYPE_PRE_ORDER);
		}
		
		if (order.getIsNeedCostAduit() == null) {
			order.setIsNeedCostAduit(Boolean.TRUE);
		}
		
		if (order.getIsNeedDelivery() == null) {
			order.setIsNeedDelivery(Boolean.TRUE);
		}
		
		if (order.getIsNeedInstall() == null) {
			order.setIsNeedInstall(Boolean.TRUE);
		}
		
		if (order.getIsNeedMeasure() == null) {
			order.setIsNeedMeasure(Boolean.TRUE);
		}
		
		if (order.getIsNeedDesign() == null) {
			order.setIsNeedDesign(Boolean.TRUE);
		}
		
		
		
		// 订单商品数据处理
		List<OrderGood> orderGoods = order.getOrderGoods();
		List<String> storageGoodIds = Lists.newArrayList();
		for (OrderGood orderGood : orderGoods) {
			storageGoodIds.add(orderGood.getStorageGoodsId());
		}
		
		// 获得仓库商品数据
		Map<String, Good> storageGoodsMap = Maps.newHashMap();
		List<Good> goods = goodMapper.findByIds(storageGoodIds);
		for (Good good : goods) {
			storageGoodsMap.put(good.getId(), good);
		}
		
		// 计算折前单价和折后单价
		BigDecimal priceBeforeDiscount = new BigDecimal(0);
		BigDecimal priceAfterDiscount = new BigDecimal(0);
		for (OrderGood orderGood : orderGoods) {
			BigDecimal num = new BigDecimal(orderGood.getOrderGoodsNum());
			priceBeforeDiscount = PriceUtils.add(priceBeforeDiscount, PriceUtils.mul(num, storageGoodsMap.get(orderGood.getStorageGoodsId()).getPrice()));
			priceAfterDiscount = PriceUtils.add(priceAfterDiscount, PriceUtils.mul(num, orderGood.getStrikeUnitPrice()));
		}
		
		
		order.setIsDel(Order.DEL_FALSE);
		order.setPriceBeforeDiscount(priceBeforeDiscount);
		order.setPriceAfterDiscount(priceAfterDiscount);
		order.setCreateDate(date);
		order.setModifyDate(date);
		
		if (orderMapper.insertSelective(order) != 1) {
			return false;
		}
		
		// 订单进度状态列表
		List<OrderProcess> orderProcesses = Lists.newArrayList();
		// 是否需要报价审核
		if (order.getIsNeedCostAduit()) {
			// 添加报价审核
			orderProcesses.add(buildOrderProcess(date, order.getId(), OrderProcess.STATUS_ADUIT));
		}
		
		// 是否需要送货
		if (order.getIsNeedDelivery()) {
			// 是否需要送货
			orderProcesses.add(buildOrderProcess(date, order.getId(), OrderProcess.STATUS_DELIVERY));
		}
		
		// 是否需要安装
		if (order.getIsNeedInstall()) {
			// 是否需要安装
			orderProcesses.add(buildOrderProcess(date, order.getId(), OrderProcess.STATUS_INSTALL));
		}
		
		// 是否需要测量
		if (order.getIsNeedMeasure()) {
			// 是否需要测量
			orderProcesses.add(buildOrderProcess(date, order.getId(), OrderProcess.STATUS_MEASURE));
		}
		
		// 是否需要设计
		if (order.getIsNeedDesign()) {
			// 是否需要设计
			orderProcesses.add(buildOrderProcess(date, order.getId(), OrderProcess.STATUS_DESIGN));
		}
		
		orderProcesses.add(buildOrderProcess(date, order.getId(), OrderProcess.STATUS_SUBSCRIPTION));
		orderProcesses.add(buildOrderProcess(date, order.getId(), OrderProcess.STATUS_PAYMENT));
		orderProcesses.add(buildOrderProcess(date, order.getId(), OrderProcess.STATUS_FINISH));
		
		// 保存订单进度状态
		if (orderProcessMapper.insertAllOrderProcess(orderProcesses) != orderProcesses.size()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		
		// 处理预留库存
		// TODO 暂未支持预留库存操作 by wct
		for (OrderGood orderGood : orderGoods) {
			orderGood.setOrderId(order.getId());
			orderGood.setCreateDate(date);
			orderGood.setModifyDate(date);
			// 默认开始剩余送货数
			orderGood.setNeedDeliverNum(orderGood.getOrderGoodsNum());
			// 默认开始安装数
			orderGood.setNeedInstallNum(orderGood.getOrderGoodsNum());
			orderGood.setOutGoodsNum(0);
		}
		
		// 订单商品保存
		if (!orderGoods.isEmpty() && orderGoodMapper.insertAllOrderGoods(orderGoods) != orderGoods.size()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}
	
	private OrderProcess buildOrderProcess(Date date, String orderId, Integer type) {
		OrderProcess orderProcess = new OrderProcess();
		orderProcess.setCreateDate(date);
		orderProcess.setModifyDate(date);
		orderProcess.setType(type);
		orderProcess.setOrderId(orderId);
		orderProcess.setIsFinish(Boolean.FALSE);
		return orderProcess;
	}
	
	@Transactional(readOnly = false)
	public Boolean update(Order order, String operatorId) {
		order.setModifyDate(new Date());
		return orderMapper.updateByPrimaryKeySelective(order) == 1;
	}
	
	
	@Transactional(readOnly = false)
	public Boolean delete(String id, Date date, String operatorId) {
		return orderMapper.deleteById(id, date) == 1;
	}
	
	public List<OrderProcess> getProcessWithOrderInfo(String orderId) {
		return orderProcessMapper.selectAllWithOrdeInfo(orderId);
	}
	
	@Transactional(readOnly = false)
	public Boolean returnOrder(String orderId, String operatorId) {
		Order order = new Order();
		order.setId(orderId);
		order.setModifyDate(new Date());
		order.setType(Order.TYPE_BACK_ORDER);
		return orderMapper.updateByPrimaryKeySelective(order) == 1;
	}
	
	@Transactional(readOnly = false)
	public Boolean finishOrder(String orderId, String operatorId) {
		Date date = new Date();
		Order order = new Order();
		order.setId(orderId);
		order.setModifyDate(date);
		order.setType(Order.TYPE_FINISH_ORDER);
		
		if (orderMapper.updateByPrimaryKeySelective(order) != 1) {
			return false;
		}
		
		OrderProcess orderProcess = new OrderProcess();
		orderProcess.setModifyDate(date);
		orderProcess.setIsFinish(Boolean.TRUE);
		Example example = new Example(OrderProcess.class);
		example.createCriteria().andEqualTo("orderId", orderId).andEqualTo("type", OrderProcess.STATUS_FINISH);
		if (orderProcessMapper.updateByExampleSelective(orderProcess, example) != 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}
	
}