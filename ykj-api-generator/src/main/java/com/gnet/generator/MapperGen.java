package com.gnet.generator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gnet.tools.TemplateKit;
import com.gnet.tools.ToCamelNameTemplateMethodModel;
import com.google.common.collect.Maps;

public class MapperGen {
	
	private static Logger LOG = LoggerFactory.getLogger(MapperGen.class);
	
	@SuppressWarnings("unchecked")
	public static void process(Map<String, Object> paramsMap) {
		
		String targetDirPath = (String) paramsMap.get("targetDirPath");
		String packageParentDir = (String) paramsMap.get("packageParentDir");
		String resourceName = (String) paramsMap.get("resourceName");
		String resourceNameComplex = (String) paramsMap.get("resourceNameComplex");
		String tableName = (String) paramsMap.get("tableName");
		Boolean isSoftDel = (Boolean) paramsMap.get("isSoftDel");
		String softDelColumn = (String) paramsMap.get("softDelColumn");
		List<String> searchList = (List<String>) paramsMap.get("searchList");
		Map<String, List<Object>> columnMap = (Map<String, List<Object>>) paramsMap.get("columnMap");
		Boolean isUseXmlMapper = (Boolean) paramsMap.get("isUseXmlMapper");
		
		Map<String, String> columnTypeMap = Maps.newLinkedHashMap();
		for (String item : searchList) {
			if (columnMap.get(item) == null) {
				LOG.error(item + "不存在" + tableName + "中");
				throw new RuntimeException(item + "不存在" + tableName + "中");
			}
			
			columnTypeMap.put(item, columnMap.get(item).get(0).toString());
		}
		
		Map<String, Object> model = new HashMap<>();
		model.put("packageDir", packageParentDir);
		model.put("resourceName", resourceName);
		model.put("tableName", tableName);
		model.put("isSoftDel", isSoftDel);
		model.put("softDelColumn", softDelColumn);
		model.put("searchList", searchList);
		model.put("searchMap", columnTypeMap);
		model.put("columnMap", columnMap);
		model.put("isUseXmlMapper", isUseXmlMapper);
		model.put("resourceNameComplex", resourceNameComplex == null ? resourceName + "s" : resourceNameComplex);
		
		
		String className = ToCamelNameTemplateMethodModel.camelName(resourceName);
		className = new StringBuilder().append(Character.toUpperCase(className.charAt(0))).append(className.substring(1)).toString();
		TemplateKit.process("Mapper.ftl"
				, targetDirPath + File.separator + className + "Mapper.java"
				, model);
		
		LOG.info(className + "Mapper生成成功");
		
		if (isUseXmlMapper) {
			Map<String, String> jdbcTypeMap = Maps.newHashMap(); 
			jdbcTypeMap.put("date", "DATE");
			jdbcTypeMap.put("tinyint", "TINYINT");
			jdbcTypeMap.put("int", "INTEGER");
			jdbcTypeMap.put("smallint", "SMALLINT");
			jdbcTypeMap.put("mediumint", "INTEGER");
			jdbcTypeMap.put("bigint", "BIGINT");
			jdbcTypeMap.put("char", "CHAR");
			jdbcTypeMap.put("varchar", "VARCHAR");
			jdbcTypeMap.put("text", "VARCHAR");
			jdbcTypeMap.put("tinytext", "VARCHAR");
			jdbcTypeMap.put("mediumtext", "VARCHAR");
			jdbcTypeMap.put("longtext", "LONGVARCHAR");
			jdbcTypeMap.put("bit", "BOOLEAN");
			jdbcTypeMap.put("decimal", "DECIMAL");
			model.put("jdbcTypeMap", jdbcTypeMap);
			
			TemplateKit.process("MapperXml.ftl"
					, targetDirPath + File.separator + className + "Mapper.xml"
					, model);
			
			LOG.info(className + "Mapper的xml生成成功");
		}
	}

}
