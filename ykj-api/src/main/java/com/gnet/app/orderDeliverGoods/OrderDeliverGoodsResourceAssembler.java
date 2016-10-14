package com.gnet.app.orderDeliverGoods;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class OrderDeliverGoodsResourceAssembler implements ResourceAssembler<OrderDeliverGoods, OrderDeliverGoodsResource> {

	@Override
	public OrderDeliverGoodsResource toResource(OrderDeliverGoods entity) {
		OrderDeliverGoodsResource resource = new OrderDeliverGoodsResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(OrderDeliverGoodsController.class).getOrderDeliverGoods(entity.getId())).withSelfRel());
		return resource;
	}
	
}