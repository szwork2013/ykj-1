package com.gnet.app.customerTags;


import java.util.Map;

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

import com.gnet.app.tags.TagsErrorBuilder;
import com.gnet.app.tags.TagsValidator;
import com.gnet.resource.boolResource.BooleanResourceAssembler;


@RepositoryRestController
@ExposesResourceFor(CustomerTags.class)
@RequestMapping("/api/customer_tags")
public class CustomerTagsController implements ResourceProcessor<RepositoryLinksResource>{

	@Autowired
	private CustomerTagsService customerTagsService;
	
	/**
	 * 删除客户标签
	 * @param tagsId
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCustomerTags(
		@PathVariable("id") String customerTagsId
	){
		Map<String, Object> error = TagsValidator.validateBeforeDeleteTags(customerTagsId);
	    if(error != null){
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TagsErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
	    }
	    
		if(customerTagsService.delete(customerTagsId)){
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TagsErrorBuilder(TagsErrorBuilder.ERROR_DELETED, "删除错误").build());
	}
	
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(CustomerTagsController.class).withRel("customer_tags"));
		return resource;
	}

}
