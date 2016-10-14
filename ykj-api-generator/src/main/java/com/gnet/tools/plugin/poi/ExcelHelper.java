package com.gnet.tools.plugin.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

import com.gnet.exception.CellTypeValidationException;
import com.jfinal.kit.JsonKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

import jodd.datetime.JDateTime;
import jodd.util.StringUtil;

/**
 * Excel助手类
 * 
 * @description Excel助手类 提供了将excel文件导入到列表、导出excel文件、将报表结构转换成Map
 *              生成并导出excel文件的数据、将Excel文件内容转换为列表、根据Excel生成数据对象、将行转行成map格式的操作
 * @createTime: 2012-4-19 上午10:14:46
 * @author: <a href="mailto:hubo@feinno.com">hubo</a>
 * @version: 0.2
 * @lastVersion: 0.1
 * @updateTime:
 * @updateAuthor: <a href="mailto:hubo@feinno.com">hubo</a>
 * @changesSum:
 */
@Component
public class ExcelHelper {

	private static final Log logger = Log.getLog(ExcelHelper.class);
	
	public static Map<String, ExcelHead> mappings = new ConcurrentHashMap<String, ExcelHead>();

	/**
	 * 将Excel文件导入到list对象
	 * 
	 * @description 传入头文件信息、文件以及类信息，将excel文件内内容转化列表信息后，删除头信息
	 *              将表结构转化为map格式，构建成list对象返回
	 * @call {@linkplain ExcelHelper this}
	 *       {@linkplain ExcelHelper#excelFileConvertToList(FileInputStream, ExcelHead)
	 *       excelFileConvertToList}
	 * @call {@linkplain ExcelHelper this}
	 *       {@linkplain ExcelHelper#convertExcelHeadToMap(List)
	 *       convertExcelHeadToMap}
	 * @call {@linkplain ExcelHelper this}
	 *       {@linkplain ExcelHelper#buildDataObject(Map, Map, Map, List, Class)
	 *       buildDataObject}
	 * @call {@linkplain ExcelHead head} {@linkplain ExcelHead#getRowCount()
	 *       getRowCount}
	 * @call {@linkplain ExcelHead head}
	 *       {@linkplain ExcelHead#getColumnsConvertMap() getColumnsConvertMap}
	 * @call {@linkplain ExcelHead head}
	 *       {@linkplain ExcelHead#getReColumnsConvertMap()
	 *       getReColumnsConvertMap}
	 * @version 1.0
	 * @param head
	 *            文件头信息
	 * @param file
	 *            导入的数据源
	 * @param cls
	 *            保存当前数据的对象
	 * @return
	 * @auther <a href="mailto:hubo@feinno.com">hubo</a>
	 * @return List 2012-4-19 下午01:17:48
	 * @throws CellTypeValidationException 
	 * @throws FileNotFoundException 
	 * @changeFirst &emsp;<b>chageAuthor</b> xuq<br/>
	 *              &emsp;<b>chageDate</b> 2015年1月21日<br/>
	 *              &emsp;<b>changeDescription</b> 更改为导入Model集合
	 */
	@SuppressWarnings("rawtypes")
	public <T extends Model> List<T> importToCommonList(String mapping, File file, Class<T> cls) throws CellTypeValidationException, FileNotFoundException, Exception {
		ExcelHead head = mappings.get(mapping);
		List<T> contents = null;
		FileInputStream fis;
		// 根据excel生成list类型的数据
		List<List<T>> rows;
		try {
			fis = new FileInputStream(file);
			rows = excelFileConvertToList(fis, head);

			// 删除头信息
			for (int i = 0; i < head.getRowCount(); i++) {
				rows.remove(0);
			}

			// 将表结构转换成Map
			Map<Integer, ExcelColumn> excelHeadMap = convertExcelHeadToMap(head.getColumns());
			// 构建为对象
			contents = buildDataObject(excelHeadMap, head.getColumnsConvertMap(), head.getReColumnsConvertMap(), rows, cls);
		} catch (FileNotFoundException ex) {
			if (logger.isErrorEnabled()) {
				logger.error("导入文件未找到", ex);
			}
			throw ex;
		} catch (CellTypeValidationException e) {
			if (logger.isErrorEnabled()) {
				logger.error("转化类型验证异常", e);
			}
			e.setExcelHead(head);
			throw e;
		} catch (Exception ex) {
			if (logger.isErrorEnabled()) {
				logger.error("异常", ex);
			}
			throw ex;
		}
		return contents;
	}

