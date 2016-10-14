package com.gnet.app.codeword;

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

import com.gnet.app.businessCodeword.BusinessCodeword;
import com.gnet.app.businessCodeword.BusinessCodewordService;
import com.gnet.app.clerk.Clerk;
import com.gnet.resource.boolResource.BooleanResourceAssembler;
import com.gnet.resource.listResource.ListResourcesAssembler;
import com.gnet.security.user.CustomUser;
import com.gnet.utils.sort.ParamSceneUtils;
import com.gnet.utils.sort.exception.NotFoundOrderDirectionException;
import com.gnet.utils.sort.exception.NotFoundOrderPropertyException;

@RepositoryRestController
@ExposesResourceFor(Codeword.class)
@RequestMapping("/api/codeWords")
public class CodewordController implements ResourceProcessor<RepositoryLinksResource>  {
	
	@Autowired
	private CodewordService codewordService;
	@Autowired
	private BusinessCodewordService businessCodewordService;
	@Autowired
	private PagedResourcesAssembler<Codeword> pagedResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<Codeword> listResourcesAssembler;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getCodewords(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll,
		Authentication authentication,
		@RequestParam(name = "typeValue", required = false) String typeValue
	) {
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Clerk clerk = customUser.getClerk();
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, CodewordOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CodewordErrorBuilder(CodewordErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CodewordErrorBuilder(CodewordErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		// 判断是否分页
		Resources<CodewordResource> resources = null;
		CodewordResourceAssembler codewordResourceAssembler = new CodewordResourceAssembler();
		if (isAll != null && isAll) {
			List<Codeword> codewords = codewordService.findCodewords(orderList, clerk.getBusinessId(), typeValue);
			
			resources = listResourcesAssembler.toResource(codewords, codewordResourceAssembler);
		} else {
			Page<Codeword> codewords = codewordService.pagination(pageable, clerk.getBusinessId(), typeValue, orderList);
			
			resources = pagedResourcesAssembler.toResource(codewords, codewordResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCodeword(
		@PathVariable("id") String id
	) {
		Codeword codeword = codewordService.findById(id);
		if (codeword == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CodewordErrorBuilder(CodewordErrorBuilder.ERROR_CODEWORD_NULL, "找不到数据字典项").build());
		}
		
		CodewordResourceAssembler codewordResourceAssembler = new CodewordResourceAssembler();
		CodewordResource codewordResource = codewordResourceAssembler.toResource(codeword);
		
		return ResponseEntity.ok(codewordResource);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createCodeword(
		@RequestBody Codeword codeword,
		Authentication authentication
	) {
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Clerk clerk = customUser.getClerk();
		String businessId = clerk.getBusinessId();
		
		Map<String, Object> error = CodewordValidator.validateBeforeCreateCodeword(codeword);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CodewordErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		// 查看总数据字典中有无相同值
		Codeword existcodeWord = codewordService.findByTypeAndValue(codeword.getCodewordTypeId(), codeword.getValue());
		
		Boolean result = null;
		if (existcodeWord != null) {
			BusinessCodeword businessCodeword = businessCodewordService.findByWordAndBusiness(existcodeWord.getId(), businessId);
			// 若存在关联则返回添加失败
			if (businessCodeword != null) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CodewordErrorBuilder(CodewordErrorBuilder.ERROR_CREATED, "该数据项已存在").build());
			} else {
				result = businessCodewordService.createBusinessCodeword(existcodeWord.getId(), businessId);
			}
			
		} else {
			result = codewordService.create(codeword, businessId);
		}
		
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CodewordController.class).getCodeword(codeword.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CodewordErrorBuilder(CodewordErrorBuilder.ERROR_CREATED, "创建错误").build());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCodeword(
		@PathVariable("id") String id,
		Authentication authentication
	) {
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		Clerk clerk = customUser.getClerk();
		// 获得职工的直属部门与职工角色信息
		Codeword codeword = codewordService.findById(id);
		if (codeword == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CodewordErrorBuilder(CodewordErrorBuilder.ERROR_CODEWORD_NULL, "找不到数据字典项").build());
		}
		
		if (codeword.getIsSystem()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CodewordErrorBuilder(CodewordErrorBuilder.ERROR_CODE_NULL, "数据项为系统内置不能删除").build());
		}
		
		if (codewordService.delete(id, clerk.getBusinessId())) {
			return ResponseEntity.ok(new BooleanResourceAssembler().toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CodewordErrorBuilder(CodewordErrorBuilder.ERROR_DELETED, "删除错误").build());
	}

	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(CodewordController.class).withRel("codeWords"));
		return resource;
	}

}
