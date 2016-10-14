package com.gnet.app.orderService;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class OrderServiceResource extends Resource<OrderSer> {

	public OrderServiceResource(OrderSer content, Iterable<Link> links) {
		super(content, links);
	}

	public OrderServiceResource(OrderSer content, Link... links) {
		super(content, links);
	}
	
}