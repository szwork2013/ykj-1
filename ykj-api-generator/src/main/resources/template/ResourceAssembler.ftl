package ${packageDir}.${toCamelName(resourceName)};

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

public class ${toCamelName(resourceName)?cap_first}ResourceAssembler implements ResourceAssembler<${toCamelName(resourceName)?cap_first}, ${toCamelName(resourceName)?cap_first}Resource> {

	@Override
	public ${toCamelName(resourceName)?cap_first}Resource toResource(${toCamelName(resourceName)?cap_first} entity) {
		${toCamelName(resourceName)?cap_first}Resource resource = new ${toCamelName(resourceName)?cap_first}Resource(entity);
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(${toCamelName(resourceName)?cap_first}Controller.class).get${toCamelName(resourceName)?cap_first}(entity.getId())).withSelfRel());
		return resource;
	}
	
}