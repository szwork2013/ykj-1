package com.gnet.app.order;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class OrderResource extends Resource<Order> {

	public OrderResource(Order content, Iterable<Link> links) {
		super(content, links);
	}

	public OrderResource(Order content, Link... links) {
		super(content, links);
	}
	
}