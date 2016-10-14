package com.gnet.app.noticeMsg;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "ykj_notice_msg")
public class NoticeMsg extends BaseEntity {

	private static final long serialVersionUID = 7794669145985862815L;

	/** 创建日期 **/
	private Date createDate;
	
	/** 修改日期 **/
	private Date modifyDate;
	
	/** 消息内容 **/
	private String content;
	
	/** 提醒类型,0:跟进提醒,1:服务提醒,2:缺货提醒,3:预留库存到期提醒 **/
	private Integer noticeType;
	
	/** 提醒方式,0:移动APP,1:短信,2:站内信 **/
	private Integer noticeMethod;
	
	/** 提醒日期 **/
	private Date noticeDate;
	
	/** 来源编号 **/
	private String fromId;
	
	/** 是否删除 **/
	private @JsonIgnore Boolean isDel;
	
}
