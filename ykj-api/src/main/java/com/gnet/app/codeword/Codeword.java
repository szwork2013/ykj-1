package com.gnet.app.codeword;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tk.mybatis.mapper.entity.IDynamicTableName;

@Getter
@Setter
@ToString
@Entity
public class Codeword extends BaseEntity implements IDynamicTableName {

	private static final long serialVersionUID = 1957495573882270031L;
	
	public static final String NO_PARENT_ID = "0";
	
	private String value;
	private String code;
	private String codewordTypeId;
	private String superid;
	private String codeLevel;
	private String remark;
	private String codeFlag;
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date updateTime;
	private Boolean isSystem;
	
	private @JsonIgnore @Transient String tableName;
	
	@Override
	public String getDynamicTableName() {
		return this.tableName;
	}
	
	public void setDynamicTableName(String tableName) {
		this.tableName = tableName;
	}
	
}
