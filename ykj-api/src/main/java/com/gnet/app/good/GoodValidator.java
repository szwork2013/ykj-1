package com.gnet.app.good;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.utils.spring.SpringContextHolder;

public class GoodValidator {
	
	private GoodValidator(){}
	
	static Map<String, Object> validateBeforeCreateGood(Good good) {
		GoodService goodService = SpringContextHolder.getBean(GoodService.class);
		Map<String, Object> map = new HashMap<>();
		
		// 供货商编号不能为空过滤
		if (StringUtils.isBlank(good.getSupplierId())) {
			map.put("code", GoodErrorBuilder.ERROR_SUPPLIERID_NULL);
			map.put("msg", "供货商编号不能为空");
			return map;
		}

		// 商品名称不能为空过滤
		if (StringUtils.isBlank(good.getName())) {
			map.put("code", GoodErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "商品名称不能为空");
			return map;
		}

		// 商品型号不能为空过滤
		if (StringUtils.isBlank(good.getModel())) {
			map.put("code", GoodErrorBuilder.ERROR_MODEL_NULL);
			map.put("msg", "商品型号不能为空");
			return map;
		}
		
		// 售价不能为空过滤
		if (good.getPrice() == null) {
			map.put("code", GoodErrorBuilder.ERROR_PRICE_NULL);
			map.put("msg", "商品售价不能为空");
		}
		
		// 商品最小单位不能为空
		if (good.getAtomUnit() == null) {
			map.put("code", GoodErrorBuilder.ERROR_ATOMUNIT_NULL);
			map.put("msg", "商品最小单位不能为空");
		}
		
		// 检验数据长度
		Map<String, Object> lengthErrorCode = validateLength(good);
		if (lengthErrorCode != null) {
			return lengthErrorCode;
		}
		
		// 验证唯一性
		if (goodService.isExists(good.getModel(), null, good.getBusinessId())) {
			map.put("code", GoodErrorBuilder.ERROR_MODEL_REPEAT);
			map.put("msg", "商品型号不能重复");
			return map;
		}

		return null;
	}
	
	static Map<String, Object> validateBeforeUpdateGood(Good good) {
		GoodService goodService = SpringContextHolder.getBean(GoodService.class);
		Map<String, Object> map = new HashMap<>();
		
		if (good.getId() == null) {
			map.put("code", GoodErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "商品编号为空");
			return map;
		}
		
		// 供货商编号不能为空过滤
		if (StringUtils.isBlank(good.getSupplierId())) {
			map.put("code", GoodErrorBuilder.ERROR_SUPPLIERID_NULL);
			map.put("msg", "供货商编号不能为空");
			return map;
		}

		// 商品名称不能为空过滤
		if (StringUtils.isBlank(good.getName())) {
			map.put("code", GoodErrorBuilder.ERROR_NAME_NULL);
			map.put("msg", "商品名称不能为空");
			return map;
		}

		// 商品型号不能重复
		if (StringUtils.isBlank(good.getModel())) {
			map.put("code", GoodErrorBuilder.ERROR_MODEL_NULL);
			map.put("msg", "商品型号不能为空");
			return map;
		}
		
		// 售价不能为空过滤
		if (good.getPrice() == null) {
			map.put("code", GoodErrorBuilder.ERROR_PRICE_NULL);
			map.put("msg", "商品售价不能为空");
		}
		
		// 商品最小单位不能为空
		if (good.getAtomUnit() == null) {
			map.put("code", GoodErrorBuilder.ERROR_ATOMUNIT_NULL);
			map.put("msg", "商品最小单位不能为空");
		}
		
		// 检验数据长度
		Map<String, Object> lengthErrorCode = validateLength(good);
		if (lengthErrorCode != null) {
			return lengthErrorCode;
		}
		
		// 验证唯一性
		if (goodService.isExists(good.getModel(), good.getOriginValue(), good.getBusinessId())) {
			map.put("code", GoodErrorBuilder.ERROR_MODEL_REPEAT);
			map.put("msg", "商品型号不能重复");
			return map;
		}
		
		return null;
	}
	
	static Map<String, Object> validateLength(Good good) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtils.isNotBlank(good.getColor()) && good.getColor().length() > 10 ) {
			map.put("code", GoodErrorBuilder.ERROR_COLOR_TOOLONG);
			map.put("msg", "长度不能超过10");
			return map;
		}
		
		if (StringUtils.isNotBlank(good.getSpecification()) && good.getSpecification().length() > 20 ) {
			map.put("code", GoodErrorBuilder.ERROR_SPECIFICATION_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(good.getType()) && good.getType().length() > 20 ) {
			map.put("code", GoodErrorBuilder.ERROR_TYPE_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(good.getName()) && good.getName().length() > 50 ) {
			map.put("code", GoodErrorBuilder.ERROR_NAME_TOOLONG);
			map.put("msg", "长度不能超过50");
			return map;
		}
		
		if (StringUtils.isNotBlank(good.getModel()) && good.getModel().length() > 20 ) {
			map.put("code", GoodErrorBuilder.ERROR_MODEL_TOOLONG);
			map.put("msg", "长度不能超过20");
			return map;
		}
		
		if (StringUtils.isNotBlank(good.getId()) && good.getId().length() > 36 ) {
			map.put("code", GoodErrorBuilder.ERROR_ID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(good.getSupplierId()) && good.getSupplierId().length() > 36 ) {
			map.put("code", GoodErrorBuilder.ERROR_SUPPLIERID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		if (StringUtils.isNotBlank(good.getBusinessId()) && good.getBusinessId().length() > 36 ) {
			map.put("code", GoodErrorBuilder.ERROR_BUSINESSID_TOOLONG);
			map.put("msg", "长度不能超过36");
			return map;
		}
		
		return null;
	}
	
	static Map<String, Object> validateBeforeDeleteGood(String id) {
		GoodService goodService = SpringContextHolder.getBean(GoodService.class);
		Map<String, Object> errorCode = new HashMap<>();
		
		if (goodService.useInOrder(id)) {
			errorCode.put("code", GoodErrorBuilder.ERROR_GOOD_INUSE);
			errorCode.put("msg", "商品处于使用中");
			return errorCode;
		}
		
		return null;
	}
	
	static Map<String, Object> validateBeforeChangeStatus(Integer onsaleStatus) {
		Map<String, Object> errorCode = new HashMap<>();
		if (!Good.ONSALE_STATUS.equals(onsaleStatus) && !Good.SOLD_OUT.equals(onsaleStatus) && !Good.STOP_PRODUCT.equals(onsaleStatus)) {
			errorCode.put("code", GoodErrorBuilder.ERROR_EDITED);
			errorCode.put("msg", "没有发现该状态的商品信息");
			return errorCode;
		}
		
		return null;
	}
	
}