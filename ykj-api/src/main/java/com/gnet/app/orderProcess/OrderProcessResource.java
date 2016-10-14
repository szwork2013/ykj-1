package com.gnet.app.orderProcess;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class OrderProcessResource extends Resource<OrderProcess> {

	public OrderProcessResource(OrderProcess content, Iterable<Link> links) {
		super(content, links);
	}

	public OrderProcessResource(OrderProcess content, Link... links) {
		super(content, links);
	}
	
}