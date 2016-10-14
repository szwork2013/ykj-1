package com.gnet.app.hello;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class HelloResourceAssembler implements ResourceAssembler<Hello, HelloResource> {

	@Override
	public HelloResource toResource(Hello entity) {
		HelloResource helloResource = new HelloResource(entity);
		helloResource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(HelloController.class).getHello(entity.getId())).withSelfRel());
		return helloResource;
	}

}
