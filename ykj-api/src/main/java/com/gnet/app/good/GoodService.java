package com.gnet.app.good;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gnet.utils.page.PageUtil;

@Service
@Transactional(readOnly = true)
public class GoodService {
	
	@Autowired
	private GoodMapper goodMapper;
	
	public Good findById(String id) {
		return goodMapper.selectByPrimaryKey(id);
	}
	
	public Good find(Good good) {
		return goodMapper.selectOne(good);
	}
	
	
	public List<Good> findAll(String name, Integer onsaleStatus, String businessId, List<String> orderList) {
		return goodMapper.selectGoodsAllList(orderList, name, onsaleStatus, businessId);
	}
	
	
	public Page<Good> pagination(Pageable pageable, String name, Integer onsaleStatus, String businessId, List<String> orderList) {
		return PageUtil.pagination(pageable, orderList, new PageUtil.Callback<Good>() {
			
			@Override
			public List<Good> getPageContent() {
				return goodMapper.selectGoodsAll(name, onsaleStatus, businessId);
			}
			
		});
	}
	
	@Transactional(readOnly = false)
	public Boolean create(Good good) {
		Date date = new Date();
		good.setCreateDate(date);
		good.setModifyDate(date);
		return goodMapper.insertSelective(good) == 1;
	}
	
	@Transactional(readOnly = false)
	public Boolean update(Good good) {
		good.setModifyDate(new Date());
		return goodMapper.updateByPrimaryKeySelective(good) == 1;
	}
	
	
	@Transactional(readOnly = false)
	public Boolean delete(String id) {
		return goodMapper.deleteByPrimaryKey(id) == 1;
	}
	
	/**
	 * 验证商品型号唯一
	 * @param model
	 * @param originValue
	 * @return
	 */
	public Boolean isExists(String model, String originValue, String businessId) {
		return goodMapper.isExists(model, originValue, businessId) > 0;
	}
	
	/**
	 * 商品在订单中的使用情况
	 * @param storageGoodsId
	 * @param businessId
	 * @return
	 */
	public Boolean useInOrder(String storageGoodsId) {
		return goodMapper.useInOrder(storageGoodsId) > 0;
	}
	
	/**
	 * 修改商品的在售状态
	 * @param id
	 * @param onsaleStatus
	 * @return
	 */
	@Transactional(readOnly = false)
	public Boolean changeOnsaleStatus(String id, Integer onsaleStatus) {
		Good good = new Good();
		good.setId(id);
		good.setOnsaleStatus(onsaleStatus);
		good.setModifyDate(new Date());
		return goodMapper.updateByPrimaryKeySelective(good) == 1;
	}
	
	
}