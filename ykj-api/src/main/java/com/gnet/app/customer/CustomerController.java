package com.gnet.app.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
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
import com.gnet.security.user.CustomUser;
import com.gnet.utils.sort.ParamSceneUtils;
import com.gnet.utils.sort.exception.NotFoundOrderDirectionException;
import com.gnet.utils.sort.exception.NotFoundOrderPropertyException;
import com.gnet.app.clerk.Clerk;
import com.gnet.app.clerk.ClerkErrorBuilder;
import com.gnet.app.clerk.ClerkService;
import com.gnet.app.customerHouseProperty.CustomerHouseProperty;
import com.gnet.app.customerHouseProperty.CustomerHousePropertyResource;
import com.gnet.app.customerHouseProperty.CustomerHousePropertyResourceAssembler;
import com.gnet.app.customerHouseProperty.CustomerHousePropertyService;
import com.gnet.app.customerTrack.CustomerTrack;
import com.gnet.app.customerTrack.CustomerTrackErrorBuilder;
import com.gnet.app.customerTrack.CustomerTrackOrderType;
import com.gnet.app.customerTrack.CustomerTrackResource;
import com.gnet.app.customerTrack.CustomerTrackResourceAssembler;
import com.gnet.app.customerTrack.CustomerTrackService;
import com.gnet.app.tags.Tags;
import com.gnet.app.tags.TagsController;
import com.gnet.app.tags.TagsErrorBuilder;
import com.gnet.app.tags.TagsResource;
import com.gnet.app.tags.TagsResourceAssembler;
import com.gnet.app.tags.TagsService;
import com.gnet.app.tags.TagsValidator;
import com.gnet.resource.boolResource.BooleanResourceAssembler;
import com.gnet.resource.listResource.ListResourcesAssembler;


@RepositoryRestController
@ExposesResourceFor(Customer.class)
@RequestMapping("/api/customers")
public class CustomerController implements ResourceProcessor<RepositoryLinksResource>{

	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private ClerkService clerkService;
	@Autowired
	private CustomerTrackService customerTrackService;
	@Autowired
	private TagsService tagsService;
	@Autowired
	private CustomerHousePropertyService customerHousePropertyService;
	
