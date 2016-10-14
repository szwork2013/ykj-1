package com.gnet.app.hello;

import javax.persistence.Entity;

import com.gnet.mybatis.BaseEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Hello extends BaseEntity {

	private static final long serialVersionUID = 3295029663972540833L;
	
	private String name;
	
}
