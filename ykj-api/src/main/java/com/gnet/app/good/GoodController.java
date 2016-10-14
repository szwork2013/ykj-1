package com.gnet.app.good;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gnet.resource.boolResource.BooleanResourceAssembler;
import com.gnet.resource.listResource.ListResourcesAssembler;
import com.gnet.security.user.CustomUser;
import com.gnet.utils.sort.ParamSceneUtils;
import com.gnet.utils.sort.exception.NotFoundOrderDirectionException;
import com.gnet.utils.sort.exception.NotFoundOrderPropertyException;


@RepositoryRestController
@ExposesResourceFor(Good.class)
@RequestMapping("/api/goods")
public class GoodController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private GoodService goodService;
	@Autowired
	private PagedResourcesAssembler<Good> pagedResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<Good> listResourcesAssembler;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getGoods(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll,
		Authentication authentication
	) {
		return searchGoods(pageable, isAll, null, null, authentication);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> searchGoods(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll,
		@RequestParam(name = "name", required = false) String name,
		@RequestParam(name = "onsaleStatus", required = false) Integer onsaleStatus,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		String businessId = customUser.getClerk().getBusinessId();
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, GoodOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GoodErrorBuilder(GoodErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GoodErrorBuilder(GoodErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		// 判断是否分页
		Resources<GoodResource> resources = null;
		if (isAll != null && isAll) {
			List<Good> goods = goodService.findAll(name, onsaleStatus, businessId, orderList);
			
			resources = listResourcesAssembler.toResource(goods, new GoodResourceAssembler());
		} else {
			Page<Good> goods = goodService.pagination(pageable, name, onsaleStatus, businessId, orderList);
		
			resources = pagedResourcesAssembler.toResource(goods, new GoodResourceAssembler());
		}
		
		return ResponseEntity.ok(resources);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getGood(
		@PathVariable("id") String id
	){
		Good good = goodService.findById(id);
		if (good == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GoodErrorBuilder(GoodErrorBuilder.ERROR_GOOD_NULL, "找不到该商品").build());
		}
		
		GoodResourceAssembler goodResourceAssembler = new GoodResourceAssembler();
		GoodResource goodResource = goodResourceAssembler.toResource(good);
		
		return ResponseEntity.ok(goodResource);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createGood(
		@RequestBody Good good,
		Authentication authentication
	) {
		
		Map<String, Object> error = GoodValidator.validateBeforeCreateGood(good);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GoodErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		String businessId = customUser.getClerk().getBusinessId();
		good.setBusinessId(businessId);
		
		Boolean result = goodService.create(good);
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(GoodController.class).getGood(good.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GoodErrorBuilder(GoodErrorBuilder.ERROR_CREATED, "创建错误").build());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateGood(
		@PathVariable("id") String id,
		@RequestBody Good good,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		String businessId = customUser.getClerk().getBusinessId();
		
		good.setId(id);
		good.setBusinessId(businessId);
		
		Map<String, Object> error = GoodValidator.validateBeforeUpdateGood(good);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GoodErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = goodService.update(good);
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(GoodController.class).getGood(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GoodErrorBuilder(GoodErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteGood(
		@PathVariable("id") String id
	){
		Map<String, Object> error = GoodValidator.validateBeforeDeleteGood(id);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GoodErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		if (goodService.delete(id)) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GoodErrorBuilder(GoodErrorBuilder.ERROR_DELETED, "删除错误").build());
	}
	
	@RequestMapping(value = "/{id}/status_result", method = RequestMethod.PATCH)
	public ResponseEntity<?> statusResult(
		@PathVariable("id") String id,
		@RequestParam("onsaleStatus") Integer onsaleStatus,
		Authentication authentication
	){
		
		Map<String, Object> error = GoodValidator.validateBeforeChangeStatus(onsaleStatus);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GoodErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		if (goodService.changeOnsaleStatus(id, onsaleStatus)) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GoodErrorBuilder(GoodErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(GoodController.class).withRel("goods"));
		return resource;
	}
	
}