package com.gnet.app.measure;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import com.gnet.app.orderService.OrderSer;
import com.gnet.app.orderService.OrderServiceResource;

public class MeasureResourceAssembler implements ResourceAssembler<OrderSer, OrderServiceResource> {

	@Override
	public OrderServiceResource toResource(OrderSer entity) {
		OrderServiceResource resource = new OrderServiceResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(MeasureController.class).getMeasure(entity.getId())).withSelfRel());
		return resource;
	}
	
}