package com.gnet.poi.builder;

import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * POI EXCEL 模版Builder接口
 *
 * @ClassName IPoiExcelModelBuilder
 * @Description TODO
 * @author wct
 * @date 2016年9月1日
 * @param <T>
 * @param <S>
 */
public interface IPoiExcelModelBuilder<T extends Object, S extends Map<String, T>> {
	
	public void build(Workbook wb, S map);
	
}
