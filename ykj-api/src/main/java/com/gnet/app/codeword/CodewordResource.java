package com.gnet.app.codeword;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class CodewordResource extends Resource<Codeword> {
	
	public CodewordResource(Codeword content, Iterable<Link> links) {
		super(content, links);
	}

	public CodewordResource(Codeword content, Link... links) {
		super(content, links);
	}
}
