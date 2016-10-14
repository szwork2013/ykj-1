package com.gnet.app.supplier;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class SupplierResource extends Resource<Supplier> {

	public SupplierResource(Supplier content, Iterable<Link> links) {
		super(content, links);
	}

	public SupplierResource(Supplier content, Link... links) {
		super(content, links);
	}
	
}