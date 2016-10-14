package ${packageDir}.${toCamelName(resourceName)};

import tk.mybatis.mapper.common.Mapper;
<#if isSoftDel == "true">
import java.util.Date;

import org.apache.ibatis.annotations.Update;
<#else>
import org.apache.ibatis.annotations.Delete;
</#if>
import org.apache.ibatis.annotations.Select;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ${toCamelName(resourceName)?cap_first}Mapper extends Mapper<${toCamelName(resourceName)?cap_first}> {
	
	<#if "${isUseXmlMapper}" == "false"> 
	<#if "${isSoftDel}" == "true" >
	@Update(
		  "update ${tableName} set ${softDelColumn} = 1 , modify_date = ${r'#{date}'}, operate_delete_time = ${r'#{date}'}, operator_delete_user_id = ${r'#{userId}'} " + 
		  "where ${softDelColumn} = 0 and id = ${r'#{id}'}"
	)
	public int deleteById(@Param("id") String id, @Param("userId") String userId, @Param("date") Date date);
	
	@Update(
		 "<script> " +
			 "update ${tableName} set ${softDelColumn} = 1 , modify_date = ${r'#{date}'}, operate_delete_time = ${r'#{date}'}, operator_delete_user_id = ${r'#{userId}'} " +
			 "where ${softDelColumn} = 0 " +
			 "and id in " +
		 "<foreach collection=\"ids\" index=\"index\" item=\"id\" open=\" (\" separator=\" ,\" close=\")\">  " +
		 	  "${r'#{id}'}" +
		 "</foreach> " +
		 "</script> "
	)
	public int deleteByIds(@Param("ids") String ids[], @Param("userId") String userId, @Param("date") Date date);
	<#else>
	@Delete(
		 "<script> " +
			 "delete from ${tableName} " +
			 "where id in " +
		 "<foreach collection=\"ids\" index=\"index\" item=\"id\" open=\" (\" separator=\" ,\" close=\")\">  " +
		 	  "${r'#{id}'}" +
		 "</foreach> " +
		 "</script> "
	)
	public int deleteByIds(@Param("ids") String ids[]);
	</#if>
	
	<#if (searchMap.entrySet()?size > 0) >
	@Select(
		"<script>" +
			"select <#list columnMap.entrySet() as entry>${entry.key} as ${toCamelName(entry.key)}<#if entry_index + 1 < columnMap.entrySet()?size >, </#if></#list> from ${tableName} " +
		<#if "${isSoftDel}" == "true" >
			"where operate_delete_status = ${r'#{delFlag}'} " + 
		<#list searchList as item>
		"<if test=\" ${toCamelName(item)} != null \">" +
			"and ${item} = ${r'#{'}${toCamelName(item)}${r'}'} " +
		"</if>" +
		</#list>
		<#else>
		"<where>" +
		<#list searchList as item>
			"<if test=\" ${toCamelName(item)} != null \">" +
				"and ${item} = ${r'#{'}${toCamelName(item)}${r'}'} " +
			"</if>" +
		</#list>
		"</where>" +
		</#if>
		"</script>"
	)
	public List<${toCamelName(resourceName)?cap_first}> select${toCamelName(resourceNameComplex)?cap_first}All(<#list searchMap.entrySet() as item>@Param("${toCamelName(item.key)}") ${item.value} ${toCamelName(item.key)}<#if item_index + 1 < searchMap.entrySet()?size >, </#if></#list><#if "${isSoftDel}" == "true" >, @Param("delFlag") Integer delFlag</#if>);
	</#if>
	
	@Select(
		"<script>" +
			"select <#list columnMap.entrySet() as entry>${entry.key} as ${toCamelName(entry.key)}<#if entry_index + 1 < columnMap.entrySet()?size >, </#if></#list> from ${tableName} " +
		<#if "${isSoftDel}" == "true" >
			"where operate_delete_status = ${r'#{delFlag}'} " + 
		<#if (searchMap.entrySet()?size > 0) >
		<#list searchList as item>
		"<if test=\" ${toCamelName(item)} != null \">" +
			"and ${item} = ${r'#{'}${toCamelName(item)}${r'}'} " +
		"</if>" +
		</#list>
		</#if>
		<#else>
		<#if (searchMap.entrySet()?size > 0) >
		"<where>" +
		<#list searchList as item>
			"<if test=\" ${toCamelName(item)} != null \">" +
				"and ${item} = ${r'#{'}${toCamelName(item)}${r'}'} " +
			"</if>" +
		</#list>
		"</where>" +
		</#if>
		</#if>
		"<if test=\" orderList != null \">" +
		"<foreach collection=\"orderList\" index=\"index\" item=\"orderItem\" open=\"order by \" separator=\",\" close=\"\">  " +
			  "${r'#{orderItem}'}" +
		"</foreach> " +
		"</if>" +
		"</script>"
	)
	public List<${toCamelName(resourceName)?cap_first}> select${toCamelName(resourceNameComplex)?cap_first}AllList(@Param("orderList") List<String> orderList, <#if (searchMap.entrySet()?size > 0) ><#list searchMap.entrySet() as item>@Param("${toCamelName(item.key)}") ${item.value} ${toCamelName(item.key)}<#if item_index + 1 < searchMap.entrySet()?size >, </#if></#list></#if><#if "${isSoftDel}" == "true" >, @Param("delFlag") Integer delFlag</#if>);
	<#else>
	<#if "${isSoftDel}" == "true" >
	public int deleteById(@Param("id") String id, @Param("date") Date date);
	
	public int deleteByIds(@Param("ids") String ids[], @Param("date") Date date);
	<#else>
	public int deleteByIds(@Param("ids") String ids[]);
	</#if>
	
	<#if (searchMap.entrySet()?size > 0) >
	public List<${toCamelName(resourceName)?cap_first}> select${toCamelName(resourceNameComplex)?cap_first}All(<#list searchMap.entrySet() as item>@Param("${toCamelName(item.key)}") ${item.value} ${toCamelName(item.key)}<#if item_index + 1 < searchMap.entrySet()?size >, </#if></#list><#if "${isSoftDel}" == "true" >, @Param("delFlag") Integer delFlag</#if>);
	
	</#if>
	public List<${toCamelName(resourceName)?cap_first}> select${toCamelName(resourceNameComplex)?cap_first}AllList(@Param("orderList") List<String> orderList, <#if (searchMap.entrySet()?size > 0) ><#list searchMap.entrySet() as item>@Param("${toCamelName(item.key)}") ${item.value} ${toCamelName(item.key)}<#if item_index + 1 < searchMap.entrySet()?size >, </#if></#list></#if><#if "${isSoftDel}" == "true" >, @Param("delFlag") Integer delFlag</#if>);
	</#if>
}
