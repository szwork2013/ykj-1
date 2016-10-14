package com.gnet.tools.plugin.poi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

	public static final String CELL_TYPE_NUMERIC = "0";
	public static final String CELL_TYPE_STRING = "1";
	public static final String CELL_TYPE_FORMULA = "2";
	public static final String CELL_TYPE_BLANK = "3";
	public static final String CELL_TYPE_BOOLEAN = "4";
	public static final String CELL_TYPE_ERROR = "5";

	/**
	 * 字段索引
	 */
	public String index();

	/**
	 * 字段名
	 */
	public String fieldName();

	/**
	 * 字段标题
	 */
	public String title();

	/**
	 * 共享的列数(暂不支持)
	 */
	@Deprecated
	public String shareColumnCount() default "1";

	/**
	 * 共享的目标（切记：共享的目标的fieldName不得与其他列名的开头一致，否则可能导致导入失败；即：otherFieldName.
	 * startWith(shareTargetFieldName) == false。）
	 */
	public String shareColumnTarget() default "";

	/**
	 * 字段类型
	 */
	public String type() default ExcelField.CELL_TYPE_STRING;

	/**
	 * 数据字典
	 */
	public String[] convert() default {};

}
