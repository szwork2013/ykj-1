package com.gnet.app.orderServiceAttachment;

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
@Table(name = "ykj_order_service_attachment")
public class OrderServiceAttachment extends BaseEntity {

	private static final long serialVersionUID = -4560069060184998401L;

	/** 创建日期 **/
	private Date createDate;
	
	/** 修改日期 **/
	private Date modifyDate;
	
	/** 附件路径 **/
	private String attachmentRoot;
	
	/** 附件大小 **/
	private String attachmentSize;
	
	/** 附件名称 **/
	private String attachmentFilename;
	
	/** 附件尾缀 **/
	private String attachmentSuffix;
	
	/** 上传日期 **/
	private Date uploadDate;
	
	/** 上传者 **/
	private String uploadPersonId;
	
	/** 备注 **/
	private String remark;
	
}
