package ${packageDir}.${toCamelName(resourceName)};

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gnet.utils.spring.SpringContextHolder;

public class ${toCamelName(resourceName)?cap_first}Validator {
	
	private ${toCamelName(resourceName)?cap_first}Validator(){}
	
	static Map<String, Object> validateBeforeCreate${toCamelName(resourceName)?cap_first}(${toCamelName(resourceName)?cap_first} ${toCamelName(resourceName)}) {
		Map<String, Object> map = new HashMap<>();
		
		<#if validateColumns??>
		<#list validateColumns.entrySet() as column>
		// ${column.value[1]}不能为空过滤
		<#if "${column.value[2]}" == "true">
		if (StringUtils.isBlank(${toCamelName(resourceName)}.get${column.key?cap_first}())) {
		<#else>
		if (${toCamelName(resourceName)}.get${column.key?cap_first}() == null) {
		</#if>
			map.put("code", ${toCamelName(resourceName)?cap_first}ErrorBuilder.${column.value[0]});
			map.put("msg", "${column.value[1]}不能为空");
			return map;
		}

		</#list>
		</#if>
		return null;
	}
	
	static Map<String, Object> validateBeforeUpdate${toCamelName(resourceName)?cap_first}(${toCamelName(resourceName)?cap_first} ${toCamelName(resourceName)}) {
		Map<String, Object> map = new HashMap<>();
		
		if (${toCamelName(resourceName)}.getId() == null) {
			map.put("code", ${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_ID_NULL);
			map.put("msg", "${resourceChineseName}编号为空");
			return map;
		}
		
		<#if validateColumns??>
		<#list validateColumns.entrySet() as column>
		// ${column.value[1]}不能为空过滤
		<#if "${column.value[2]}" == "true">
		if (StringUtils.isBlank(${toCamelName(resourceName)}.get${toCamelName(column.key)?cap_first}())) {
		<#else>
		if (${toCamelName(resourceName)}.get${toCamelName(column.key)?cap_first}() == null) {
		</#if>
			map.put("code", ${toCamelName(resourceName)?cap_first}ErrorBuilder.${column.value[0]});
			map.put("msg", "${column.value[1]}不能为空");
			return map;
		}

		</#list>
		</#if>
		return null;
	}
	
	static Map<String, Object> validateLength(${toCamelName(resourceName)?cap_first} ${toCamelName(resourceName)}) {
		Map<String, Object> map = new HashMap<>();
		<#if columnMap??>
		<#list columnMap.entrySet() as entry>
		<#if entry.value[1] ??>
		<#if entry.value[0] == "String">
		if (StringUtils.isNotBlank(${toCamelName(resourceName)}.get${toCamelName(entry.key)?cap_first}()) && ${toCamelName(resourceName)}.get${toCamelName(entry.key)?cap_first}().length() > ${entry.value[1]} ) {
			map.put("code", ${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_${toCamelName(entry.key)?upper_case}_TOOLONG);
			map.put("msg", "长度不能超过${entry.value[1]}");
			return map;
		}
		
		<#else>
		if (${toCamelName(resourceName)}.get${toCamelName(entry.key)?cap_first}() != null && ${toCamelName(resourceName)}.get${toCamelName(entry.key)?cap_first}().length() > ${entry.value[1]} ) {
			map.put("code", ${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_${toCamelName(entry.key)?upper_case}_TOOLONG);
			map.put("msg", "长度不能超过${entry.value[1]}");
			return map;
		}
		
		</#if>
		</#if>
		</#list>
		</#if>
		return null;
	}
	
	// TODO
	static Map<String, Object> validateBeforeDelete${toCamelName(resourceName)?cap_first}(String id) {
		${toCamelName(resourceName)?cap_first}Service ${toCamelName(resourceName)}Service = SpringContextHolder.getBean(${toCamelName(resourceName)?cap_first}Service.class);
		Map<String, Object> map = new HashMap<>();
		
		return null;
	}
	
	// TODO
	static Map<String, Object> validateBeforeDelete${resourceNameComplex?cap_first}(String[] ids) {
		${toCamelName(resourceName)?cap_first}Service ${toCamelName(resourceName)}Service = SpringContextHolder.getBean(${toCamelName(resourceName)?cap_first}Service.class);
		Map<String, Object> map = new HashMap<>();
		
		return null;
	}
	
	
}