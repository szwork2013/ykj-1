package com.gnet.app.business;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gnet.app.store.Store;
import com.gnet.app.store.StoreController;
import com.gnet.app.store.StoreErrorBuilder;
import com.gnet.app.store.StoreOrderType;
import com.gnet.app.store.StoreResource;
import com.gnet.app.store.StoreResourceAssembler;
import com.gnet.app.store.StoreService;
import com.gnet.app.store.StoreValidator;
import com.gnet.resource.boolResource.BooleanResourceAssembler;
import com.gnet.resource.listResource.ListResourcesAssembler;
import com.gnet.utils.sort.ParamSceneUtils;
import com.gnet.utils.sort.exception.NotFoundOrderDirectionException;
import com.gnet.utils.sort.exception.NotFoundOrderPropertyException;



@RepositoryRestController
@ExposesResourceFor(Business.class)
@RequestMapping("/api/sellers")
public class BusinessController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private BusinessService businessService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private PagedResourcesAssembler<Business> pagedResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<Business> listResourcesAssembler;
	@Autowired
	private PagedResourcesAssembler<Store> pagedStoreResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<Store> listStoreResourcesAssembler;

	
	/**
	 * 所有的商家列表
	 * @param pageable
	 * @param isAll
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getBusinesses(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll
	) {
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, BusinessOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BusinessErrorBuilder(BusinessErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BusinessErrorBuilder(BusinessErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		// 判断是否分页
		Resources<BusinessResource> resources = null;
		if (isAll != null && isAll) {
			List<Business> businesses = businessService.findAll(orderList);
			
			BusinessResourceAssembler businessResourceAssembler = new BusinessResourceAssembler();
			resources = listResourcesAssembler.toResource(businesses, businessResourceAssembler);
		} else {
			Page<Business> businesses = businessService.pagination(pageable, orderList);
		
			BusinessResourceAssembler businessResourceAssembler = new BusinessResourceAssembler();
			resources = pagedResourcesAssembler.toResource(businesses, businessResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	
	/**
	 * 返回某一商家下的门店信息
	 * @param pageable
	 * @param isAll
	 * @return
	 */
	@RequestMapping(value = "/{id}/stores",  method = RequestMethod.GET)
	public ResponseEntity<?> getStores(
		@PageableDefault Pageable pageable,
		@PathVariable("id")  String businessId,
		@RequestParam(name = "isall", required = false) Boolean isAll
	) {
		
		if(StringUtils.isBlank(businessId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BusinessErrorBuilder(BusinessErrorBuilder.ERROR_ID_NULL, "商家编号为空").build());
		}
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, StoreOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StoreErrorBuilder(StoreErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StoreErrorBuilder(StoreErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		// 判断是否分页
		Resources<StoreResource> resources = null;
		if (isAll != null && isAll) {
			List<Store> stores = storeService.findAllByBusinessId(orderList, businessId);
			
			StoreResourceAssembler storeResourceAssembler = new StoreResourceAssembler();
			resources = listStoreResourcesAssembler.toResource(stores, storeResourceAssembler);
		} else {
			Page<Store> stores = storeService.pagination(pageable, orderList, businessId);
		
			StoreResourceAssembler storeResourceAssembler = new StoreResourceAssembler();
			resources = pagedStoreResourcesAssembler.toResource(stores, storeResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	
	/**
	 * 新增商家的门店信息
	 * @param store
	 * @return
	 */
	@RequestMapping(value = "/{id}/stores", method = RequestMethod.POST)
	public ResponseEntity<?> createStore(
		@RequestBody Store store,
		@PathVariable("id") String id
	) {
		Date date = new Date();
		store.setCreateDate(date);
		store.setModifyDate(date);
		store.setBusinessId(id);
		store.setIsDel(Boolean.FALSE);
		
		Map<String, Object> error = StoreValidator.validateBeforeCreateStore(store);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StoreErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = storeService.create(store);
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(StoreController.class).getStore(store.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StoreErrorBuilder(StoreErrorBuilder.ERROR_CREATED, "创建错误").build());
	}
	
	
	
	/**
	 * 根据查询返回相应商家信息
	 * @param pageable
	 * @param isAll
	 * @param name
	 * @param saleBrands
	 * @param contactPerson
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> searchBusinesses(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll,
		@RequestParam(name = "name", required = false) String name,
		@RequestParam(name = "saleBrands", required = false) String saleBrands,
		@RequestParam(name = "contactPerson", required = false) String contactPerson
	){
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, BusinessOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BusinessErrorBuilder(BusinessErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BusinessErrorBuilder(BusinessErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		
		// 判断是否分页
		Resources<BusinessResource> resources = null;
		if (isAll != null && isAll) {
			List<Business> businesses = businessService.findAll(name, saleBrands, contactPerson, orderList);
			
			BusinessResourceAssembler businessResourceAssembler = new BusinessResourceAssembler();
			resources = listResourcesAssembler.toResource(businesses, businessResourceAssembler);
		} else {
			Page<Business> businesses = businessService.pagination(pageable, name, saleBrands, contactPerson, orderList);
		
			BusinessResourceAssembler businessResourceAssembler = new BusinessResourceAssembler();
			resources = pagedResourcesAssembler.toResource(businesses, businessResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	
	/**
	 * 返回商家的详细信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getBusiness(
		@PathVariable("id") String id
	){
		Business business = businessService.findById(id);
		if (business == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BusinessErrorBuilder(BusinessErrorBuilder.ERROR_BUSINESS_NULL, "找不到该商家").build());
		}
		
		BusinessResourceAssembler businessResourceAssembler = new BusinessResourceAssembler();
		BusinessResource businessResource = businessResourceAssembler.toResource(business);
		
		return ResponseEntity.ok(businessResource);
	}
	
	
	/**
	 * 新增商家信息
	 * @param business
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createBusiness(
		@RequestBody Business business
	) {
		
		Date date = new Date();
		business.setCreateDate(date);
		business.setModifyDate(date);
		business.setIsDel(Boolean.FALSE);
		Map<String, Object> error = BusinessValidator.validateBeforeCreateBusiness(business);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BusinessErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = businessService.create(business);
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BusinessController.class).getBusiness(business.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BusinessErrorBuilder(BusinessErrorBuilder.ERROR_CREATED, "创建错误").build());
	}
	
	
	/**
	 * 更新商家信息
	 * @param id
	 * @param business
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateBusiness(
		@PathVariable("id") String id,
		@RequestBody Business business
	){
		Date date = new Date();
		business.setModifyDate(date);
		business.setId(id);
		business.setIsDel(Boolean.FALSE);
		
		Map<String, Object> error = BusinessValidator.validateBeforeUpdateBusiness(business);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BusinessErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = businessService.update(business);
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BusinessController.class).getBusiness(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BusinessErrorBuilder(BusinessErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	
	/**
	 * 删除商家信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteBusiness(
		@PathVariable("id") String id
	){
		Map<String, Object> error = BusinessValidator.validateBeforeDeleteBusiness(id);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BusinessErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Date date = new Date();
		if (businessService.delete(id, date)) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BusinessErrorBuilder(BusinessErrorBuilder.ERROR_DELETED, "删除错误").build());
	}
	
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(BusinessController.class).withRel("sellers"));
		return resource;
	}
	
}