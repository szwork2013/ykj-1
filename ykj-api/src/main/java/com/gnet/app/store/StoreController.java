package com.gnet.app.store;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.gnet.resource.boolResource.BooleanResourceAssembler;



@RepositoryRestController
@ExposesResourceFor(Store.class)
@RequestMapping("/api/stores")
public class StoreController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private StoreService storeService;

	
	/**
	 * 获得某个门店的详细信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getStore(
		@PathVariable("id") String id
	){
		Store store = storeService.findById(id);
		if (store == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StoreErrorBuilder(StoreErrorBuilder.ERROR_STORE_NULL, "找不到该门店").build());
		}
		
		StoreResourceAssembler storeResourceAssembler = new StoreResourceAssembler();
		StoreResource storeResource = storeResourceAssembler.toResource(store);
		
		return ResponseEntity.ok(storeResource);
	}
	
		
	/**
	 * 更新门店信息
	 * @param id
	 * @param store
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateStore(
		@PathVariable("id") String id,
		@RequestBody Store store
	){
		Date date = new Date();
		store.setModifyDate(date);
		store.setId(id);
		store.setIsDel(Boolean.FALSE);
		
		Map<String, Object> error = StoreValidator.validateBeforeUpdateStore(store);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StoreErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = storeService.update(store);
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(StoreController.class).getStore(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StoreErrorBuilder(StoreErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteStore(
		@PathVariable("id") String id
	){
		Map<String, Object> error = StoreValidator.validateBeforeDeleteStore(id);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StoreErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Date date = new Date();
		if (storeService.delete(id, date)) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StoreErrorBuilder(StoreErrorBuilder.ERROR_DELETED, "删除错误").build());
	}

	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(StoreController.class).withRel("stores"));
		return resource;
	}
	
}