package com.gnet.app.orderServiceAttachment;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class OrderServiceAttachmentResource extends Resource<OrderServiceAttachment> {

	public OrderServiceAttachmentResource(OrderServiceAttachment content, Iterable<Link> links) {
		super(content, links);
	}

	public OrderServiceAttachmentResource(OrderServiceAttachment content, Link... links) {
		super(content, links);
	}
	
}