package com.gnet.tools.plugin.poi;

/**
 * excel列信息
 * 
 * @description excel列信息 记录了索引、字段名称、字段显示名称、字段类型、字段分享列数、字段分享目标信息
 * @createTime: 2012-4-19 下午12:03:49
 * @author: <a href="mailto:hubo@feinno.com">hubo</a>
 * @version: 0.1
 * @lastVersion: 0.1
 * @updateTime:
 * @updateAuthor: <a href="mailto:hubo@feinno.com">hubo</a>
 * @changesSum:
 * 
 */
public class ExcelColumn {
	/**
	 * 索引
	 */
	private int index;

	/**
	 * 字段名称
	 */
	private String fieldName;

	/**
	 * 字段显示名称
	 */
	private String fieldDispName;

	/**
	 * 字段类型
	 */
	private int type;

	/**
	 * 字段类型
	 */
	private Class<?> clazz;
	
	/**
	 * 字段分享的列数
	 */
	private int shareColumnCount;

	/**
	 * 字段分享的目标
	 */
	private String shareColumnTarget;

	public ExcelColumn() {

	}

	public ExcelColumn(int index, String fieldName, String fieldDispName) {
		super();
		this.index = index;
		this.fieldName = fieldName;
		this.fieldDispName = fieldDispName;
	}

	public ExcelColumn(int index, String fieldName, String fieldDispName, Class<?> clazz) {
		super();
		this.index = index;
		this.fieldName = fieldName;
		this.fieldDispName = fieldDispName;
		this.clazz = clazz;
	}

	public ExcelColumn(int index, String fieldName, String fieldDispName, Class<?> clazz, int shareColumnCount) {
		super();
		this.index = index;
		this.fieldName = fieldName;
		this.fieldDispName = fieldDispName;
		this.clazz = clazz;
		this.shareColumnCount = shareColumnCount;
	}

	public int getIndex() {
		return index;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldDispName() {
		return fieldDispName;
	}

	public void setFieldDispName(String fieldDispName) {
		this.fieldDispName = fieldDispName;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getShareColumnCount() {
		return shareColumnCount;
	}

	public void setShareColumnCount(int shareColumnCount) {
		this.shareColumnCount = shareColumnCount;
	}

	public String getShareColumnTarget() {
		return shareColumnTarget;
	}

	public void setShareColumnTarget(String shareColumnTarget) {
		this.shareColumnTarget = shareColumnTarget;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public String toString() {
		return "ExcelColumn [fieldDispName=" + fieldDispName + ", fieldName=" + fieldName + ", index=" + index + ", class=" + clazz + ",shareColumnCount=" + shareColumnCount + ",shareColumnTarget=" + shareColumnTarget + "]";
	}
}
