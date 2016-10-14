package com.gnet.tools.plugin.poi;

import java.util.List;
import java.util.Map;

/**
 * Excel头信息
 * 
 * @description Excel头信息
 *              记录了列信息、需要转换的列、需要转换的列(数据反转)、头部所占用的行数、头部所占用的列数、目标Excel的Sheet索引（
 *              从左往右从0开始）
 * @createTime: 2012-4-18 下午01:17:53
 * @author: <a href="mailto:hubo@feinno.com">hubo</a>
 * @version: 0.1
 * @lastVersion: 0.1
 * @updateTime:
 * @updateAuthor: <a href="mailto:hubo@feinno.com">hubo</a>
 * @changesSum:
 * 
 */
public class ExcelHead {
	/**
	 * 列信息
	 */
	private List<ExcelColumn> columns;

	/**
	 * 需要转换的列
	 */
	private Map<String, Map<Object, Object>> columnsConvertMap;

	/**
	 * 需要转换的列(数据反转)
	 */
	private Map<String, Map<Object, Object>> reColumnsConvertMap;

	/**
	 * 头部所占用的行数
	 */
	private int rowCount;

	/**
	 * 头部所占用的列数
	 */
	private int columnCount;

	/**
	 * 目标Excel的Sheet索引（从左往右从0开始）
	 */
	private int targetSheetIndex;

	public List<ExcelColumn> getColumns() {
		return columns;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumns(List<ExcelColumn> columns) {
		this.columns = columns;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public Map<String, Map<Object, Object>> getColumnsConvertMap() {
		return columnsConvertMap;
	}

	public void setColumnsConvertMap(Map<String, Map<Object, Object>> columnsConvertMap) {
		this.columnsConvertMap = columnsConvertMap;
	}

	public Map<String, Map<Object, Object>> getReColumnsConvertMap() {
		return reColumnsConvertMap;
	}

	public void setReColumnsConvertMap(Map<String, Map<Object, Object>> reColumnsConvertMap) {
		this.reColumnsConvertMap = reColumnsConvertMap;
	}

	public int getTargetSheetIndex() {
		return targetSheetIndex;
	}

	public void setTargetSheetIndex(int targetSheetIndex) {
		this.targetSheetIndex = targetSheetIndex;
	}

	@Override
	public String toString() {
		return "ExcelHead [columnCount=" + columnCount + ", columns=" + columns + ", columnsConvertMap=" + columnsConvertMap + ", reColumnsConvertMap=" + reColumnsConvertMap + ", rowCount=" + rowCount + ", targetSheetIndex=" + targetSheetIndex + "]";
	}

}
