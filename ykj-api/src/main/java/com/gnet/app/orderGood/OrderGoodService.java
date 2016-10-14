package com.gnet.app.orderGood;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.gnet.app.good.Good;
import com.gnet.app.good.GoodMapper;
import com.gnet.app.order.Order;
import com.gnet.app.order.OrderMapper;
import com.gnet.app.storageGoodStatus.StorageGoodStatus;
import com.gnet.app.storageGoodStatus.StorageGoodStatusMapper;
import com.gnet.utils.math.PriceUtils;
import com.gnet.utils.page.PageUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


@Service
@Transactional(readOnly = true)
public class OrderGoodService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderGoodMapper orderGoodMapper;
	@Autowired
	private StorageGoodStatusMapper storageGoodStatusMapper;
	@Autowired
	private GoodMapper goodMapper;
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	
	public OrderGood findById(String id) {
		return orderGoodMapper.selectByPrimaryKey(id);
	}
	
	public OrderGood find(OrderGood orderGood) {
		return orderGoodMapper.selectOne(orderGood);
	}
	
	/**
	 * 订单中商品未送货数大于0的或未安装数大于0
	 * @param orderList
	 * @param orderId
	 * @param type
	 * @return
	 */
	public List<OrderGood> findAll(List<String> orderList, String orderId, Integer type) {
		return orderGoodMapper.findAll(orderList, orderId, type);
	}

	public Page<OrderGood> pagination(Pageable pageable, List<String> orderList, String orderId, Integer type) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<OrderGood>() {
			
			@Override
			public List<OrderGood> getPageContent() {
				return orderGoodMapper.selectAllList(orderId, type);
			}
			
		});
	}
	
	/**
	 * 订单商品删除
	 * 删除附加操作
	 * 1. 所有预留库存回归仓库数量
	 * 
	 * @param id
	 * @param date
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean delete(String id, String operatorId) {
		OrderGood orderGood = findById(id);
		StorageGoodStatus storageGoodStatus = storageGoodStatusMapper.selectByPrimaryKey(id);
		// 预留库存回退到仓库
		if (orderGood.getReservedGoods() != null) {
			Long newStoreNow = orderGood.getReservedGoods().longValue() + storageGoodStatus.getStoreNow();
			storageGoodStatus.setStoreNow(newStoreNow);
			storageGoodStatus.setModifyDate(new Date());
			if (storageGoodStatusMapper.updateByPrimaryKeySelective(storageGoodStatus) != 1) {
				return false;
			}
		}
		
		if (orderGoodMapper.deleteByPrimaryKey(id) != 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}
	
	/**
	 * 订单货物更新
	 * 
	 * @param orderGoods
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean batchSaveOrUpdate(List<OrderGood> orderGoods, String orderId, String operatorId) {
		List<OrderGood> oldOrderGoodList = orderGoodMapper.selectAllUnderOrder(orderId);
		Map<String, OrderGood> oldOrderGoodMap = Maps.newHashMap();
		for (OrderGood orderGood : oldOrderGoodList) {
			oldOrderGoodMap.put(orderGood.getId(), orderGood);
		}
		
		List<String> storageGoodIds = Lists.newArrayList();
		for (OrderGood orderGood : orderGoods) {
			storageGoodIds.add(orderGood.getStorageGoodsId());
		}
		
		// 获得仓库商品数据
		Map<String, Good> storageGoodsMap = Maps.newHashMap();
		List<Good> storageGoods = goodMapper.findByIds(storageGoodIds);
		for (Good storageGood : storageGoods) {
			storageGoodsMap.put(storageGood.getId(), storageGood);
		}
		
		// 计算折前单价和折后单价
		BigDecimal priceBeforeDiscount = new BigDecimal(0);
		BigDecimal priceAfterDiscount = new BigDecimal(0);
		
		// 根据商品编号来的有无来判断是否为更新还是新增
		Date date = new Date();
		List<OrderGood> needSaveOrderGoods = Lists.newArrayList();
		List<OrderGood> needUpdateOrderGoods = Lists.newArrayList();
		for (OrderGood orderGood : orderGoods) {
			if (orderGood.getId() == null) {
				orderGood.setOrderId(orderId);
				orderGood.setCreateDate(date);
				orderGood.setModifyDate(date);
				// 默认开始剩余送货数
				orderGood.setNeedDeliverNum(orderGood.getOrderGoodsNum());
				// 默认开始安装数
				orderGood.setNeedInstallNum(orderGood.getOrderGoodsNum());
				orderGood.setOutGoodsNum(0);
				needSaveOrderGoods.add(orderGood);
			} else {
				OrderGood needUpdateOrderGood = oldOrderGoodMap.get(orderGood.getId());
				// 订单商品id不存在与数据库中
				if (needUpdateOrderGood == null) {
					return false;
				}
				
				// 订单商品商品编号不允许改变
				if (!needUpdateOrderGood.getStorageGoodsId().equals(orderGood.getStorageGoodsId())) {
					return false;
				}
				
				// 当订单商品进行过送货，则商品数不得小于剩余送货商品数
				if (!needUpdateOrderGood.getNeedDeliverNum().equals(needUpdateOrderGood.getOrderGoodsNum()) && orderGood.getOrderGoodsNum() < needUpdateOrderGood.getNeedDeliverNum()) {
					return false;
				}
				
				// 当订单商品进行过安装，则商品数不得小于剩余安装商品数
				if (!needUpdateOrderGood.getNeedInstallNum().equals(needUpdateOrderGood.getOrderGoodsNum()) && orderGood.getOrderGoodsNum() < needUpdateOrderGood.getNeedInstallNum()) {
					return false;
				}
				
				orderGood.setNeedDeliverNum(needUpdateOrderGood.getNeedDeliverNum() + orderGood.getOrderGoodsNum() - needUpdateOrderGood.getOrderGoodsNum());
				orderGood.setNeedInstallNum(needUpdateOrderGood.getNeedInstallNum() + orderGood.getOrderGoodsNum() - needUpdateOrderGood.getOrderGoodsNum());
				orderGood.setOutGoodsNum(needUpdateOrderGood.getOutGoodsNum());
				orderGood.setModifyDate(date);
				orderGood.setCreateDate(needUpdateOrderGood.getCreateDate());
				needUpdateOrderGoods.add(orderGood);
				oldOrderGoodMap.remove(orderGood.getId());
			}
			
			BigDecimal num = new BigDecimal(orderGood.getOrderGoodsNum());
			priceBeforeDiscount = PriceUtils.add(priceBeforeDiscount, PriceUtils.mul(num, storageGoodsMap.get(orderGood.getStorageGoodsId()).getPrice()));
			priceAfterDiscount = PriceUtils.add(priceAfterDiscount, PriceUtils.mul(num, orderGood.getStrikeUnitPrice()));
		}
		
		// 新增订单商品数据
		if (!needSaveOrderGoods.isEmpty() && orderGoodMapper.insertAllOrderGoods(needSaveOrderGoods) != needSaveOrderGoods.size()) {
			return false;
		}
		
		// 更新订单商品数据
		SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, Boolean.FALSE);
		OrderGoodMapper batchUpdateOrderGoodMapper =  sqlSession.getMapper(OrderGoodMapper.class);
		try {
			if (!needUpdateOrderGoods.isEmpty()) {
				for (OrderGood orderGood : needUpdateOrderGoods) {
					batchUpdateOrderGoodMapper.updateByPrimaryKeySelective(orderGood);
				}
				
				sqlSession.commit();
				sqlSession.clearCache();
			}
			
		} catch (Exception e) {
			sqlSession.rollback();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		} finally {
			sqlSession.close();
		}
		
		// 删除订单商品数据
		Set<String> orderGoodKeys = oldOrderGoodMap.keySet();
		if (!orderGoodKeys.isEmpty() && orderGoodMapper.deleteByIds(orderGoodKeys) >= 0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		// 更新订单
		Order updateOrder = new Order();
		updateOrder.setPriceBeforeDiscount(priceBeforeDiscount);
		updateOrder.setPriceAfterDiscount(priceAfterDiscount);
		updateOrder.setModifyDate(date);
		updateOrder.setId(orderId);
		if (orderMapper.updateByPrimaryKeySelective(updateOrder) != 1) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		
		return true;
	}
	
	public List<OrderGood> selectAllUnderOrderWithGoodInfo(String orderId) {
		return orderGoodMapper.selectAllUnderOrderWithGoodInfo(orderId);
	}
	
}