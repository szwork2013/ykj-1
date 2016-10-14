package com.gnet.app.customerTags;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ykj_customer_tags")
public class CustomerTags extends BaseEntity{

	private static final long serialVersionUID = -4584865205360689505L;
	
	/** 创建时间  **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date createDate;
	
	/** 更新时间 **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date modifyDate;
	
	/** 客户编号 **/
	private String customerId;
	
	/** 标签编号 **/
	private String tagId;
	
	/** 标签重要度 **/
	private Integer level;
}
