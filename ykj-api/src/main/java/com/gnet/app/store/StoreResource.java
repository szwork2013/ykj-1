package com.gnet.app.store;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class StoreResource extends Resource<Store> {

	public StoreResource(Store content, Iterable<Link> links) {
		super(content, links);
	}

	public StoreResource(Store content, Link... links) {
		super(content, links);
	}
	
}