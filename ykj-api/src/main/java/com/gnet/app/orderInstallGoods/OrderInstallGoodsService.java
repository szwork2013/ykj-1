package com.gnet.app.orderInstallGoods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.utils.page.PageUtil;

@Service
@Transactional(readOnly = true)
public class OrderInstallGoodsService {
	
	@Autowired
	private OrderInstallGoodsMapper orderInstallGoodsMapper;
	
	public OrderInstallGoods findById(String id) {
		return orderInstallGoodsMapper.selectByPrimaryKey(id);
	}
	
	public OrderInstallGoods find(OrderInstallGoods orderInstallGoods) {
		return orderInstallGoodsMapper.selectOne(orderInstallGoods);
	}
	
	
	public List<OrderInstallGoods> findAll(List<String> orderList, String serviceId) {
		return orderInstallGoodsMapper.findAllList(orderList, serviceId);
	}
	
	public Page<OrderInstallGoods> pagination(Pageable pageable, List<String> orderList, String serviceId) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<OrderInstallGoods>() {
			
			@Override
			public List<OrderInstallGoods> getPageContent() {
				return orderInstallGoodsMapper.selectAllList(serviceId);
			}
			
		});
	}
	
	@Transactional(readOnly = false)
	public Boolean create(OrderInstallGoods orderInstallGoods) {
		return orderInstallGoodsMapper.insertSelective(orderInstallGoods) == 1;
	}
	
	@Transactional(readOnly = false)
	public Boolean update(OrderInstallGoods orderInstallGoods) {
		return orderInstallGoodsMapper.updateByPrimaryKeySelective(orderInstallGoods) == 1;
	}
	
}