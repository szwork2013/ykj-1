package com.gnet.poi.config;

import org.apache.poi.Version;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * POI Spring-Boot配置类
 *
 * @ClassName PoiAutoConfiguration
 * @Description TODO
 * @author wct
 * @date 2016年9月1日
 */
@Configuration
@ConditionalOnClass({Version.class, Workbook.class})
@EnableConfigurationProperties(PoiProperties.class)
public class PoiAutoConfiguration {
	
	private static final Logger LOG = LoggerFactory.getLogger(PoiAutoConfiguration.class);
	
	@Autowired
	PoiProperties poiProperties;
	
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass({HSSFWorkbook.class, XSSFWorkbook.class, SXSSFWorkbook.class})
	public PoiExcelManager poiExcelManager() {
		LOG.info("Trying to init{} by auto-configuration.", PoiExcelManager.class.getSimpleName());
		return new PoiExcelManager(poiProperties);
	}
	
}