	/**
	 * 将Excel文件导入到list对象
	 * 
	 * @description 传入头文件信息、文件 根据excel生成list类型的数据，删除头信息 将剩余表结果转换成Map，构成list对象
	 * @call {@linkplain ExcelHelper this}
	 *       {@linkplain ExcelHelper#excelFileConvertToList(FileInputStream, ExcelHead)
	 *       excelFileConvertToList}
	 * @call {@linkplain ExcelHelper this}
	 *       {@linkplain ExcelHelper#convertExcelHeadToMap(List)
	 *       convertExcelHeadToMap}
	 * @call {@linkplain ExcelHelper this}
	 *       {@linkplain ExcelHelper#buildDataObject(Map, Map, Map, List, Class)
	 *       buildDataObject}
	 * @call {@linkplain ExcelHead head} {@linkplain ExcelHead#getRowCount()
	 *       getRowCount}
	 * @call {@linkplain ExcelHead head}
	 *       {@linkplain ExcelHead#getColumnsConvertMap() getColumnsConvertMap}
	 * @call {@linkplain ExcelHead head}
	 *       {@linkplain ExcelHead#getReColumnsConvertMap()
	 *       getReColumnsConvertMap}
	 * @version 1.0
	 * @param head
	 *            文件头信息
	 * @param file
	 *            导入的数据源
	 * @return
	 * @auther xuq
	 * @return List 2012-4-19 下午01:17:48
	 * @changeFirst &emsp;<b>chageAuthor</b> xuq<br/>
	 *              &emsp;<b>chageDate</b> 2015年1月21日<br/>
	 *              &emsp;<b>changeDescription</b> 更改为导入Record集合
	 */
	public List<Record> importToRecordList(String mapping, File file) throws CellTypeValidationException, FileNotFoundException, Exception {
		ExcelHead head = mappings.get(mapping);
		List<Record> contents = null;
		FileInputStream fis;
		// 根据excel生成list类型的数据
		List<List<Record>> rows;
		try {
			fis = new FileInputStream(file);
			rows = excelFileConvertToList(fis, head);

			// 删除头信息
			for (int i = 0; i < head.getRowCount(); i++) {
				rows.remove(0);
			}

			// 将表结构转换成Map
			Map<Integer, ExcelColumn> excelHeadMap = convertExcelHeadToMap(head.getColumns());
			// 构建为对象
			contents = buildDataObject(excelHeadMap, head.getColumnsConvertMap(), head.getReColumnsConvertMap(), rows);
		} catch (FileNotFoundException ex) {
			if (logger.isErrorEnabled()) {
				logger.error("导入文件未找到", ex);
			}
			throw ex;
		} catch (CellTypeValidationException e) {
			if (logger.isErrorEnabled()) {
				logger.error("转化类型验证异常", e);
			}
			e.setExcelHead(head);
			throw e;
		} catch (Exception ex) {
			if (logger.isErrorEnabled()) {
				logger.error("异常", ex);
			}
			throw ex;
		}
		return contents;
	}

