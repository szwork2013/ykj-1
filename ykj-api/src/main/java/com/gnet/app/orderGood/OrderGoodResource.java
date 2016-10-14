package com.gnet.app.orderGood;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class OrderGoodResource extends Resource<OrderGood> {

	public OrderGoodResource(OrderGood content, Iterable<Link> links) {
		super(content, links);
	}

	public OrderGoodResource(OrderGood content, Link... links) {
		super(content, links);
	}
	
}