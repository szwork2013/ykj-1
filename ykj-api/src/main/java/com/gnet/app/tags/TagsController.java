package com.gnet.app.tags;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.gnet.app.tags.Tags;

@RepositoryRestController
@ExposesResourceFor(Tags.class)
@RequestMapping("/api/tags")
public class TagsController implements ResourceProcessor<RepositoryLinksResource>{

	@Autowired
	private TagsService tagsService;

	/**
	 * 某个标签详细信息
	 * @param customerId
	 * @param tagsId
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getTags(
		@PathVariable("id") String id
	) {
		
		if(StringUtils.isBlank(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TagsErrorBuilder(TagsErrorBuilder.ERROR_ID_NULL, "标签编号为空").build());
		}
		
		Tags tags = tagsService.findById(id);
		if (tags == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TagsErrorBuilder(TagsErrorBuilder.ERROR_TAGS_NULL, "标签详细信息为空").build());
		}
		
		TagsResourceAssembler tagsResourceAssembler = new TagsResourceAssembler();
		TagsResource tagsResource = tagsResourceAssembler.toResource(tags);
		
		return ResponseEntity.ok(tagsResource);
	}
	
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		 resource.add(ControllerLinkBuilder.linkTo(TagsController.class).withRel("tags"));
		 return resource;
	}

}
