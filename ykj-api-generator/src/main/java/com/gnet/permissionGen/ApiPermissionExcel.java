package com.gnet.permissionGen;

import com.gnet.tools.plugin.poi.ExcelField;
import com.gnet.tools.plugin.poi.ExcelModel;

/**
 * 教学班导入学生模板
 * 
 * @author wct
 * @Date 2016年7月6日
 */
@ExcelModel(name = "apiPermissionExcel", rowCount = "1")
public class ApiPermissionExcel {
	
	@ExcelField(fieldName = "code", index = "0", title = "权限code", type = ExcelField.CELL_TYPE_STRING)
	String code;
	
	@ExcelField(fieldName = "name", index = "1", title = "功能名称", type = ExcelField.CELL_TYPE_STRING)
	String name;
	
	@ExcelField(fieldName = "group", index = "2", title = "功能分组", type = ExcelField.CELL_TYPE_STRING)
	String group;
	
}