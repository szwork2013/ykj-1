package ${packageDir}.${toCamelName(resourceName)};

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "${tableName}")
public class ${toCamelName(resourceName)?cap_first} extends BaseEntity {

	<#list columns as column>
	<#if "${column.COLUMN_NAME}" == "id">
	<#else>
	<#if "${column.COLUMN_NAME}" == "operate_delete_user_id" || "${column.COLUMN_NAME}" == "operate_delete_time" || "${column.COLUMN_NAME}" == "operate_delete_status" >
	/** ${column.COLUMN_COMMENT} **/
	private @JsonIgnore ${sqlTypeToJavaType.get(column.DATA_TYPE)} ${toCamelName(column.COLUMN_NAME)};
	
	<#else>
	/** ${column.COLUMN_COMMENT} **/
	private ${sqlTypeToJavaType.get(column.DATA_TYPE)} ${toCamelName(column.COLUMN_NAME)};
	
	</#if>
	</#if>
	</#list>
}