	/**
	 * 导出数据至Excel文件
	 * 
	 * @description 传入头文件信息、文件 读取导出excel模板，生成导出数据，导出到文件中
	 * @call {@linkplain ExcelHelper this}
	 *       {@linkplain ExcelHelper#buildDataObject(Map, Map, Map, List, Class)
	 *       buildDataObject}
	 * @version 1.0
	 * @param excelColumns
	 *            报表头信息
	 * @param excelHeadConvertMap
	 *            需要对数据进行特殊转换的列
	 * @param modelFile
	 *            模板Excel文件
	 * @param outputFile
	 *            导出文件
	 * @param dataList
	 *            导入excel报表的数据来源
	 * @auther <a href="mailto:hubo@feinno.com">hubo</a>
	 * @return void 2012-4-19 上午10:04:30
	 */
	@SuppressWarnings("rawtypes")
	public <T extends Model> void exportExcelFile(String mapping, File modelFile, File outputFile, List<T> dataList) {
		ExcelHead head = mappings.get(mapping);
		// 读取导出excel模板
		InputStream inp = null;
		Workbook wb = null;
		try {
			inp = new FileInputStream(modelFile);
			wb = WorkbookFactory.create(inp);
			Sheet sheet = wb.getSheetAt(0);
			// 生成导出数据
			buildExcelData(sheet, head, dataList);

			// 导出到文件中
			FileOutputStream fileOut = new FileOutputStream(outputFile);
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (InvalidFormatException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 将报表结构转换成Map
	 * 
	 * @description 传入excel列信息，遍历excel列信息 若列信息的名字为空，则跳过本次循环 不为空则向map中加入列信息，返回map
	 * @version 1.0
	 * @param excelColumns
	 * @author <a href="mailto:hubo@feinno.com">hubo</a>
	 * @return void 2012-4-18 下午01:31:12
	 */
	private Map<Integer, ExcelColumn> convertExcelHeadToMap(List<ExcelColumn> excelColumns) {
		Map<Integer, ExcelColumn> excelHeadMap = new HashMap<Integer, ExcelColumn>();
		for (ExcelColumn excelColumn : excelColumns) {
			if (StringUtil.isEmpty(excelColumn.getFieldName())) {
				continue;
			} else {
				excelHeadMap.put(excelColumn.getIndex(), excelColumn);
			}
		}
		return excelHeadMap;
	}

	/**
	 * 生成导出至Excel文件的数据
	 * 
	 * @description 传入表信息、表头信息、list类型的数据 将表结构转换为Map，从head中获得开始插入数据的行数
	 *              创建列和单元格，存在需要转换的字段信息，则进行转换
	 * @call {@linkplain ExcelHelper this}
	 *       {@linkplain ExcelHelper#convertExcelHeadToMap(List)
	 *       convertExcelHeadToMap}
	 * @call {@linkplain ExcelColumn excelColumn}
	 *       {@linkplain ExcelColumn#getFieldName() getFieldName}
	 * @version 1.0
	 * @param sheet
	 *            工作区间
	 * @param excelColumns
	 *            excel表头
	 * @param excelHeadMap
	 *            excel表头对应实体属性
	 * @param excelHeadConvertMap
	 *            需要对数据进行特殊转换的列
	 * @param dataList
	 *            导入excel报表的数据来源
	 * @auther <a href="mailto:hubo@feinno.com">hubo</a>
	 * @return void 2012-4-19 上午09:36:37
	 * @changeFirst &emsp;<b>chageAuthor</b> xuq<br/>
	 *              &emsp;<b>chageDate</b> 2015年1月21日<br/>
	 *              &emsp;<b>changeDescription</b> 改进可以预先定义目标的Sheet索引
	 */
	@SuppressWarnings("rawtypes")
	private <T extends Model> void buildExcelData(Sheet sheet, ExcelHead head, List<T> dataList) {
		List<ExcelColumn> excelColumns = head.getColumns();
		Map<String, Map<Object, Object>> excelHeadConvertMap = head.getColumnsConvertMap();

		// 将表结构转换成Map
		Map<Integer, ExcelColumn> excelHeadMap = convertExcelHeadToMap(excelColumns);

		// 从第几行开始插入数据
		int startRow = head.getRowCount();
		int order = 1;
		for (T obj : dataList) {
			Row row = sheet.createRow(startRow++);
			for (int j = head.getColumnCount(); j < excelColumns.size() + head.getColumnCount(); j++) {
				Cell cell = row.createCell(j);
				cell.setCellType(excelColumns.get(j - head.getColumnCount()).getType());
				ExcelColumn excelColumn = excelHeadMap.get(j);
				String fieldName = excelColumn.getFieldName();
				if (fieldName != null) {
					// Object valueObject = BeanUtil.getProperty(obj,
					// fieldName);
					Object valueObject = obj.get(fieldName);

					// 如果存在需要转换的字段信息，则进行转换
					if (excelHeadConvertMap != null && excelHeadConvertMap.get(fieldName) != null) {
						valueObject = excelHeadConvertMap.get(fieldName).get(String.valueOf(valueObject));
					}

					if (valueObject == null) {
						cell.setCellValue("");
					} else if (valueObject instanceof Integer) {
						cell.setCellValue((Integer) valueObject);
					} else if (valueObject instanceof String) {
						cell.setCellValue((String) valueObject);
					} else if (valueObject instanceof Date) {
						cell.setCellValue(new JDateTime((Date) valueObject).toString("YYYY-MM-DD"));
					} else {
						cell.setCellValue(valueObject.toString());
					}
				} else {
					cell.setCellValue(order++);
				}
			}
		}
	}

	/**
	 * 将Excel文件内容转换为List对象
	 * 
	 * @description 传入文件输入流、excel头信息 新建一个excel文件的类，或取文件中的表格信息
	 *              遍历表格获取行，遍历行获取单元格，根据单元格的数据类型对单元格中的内容进行转换 向list中加入行并返回list
	 * @version 1.0
	 * @param fis
	 *            excel文件
	 * @return List<List> list存放形式的内容
	 * @throws IOException
	 * @auther <a href="mailto:hubo@feinno.com">hubo</a>
	 * @return List<List> 2012-4-18 上午11:37:17
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> List<List<T>> excelFileConvertToList(FileInputStream fis, ExcelHead head) throws Exception {
		Workbook wb = WorkbookFactory.create(fis);

		Sheet sheet = wb.getSheetAt(head.getTargetSheetIndex());

		List<List<T>> rows = new ArrayList<List<T>>();
		for (Row row : sheet) {
			List<T> cells = new ArrayList<T>();
			// for (Cell cell : row) {
			int nullSize = 0;
			for (int i = 0; i < head.getColumns().size(); i++) {
				Object obj = null;
				Cell cell = row.getCell(i);
				if (cell == null) {
					cells.add(null);
					nullSize ++;
					continue;
				}

				@SuppressWarnings("unused")
				CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					obj = cell.getRichStringCellValue().getString();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell)) {
						obj = new JDateTime(cell.getDateCellValue());
					} else {
						obj = cell.getNumericCellValue();
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					obj = cell.getBooleanCellValue();
					break;
				case Cell.CELL_TYPE_FORMULA:
					obj = cell.getCellFormula();
					break;
				default:
					obj = null;
					nullSize ++;
				}
				cells.add((T) obj);
			}
			if (nullSize == head.getColumns().size()) {
				break;
			}
			rows.add(cells);
		}
		return rows;
	}

	/**
	 * 根据Excel生成数据对象
	 * 
	 * @description 传入表头信息、需要特殊转换的单元、行信息、类信息 遍历行，若当前第一列中无数据,则忽略当前行的数据
	 *              当前行的数据放入map中,生成<fieldName, value>形式的map 将当前行转换成对应的对象加入内容后返回
	 * @call {@linkplain ExcelHelper this}
	 *       {@linkplain ExcelHelper#rowListToMap(Map, Map, Map, List)
	 *       rowListToMap}
	 * @version 1.0
	 * @param excelHeadMap
	 *            表头信息
	 * @param excelHeadConvertMap
	 *            需要特殊转换的单元
	 * @param rows
	 * @param cls
	 * @auther <a href="mailto:hubo@feinno.com">hubo</a>
	 * @return void 2012-4-18 上午11:39:43
	 * @throws CellTypeValidationException 
	 * @throws Exception 
	 * @changeFirst &emsp;<b>chageAuthor</b> xuq<br/>
	 *              &emsp;<b>chageDate</b> 2015年1月21日<br/>
	 *              &emsp;<b>changeDescription</b>
	 *              增加导出为Model集合，并且适应JFinal的Model注入形式
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T extends Model> List<T> buildDataObject(Map<Integer, ExcelColumn> excelHeadMap, Map<String, Map<Object, Object>> excelHeadConvertMap, Map<String, Map<Object, Object>> excelHeadReConvertMap, List<List<T>> rows, Class<T> cls) throws CellTypeValidationException, Exception {
		List<T> contents = new ArrayList<T>();
		for (int i = 0; i < rows.size(); i++) {
			try {
				List<T> list = rows.get(i);
				// 如果当前第一列中无数据,则忽略当前行的数据
				// if (list == null || list.get(0) == null) {
				if (list == null || list.isEmpty()) {
					break;
				}
				// 当前行的数据放入map中,生成<fieldName, value>的形式
				Map<String, Object> rowMap = rowListToMap(excelHeadMap, excelHeadConvertMap, excelHeadReConvertMap, list);
	
				// 将当前行转换成对应的对象
				T obj = null;
				try {
					obj = cls.newInstance();
				} catch (InstantiationException ex) {
					throw ex;
				} catch (IllegalAccessException ex) {
					throw ex;
				}
				obj._setAttrs(rowMap);
	
				contents.add(obj);
			} catch (CellTypeValidationException e) {
				e.setRowIndex(i + 1);	// 设置出错的第几行（从第一行开始计算）
				throw e;
			}
		}
		return contents;
	}

	/**
	 * 根据Excel生成数据对象
	 * 
	 * @description 传入表头信息、需要特殊转换的单元、行信息、类信息 若当前第一列中无数据,则忽略当前行的数据
	 *              当前行的数据放入map中,生成<fieldName, value>形式的map 将当前行转换成对应的对象
	 * @call {@linkplain ExcelHelper this}
	 *       {@linkplain ExcelHelper#rowListToMap(Map, Map, Map, List)
	 *       rowListToMap}
	 * @version 1.0
	 * @param excelHeadMap
	 *            表头信息
	 * @param excelHeadConvertMap
	 *            需要特殊转换的单元
	 * @param rows
	 * @param cls
	 * @auther <a href="mailto:hubo@feinno.com">hubo</a>
	 * @return void 2012-4-18 上午11:39:43
	 * @throws CellTypeValidationException 
	 * @changeFirst &emsp;<b>chageAuthor</b> xuq<br/>
	 *              &emsp;<b>chageDate</b> 2015年1月21日<br/>
	 *              &emsp;<b>changeDescription</b>
	 *              增加导出为Record集合，并且适应JFinal的Bean注入形式
	 */
	private List<Record> buildDataObject(Map<Integer, ExcelColumn> excelHeadMap, Map<String, Map<Object, Object>> excelHeadConvertMap, Map<String, Map<Object, Object>> excelHeadReConvertMap, List<List<Record>> rows) throws CellTypeValidationException {
		List<Record> contents = new ArrayList<Record>();
		for (int i = 0; i < rows.size(); i++) {
			try {
				List<Record> list = rows.get(i);
				// 如果当前第一列中无数据,则忽略当前行的数据
				// if (list == null || list.isEmpty() || list.get(0) == null) {
				if (list == null || list.isEmpty()) {
					break;
				}
				// 当前行的数据放入map中,生成<fieldName, value>的形式
				Map<String, Object> rowMap = rowListToMap(excelHeadMap, excelHeadConvertMap, excelHeadReConvertMap, list);
	
				// 将当前行转换成对应的对象
				Record obj = null;
				obj = new Record();
				obj.setColumns(rowMap);
	
				contents.add(obj);
			} catch (CellTypeValidationException e) {
				e.setRowIndex(i + 1);	// 设置出错的第几行（从第一行开始计算）
				throw e;
			}
		}
		return contents;
	}

	/**
	 * 将行转行成map,生成<fieldName, value>的形式
	 * 
	 * @description 传入表头信息、需要特殊转换的单元、列表 遍历列表，获得列信息 清空上一组的信息后，将存在所定义的列转化为map形式并返
	 * @version 1.0
	 * @param excelHeadMap
	 *            表头信息
	 * @param excelHeadConvertMap
	 *            excelHeadConvertMap
	 * @param list
	 * @return
	 * @auther <a href="mailto:hubo@feinno.com">hubo</a>
	 * @return Map<String,Object> 2012-4-18 下午01:48:41
	 * @throws CellTypeValidationException 
	 * @changeFirst &emsp;<b>chageAuthor</b> xuq<br/>
	 *              &emsp;<b>chageDate</b> 2015年1月21日<br/>
	 *              &emsp;<b>changeDescription</b> 增加了对于一个字段可以共享多列的功能<br/>
	 */
	private <T> Map<String, Object> rowListToMap(Map<Integer, ExcelColumn> excelHeadMap, Map<String, Map<Object, Object>> excelHeadConvertMap, Map<String, Map<Object, Object>> excelHeadReConvertMap, List<T> list) throws CellTypeValidationException {
		Integer extraNums = 0;
		Integer lastEndIndex = 0;
		Integer globalShareExcelColumnIndex = 0;
		ExcelColumn globalShareExcelColumn = null;
		Map<String, Object> rowMap = new HashMap<String, Object>();
		for (int i = 0; i < list.size(); i++) {
			ExcelColumn excelColumn = excelHeadMap.get(i - extraNums);
			String fieldName = excelColumn != null ? excelColumn.getFieldName() : null;
			Class<?> fieldType = excelColumn != null ? excelColumn.getClazz() : null;

			// 清空上一次的组
			if (globalShareExcelColumn != null && i > lastEndIndex) {
				globalShareExcelColumnIndex = 0;
				globalShareExcelColumn = null;
			}

			// 存在所定义的列
			try {
				Object value = list.get(i);
				Object originalValue = value;
				if (globalShareExcelColumn == null && fieldName != null) {
					if (originalValue != null && excelHeadConvertMap != null && excelHeadConvertMap.get(fieldName) != null) {
						value = excelHeadConvertMap.get(fieldName).get(originalValue);
					}
					if (value == null && originalValue != null && excelHeadReConvertMap != null && excelHeadReConvertMap.get(fieldName) != null) {
						value = excelHeadReConvertMap.get(fieldName).get(originalValue);
					}
					if (StringUtils.isNotBlank(excelColumn.getShareColumnTarget())) {
						globalShareExcelColumn = excelHeadMap.get(Integer.valueOf(excelColumn.getShareColumnTarget()));
						globalShareExcelColumn.setShareColumnCount(new BigDecimal(value.toString()).intValue());
						lastEndIndex = i + globalShareExcelColumn.getShareColumnCount();
						extraNums = extraNums + globalShareExcelColumn.getShareColumnCount() - 1;
					}
					
					// validate
					validate(excelColumn, value, fieldType);
					
					if (globalShareExcelColumn != null && i > lastEndIndex) {
						rowMap.put(globalShareExcelColumn.getFieldName() + "[" + globalShareExcelColumnIndex + "]", value);
						globalShareExcelColumnIndex++;
					} else {
						rowMap.put(fieldName, value);
					}
				} else if (globalShareExcelColumn != null) {
					// validate
					validate(globalShareExcelColumn, value, globalShareExcelColumn.getClazz());
					
					if (globalShareExcelColumn != null) {
						rowMap.put(globalShareExcelColumn.getFieldName() + "[" + globalShareExcelColumnIndex + "]", value);
						globalShareExcelColumnIndex++;
					}
				}
			} catch (CellTypeValidationException e) {
				e.setColIndex(i + 1);	// 设置错误的第几列（从第一行计算开始）
				throw e;
			}
		}
		return rowMap;
	}
	
	/**
	 * 验证Cell数据格式的正确性
	 * 
	 * @param excelColumn
	 * @param val
	 * @param clazz
	 * @throws CellTypeValidationException
	 */
	private void validate(ExcelColumn excelColumn, Object val, Class<?> clazz) throws CellTypeValidationException {
		if (val == null) {
			return;
		}
		if (clazz.equals(String.class)) {
			if (val != null && clazz.equals(val.getClass())) {
				return;
			}
			throw new CellTypeValidationException("请设置以文本形式存储", excelColumn);
		} else if (clazz.equals(Double.class)) {
			if (val != null && clazz.equals(val.getClass())) {
				return;
			}
			throw new CellTypeValidationException("请输入小数", excelColumn);
		} else if (clazz.equals(Date.class)) {
			if (val != null && clazz.equals(val.getClass())) {
				return;
			}
			throw new CellTypeValidationException("请输入日期值", excelColumn);
		} else if (clazz.equals(Integer.class)) {
			if (val != null && Double.class.equals(val.getClass()) && ((Double) val).intValue() == ((Double) val)) {
				return;
			}
			throw new CellTypeValidationException("请输入整数", excelColumn);
		}
		throw new CellTypeValidationException("列类型不支持出错", excelColumn);
	}

}
