package com.gnet.app.clerk;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ykj_clerk")
public class Clerk extends BaseEntity {

	private static final long serialVersionUID = -7571775426468402034L;
	
	/** 管理员 **/
	public static final Integer ROLE_TYPE_ADMIN = 0;
	
	/** 总经理 **/
	public static final Integer ROLE_TYPE_MANAGER = 1;
	
	/** 店长 **/
	public static final Integer ROLE_TYPE_STORE_MANAGER = 2;
	
	/** 店员 **/
	public static final Integer ROLE_TYPE_CLERK = 3;
	
	/** 后勤 **/
    public static final Integer ROLE_TYPE_LOGISTIC = 4;
    
    /** 仓库管理员 **/
    public static final Integer ROLE_TYPE_WAREHOUSE = 5;
    
    /** 售后  **/
    public static final Integer ROLE_TYPE_AFTER_SALES = 6;
	

	/** 创建日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date createDate;
	
	/** 修改日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date modifyDate;
	
	/** 直属部门编号 **/
	private String officeId;
	
	/** 角色身份,0:管理员,1:总经理,2:店长,3:店员,4:售后,5:仓库管理人,6:采购员 **/
	private Integer roleType;
	
	/** 姓名 **/
	private String name;
	
	/** 性别 **/
	private Integer sex;
	
	/** 出生日期 **/
	private @JsonFormat(pattern = "yyyy-MM-dd") Date birthday;
	
	/** 手机号码 **/
	private String phone;
	
	/** 备用手机号码 **/
	private String phoneSec;
	
	/** QQ **/
	private @Column(name = "QQ") String QQ;
	
	/** 微信号 **/
	private String wechat;
	
	/** 邮箱 **/
	private String email;
	
	/** 头像路径 **/
	private String photo;
	
	/** 是否已经删除 **/
	private @JsonIgnore Boolean isDel;
	
	/** 商家编号 **/
	private String businessId;
	
	/** 门店编号 **/
	private String storeId;
	
	/** SysUser表外置参数 用户名 **/
	private @Transient String username;
	
}