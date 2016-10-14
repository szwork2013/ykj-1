package com.gnet.utils.sort;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.gnet.utils.sort.exception.NotFoundOrderDirectionException;
import com.gnet.utils.sort.exception.NotFoundOrderPropertyException;


/**
 * 参数情景化处理工具类
 * @author SY
 * @Date 2016年9月10日
 */
public class ParamSceneUtils {
	
	private final static Logger logger = Logger.getLogger(ParamSceneUtils.class);
	
	
	/**
	 * 排序参数解析
	 * 若无排序参数或排序参数正确成功则返回true
	 * 若有排序参数无法获得返回false
	 * @param orderProperty
	 * 			排序参数
	 * @param orderDirection
	 * 			排序方向
	 * @param typeEnum
	 * 			排序类型枚举
	 */
	public static List<String> toOrder(Pageable pageable, Class<? extends OrderType> typeEnumClass) {
		return toOrder(pageable, typeEnumClass, false);
	}
	
	/**
	 * 排序参数解析
	 * 若无排序参数或排序参数正确成功则返回true
	 * 若有排序参数无法获得返回false
	 * @param orderProperty
	 * 			排序参数
	 * @param orderDirection
	 * 			排序方向
	 * @param typeEnum
	 * 			排序类型枚举
	 * @param isUpperCase
	 * 			是否变成大写
	 */
	public static List<String> toOrder(Pageable pageable, Class<? extends OrderType> typeEnumClass, Boolean isUpperCase){
		/*
		 *  1. 获取sort数据
		 *  2. 循环获取对象，判断是否属于适合的
		 *  3. 不适合弹出，适合就变成数据库字段，从typeName变成xx.type_name/type_name
		 */
		List<String[]> ordersList = new ArrayList<>(); 
		
		Sort sort = pageable.getSort();
		if (sort != null) {
			Map<String, String> map = null;
			try {
				map = getValueByKey(typeEnumClass);
			} catch(Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error(typeEnumClass.getName() + "failed to map", e.getCause());
				}
				return null;
			}
			
			final Map<String, String> mapSelect = new HashMap<>();
			mapSelect.putAll(map);
			
			sort.forEach(order -> {
				String[] temp = new String[2];
				String property = order.getProperty();
				Direction direction = order.getDirection();
				
				if (mapSelect.get(property) == null) {
					throw new NotFoundOrderPropertyException("can't analysis OrderProperty param '" + property + "'");
				}
				property = isUpperCase ? mapSelect.get(property).toUpperCase() : mapSelect.get(property);
				// 放入property
				temp[0] = property;
				
				if (direction == null || OrderDirectionType.DESC.getValue().equalsIgnoreCase(direction.toString())) {
					temp[1] = isUpperCase ? OrderDirectionType.DESC.getValue().toUpperCase() : OrderDirectionType.DESC.getValue();
				} else if (OrderDirectionType.ASC.getValue().equalsIgnoreCase(direction.toString())) {
					temp[1] = isUpperCase ? OrderDirectionType.ASC.getValue().toUpperCase() : OrderDirectionType.ASC.getValue();
				} else {
					throw new NotFoundOrderDirectionException("can't analysis OrderDirection param '" + direction + "'");
				}
				
				ordersList.add(temp);
			});
		}
		
		List<String> returnList = new ArrayList<>();
		for(String[] temp : ordersList) {
			String tempAdd = temp[0] + " " + temp[1];
			returnList.add(tempAdd);
		}
		return returnList.isEmpty() ? null : returnList;
	}
	
	/**
	 * 
	 * @param cls
	 * 		枚举类型的类
	 * @return
	 * 		枚举转化成map
	 * @throws Exception
	 */
	private static Map<String, String> getValueByKey(Class<? extends OrderType> cls) throws Exception {
		Map<String, String> result = new HashMap<>();
		Method method = cls.getMethod("values");
		OrderType[] orderTypes = (OrderType[]) method.invoke(null);
		for (OrderType orderType : orderTypes) {
			result.put(orderType.getKey(), orderType.getValue());
		}
		return result;
	}

}
