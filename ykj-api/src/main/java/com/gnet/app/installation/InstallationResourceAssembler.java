package com.gnet.app.installation;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import com.gnet.app.orderService.OrderSer;
import com.gnet.app.orderService.OrderServiceResource;

public class InstallationResourceAssembler implements ResourceAssembler<OrderSer, OrderServiceResource> {

	@Override
	public OrderServiceResource toResource(OrderSer entity) {
		OrderServiceResource resource = new OrderServiceResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(InstallationController.class).getInstallation(entity.getId())).withSelfRel());
		return resource;
	}
	
}