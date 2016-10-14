package com.gnet.app.orderGood;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gnet.app.order.OrderErrorBuilder;
import com.gnet.resource.boolResource.BooleanResourceAssembler;
import com.gnet.security.user.CustomUser;


@RepositoryRestController
@ExposesResourceFor(OrderGood.class)
@RequestMapping("/api/order_goods")
public class OrderGoodController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private OrderGoodService orderGoodService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOrderGood(
		@PathVariable("id") String id
	){
		OrderGood orderGood = orderGoodService.findById(id);
		if (orderGood == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderGoodErrorBuilder(OrderGoodErrorBuilder.ERROR_ORDERGOOD_NULL, "找不到该订单商品").build());
		}
		
		OrderGoodResourceAssembler orderGoodResourceAssembler = new OrderGoodResourceAssembler();
		OrderGoodResource orderGoodResource = orderGoodResourceAssembler.toResource(orderGood);
		
		return ResponseEntity.ok(orderGoodResource);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteOrderGood(
		@PathVariable("id") String id,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		
		Map<String, Object> error = OrderGoodValidator.validateBeforeDeleteOrderGood(id);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderGoodErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		if (orderGoodService.delete(id, customUser.getId())) {
			return ResponseEntity.ok(new BooleanResourceAssembler().toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_DELETED, "删除错误").build());
	}
	
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(OrderGoodController.class).withRel("order_goods"));
		return resource;
	}
	
}