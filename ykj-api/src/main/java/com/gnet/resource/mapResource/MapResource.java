package com.gnet.resource.mapResource;

import java.util.Map;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class MapResource<K, V> extends Resource<Map<K, V>> {

	public MapResource(Map<K, V> content, Iterable<Link> links) {
		super(content, links);
	}

	public MapResource(Map<K, V> content, Link... links) {
		super(content, links);
	}

}