	@Autowired
	private ListResourcesAssembler<Tags> listResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<CustomerHouseProperty> listCustomerHouseResourcesAssembler;
	@Autowired
	private PagedResourcesAssembler<Customer> pagedResourcesAssembler;
	@Autowired
	private PagedResourcesAssembler<CustomerTrack> pageCustomerTrackResourcesAssembler;
	
	
	/**
	 * 获取客户信息列表
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getCustomers(
		@PageableDefault Pageable pageable,
		Authentication authentication
			
	) {
		return SearchCustomUsers(pageable, null, null, null, authentication);
	}
	
	/**
	 * 根据查询获取相应的客户信息
	 * @param pageable
	 * @param name
	 * @param phone
	 * @param buildingName
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> SearchCustomUsers(
			@PageableDefault Pageable pageable,
			@Param("name") String name,
			@Param("phone") String phone,
			@Param("buildingName") String buildingName,
			Authentication authentication
	){
		
		CustomUser customUser = (CustomUser) authentication.getPrincipal();   
		String id = customUser.getId();
		
		//排序处理
		List<String> orderList = null;
		
		try {
			orderList = ParamSceneUtils.toOrder(pageable, CustomerOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		Page<Customer> customers = null;
		
		Clerk clerk =  clerkService.findById(id);
		if(clerk == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClerkErrorBuilder(ClerkErrorBuilder.ERROR_CLERK_NULL, "公司人员信息为空").build());
		}
		
		//店长返回同一门店下的客户，店员返回自己负责的客户,其余的返回商家下的客户
		if(clerk.getRoleType().equals(Clerk.ROLE_TYPE_STORE_MANAGER)){
			customers = customerService.pagination(pageable, orderList, clerk.getRoleType(), clerk.getStoreId(), name, phone, buildingName);
		}else if(clerk.getRoleType().equals(Clerk.ROLE_TYPE_CLERK)){
			customers = customerService.pagination(pageable, orderList, clerk.getId(), name, phone, buildingName);
		}else{
			customers = customerService.pagination(pageable, orderList, clerk.getRoleType(), clerk.getOfficeId(), name, phone, buildingName);
		}
		
		CustomerResourceAssembler customerResourceAssembler = new CustomerResourceAssembler();
		PagedResources<CustomerResource> pagedResources = pagedResourcesAssembler.toResource(customers, customerResourceAssembler);
		
		return ResponseEntity.ok(pagedResources);
		
	}
	
	
	/**
	 * 获取客户详细信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomer(
			@PathVariable("id") String id
	){
		Customer customer =customerService.findDetailById(id);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_CUSTOMER_NULL, "客户信息为空").build());
		}
		
		CustomerTrack customerTrack = customerTrackService.findLatestTrack(customer.getId());
	    //跟踪记录不为空的返回最新跟踪时间	
		if(customerTrack != null){
			customer.setTime(customerTrack.getTime());
		}
		
		CustomerResourceAssembler customerResourceAssembler = new CustomerResourceAssembler();
		CustomerResource customerResource = customerResourceAssembler.toResource(customer);
		
		return ResponseEntity.ok(customerResource);
	}
	
	
	/**
	 * 保存客户信息
	 * @param customer
	 * @param authentication
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createCustomer(
			@RequestBody Customer customer	
	) {
		Clerk clerk =  clerkService.findById(customer.getCustomerResponsibleId());
		if(clerk == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClerkErrorBuilder(ClerkErrorBuilder.ERROR_CLERK_NULL, "公司人员信息为空").build());
		}
	    Date date = new Date();
	    customer.setCreateDate(date);
	    customer.setModifyDate(date);
	    customer.setIsEffectivity(Boolean.FALSE);
		customer.setIsDel(Boolean.FALSE);
		customer.setBusinessId(clerk.getBusinessId());
		customer.setType(Customer.TYPE_POTENTIAL_CUSTOMER);
		
	    String[] tags = customer.getTags();
	    Map<String, Object> error = CustomerValidator.validateBeforeCreateCustomer(customer, tags);
	    if(error != null){
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ClerkErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
	    }
	    
	    Boolean result = customerService.create(customer, tags, clerk.getId());
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerController.class).getCustomer(customer.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_CREATED_ERROR, "创建错误").build());
	}
	
	
	/**
	 * 更新客户信息
	 * @param id
	 * @param customer
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCustomer(
			@PathVariable("id") String id,
			@RequestBody Customer customer
	){
		Customer oldCustomer = customerService.findById(id);
		if(oldCustomer == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_CUSTOMER_NULL, "客户原本信息为空").build());
		}
		
		Date date = new Date();
		customer.setModifyDate(date);
		customer.setId(id);
		customer.setIsDel(Boolean.FALSE);
		
		Map<String, Object> error = CustomerValidator.validateBeforeUpdateCustomer(customer, oldCustomer);
	    if(error != null){
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ClerkErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
	    }
	    
	    Boolean result =  customerService.update(customer);
	    
	    if(result) {
	    	return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerController.class).getCustomer(id)).toUri()).build();
	    }
	    
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_UPDATED_ERROR, "更新错误").build());
		
	}
	
	
	/**
	 * 将客户置为有效
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/enabled", method = RequestMethod.PATCH)
	public ResponseEntity<?> setCustomerEffectivity(
			@PathVariable("id") String id
	){
		Customer oldCustomer = customerService.findById(id);
		if(oldCustomer == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_CUSTOMER_NULL, "客户原本信息为空").build());
		}
	
	    Map<String, Object> error = CustomerValidator.validateBeforeSetCustomerEffectivity(oldCustomer);
	    if(error != null){
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ClerkErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
	    }
	    
		Date date = new Date();
		oldCustomer.setModifyDate(date);
	    oldCustomer.setIsEffectivity(Boolean.TRUE);
	    
	    Boolean result =  customerService.update(oldCustomer);
	    
	    if(result) {
	    	return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerController.class).getCustomer(id)).toUri()).build();
	    }
	    
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_UPDATED_ERROR, "更新错误").build());
	    
	}
	
	/**
	 * 将客户置为无效
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/disabled", method = RequestMethod.PATCH)
	public ResponseEntity<?> setCustomerUnEffectivity(
			@PathVariable("id") String id
	){
		Customer oldCustomer = customerService.findById(id);
		if(oldCustomer == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_CUSTOMER_NULL, "客户原本信息为空").build());
		}
	
	    Map<String, Object> error = CustomerValidator.validateBeforeSetCustomerUnEffectivity(oldCustomer);
	    if(error != null){
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ClerkErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
	    }
	    
		Date date = new Date();
		oldCustomer.setModifyDate(date);
	    oldCustomer.setIsEffectivity(Boolean.FALSE);
	    
	    Boolean result =  customerService.update(oldCustomer);
	    
	    if(result) {
	    	return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerController.class).getCustomer(id)).toUri()).build();
	    }
	    
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_UPDATED_ERROR, "更新错误").build());
		
	}
	
	/**
	 * 删除客户
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCustomer(
		@PathVariable("id") String id
	){
		Map<String, Object> error = CustomerValidator.validateBeforeDeleteCustomer(id);
	    if(error != null){
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ClerkErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
	    }
	    
	    Date date = new Date();
		if(customerService.deleteById(id, date)){
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_DELETED_ERROR, "删除错误").build());
	}
	
	
	/**
	 * 返回某个客户已有的标签
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/{id}/tags", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerTags(
	   @PathVariable("id") String customerId
	) {
		if(StringUtils.isBlank(customerId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_ID_NULL, "客户编号为空").build());
		}
		
		TagsResourceAssembler tagsResourceAssembler = new TagsResourceAssembler();
		List<Tags> customerTags = tagsService.findCustomerTags(customerId);
		Resources<TagsResource> resources = listResourcesAssembler.toResource(customerTags, tagsResourceAssembler);
		
		return ResponseEntity.ok(resources);
	}
	

	/**
	 * 增加客户标签
	 * @param tags
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/{id}/tags",  method = RequestMethod.POST)
	public ResponseEntity<?> createTags(
			@RequestBody Tags tags,
			@PathVariable("id") String customerId,
			Authentication authentication
	){		
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		String creatorId = customUser.getId();
		Date date = new Date();
		tags.setCreateDate(date);
		tags.setModifyDate(date);
		tags.setCreatorId(creatorId);
		
		Map<String, Object> error = TagsValidator.validateBeforeCreateTags(tags, customerId);
		if(error != null){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new TagsErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = tagsService.createCustomerTags(tags, customerId);
		if (result) {
		    return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TagsController.class).getTags(tags.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new TagsErrorBuilder(TagsErrorBuilder.ERROR_CREATED, "创建错误").build());	
	}
	

	/**
	 * 客户跟进记录
	 * @param customerId
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value = "/{id}/tracks", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerTracks(
	   @PathVariable("id") String customerId,
	   @PageableDefault Pageable pageable
	) {
		if(StringUtils.isBlank(customerId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_ID_NULL, "客户编号为空").build());
		}
		
		//排序处理
		List<String> orderList = null;
		try {
			orderList = ParamSceneUtils.toOrder(pageable, CustomerTrackOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerTrackErrorBuilder(CustomerTrackErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerTrackErrorBuilder(CustomerTrackErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		Page<CustomerTrack> customerTracks = customerTrackService.pagination(pageable, orderList, customerId);
		
		CustomerTrackResourceAssembler customerTrackResourceAssembler = new CustomerTrackResourceAssembler();
		PagedResources<CustomerTrackResource> pagedResources = pageCustomerTrackResourcesAssembler.toResource(customerTracks, customerTrackResourceAssembler);
		
		return ResponseEntity.ok(pagedResources);
	}
	
	/**
	 * 返回某个客户已有的房产
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/{id}/houses", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerHousePropertys(
	   @PathVariable("id") String customerId
	) {
		if(StringUtils.isBlank(customerId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomerErrorBuilder(CustomerErrorBuilder.ERROR_ID_NULL, "客户编号为空").build());
		}
		
		CustomerHousePropertyResourceAssembler housePropertyResourceAssembler = new CustomerHousePropertyResourceAssembler();
		List<CustomerHouseProperty> customerHouses = customerHousePropertyService.findCustomerHouses(customerId);
		Resources<CustomerHousePropertyResource> resources = listCustomerHouseResourcesAssembler.toResource(customerHouses, housePropertyResourceAssembler);
		
		return ResponseEntity.ok(resources);
	}
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(CustomerController.class).withRel("customers"));
		return resource;
	}

}
