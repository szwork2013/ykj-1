package com.gnet.app.good;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class GoodResource extends Resource<Good> {

	public GoodResource(Good content, Iterable<Link> links) {
		super(content, links);
	}

	public GoodResource(Good content, Link... links) {
		super(content, links);
	}
	
}