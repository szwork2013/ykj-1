package com.gnet.app.orderServiceAttachment;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class OrderServiceAttachmentResourceAssembler implements ResourceAssembler<OrderServiceAttachment, OrderServiceAttachmentResource> {

	@Override
	public OrderServiceAttachmentResource toResource(OrderServiceAttachment entity) {
		OrderServiceAttachmentResource resource = new OrderServiceAttachmentResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(OrderServiceAttachmentController.class).getOrderServiceAttachment(entity.getId())).withSelfRel());
		return resource;
	}
	
}