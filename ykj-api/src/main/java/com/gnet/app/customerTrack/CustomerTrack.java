package com.gnet.app.customerTrack;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "ykj_customer_track")
public class CustomerTrack extends BaseEntity{
	
	private static final long serialVersionUID = -4584865205360689505L;
	
	/** 跟进方式-电话 **/
	public static Integer TRACK_WAY_PHONE = 0;
	
	/** 跟进方式-微信 **/
	public static Integer TRACK_WAY_WECHAT = 1;
	
	/** 跟进方式-短信 **/
	public static Integer TRACK_WAY_MESSAGE = 2;
	
	/** 跟进方式-面谈 **/
	public static Integer TRACK_WAY_INTERVIEW = 3;
	
	/** 创建时间  **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date createDate;
	
	/** 更新时间 **/
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date modifyDate;
	/**
	 * 客户编号
	 */
	private String customerId;
	
	/**
	 * 客户负责人编号
	 */
	private String customerResponsibleId;
	
	/** 客户负责人姓名 **/
	private @Transient String customerResponsibleName;
	
	/**
	 * 跟进时间
	 */
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date time ;
	
	/**
	 * 跟进方式
	 */
	private Integer way;
	
	/**
	 * 跟进内容
	 */
	private String content;
	
	/**
	 * 下次跟进时间
	 */
	private @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date nextTrackTime;
	
	/**
	 * 附件路径
	 */
	private String attachmentRoot;

}
