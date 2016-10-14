package com.gnet.poi.reader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.gnet.poi.config.PoiProperties;
import com.gnet.poi.praser.IPoiExcelPraser;

/**
 * POI EXCEL Default导入类
 *
 * @ClassName DefaultPoiExcelReader
 * @Description TODO
 * @author wct
 * @date 2016年9月1日
 */
public class DefaultPoiExcelReader implements IPoiExcelReader {
	
	private static final Logger LOG = LoggerFactory.getLogger(DefaultPoiExcelReader.class);
	
	private PoiProperties properties;
	
	private Workbook wb;
	
	public DefaultPoiExcelReader(PoiProperties properties) {
		this.properties = properties;
	}
	
	/**
	 * 根据默认路径+文件名读取导入文件
	 */
	@Override
	public IPoiExcelReader openUploadFile(String filename) {
		return openUploadFile(new FileSystemResource(properties.getUploadExcelRootPath() + filename));
	}
	
	/**
	 * 传入文件读取导入文件
	 */
	@Override
	public IPoiExcelReader openUploadFile(File file) {
		return openUploadFile(new FileSystemResource(file));
	}
	
	/**
	 * 传入文件资源读取导入文件
	 */
	@Override
	public IPoiExcelReader openUploadFile(Resource uploadResource) {
		Assert.notNull(uploadResource);
		
		try {
			wb = WorkbookFactory.create(uploadResource.getFile());
			
		} catch (EncryptedDocumentException e) {
			LOG.error("open template file '" + uploadResource.getFilename() +  "' faild", e);
		} catch (InvalidFormatException e) {
			LOG.error("open template file '" + uploadResource.getFilename() +  "' faild", e);
		} catch (IOException e) {
			LOG.error("open template file '" + uploadResource.getFilename() +  "' faild", e);
		}
		
		return this;
	}

	/**
	 * 导入文件解析成数据
	 */
	@Override
	public <T extends Object> IPoiExcelReader prase(IPoiExcelPraser<T> praser, List<T> dataList) {
		praser.prase(wb, dataList);
		return this;
	}
	
	/**
	 * 导入结束处理方法
	 */
	@Override
	public void end() {
		try {
			if (wb != null) {
				wb.close();
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

	@Override
	public Workbook getWorkbook() {
		return wb;
	}
}
