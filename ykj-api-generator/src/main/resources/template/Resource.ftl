package ${packageDir}.${toCamelName(resourceName)};

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class ${toCamelName(resourceName)?cap_first}Resource extends Resource<${toCamelName(resourceName)?cap_first}> {

	public ${toCamelName(resourceName)?cap_first}Resource(${toCamelName(resourceName)?cap_first} content, Iterable<Link> links) {
		super(content, links);
	}

	public ${toCamelName(resourceName)?cap_first}Resource(${toCamelName(resourceName)?cap_first} content, Link... links) {
		super(content, links);
	}
	
}