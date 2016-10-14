package com.gnet.app.clerkWeekly;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ykj_clerk_weekly")
public class ClerkWeekly extends BaseEntity {

	private static final long serialVersionUID = -7571775426468402034L;

	/** 创建日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date createDate;
	
	/** 修改日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss") Date modifyDate;
	
	/** 星期  **/
	private String week;
	
	/** 日期 **/
	private @JsonFormat(pattern="yyyy-MM-dd") Date day;
	
	/** 工作计划 **/
	private String plan;
	
	/** 实际工作内容 **/
	private String realWork;
	
	/** 是否已经完成 **/
	private Boolean isFinish;
	
	/** 公司员工编号 **/
	private String clerkId;

}
