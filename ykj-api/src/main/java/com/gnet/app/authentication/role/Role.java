package com.gnet.app.authentication.role;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gnet.mybatis.BaseEntity;
import com.gnet.mybatis.typeHanler.StringArrayTypeHandler;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import tk.mybatis.mapper.annotation.ColumnType;

@Setter
@Getter
@ToString
@Entity
@Table(name = "sys_role")
public class Role extends BaseEntity {
	
	private static final long serialVersionUID = -3983397928572487777L;

	/**
	 * 角色类型：超级管理员
	 */
	public static Integer ROLE_TYPE_ADMIN = 0;

	

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) String id;
	private String name;
	private @ColumnType(typeHandler = StringArrayTypeHandler.class) String[] permissions;
	private Boolean isSystem;
	private String remark;
	private Date createDate;
	private Date modifyDate;
	private String operatorDeleteUserId;
	private Date operateDeleteTime;
	private Integer operateDeleteStatus;
	
	public Role(String name, String... permissions) {
		this.name = name;
		this.permissions = permissions;
	}
	
}
