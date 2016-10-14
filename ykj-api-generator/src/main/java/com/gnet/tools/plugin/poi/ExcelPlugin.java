package com.gnet.tools.plugin.poi;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;

/**
 * Excel插件
 * 
 * @author <a href="mailto:104xuqiang@163.com">xuq</a>
 * @date Jan 16, 2015
 */
public class ExcelPlugin implements IPlugin {

	private static final Log logger = Log.getLog(ExcelPlugin.class);

	@SuppressWarnings("deprecation")
	@Override
	public boolean start() {
		try {
			List<Class<?>> clazzes = ClassSearcher.findClasses(ExcelModel.class);
			for (Class<?> clazz : clazzes) {
				ExcelHead head = new ExcelHead();
				ExcelModel excelModel = clazz.getAnnotation(ExcelModel.class);

				Field[] fields = clazz.getDeclaredFields();
				List<ExcelColumn> excelColumns = new ArrayList<ExcelColumn>();

				Map<String, Map<Object, Object>> convertMap = new ConcurrentHashMap<String, Map<Object, Object>>();
				Map<String, Map<Object, Object>> reConvertMap = new ConcurrentHashMap<String, Map<Object, Object>>();
				for (Field field : fields) {
					ExcelField excelField = field.getAnnotation(ExcelField.class);
					String index = excelField.index();
					String fieldName = excelField.fieldName();
					String title = excelField.title();
//					String type = excelField.type();
					String[] converts = excelField.convert();
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

				logger.info("Excel Model Mapping:" + excelModel.name() + "==> targetClass:" + excelModel.clazz().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}
