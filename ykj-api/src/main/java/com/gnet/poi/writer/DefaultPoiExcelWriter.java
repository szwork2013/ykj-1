package com.gnet.poi.writer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.gnet.poi.builder.IPoiExcelBuilder;
import com.gnet.poi.builder.IPoiExcelModelBuilder;
import com.gnet.poi.config.PoiProperties;

/**
 * POI EXCEL 默认导出类
 *
 * @ClassName DefaultPoiExcelWriter
 * @Description TODO
 * @author wct
 * @date 2016年9月1日
 */
public class DefaultPoiExcelWriter implements IPoiExcelWriter {
	
	private static final Logger LOG = LoggerFactory.getLogger(DefaultPoiExcelWriter.class);
	
	private PoiProperties properties;
	
	private Workbook wb;
	
	
	public DefaultPoiExcelWriter(PoiProperties properties) {
		this.properties = properties;
	}
	
	/**
	 * 读取导出模板文件
	 */
	@Override
	public IPoiExcelWriter openTemplateFile() {
		return openTemplateFile(null, false);
	}
	
	/**
	 * 读取导出模板文件，并选择使用SXSSFWorkbook
	 */
	@Override
	public IPoiExcelWriter openTemplateFile(boolean useSXSSF) {
		return openTemplateFile(null, useSXSSF);
	}
	
	/**
	 * 传入并读取导出模板资源文件，并选择使用SXSSFWorkbook
	 */
	@Override
	public IPoiExcelWriter openTemplateFile(Resource templateResource, boolean useSXSSF) {
		if (templateResource == null) {
			templateResource = new FileSystemResource(properties.getDefaultTemplateExcelPath());
		}
		
		try {
			wb = WorkbookFactory.create(templateResource.getInputStream());
			if (useSXSSF && wb instanceof XSSFWorkbook) {
				wb = new SXSSFWorkbook((XSSFWorkbook) wb, properties.getFlushSize());
			}
			
		} catch (EncryptedDocumentException e) {
			LOG.error("open template file '" + templateResource.getFilename() +  "' faild", e);
		} catch (InvalidFormatException e) {
			LOG.error("open template file '" + templateResource.getFilename() +  "' faild", e);
		} catch (IOException e) {
			LOG.error("open template file '" + templateResource.getFilename() +  "' faild", e);
		}
		
		return this;
	}
	
	@Override
	public void setWorkbook(Workbook workbook) {
		this.wb = workbook;
	}
	
	/**
	 * 构建Excel表结构
	 */
	@Override
	public IPoiExcelWriter build(IPoiExcelBuilder builder) {
		builder.build(wb);
		return this;
	}
	
	/**
	 * 使用模板Builder构建Excel表结构
	 */
	@Override
	public <T extends Object, S extends Map<String, T>> IPoiExcelWriter build(IPoiExcelModelBuilder<T, S> builder, S dataMap) {
		builder.build(wb, dataMap);
		return this;
	}
	
	/**
	 * 生成导出文件
	 */
	@Override
	public void process(Resource outputResource) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(outputResource.getFile());
			wb.write(out);
			IOUtils.closeQuietly(out);
		} catch (FileNotFoundException e) {
			LOG.error("process download file '" + outputResource.getFilename() + "' faild", e);
		} catch (IOException e) {
			LOG.error("process download file '" + outputResource.getFilename() + "' faild", e);
		} finally {
			try {
				if (wb != null) {
					if (wb instanceof SXSSFWorkbook) {
						((SXSSFWorkbook) wb).dispose();
					}
					
					wb.close();
				}
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
		}
		
	}

}
