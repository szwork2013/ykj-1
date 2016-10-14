package com.gnet.app.customerHouseProperty;

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

import com.gnet.app.customer.CustomerErrorBuilder;
import com.gnet.resource.boolResource.BooleanResourceAssembler;


@RepositoryRestController
@ExposesResourceFor(CustomerHouseProperty.class)
@RequestMapping("/api/customer_houses")
public class CustomerHousePropertyController implements ResourceProcessor<RepositoryLinksResource>{

	@Autowired
	private CustomerHousePropertyService customerHousePropertyService;
	
	/**
	 * 某个客户房产的详细信息
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerHouseProperty(
		@PathVariable("id") String customerHouseId
	) {
		
		if(StringUtils.isBlank(customerHouseId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerHousePropertyErrorBuilder(CustomerHousePropertyErrorBuilder.ERROR_ID_NULL, "客户房产编号为空").build());
		}
		
		CustomerHouseProperty customerHouseProperty = customerHousePropertyService.findById(customerHouseId);
		if (customerHouseProperty == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerHousePropertyErrorBuilder(CustomerHousePropertyErrorBuilder.ERROR_CUSTOMER_HOUSE_PROPERTY_NULL, "客户房产信息为空").build());
		}
		
		CustomerHousePropertyResourceAssembler housePropertyResourceAssembler = new CustomerHousePropertyResourceAssembler();
		CustomerHousePropertyResource resource = housePropertyResourceAssembler.toResource(customerHouseProperty);
		
		return ResponseEntity.ok(resource);
	}
	
	
	/**
	 * 增加客户房产
	 * @param 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createCustomerHouseProperty(
			@RequestBody CustomerHouseProperty customerHouseProperty
	){		
		
		Date date = new Date();
		customerHouseProperty.setCreateDate(date);
		customerHouseProperty.setModifyDate(date);
		
		Map<String, Object> error = CustomerHousePropertyValidator.validateBeforeCreateCustomerHouseProperty(customerHouseProperty);
		if(error != null){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomerHousePropertyErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = customerHousePropertyService.create(customerHouseProperty);
		
		if (result) {
		    return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerHousePropertyController.class).getCustomerHouseProperty(customerHouseProperty.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomerHousePropertyErrorBuilder(CustomerHousePropertyErrorBuilder.ERROR_CREATED, "创建错误").build());	
	}
	
	
	/**
	 * 更新客户某处房产信息
	 * @param customerHouseId
	 * @param customerHouseProperty
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCustomerHouseProperty(
			@PathVariable("id") String customerHouseId,
			@RequestBody CustomerHouseProperty customerHouseProperty
	){
		
		if(StringUtils.isBlank(customerHouseId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerHousePropertyErrorBuilder(CustomerHousePropertyErrorBuilder.ERROR_ID_NULL, "客户房产编号为空").build());
		}
		Date date = new Date();
		customerHouseProperty.setModifyDate(date);
		customerHouseProperty.setId(customerHouseId);

		Map<String, Object> error = CustomerHousePropertyValidator.validateBeforeCreateCustomerHouseProperty(customerHouseProperty);
		if(error != null){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomerHousePropertyErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
	    
	    Boolean result = customerHousePropertyService.update(customerHouseProperty);
	    if(result) {
	    	return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerHousePropertyController.class).getCustomerHouseProperty(customerHouseId)).toUri()).build();
	    }
	    
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomerHousePropertyErrorBuilder(CustomerHousePropertyErrorBuilder.ERROR_DELETED, "更新错误").build());
		
	}
	
	/**
	 * 删除客户某处房产信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCustomerHouseProperty(
		@PathVariable("id") String customerHouseId
	){
		if(StringUtils.isBlank(customerHouseId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerHousePropertyErrorBuilder(CustomerHousePropertyErrorBuilder.ERROR_ID_NULL, "客户房产编号为空").build());
		}
	    
		if(customerHousePropertyService.delete(customerHouseId)){
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_DELETED_ERROR, "删除错误").build());
	}
	
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(CustomerHousePropertyController.class).withRel("customer_houses"));
		return resource;
	}

}
