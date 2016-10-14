package com.gnet.app.businessCodeword;

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
@Table(name = "ykj_business_codeword")
public class BusinessCodeword extends BaseEntity {

	private static final long serialVersionUID = 2935602845858782302L;

	private Date createDate;
	
	private Date modifyDate;
	
	private String codewordId;
	
	private String businessId;
}
