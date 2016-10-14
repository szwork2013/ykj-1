package com.gnet.app.storageGoodStatus;

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
@Table(name = "ykj_storage_goods_status")
public class StorageGoodStatus extends BaseEntity {

	private static final long serialVersionUID = 5110909209855011463L;

	/** 创建日期 **/
	private Date createDate;
	
	/** 修改日期 **/
	private Date modifyDate;
	
	/** 当前库存量 **/
	private Long storeNow;
	
}
