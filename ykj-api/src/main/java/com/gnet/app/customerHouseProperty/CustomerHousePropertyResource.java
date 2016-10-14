package com.gnet.app.customerHouseProperty;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;


public class CustomerHousePropertyResource extends Resource<CustomerHouseProperty>{
	
	public CustomerHousePropertyResource(CustomerHouseProperty content, Iterable<Link> links) {
		super(content, links);
	}

	public CustomerHousePropertyResource(CustomerHouseProperty content, Link... links) {
		super(content, links);
	}

}
