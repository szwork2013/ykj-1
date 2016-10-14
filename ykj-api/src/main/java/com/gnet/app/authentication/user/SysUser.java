package com.gnet.app.authentication.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(exclude = "password")
@NoArgsConstructor
@Entity
@Table(name = "sys_user")
public class SysUser extends BaseEntity {

	private static final long serialVersionUID = 8018030850820191441L;

	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	

	/**
	 * 0 类型：管理员
	 */
	public static final Integer TYPE_ADMIN = 0;
	
	/**
	 * 1 类型: 公司职工
	 */
	public static final Integer TYPE_CLERK = 1;
	
	/**
	 * 1 类型
	 */
	
	/**
	 * 0 禁用
	 */
	public static final Integer STATUS_DISABLED = 0;
	
	/**
	 * 1 启用
	 */
	public static final Integer STATUS_ENABLED = 1;

	private String name;
	private String username;
	private @JsonIgnore String password;
	private String nickname;
	private @JsonIgnore Date createDate;
	private @JsonIgnore Date modifyDate;
	private String remark;
	private Integer roleType;
	private Date lastLoginTime;
	private @JsonIgnore Integer status;
	private @JsonIgnore Boolean operateDeleteStatus;
	private @JsonIgnore String operatorDeleteUserId;
	private @JsonIgnore Date operateDeleteTime;
	
	public SysUser(String username) {
		super();
		this.username = username;
	}
	

}
