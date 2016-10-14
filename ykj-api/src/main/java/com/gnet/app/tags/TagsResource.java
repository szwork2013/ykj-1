package com.gnet.app.tags;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;


public class TagsResource extends Resource<Tags>{
	
	public TagsResource(Tags content, Iterable<Link> links) {
		super(content, links);
	}

	public TagsResource(Tags content, Link... links) {
		super(content, links);
	}

}
