package ${packageDir}.${toCamelName(resourceName)};

import com.gnet.utils.sort.OrderType;

public enum ${toCamelName(resourceName)?cap_first}OrderType implements OrderType{

	<#if sortMap??>
	<#list sortMap.entrySet() as entry>
	${entry.key?upper_case}("${entry.key}", "${entry.value}")<#if (entry_index + 1 < sortMap.entrySet()?size) >,<#else>;</#if>
	</#list>
	</#if>
	
	private String key;
	private String value;

	private ${toCamelName(resourceName)?cap_first}OrderType(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
}