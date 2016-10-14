package com.gnet.app.hello;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class HelloResource extends Resource<Hello> {

	public HelloResource(Hello content, Iterable<Link> links) {
		super(content, links);
	}

	public HelloResource(Hello content, Link... links) {
		super(content, links);
	}

}
