package com.gnet.poi.writer;

import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;

import com.gnet.poi.builder.IPoiExcelBuilder;
import com.gnet.poi.builder.IPoiExcelModelBuilder;

/**
 * POI EXCEL 导出接口
 *
 * @ClassName IPoiExcelWriter
 * @Description TODO
 * @author wct
 * @date 2016年9月1日
 */
public interface IPoiExcelWriter {
	
	public IPoiExcelWriter openTemplateFile();
	
	public IPoiExcelWriter openTemplateFile(boolean useSXSSF);
	
	public IPoiExcelWriter openTemplateFile(Resource templateResource, boolean useSXSSF);
	
	public void setWorkbook(Workbook workbook);
	
	public void process(Resource outputResource);
	
	public IPoiExcelWriter build(IPoiExcelBuilder builder);
	
	public <T extends Object, S extends Map<String, T>> IPoiExcelWriter build(IPoiExcelModelBuilder<T, S> builder, S map);
}
