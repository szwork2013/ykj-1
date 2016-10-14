package com.gnet.resource.mapResource;

import java.util.Map;

import org.springframework.hateoas.ResourceAssembler;

public class MapResourceAssembler<K, V> implements ResourceAssembler<Map<K, V>, MapResource<K, V>> {

	@Override
	public MapResource<K, V> toResource(Map<K, V> entity) {
		return new MapResource<>(entity);
	}

}
