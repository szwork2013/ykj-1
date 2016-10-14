package ${packageDir}.${toCamelName(resourceName)};

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
import org.springframework.web.bind.annotation.RequestParam;

import com.gnet.resource.boolResource.BooleanResourceAssembler;
import com.gnet.resource.listResource.ListResourcesAssembler;
import com.gnet.utils.sort.ParamSceneUtils;
import com.gnet.utils.sort.exception.NotFoundOrderDirectionException;
import com.gnet.utils.sort.exception.NotFoundOrderPropertyException;
import com.gnet.security.user.CustomUser;


@RepositoryRestController
@ExposesResourceFor(${toCamelName(resourceName)?cap_first}.class)
@RequestMapping("/api/${resourceNameComplex}")
public class ${toCamelName(resourceName)?cap_first}Controller implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private ${toCamelName(resourceName)?cap_first}Service ${toCamelName(resourceName)}Service;
	@Autowired
	private PagedResourcesAssembler<${toCamelName(resourceName)?cap_first}> pagedResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<${toCamelName(resourceName)?cap_first}> listResourcesAssembler;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> get${resourceNameComplex?cap_first}(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll
	) {
		return search${resourceNameComplex?cap_first}(pageable, isAll<#list searchMap.entrySet() as item>, null</#list>);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> search${resourceNameComplex?cap_first}(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll<#if (searchMap.entrySet()?size > 0) >,</#if>
		<#list searchMap.entrySet() as item>
		@RequestParam(name = "${toCamelName(item.key)}", required = false) ${item.value} ${toCamelName(item.key)}<#if item_index + 1 < searchMap.entrySet()?size >,</#if>
		</#list>
	){
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, ${toCamelName(resourceName)?cap_first}OrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		
		// 判断是否分页
		Resources<${toCamelName(resourceName)?cap_first}Resource> resources = null;
		if (isAll != null && isAll) {
			List<${toCamelName(resourceName)?cap_first}> ${resourceNameComplex?uncap_first} = ${toCamelName(resourceName)}Service.findAll(<#list searchMap.entrySet() as item>${toCamelName(item.key)}, </#list>orderList);
			
			${toCamelName(resourceName)?cap_first}ResourceAssembler ${toCamelName(resourceName)}ResourceAssembler = new ${toCamelName(resourceName)?cap_first}ResourceAssembler();
			resources = listResourcesAssembler.toResource(${resourceNameComplex?uncap_first}, ${toCamelName(resourceName)}ResourceAssembler);
		} else {
			Page<${toCamelName(resourceName)?cap_first}> ${resourceNameComplex?uncap_first} = ${toCamelName(resourceName)}Service.pagination(pageable<#list searchMap.entrySet() as item>, ${toCamelName(item.key)}</#list>, orderList);
		
			${toCamelName(resourceName)?cap_first}ResourceAssembler ${toCamelName(resourceName)}ResourceAssembler = new ${toCamelName(resourceName)?cap_first}ResourceAssembler();
			resources = pagedResourcesAssembler.toResource(${resourceNameComplex?uncap_first}, ${toCamelName(resourceName)}ResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> get${toCamelName(resourceName)?cap_first}(
		@PathVariable("id") String id
	){
		${toCamelName(resourceName)?cap_first} ${toCamelName(resourceName)} = ${toCamelName(resourceName)}Service.findById(id);
		if (${toCamelName(resourceName)} == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_${toCamelName(resourceName)?upper_case}_NULL, "找不到该${resourceChineseName}").build());
		}
		
		${toCamelName(resourceName)?cap_first}ResourceAssembler ${toCamelName(resourceName)}ResourceAssembler = new ${toCamelName(resourceName)?cap_first}ResourceAssembler();
		${toCamelName(resourceName)?cap_first}Resource ${toCamelName(resourceName)}Resource = ${toCamelName(resourceName)}ResourceAssembler.toResource(${toCamelName(resourceName)});
		
		return ResponseEntity.ok(${toCamelName(resourceName)}Resource);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> create${toCamelName(resourceName)?cap_first}(
		@RequestBody ${toCamelName(resourceName)?cap_first} ${toCamelName(resourceName)}
	) {
		
		Map<String, Object> error = ${toCamelName(resourceName)?cap_first}Validator.validateBeforeCreate${toCamelName(resourceName)?cap_first}(${toCamelName(resourceName)});
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = ${toCamelName(resourceName)}Service.create(${toCamelName(resourceName)});
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(${toCamelName(resourceName)?cap_first}Controller.class).get${toCamelName(resourceName)?cap_first}(${toCamelName(resourceName)}.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_CREATED_ERROR, "创建错误").build());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update${toCamelName(resourceName)?cap_first}(
		@PathVariable("id") String id,
		@RequestBody ${toCamelName(resourceName)?cap_first} ${toCamelName(resourceName)}
	){
		${toCamelName(resourceName)}.setId(id);
		
		Map<String, Object> error = ${toCamelName(resourceName)?cap_first}Validator.validateBeforeUpdate${toCamelName(resourceName)?cap_first}(${toCamelName(resourceName)});
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = ${toCamelName(resourceName)}Service.update(${toCamelName(resourceName)});
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(${toCamelName(resourceName)?cap_first}Controller.class).get${toCamelName(resourceName)?cap_first}(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_UPDATED_ERROR, "更新错误").build());
	}
	
	<#if "${isSoftDel}" == "true">
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete${toCamelName(resourceName)?cap_first}(
		@PathVariable("id") String id,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		
		Map<String, Object> error = ${toCamelName(resourceName)?cap_first}Validator.validateBeforeDelete${toCamelName(resourceName)?cap_first}(id);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		if (${toCamelName(resourceName)}Service.delete(id, customUser.getId(), new Date())) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_DELETED_ERROR, "删除错误").build());
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAll${resourceNameComplex?cap_first}(
		@RequestBody String[] ids,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		
		Map<String, Object> error = ${toCamelName(resourceName)?cap_first}Validator.validateBeforeDelete${resourceNameComplex?cap_first}(ids);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		if (${toCamelName(resourceName)}Service.deleteAll(ids, customUser.getId(), new Date())) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_DELETED_ERROR, "删除错误").build());
	}
	<#else>
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete${toCamelName(resourceName)?cap_first}(
		@PathVariable("id") String id
	){
		Map<String, Object> error = ${toCamelName(resourceName)?cap_first}Validator.validateBeforeDelete${toCamelName(resourceName)?cap_first}(id);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		if (${toCamelName(resourceName)}Service.delete(id)) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_DELETED_ERROR, "删除错误").build());
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAll${resourceNameComplex?cap_first}(
		@RequestBody String[] ids
	){
		Map<String, Object> error = ${toCamelName(resourceName)?cap_first}Validator.validateBeforeDelete${resourceNameComplex?cap_first}(ids);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		if (${toCamelName(resourceName)}Service.deleteAll(ids)) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ${toCamelName(resourceName)?cap_first}ErrorBuilder(${toCamelName(resourceName)?cap_first}ErrorBuilder.ERROR_DELETED_ERROR, "删除错误").build());
	}
	</#if>
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(${toCamelName(resourceName)?cap_first}Controller.class).withRel("${resourceNameComplex}"));
		return resource;
	}
	
}