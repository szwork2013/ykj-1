package com.gnet.poi.config;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * POI 属性文件
 *
 * @ClassName PoiProperties
 * @Description TODO
 * @author wct
 * @date 2016年9月1日
 */
@Getter
@Setter
@ToString
@ConfigurationProperties
public class PoiProperties {
	
	// SXSSF window size, default 100 rows in memory
	private Integer flushSize = SXSSFWorkbook.DEFAULT_WINDOW_SIZE;
	// 默认模版（空白模板）路径
	private String defaultTemplateExcelPath = "default.xlsx";
	// 上传模板默认路径
	private String uploadExcelRootPath = "upload/";
	
}
