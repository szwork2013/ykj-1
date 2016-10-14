package com.gnet.app.orderInstallGoods;


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
@ExposesResourceFor(OrderInstallGoods.class)
@RequestMapping("/api/orderInstallGoods")
public class OrderInstallGoodsController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private OrderInstallGoodsService orderInstallGoodsService;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOrderInstallGoods(
		@PathVariable("id") String id
	){
		OrderInstallGoods orderInstallGoods = orderInstallGoodsService.findById(id);
		if (orderInstallGoods == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderInstallGoodsErrorBuilder(OrderInstallGoodsErrorBuilder.ERROR_ORDERINSTALLGOODS_NULL, "找不到该订单商品安装").build());
		}
		
		OrderInstallGoodsResourceAssembler orderInstallGoodsResourceAssembler = new OrderInstallGoodsResourceAssembler();
		OrderInstallGoodsResource orderInstallGoodsResource = orderInstallGoodsResourceAssembler.toResource(orderInstallGoods);
		
		return ResponseEntity.ok(orderInstallGoodsResource);
	}
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(OrderInstallGoodsController.class).withRel("orderInstallGoods"));
		return resource;
	}
	
}