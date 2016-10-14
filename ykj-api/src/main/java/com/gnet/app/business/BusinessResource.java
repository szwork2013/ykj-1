package com.gnet.app.business;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class BusinessResource extends Resource<Business> {

	public BusinessResource(Business content, Iterable<Link> links) {
		super(content, links);
	}

	public BusinessResource(Business content, Link... links) {
		super(content, links);
	}
	
}