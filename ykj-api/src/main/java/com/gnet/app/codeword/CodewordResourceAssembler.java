package com.gnet.app.codeword;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class CodewordResourceAssembler implements ResourceAssembler<Codeword, CodewordResource> {

	@Override
	public CodewordResource toResource(Codeword entity) {
		CodewordResource resource = new CodewordResource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CodewordController.class).getCodeword(entity.getId())).withSelfRel());
		return resource;
	}

}
