package com.gnet.poi.praser;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * POI EXCEL 导入解析接口
 *
 * @ClassName IPoiExcelPraser
 * @Description TODO
 * @author wct
 * @date 2016年9月1日
 * @param <T>
 */
public interface IPoiExcelPraser<T> {
	
	public void prase(Workbook wb, List<T> dataList);
}
