package com.gnet.permissionGen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.gnet.exception.CellTypeValidationException;
import com.gnet.tools.plugin.poi.ExcelColumn;
import com.gnet.tools.plugin.poi.ExcelField;
import com.gnet.tools.plugin.poi.ExcelHead;
import com.gnet.tools.plugin.poi.ExcelHelper;
import com.gnet.tools.plugin.poi.ExcelModel;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jfinal.aop.Before;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlReporter;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.druid.DruidPlugin;

import jodd.util.PropertiesUtil;

public class PermissionGen {
	
	private static final Log LOG = Log.getLog(PermissionGen.class);
	
	private static ExcelHelper excelHelper = new ExcelHelper();
	
	public static void main(String[] args) {
		init();
		List<Record> records = Lists.newArrayList();
		readExcel(records, PathKit.getRootClassPath() + "/excel/permissions.xls");
		
		// 检查功能权限是否都在表中，若不在添加
		if (!checkPermissionInDB(records)) {
			System.err.println("功能权限检查添加失败");
			return;
		}
		
		LOG.info("权限导入完成");
	}

	private static void init() {
		Properties p = getProperties("application.properties");
		DruidPlugin druidPlugin = new DruidPlugin(p.getProperty("spring.datasource.url"), p.getProperty("spring.datasource.username"), p.getProperty("spring.datasource.password"));
		ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
		activeRecordPlugin.setDialect(new MysqlDialect());
		activeRecordPlugin.setShowSql(true);
		
		activeRecordPlugin.addMapping("sys_permission", Permission.class);
		
		druidPlugin.start();
		activeRecordPlugin.start();
		// sql记录
		SqlReporter.setLog(true);
		
		ExcelHead head = new ExcelHead();
		ExcelModel excelModel = ApiPermissionExcel.class.getAnnotation(ExcelModel.class);
		Field[] fields = ApiPermissionExcel.class.getDeclaredFields();
		List<ExcelColumn> excelColumns = new ArrayList<ExcelColumn>();

		Map<String, Map<Object, Object>> convertMap = new ConcurrentHashMap<String, Map<Object, Object>>();
		Map<String, Map<Object, Object>> reConvertMap = new ConcurrentHashMap<String, Map<Object, Object>>();
		for (Field field : fields) {
			ExcelField excelField = field.getAnnotation(ExcelField.class);
			String index = excelField.index();
			String fieldName = excelField.fieldName();
			String title = excelField.title();
			String[] converts = excelField.convert();
			@SuppressWarnings("deprecation")
			String shareColumnCount = excelField.shareColumnCount();
			String shareColumnTarget = excelField.shareColumnTarget();
			Class<?> fieldType = field.getType();

			ExcelColumn excelColumn = new ExcelColumn(Integer.valueOf(index), fieldName, title, fieldType, Integer.valueOf(shareColumnCount));
			if (StringUtils.isNotBlank(shareColumnTarget)) {
				excelColumn.setShareColumnTarget(shareColumnTarget);
			}
			excelColumns.add(excelColumn);

			Map<Object, Object> map = new ConcurrentHashMap<Object, Object>();
			Map<Object, Object> reMap = new ConcurrentHashMap<Object, Object>();
			for (String convert : converts) {
				String[] entry = convert.split(":");
				map.put(entry[0], entry[1]);
				reMap.put(entry[1], entry[0]);
			}
			if (!map.isEmpty()) {
				convertMap.put(fieldName, map);
				reConvertMap.put(fieldName, reMap);
			}
		}
		head.setColumnCount(Integer.valueOf(excelModel.colsCount()));
		head.setRowCount(Integer.valueOf(excelModel.rowCount()));
		head.setColumns(excelColumns);
		head.setColumnsConvertMap(convertMap);
		head.setReColumnsConvertMap(reConvertMap);
		head.setTargetSheetIndex(Integer.valueOf(excelModel.targetSheetIndex()));
		ExcelHelper.mappings.put(excelModel.name(), head);
	}
	
	@Before(Tx.class)
	private static boolean checkPermissionInDB(List<Record> records) {
		List<Permission> permissions = Permission.dao.find("select * from sys_permission");
		HashSet<String> codes = Sets.newHashSet();
		for (Permission permission : permissions) {
			codes.add(permission.getStr("code"));
		}
		
		List<Permission> addList = Lists.newArrayList();
		StringBuilder info = new StringBuilder();
		for (Record record : records) {
			String code  = record.getStr("code").trim();
			if (!codes.contains(code)) {
				Permission permission = new Permission();
				permission.set("code", record.getStr("code").trim());
				permission.set("name", record.getStr("name").trim());
				permission.set("group", record.getStr("group").trim());
				permission.set("id", Db.queryStr("select uuid() from dual"));
				addList.add(permission);
				
				info.append("\n新增功能权限" + code + ", 编号为" + permission.getStr("id")); 
			}
		}
		
		LOG.info(info.toString());
		return addList.isEmpty() || !ArrayUtils.contains(Db.batchSave(addList, addList.size()), 0);
	}
	
	private static void readExcel(List<Record> records, String excelFile) {
		File file = new File(excelFile);
		try {
			records.addAll(excelHelper.importToRecordList("apiPermissionExcel", file));
		} catch (FileNotFoundException e) {
			LOG.error("权限EXCEL未找到, path:" + excelFile);
		} catch (CellTypeValidationException e) {
			LOG.error("文件导入转化错误");
		} catch (Exception e) {
			LOG.error("文件导入转化错误");
		}
	}
	
	private static Properties getProperties(String fileName) {
		try {
			return PropertiesUtil.createFromFile(PathKit.getRootClassPath() + File.separator + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}

