package com.gnet.poi.config;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gnet.poi.reader.DefaultPoiExcelReader;
import com.gnet.poi.reader.IPoiExcelReader;
import com.gnet.poi.writer.DefaultPoiExcelWriter;
import com.gnet.poi.writer.IPoiExcelWriter;

/**
 * POI EXCEL 操作管理类
 *
 * @ClassName PoiExcelManager
 * @Description TODO
 * @author wct
 * @date 2016年9月1日
 */
public class PoiExcelManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(PoiExcelManager.class);
	
	private PoiProperties properties;
	
	protected PoiExcelManager(PoiProperties properties) {
		this.properties = properties;
	}
	
	/**
	 * 获得默认的导出类对象
	 * @return
	 */
	public DefaultPoiExcelWriter getWriter() {
		return getWriter(null);
	}
	
	/**
	 * 传入POI配置文件，获得默认的导出类对象
	 * @param properties POI配置文件
	 * @return
	 */
	public DefaultPoiExcelWriter getWriter(PoiProperties properties) {
		if (properties != null) {
			return new DefaultPoiExcelWriter(properties);
		}
		
		return new DefaultPoiExcelWriter(this.properties);
	}
	
	/**
	 * 传入POI配置文件与自定义导出类，获得导出类对象
	 * @param properties POI配置文件
	 * @param writerClass 自定义导出类
	 * @return
	 */
	public <T extends IPoiExcelWriter> T getWriter(PoiProperties properties, Class<T> writerClass) {
		T result = null;
		try {
			Constructor<T> constructor = writerClass.getDeclaredConstructor(PoiProperties.class);
			if (properties != null) {
				result =  constructor.newInstance(properties);
			}
			
			result =  constructor.newInstance(this.properties);
		} catch (Exception e) {
			LOG.error(writerClass.getSimpleName() + " instance failed", e);
		}
		
		return result;
	}
	
	/**
	 * 获得默认导入类
	 * @return
	 */
	public DefaultPoiExcelReader getReader() {
		return getReader(null);
	}
	
	/**
	 * 传入POI配置文件，获得默认的导入类对象
	 * @param properties POI配置文件
	 * @return
	 */
	public DefaultPoiExcelReader getReader(PoiProperties properties) {
		if (properties != null) {
			return new DefaultPoiExcelReader(properties);
		}
		
		return new DefaultPoiExcelReader(this.properties);
	}
	
	/**
	 * 传入POI配置文件和自定义导入类，获得导入类对象
	 * @param properties POI配置文件
	 * @param writerClass 自定义导出类
	 * @return
	 */
	public <T extends IPoiExcelReader> T getReader(PoiProperties properties, Class<T> readerClass) {
		T result = null;
		try {
			Constructor<T> constructor = readerClass.getDeclaredConstructor(PoiProperties.class);
			if (properties != null) {
				result =  constructor.newInstance(properties);
			}
			
			result =  constructor.newInstance(this.properties);
		} catch (Exception e) {
			LOG.error(readerClass.getSimpleName() + " instance failed", e);
		}
		
		return result;
	}
}
