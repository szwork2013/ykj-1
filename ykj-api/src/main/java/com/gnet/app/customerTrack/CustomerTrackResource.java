package com.gnet.app.customerTrack;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class CustomerTrackResource extends Resource<CustomerTrack>{

	public CustomerTrackResource(CustomerTrack content, Iterable<Link> links) {
		super(content, links);
	}

	public CustomerTrackResource(CustomerTrack content, Link... links) {
		super(content, links);
	}
}
