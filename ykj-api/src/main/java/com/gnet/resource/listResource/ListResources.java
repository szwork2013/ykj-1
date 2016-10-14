package com.gnet.resource.listResource;


import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;

public class ListResources<E> extends Resources<E> {
	
	public ListResources(List<E> content, Link... links) {
		super(content, links);
	}

	public ListResources(List<E> content, Iterable<Link> links) {
		super(content, links);
	}
	
}
