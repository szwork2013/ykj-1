package com.gnet.app.orderInstallGoods;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class OrderInstallGoodsResource extends Resource<OrderInstallGoods> {

	public OrderInstallGoodsResource(OrderInstallGoods content, Iterable<Link> links) {
		super(content, links);
	}

	public OrderInstallGoodsResource(OrderInstallGoods content, Link... links) {
		super(content, links);
	}
	
}