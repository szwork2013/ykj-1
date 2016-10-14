package com.gnet.app.orderDeliverGoods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.utils.page.PageUtil;

@Service
@Transactional(readOnly = true)
public class OrderDeliverGoodsService {
	
	@Autowired
	private OrderDeliverGoodsMapper orderDeliverGoodsMapper;
	
	public OrderDeliverGoods findById(String id) {
		return orderDeliverGoodsMapper.selectByPrimaryKey(id);
	}
	
	public OrderDeliverGoods find(OrderDeliverGoods orderDeliverGoods) {
		return orderDeliverGoodsMapper.selectOne(orderDeliverGoods);
	}
	
	public List<OrderDeliverGoods> findAll(List<String> orderList, String serviceId) {
		return orderDeliverGoodsMapper.findAllList(orderList, serviceId);
	}

	public Page<OrderDeliverGoods> pagination(Pageable pageable, List<String> orderList, String serviceId) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<OrderDeliverGoods>() {
			
			@Override
			public List<OrderDeliverGoods> getPageContent() {
				return orderDeliverGoodsMapper.selectAllList(serviceId);
			}
			
		});
	}
	
	@Transactional(readOnly = false)
	public Boolean create(OrderDeliverGoods orderDeliverGoods) {
		return orderDeliverGoodsMapper.insertSelective(orderDeliverGoods) == 1;
	}
	
	@Transactional(readOnly = false)
	public Boolean update(OrderDeliverGoods orderDeliverGoods) {
		return orderDeliverGoodsMapper.updateByPrimaryKeySelective(orderDeliverGoods) == 1;
	}
		
}