package com.gnet.app.supplier;

import java.util.Date;
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

import com.gnet.app.clerk.Clerk;
import com.gnet.app.clerk.ClerkErrorBuilder;
import com.gnet.app.clerk.ClerkService;
import com.gnet.resource.boolResource.BooleanResourceAssembler;
import com.gnet.resource.listResource.ListResourcesAssembler;
import com.gnet.utils.sort.ParamSceneUtils;
import com.gnet.utils.sort.exception.NotFoundOrderDirectionException;
import com.gnet.utils.sort.exception.NotFoundOrderPropertyException;
import com.gnet.security.user.CustomUser;


@RepositoryRestController
@ExposesResourceFor(Supplier.class)
@RequestMapping("/api/suppliers")
public class SupplierController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private SupplierService supplierService;
	@Autowired 
	private ClerkService clerkService;
	@Autowired
	private PagedResourcesAssembler<Supplier> pagedResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<Supplier> listResourcesAssembler;

	/**
	 * 获取所有的品牌/供货商
	 * @param pageable
	 * @param isAll
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getSuppliers(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll
	) {
        return searchSuppliers(pageable, isAll, null, null);
	}
	
	
	/**
	 * 根据查询返回相应的品牌/供货商
	 * @param pageable
	 * @param isAll
	 * @param supplierName
	 * @param contactName
	 * @return
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> searchSuppliers(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll,
		@RequestParam(name = "supplierName", required = false) String supplierName,
		@RequestParam(name = "contactName", required = false) String contactName
	){
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, SupplierOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SupplierErrorBuilder(SupplierErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SupplierErrorBuilder(SupplierErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		
		// 判断是否分页
		Resources<SupplierResource> resources = null;
		if (isAll != null && isAll) {
			List<Supplier> suppliers = supplierService.findAll(supplierName, contactName, orderList);
			
			SupplierResourceAssembler supplierResourceAssembler = new SupplierResourceAssembler();
			resources = listResourcesAssembler.toResource(suppliers, supplierResourceAssembler);
		} else {
			Page<Supplier> suppliers = supplierService.pagination(pageable, supplierName, contactName, orderList);
		
			SupplierResourceAssembler supplierResourceAssembler = new SupplierResourceAssembler();
			resources = pagedResourcesAssembler.toResource(suppliers, supplierResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	
	/**
	 * 返回某个品牌供货商的详情
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getSupplier(
		@PathVariable("id") String id
	){
		Supplier supplier = supplierService.findById(id);
		if (supplier == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SupplierErrorBuilder(SupplierErrorBuilder.ERROR_SUPPLIER_NULL, "找不到该品牌供货商").build());
		}
		
		SupplierResourceAssembler supplierResourceAssembler = new SupplierResourceAssembler();
		SupplierResource supplierResource = supplierResourceAssembler.toResource(supplier);
		
		return ResponseEntity.ok(supplierResource);
	}
	
	
	
	/**
	 * 增加品牌供货商
	 * @param supplier
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createSupplier(
		@RequestBody Supplier supplier,
		Authentication authentication
	) {
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Clerk clerk =  clerkService.findById(customUser.getId());
		if(clerk == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClerkErrorBuilder(ClerkErrorBuilder.ERROR_CLERK_NULL, "公司人员信息为空").build());
		}
		Date date = new Date();
		supplier.setCreateDate(date);
		supplier.setModifyDate(date);
		supplier.setBusinessId(clerk.getBusinessId());
		
		Map<String, Object> error = SupplierValidator.validateBeforeCreateSupplier(supplier);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SupplierErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = supplierService.create(supplier);
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(SupplierController.class).getSupplier(supplier.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SupplierErrorBuilder(SupplierErrorBuilder.ERROR_CREATED, "创建错误").build());
	}
	
	
	
	/**
	 * 更新品牌供货商
	 * @param id
	 * @param supplier
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateSupplier(
		@PathVariable("id") String id,
		@RequestBody Supplier supplier
	){
		Date date = new Date();
		supplier.setId(id);
		supplier.setModifyDate(date);  
		
		Map<String, Object> error = SupplierValidator.validateBeforeUpdateSupplier(supplier);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SupplierErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = supplierService.update(supplier);
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(SupplierController.class).getSupplier(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SupplierErrorBuilder(SupplierErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	
	/**
	 * 删除品牌供货商
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteSupplier(
		@PathVariable("id") String id
	){
		Map<String, Object> error = SupplierValidator.validateBeforeDeleteSupplier(id);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new SupplierErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		if (supplierService.delete(id)) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new SupplierErrorBuilder(SupplierErrorBuilder.ERROR_DELETED, "删除错误").build());
	}
	
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(SupplierController.class).withRel("suppliers"));
		return resource;
	}
	
}