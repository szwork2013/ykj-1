package ${packageDir}.${toCamelName(resourceName)};

import com.gnet.resource.errorBuilder.BaseErrorBuilder;

public class ${toCamelName(resourceName)?cap_first}ErrorBuilder extends BaseErrorBuilder {
	
	<#if errorCodes??>
	<#if errorCodes.get(0)?? >
	/**
	 * ${resourceChineseName}编号为空
	 */
	static final Integer ERROR_ID_NULL = ${errorCodes.get(0)};
	</#if>
	
	<#if errorCodes.get(1)?? >
	/**
	 * 编号无法找到对应的${resourceChineseName}信息
	 */
	static final Integer ERROR_${toCamelName(resourceName)?upper_case}_NULL = ${errorCodes.get(1)};
	</#if>
	
	<#if errorCodes.get(2)?? >
	/**
	 * 新增错误
	 */
	static final Integer ERROR_CREATED_ERROR = ${errorCodes.get(2)};
	</#if>
	
	<#if errorCodes.get(3)?? >
	/**
	 * 更新错误
	 */
	static final Integer ERROR_UPDATED_ERROR = ${errorCodes.get(3)};
	</#if>
	
	<#if errorCodes.get(4)?? >
	/**
	 * 删除错误
	 */
	static final Integer ERROR_DELETED_ERROR = ${errorCodes.get(4)};
	</#if>
	<#if errorCodes.get(5)?? >
	/**
	 * 排序字段不符合要求
	 */
	static final Integer ERROR_SORT_PROPERTY_NOTFOUND = ${errorCodes.get(5)};
	</#if>
	<#if errorCodes.get(6)?? >
	/**
	 * 排序方向不符合要求
	 */
	static final Integer ERROR_SORT_DIRECTION_NOTFOUND = ${errorCodes.get(6)};
	</#if>
	</#if>
	
	<#if customErrorCodes??>
	<#list customErrorCodes.entrySet() as entry>
	/**
	 * ${entry.value[1]}
	 */
	static final Integer ERROR_${toCamelName(entry.key)?upper_case}_NULL = ${entry.value[0]};
	
	</#list>
	</#if>
	
	<#if lengthErrorCodes??>
	<#list lengthErrorCodes.entrySet() as entry>
	static final Integer ERROR_${toCamelName(entry.key)?upper_case}_TOOLONG = ${entry.value};
	
	</#list>
	</#if>
	
	
	public ${toCamelName(resourceName)?cap_first}ErrorBuilder(Integer code, String msg) {
		super(code, msg);
	}

}
