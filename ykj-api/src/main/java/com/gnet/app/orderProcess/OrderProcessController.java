package com.gnet.app.orderProcess;

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


@RepositoryRestController
@ExposesResourceFor(OrderProcess.class)
@RequestMapping("/api/orderProcesses")
public class OrderProcessController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private OrderProcessService orderProcessService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOrderProcess(
		@PathVariable("id") String id
	){
		OrderProcess orderProcess = orderProcessService.findById(id);
		if (orderProcess == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderProcessErrorBuilder(OrderProcessErrorBuilder.ERROR_ORDERPROCESS_NULL, "找不到该订单进度状态").build());
		}
		
		OrderProcessResourceAssembler orderProcessResourceAssembler = new OrderProcessResourceAssembler();
		OrderProcessResource orderProcessResource = orderProcessResourceAssembler.toResource(orderProcess);
		
		return ResponseEntity.ok(orderProcessResource);
	}
	
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(OrderProcessController.class).withRel("orderProcesses"));
		return resource;
	}
	
}