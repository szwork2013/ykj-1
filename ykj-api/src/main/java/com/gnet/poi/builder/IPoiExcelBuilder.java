package com.gnet.poi.builder;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * POI EXCEL Builder接口
 * 
 * @ClassName IPoiExcelBuilder
 * @Description TODO
 * @author wct
 * @date 2016年9月1日
 */
public interface IPoiExcelBuilder {
	
	public void build(Workbook wb);
	
}
