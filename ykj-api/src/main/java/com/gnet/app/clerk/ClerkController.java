package com.gnet.app.clerk;

import java.util.ArrayList;
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

import com.gnet.app.authentication.user.SysUser;
import com.gnet.authentication.user.UserService;
import com.gnet.resource.boolResource.BooleanResourceAssembler;
import com.gnet.resource.listResource.ListResourcesAssembler;
import com.gnet.utils.sort.ParamSceneUtils;
import com.gnet.utils.sort.exception.NotFoundOrderDirectionException;
import com.gnet.utils.sort.exception.NotFoundOrderPropertyException;
import com.gnet.security.user.CustomUser;


@RepositoryRestController
@ExposesResourceFor(Clerk.class)
@RequestMapping("/api/clerks")
public class ClerkController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private ClerkService clerkService;
	@Autowired
	private UserService userService;
	@Autowired
	private PagedResourcesAssembler<Clerk> pagedResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<Clerk> listResourcesAssembler;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getClerks(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll,
		Authentication authentication
	) {
		return searchClerks(pageable, isAll, null, authentication);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> searchClerks(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll,
		@RequestParam(name = "name", required = false) String name,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Clerk clerk = customUser.getClerk();
		// 数据权限
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, ClerkOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClerkErrorBuilder(ClerkErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClerkErrorBuilder(ClerkErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		// 判断是否分页
		Resources<ClerkResource> resources = null;
		if (isAll != null && isAll) {
			// 数据权限判断
			List<Clerk> clerks = null;
			if (clerkService.belongBusiness(clerk.getRoleType())) {
				clerks = clerkService.findClerksUnderBusiness(clerk.getBusinessId(), name, orderList);
			} else if (clerkService.belongStore(clerk.getRoleType())) {
				clerks = clerkService.findClerksUnderStore(clerk.getStoreId(), name, orderList);
			} else {
				clerks = new ArrayList<>();
			}
			
			ClerkResourceAssembler clerkResourceAssembler = new ClerkResourceAssembler();
			resources = listResourcesAssembler.toResource(clerks, clerkResourceAssembler);
		} else {
			// 数据权限判断
			Page<Clerk> clerks = null;
			if (clerkService.belongBusiness(clerk.getRoleType())) {
				clerks = clerkService.paginationClerksUnderBusiness(pageable, clerk.getBusinessId(), name, orderList);
			} else if (clerkService.belongStore(clerk.getRoleType())) {
				clerks = clerkService.paginationClerksUnderStore(pageable, clerk.getStoreId(), name, orderList);
			} else {
				clerks = clerkService.paginationEmpty(pageable);
			}
		
			ClerkResourceAssembler clerkResourceAssembler = new ClerkResourceAssembler();
			resources = pagedResourcesAssembler.toResource(clerks, clerkResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}

	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getClerk(
		@PathVariable("id") String id
	){
		Clerk clerk = clerkService.findById(id);
		if (clerk == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ClerkErrorBuilder(ClerkErrorBuilder.ERROR_CLERK_NULL, "找不到该公司人员").build());
		}
		
		SysUser sysUser = userService.findById(id);
		clerk.setUsername(sysUser.getUsername());
		
		ClerkResourceAssembler clerkResourceAssembler = new ClerkResourceAssembler();
		ClerkResource clerkResource = clerkResourceAssembler.toResource(clerk);
		
		return ResponseEntity.ok(clerkResource);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createClerk(
		@RequestBody Map<String, Object> map,
		Authentication authentication
	) {
		
		Clerk clerk = clerkService.mapToClerk(map);
		Map<String, Object> error = ClerkValidator.validateBeforeCreateClerk(clerk, map);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ClerkErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = clerkService.create(clerk, map);
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ClerkController.class).getClerk(clerk.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ClerkErrorBuilder(ClerkErrorBuilder.ERROR_CREATED, "创建错误").build());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateClerk(
		@PathVariable("id") String id,
		@RequestBody Clerk clerk
	){
		clerk.setId(id);
		
		Map<String, Object> error = ClerkValidator.validateBeforeUpdateClerk(clerk);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ClerkErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = clerkService.update(clerk);
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(ClerkController.class).getClerk(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ClerkErrorBuilder(ClerkErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteClerk(
		@PathVariable("id") String id,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		
		if (clerkService.delete(id) && userService.delete(id, customUser.getId())) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ClerkErrorBuilder(ClerkErrorBuilder.ERROR_DELETED, "删除错误").build());
	}
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(ClerkController.class).withRel("clerks"));
		return resource;
	}
	
}