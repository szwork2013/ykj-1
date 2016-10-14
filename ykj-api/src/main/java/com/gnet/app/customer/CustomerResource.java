package com.gnet.app.customer;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class CustomerResource extends Resource<Customer>{

	public CustomerResource(Customer content, Iterable<Link> links) {
		super(content, links);
	}

	public CustomerResource(Customer content, Link... links) {
		super(content, links);
	}
}
