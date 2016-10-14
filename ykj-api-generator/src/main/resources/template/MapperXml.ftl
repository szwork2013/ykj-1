<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${packageDir}.${toCamelName(resourceName)}.${toCamelName(resourceName)?cap_first}Mapper" >
    <resultMap id="Base${toCamelName(resourceName)?cap_first}Map" type="${packageDir}.${toCamelName(resourceName)}.${toCamelName(resourceName)?cap_first}" >
        <id column="id" property="id" jdbcType="CHAR" />
		<#list columnMap.entrySet() as entry>
		<#if entry.key != "id">
		<result column="${entry.key}" property="${toCamelName(entry.key)}" <#if jdbcTypeMap.get(entry.value.get(2)) ??>jdbcType="${jdbcTypeMap.get(entry.value.get(2))}"</#if>/>
		</#if>
		</#list>
    </resultMap>
    
	<#if "${isSoftDel}" == "true" >
	<update id="deleteById">
        update ${tableName} set ${softDelColumn} = ${r'${'}@${packageDir}.${toCamelName(resourceName)}.${toCamelName(resourceName)?cap_first}@DEL_TRUE${r'}'}, modify_date = ${r'#{date}'}
        where ${softDelColumn} = ${r'${'}@${packageDir}.${toCamelName(resourceName)}.${toCamelName(resourceName)?cap_first}@DEL_FALSE${r'}'} and id = ${r'#{id}'}
    </update>
	
	<update id="deleteByIds">
        update ${tableName} set ${softDelColumn} = ${r'${'}@${packageDir}.${toCamelName(resourceName)}.${toCamelName(resourceName)?cap_first}@DEL_TRUE${r'}'}, modify_date = ${r'#{date}'}
        where ${softDelColumn} = ${r'${'}@${packageDir}.${toCamelName(resourceName)}.${toCamelName(resourceName)?cap_first}@DEL_FALSE${r'}'} 
		and id in 
		<foreach collection="ids" index="index" item="id" open=" (" separator=" ," close=")">
			${r'#{id}'}
		</foreach>
    </update>
	
	<#else>
	<delete id="deleteByIds">
		delete from ${tableName} 
		where id in 
		<foreach collection="ids" index="index" item="id" open=" (" separator=" ," close=")">
			${r'#{id}'}
		</foreach>
	</delete>
	
	</#if>
	<#if (searchMap.entrySet()?size > 0) >
	<select id="select${toCamelName(resourceNameComplex)?cap_first}All" resultMap="Base${toCamelName(resourceName)?cap_first}Map">
		select * from ${tableName} 
		<#if "${isSoftDel}" == "true" >
		where ${softDelColumn} = ${r'${'}@${packageDir}.${toCamelName(resourceName)}.${toCamelName(resourceName)?cap_first}@DEL_FALSE${r'}'}
		<#list searchList as item>
		<if test=" ${toCamelName(item)} != null ">
		and ${item} = ${r'#{'}${toCamelName(item)}${r'}'}
		</if>
		</#list>
		<#else>
		<where>
		<if test=" ${toCamelName(item)} != null ">
			and ${item} = ${r'#{'}${toCamelName(item)}${r'}'}
		</if>
		</where>
		</#if>
	</select>
	
	</#if>
	<select id="select${toCamelName(resourceNameComplex)?cap_first}AllList" resultMap="Base${toCamelName(resourceName)?cap_first}Map">
		select * from ${tableName} 
		<#if "${isSoftDel}" == "true" >
		where ${softDelColumn} = ${r'${'}@${packageDir}.${toCamelName(resourceName)}.${toCamelName(resourceName)?cap_first}@DEL_FALSE${r'}'}
		<#if (searchMap.entrySet()?size > 0) >
		<#list searchList as item>
		<if test=" ${toCamelName(item)} != null ">
		and ${item} = ${r'#{'}${toCamelName(item)}${r'}'}
		</if>
		</#list>
		</#if>
		<#else>
		<#if (searchMap.entrySet()?size > 0) >
		<where>
			<#list searchList as item>
			<if test=" ${toCamelName(item)} != null ">
				and ${item} = ${r'#{'}${toCamelName(item)}${r'}'}
			</if>
			</#list>
		</where>
		</#if>
		</#if>
		<if test=" orderList != null ">
		<foreach collection="orderList" index="index" item="orderItem" open="order by " separator="," close="">
			  ${r'#{orderItem}'}
		</foreach>
		</if>
	</select>
</mapper>