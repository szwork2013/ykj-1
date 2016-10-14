package com.gnet.exception;

import com.gnet.tools.plugin.poi.ExcelColumn;
import com.gnet.tools.plugin.poi.ExcelHead;

/**
 * 单元格类型验证异常
 * 
 * @author xuq
 * @date 2016年1月22日
 * @version 1.0
 */
public class CellTypeValidationException extends Exception {

	private static final long serialVersionUID = 9065094923499393789L;
	
	private ExcelHead excelHead;
	private Integer rowIndex;
	private ExcelColumn excelColumn;
	private Integer colIndex;
	
	public CellTypeValidationException() {
	}
	
	public CellTypeValidationException(String msg) {
		super(msg);
	}
	
	public CellTypeValidationException(String msg, ExcelColumn excelColumn) {
		super(msg);
		this.excelColumn = excelColumn;
	}
	
	public CellTypeValidationException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public CellTypeValidationException(String msg, Throwable t, ExcelColumn excelColumn) {
		super(msg, t);
		this.excelColumn = excelColumn;
	}

	public ExcelColumn getExcelColumn() {
		return excelColumn;
	}

	public void setExcelHead(ExcelHead excelHead) {
		this.excelHead = excelHead;
	}
	
	public ExcelHead getExcelHead() {
		return excelHead;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public Integer getColIndex() {
		return colIndex;
	}

	public void setColIndex(Integer colIndex) {
		this.colIndex = colIndex;
	}

}
