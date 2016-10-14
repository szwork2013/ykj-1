package com.gnet.app.tags;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ykj_tags")
public class Tags extends BaseEntity{
	
	private static final long serialVersionUID = -4584865205360689505L;
	
	/** 创建时间  **/
	private @JsonIgnore Date createDate;
	
	/** 更新时间 **/
	private @JsonIgnore Date modifyDate;
	
	/** 标签名称  **/
	private String name;
	
	/** 重要度 **/
	private Integer level;
	
	/** 创建人编号 **/
	private String creatorId;
	
	/** 客户标签关联表编号 **/
	private @Transient String customerTagsId;
}
