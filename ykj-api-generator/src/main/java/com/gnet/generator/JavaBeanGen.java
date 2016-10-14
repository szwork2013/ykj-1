package com.gnet.generator;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gnet.tools.TemplateKit;
import com.gnet.tools.ToCamelNameTemplateMethodModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class JavaBeanGen {
	
	private static Logger LOG = LoggerFactory.getLogger(JavaBeanGen.class);
	
	public static void process(String targetDirPath, String packageParentDir
			, String dbName, String tableName, String resourceName, Map<String, List<Object>> columnMap) {
		
		// 获得表结构
		List<Record> columns = Db.find("select TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, IS_NULLABLE, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH, COLUMN_KEY, COLUMN_DEFAULT, NUMERIC_PRECISION, COLUMN_COMMENT from information_schema.columns where table_name = ? and TABLE_SCHEMA = ?", tableName, dbName);
		
		// sql类型映射java类型
		Map<String, String> sqlTypeToJavaType = new HashMap<>();
		
		sqlTypeToJavaType.put("datetime", "Date");
		sqlTypeToJavaType.put("date", "Date");
		sqlTypeToJavaType.put("tinyint", "Integer");
		sqlTypeToJavaType.put("int", "Integer");
		sqlTypeToJavaType.put("smallint", "Integer");
		sqlTypeToJavaType.put("mediumint", "Integer");
		sqlTypeToJavaType.put("bigint", "Long");
		sqlTypeToJavaType.put("char", "String");
		sqlTypeToJavaType.put("varchar", "String");
		sqlTypeToJavaType.put("text", "String");
		sqlTypeToJavaType.put("tinytext", "String");
		sqlTypeToJavaType.put("mediumtext", "String");
		sqlTypeToJavaType.put("longtext", "String");
		sqlTypeToJavaType.put("bit", "Boolean");
		sqlTypeToJavaType.put("decimal", "BigDecimal");
		
		for (Record column : columns) {
			List<Object> list = Lists.newArrayList();
			list.add(sqlTypeToJavaType.get(column.getStr("DATA_TYPE")));
			list.add(column.getBigInteger("CHARACTER_MAXIMUM_LENGTH"));
			list.add(column.getStr("DATA_TYPE"));
			columnMap.put(column.getStr("COLUMN_NAME"), list);
		}
		
		// 定义模板变量
		Map<String, Object> model = Maps.newHashMap();
		model.put("columns", columns);
		model.put("tableName", tableName);
		model.put("resourceName", resourceName);
		model.put("sqlTypeToJavaType", sqlTypeToJavaType);
		model.put("packageDir", packageParentDir);
		
		String className = ToCamelNameTemplateMethodModel.camelName(resourceName);
		className = new StringBuilder().append(Character.toUpperCase(className.charAt(0))).append(className.substring(1)).toString();
		TemplateKit.process("JavaBean.ftl"
							, targetDirPath + File.separator + className + ".java"
							, model);
		
		LOG.info(resourceName + "生成成功");
	}
}
