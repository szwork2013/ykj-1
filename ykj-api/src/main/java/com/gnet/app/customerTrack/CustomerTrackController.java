package com.gnet.app.customerTrack;

import java.util.Date;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.gnet.resource.boolResource.BooleanResourceAssembler;


@RepositoryRestController
@ExposesResourceFor(CustomerTrack.class)
@RequestMapping("/api/customer_tracks")
public class CustomerTrackController implements ResourceProcessor<RepositoryLinksResource>{

	@Autowired
	private CustomerTrackService customerTrackService;
	
	/**
	 * 客户某条跟踪记录详情
	 * @param customerTrackId
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerTrack(
			@PathVariable("id") String customerTrackId
	){
		if(StringUtils.isBlank(customerTrackId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerTrackErrorBuilder(CustomerTrackErrorBuilder.ERROR_ID_NULL, "客户跟进编号为空").build());
		}
		CustomerTrack customerTrack = customerTrackService.findById(customerTrackId);
		if (customerTrack == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerTrackErrorBuilder(CustomerTrackErrorBuilder.ERROR_CUSTOMER_TRACK_NULL, "客户跟进记录为空").build());
		}
				
		CustomerTrackResourceAssembler customerTrackResourceAssembler = new CustomerTrackResourceAssembler();
		CustomerTrackResource customerTrackResource = customerTrackResourceAssembler.toResource(customerTrack);
		
		return ResponseEntity.ok(customerTrackResource);
	}
	
	
	/**
	 * 新增跟进记录
	 * @param customerId
	 * @param customerTrack
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createCustomerTrack(
			@RequestBody CustomerTrack customerTrack
	) {
		Date date = new Date();
		customerTrack.setCreateDate(date);
		customerTrack.setModifyDate(date);
		
	    Map<String, Object> error = CustomerTrackValidator.validateBeforeCreateCustomerTrack(customerTrack);
	    if(error != null){
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomerTrackErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
	    }
	    
	    Boolean result = customerTrackService.create(customerTrack);
	    
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerTrackController.class).getCustomerTrack(customerTrack.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomerTrackErrorBuilder(CustomerTrackErrorBuilder.ERROR_CREATED, "创建错误").build());
	}
	
	
	/**
	 * 更新客户跟进记录
	 * @param customerTrackId
	 * @param customerTrack
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCustomerTrack(
			@PathVariable("id") String customerTrackId,
			@RequestBody CustomerTrack customerTrack
	){
		if(StringUtils.isBlank(customerTrackId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerTrackErrorBuilder(CustomerTrackErrorBuilder.ERROR_ID_NULL, "客户跟进编号为空").build());
		}
		
		Date date = new Date();
		customerTrack.setModifyDate(date);
		customerTrack.setId(customerTrackId);
		//编辑和新增的验证一样
		Map<String, Object> error = CustomerTrackValidator.validateBeforeCreateCustomerTrack(customerTrack);
	    if(error != null){
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomerTrackErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
	    }
	    
	    Boolean result = customerTrackService.update(customerTrack);
	    
	    if(result) {
	    	return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerTrackController.class).getCustomerTrack(customerTrackId)).toUri()).build();
	    }
	    
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomerTrackErrorBuilder(CustomerTrackErrorBuilder.ERROR_EDITED, "更新错误").build());
		
	}
	
	
	/**
	 * 删除客户跟进记录
	 * @param customerTrackId
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCustomerTrack(
		@PathVariable("id") String customerTrackId
	){
		if(StringUtils.isBlank(customerTrackId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerTrackErrorBuilder(CustomerTrackErrorBuilder.ERROR_ID_NULL, "客户跟进编号为空").build());
		}
		
		if(customerTrackService.delete(customerTrackId)){
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomerTrackErrorBuilder(CustomerTrackErrorBuilder.ERROR_DELETED, "删除错误").build());
	}
	
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(CustomerTrackController.class).withRel("customer_tracks"));
		return resource;
	}

}
