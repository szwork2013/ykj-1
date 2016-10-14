package com.gnet.app.clerk;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class ClerkResource extends Resource<Clerk> {

	public ClerkResource(Clerk content, Iterable<Link> links) {
		super(content, links);
	}

	public ClerkResource(Clerk content, Link... links) {
		super(content, links);
	}
	
}