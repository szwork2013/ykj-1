package com.gnet.app.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RepositoryRestController
@ExposesResourceFor(Hello.class)
@RequestMapping("/api/hellos")
public class HelloController implements ResourceProcessor<RepositoryLinksResource> {
	
	@Autowired
	private HelloService helloService;
	@Autowired
	private PagedResourcesAssembler<Hello> pagedResourcesAssembler;
	
	@RequestMapping(method = RequestMethod.GET)
	@PreAuthorize("#oauth2.hasScope('hello') and #oauth2.clientHasRole('ROLE_TRUSTED_CLIENT')")
	public ResponseEntity<PagedResources<HelloResource>> getHellos(
		@RequestParam(name = "page", required = false) Integer page,
		@RequestParam(name = "number", required = false) Integer number,
		@PageableDefault Pageable pageable
	) {
		Page<Hello> hellos = helloService.pagination(pageable);
		
		HelloResourceAssembler helloResourceAssembler = new HelloResourceAssembler();
		PagedResources<HelloResource> pagedResources = pagedResourcesAssembler.toResource(hellos, helloResourceAssembler);
		
		return ResponseEntity.ok(pagedResources);
	}
	
	@ConditionalOnProperty(name = "spring.datasource2.enable")
	@RequestMapping(path = "/datasource2", method = RequestMethod.GET)
	@PreAuthorize("#oauth2.hasScope('hello') and #oauth2.clientHasRole('ROLE_TRUSTED_CLIENT')")
	public ResponseEntity<PagedResources<HelloResource>> getHellosFromDataSource2(
		@RequestParam(name = "page", required = false) Integer page,
		@RequestParam(name = "number", required = false) Integer number,
		@PageableDefault Pageable pageable
	) {
		Page<Hello> hellos = helloService.paginationFromDataSource2(pageable);
		
		HelloResourceAssembler helloResourceAssembler = new HelloResourceAssembler();
		PagedResources<HelloResource> pagedResources = pagedResourcesAssembler.toResource(hellos, helloResourceAssembler);
		
		return ResponseEntity.ok(pagedResources);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<HelloResource> getHello(
		@PathVariable("id") String id
	){
		Hello hello = helloService.findById(id);
		
		HelloResourceAssembler helloResourceAssembler = new HelloResourceAssembler();
		HelloResource helloResource = helloResourceAssembler.toResource(hello);
		
		return ResponseEntity.ok(helloResource);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("#oauth2.hasScope('hello')")
	public ResponseEntity<?> createHello(
		@RequestBody Hello hello
	) {
		Boolean result = helloService.create(hello);
		
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(HelloController.class).getHello(hello.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HelloErrorBuilder(HelloErrorBuilder.ERROR_CREATED_ERROR, "创建错误").build());
	}

	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(HelloController.class).withRel("hellos"));
		return resource;
	}

}
