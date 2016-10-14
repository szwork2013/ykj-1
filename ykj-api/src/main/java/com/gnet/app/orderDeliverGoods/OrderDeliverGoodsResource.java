package com.gnet.app.orderDeliverGoods;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class OrderDeliverGoodsResource extends Resource<OrderDeliverGoods> {

	public OrderDeliverGoodsResource(OrderDeliverGoods content, Iterable<Link> links) {
		super(content, links);
	}

	public OrderDeliverGoodsResource(OrderDeliverGoods content, Link... links) {
		super(content, links);
	}
	
}