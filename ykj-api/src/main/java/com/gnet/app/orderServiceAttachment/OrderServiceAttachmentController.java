package com.gnet.app.orderServiceAttachment;

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
@ExposesResourceFor(OrderServiceAttachment.class)
@RequestMapping("/api/orderServiceAttachments")
public class OrderServiceAttachmentController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private OrderServiceAttachmentService orderServiceAttachmentService;


	/**
	 * 附件详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOrderServiceAttachment(
		@PathVariable("id") String id
	){
		OrderServiceAttachment orderServiceAttachment = orderServiceAttachmentService.findById(id);
		if (orderServiceAttachment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceAttachmentErrorBuilder(OrderServiceAttachmentErrorBuilder.ERROR_ORDERSERVICEATTACHMENT_NULL, "找不到该订单服务附件表").build());
		}
		
		OrderServiceAttachmentResourceAssembler orderServiceAttachmentResourceAssembler = new OrderServiceAttachmentResourceAssembler();
		OrderServiceAttachmentResource orderServiceAttachmentResource = orderServiceAttachmentResourceAssembler.toResource(orderServiceAttachment);
		
		return ResponseEntity.ok(orderServiceAttachmentResource);
	}
	

	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(OrderServiceAttachmentController.class).withRel("orderServiceAttachments"));
		return resource;
	}
	
}