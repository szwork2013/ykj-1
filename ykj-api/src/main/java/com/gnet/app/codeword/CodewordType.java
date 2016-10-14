package com.gnet.app.codeword;

import javax.persistence.Transient;

import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tk.mybatis.mapper.entity.IDynamicTableName;

@Getter
@Setter
@ToString
public class CodewordType extends BaseEntity implements IDynamicTableName {

	private static final long serialVersionUID = 5322720757403613984L;
	
	private String typeName;
	private String typeValue;
	private Boolean permitCustom;
	
	private @Transient String tableName;
	
	@Override
	public String getDynamicTableName() {
		return this.tableName;
	}
	
	public void setDynamicTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
