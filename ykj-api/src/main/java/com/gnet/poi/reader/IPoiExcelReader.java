package com.gnet.poi.reader;

import java.io.File;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;

import com.gnet.poi.praser.IPoiExcelPraser;

/**
 * POI EXCEL 导入接口
 *
 * @ClassName IPoiExcelReader
 * @Description TODO
 * @author wct
 * @date 2016年9月1日
 */
public interface IPoiExcelReader {
	
	public IPoiExcelReader openUploadFile(String filename);
	
	public IPoiExcelReader openUploadFile(File file);
	
	public IPoiExcelReader openUploadFile(Resource uploadResource);
	
	public <T extends Object> IPoiExcelReader prase(IPoiExcelPraser<T> praser, List<T> dataList);
	
	public void end();
	
	public Workbook getWorkbook();
}
