package com.gnet.app.orderInstallGoods;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class OrderInstallGoodsResourceAssembler implements ResourceAssembler<OrderInstallGoods, OrderInstallGoodsResource> {

	@Override
	public OrderInstallGoodsResource toResource(OrderInstallGoods entity) {
		OrderInstallGoodsResource resource = new OrderInstallGoodsResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(OrderInstallGoodsController.class).getOrderInstallGoods(entity.getId())).withSelfRel());
		return resource;
	}
	
}