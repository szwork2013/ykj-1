package com.gnet.app.tags;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class TagsResourceAssembler implements ResourceAssembler<Tags, TagsResource>{
	
	@Override
	public TagsResource toResource(Tags entity) {
		TagsResource resource = new TagsResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TagsController.class).getTags(entity.getId())).withSelfRel());
		return resource;
	}


}
