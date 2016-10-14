package com.gnet.app.installation;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.gnet.app.clerk.Clerk;
import com.gnet.app.orderDeliverGoods.OrderDeliverGoodsErrorBuilder;
import com.gnet.app.orderInstallGoods.OrderInstallGoods;
import com.gnet.app.orderInstallGoods.OrderInstallGoodsOrderType;
import com.gnet.app.orderInstallGoods.OrderInstallGoodsResource;
import com.gnet.app.orderInstallGoods.OrderInstallGoodsResourceAssembler;
import com.gnet.app.orderInstallGoods.OrderInstallGoodsService;
import com.gnet.app.orderService.OrderSer;
import com.gnet.app.orderService.OrderServiceErrorBuilder;
import com.gnet.app.orderService.OrderServiceResource;
import com.gnet.app.orderServiceAttachment.OrderServiceAttachment;
import com.gnet.app.orderServiceAttachment.OrderServiceAttachmentController;
import com.gnet.app.orderServiceAttachment.OrderServiceAttachmentErrorBuilder;
import com.gnet.app.orderServiceAttachment.OrderServiceAttachmentService;
import com.gnet.resource.boolResource.BooleanResourceAssembler;
import com.gnet.security.user.CustomUser;
import com.gnet.upload.FileUploadService;
import com.gnet.utils.download.DownResponseBuilder;
import com.gnet.utils.spring.SpringContextHolder;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resources;
import com.gnet.resource.listResource.ListResourcesAssembler;
import com.gnet.utils.sort.ParamSceneUtils;
import com.gnet.utils.sort.exception.NotFoundOrderDirectionException;
import com.gnet.utils.sort.exception.NotFoundOrderPropertyException;


@RepositoryRestController
@ExposesResourceFor(OrderSer.class)
@RequestMapping("/api/installations")
public class InstallationController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private InstallationService installationService;
	@Autowired
	private OrderServiceAttachmentService orderServiceAttachmentService;
	@Autowired
	private OrderInstallGoodsService orderInstallGoodsService;
	@Autowired
	private PagedResourcesAssembler<OrderInstallGoods> pagedResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<OrderInstallGoods> listResourcesAssembler;

	/**
	 * 安装的详细信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getInstallation(
		@PathVariable("id") String id
	){
		if(StringUtils.isBlank(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_ID_NULL, "安装服务编号为空").build());
		}
		OrderSer installation = installationService.findById(id);
		if (installation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SERVICE_NULL, "找不到安装服务").build());
		}
		
		InstallationResourceAssembler orderServiceResourceAssembler = new InstallationResourceAssembler();
		OrderServiceResource orderServiceResource = orderServiceResourceAssembler.toResource(installation);
		
		return ResponseEntity.ok(orderServiceResource);
	}
	
	
	/**
	 * 安装商品列表
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/installation_goods", method = RequestMethod.GET)
	public ResponseEntity<?> getInstallationGoods(
		@PathVariable("id") String serviceId,
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll
	){
		if(StringUtils.isBlank(serviceId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_ID_NULL, "安装服务编号为空").build());
		}
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, OrderInstallGoodsOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderDeliverGoodsErrorBuilder(OrderDeliverGoodsErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderDeliverGoodsErrorBuilder(OrderDeliverGoodsErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		
		// 判断是否分页
		Resources<OrderInstallGoodsResource> resources = null;
		if (isAll != null && isAll) {
			List<OrderInstallGoods> orderInstallGoods = orderInstallGoodsService.findAll(orderList, serviceId);
			
			OrderInstallGoodsResourceAssembler orderInstallationGoodsResourceAssembler = new OrderInstallGoodsResourceAssembler();
			resources = listResourcesAssembler.toResource(orderInstallGoods, orderInstallationGoodsResourceAssembler);
		} else {
			Page<OrderInstallGoods> orderInstallGoods = orderInstallGoodsService.pagination(pageable, orderList, serviceId);
		
			OrderInstallGoodsResourceAssembler orderInstallationGoodsResourceAssembler = new OrderInstallGoodsResourceAssembler();
			resources = pagedResourcesAssembler.toResource(orderInstallGoods, orderInstallationGoodsResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	
	
	/**
	 * 增加安装服务（还需要增加商品）
	 * @param installation
	 * @param authentication
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createInstallation( 	
		@RequestBody OrderSer installation,
		Authentication authentication
	) {
		CustomUser customUser = (CustomUser) authentication.getPrincipal(); 
		Clerk clerk = customUser.getClerk();
		Date date = new Date();
		installation.setCreateDate(date);
		installation.setModifyDate(date);
		installation.setType(OrderSer.TYPE_INSTALLATION);
		installation.setIsDel(Boolean.FALSE);
		installation.setIsClear(Boolean.FALSE);
		
		Map<String, Object> error = InstallationValidator.validateBeforeCreateInstation(installation, clerk.getBusinessId());
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		Boolean result = installationService.create(installation);
		
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(InstallationController.class).getInstallation(installation.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_CREATED, "创建错误").build());
	}
	
	
	/**
	 * 更新安装商品
	 * @param id
	 * @param installation
	 * @return
	 */
	@RequestMapping(value = "/{id}/installation_goods", method = RequestMethod.PUT)
	public ResponseEntity<?> updateInstallationGoods(
		@PathVariable("id") String id,
		@RequestBody OrderSer installation,
		Authentication authentication
	){
		if(StringUtils.isBlank(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_ID_NULL, "安装服务编号为空").build());
		}
		
		Map<String, Object> error = InstallationValidator.validateBeforeUpdateInstallationGoods(installation);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = installationService.updateGoods(installation);
		
		if (result) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	
	/**
	 * 更新安装服务
	 * @param id
	 * @param installation
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateInstallation(
		@PathVariable("id") String id,
		@RequestBody OrderSer installation,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal(); 
		Clerk clerk = customUser.getClerk();
		Date date = new Date();
		installation.setModifyDate(date);
		installation.setId(id);
		
		Map<String, Object> error = InstallationValidator.validateBeforeUpdateInstallation(installation, clerk.getBusinessId());
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = installationService.update(installation);
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(InstallationController.class).getInstallation(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	
	/**
	 * 结算安装服务
	 * @param id
	 * @param measure
	 * @return
	 */
	@RequestMapping(value = "/{id}/statement", method = RequestMethod.PATCH)
	public ResponseEntity<?> stateInstallation(
		@PathVariable("id") String id
	){
		if(StringUtils.isBlank(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_ID_NULL, "安装服务编号为空").build());
		}
		OrderSer installation = installationService.findById(id);
		if (installation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SERVICE_NULL, "找不到安装服务").build());
		}
		
		Map<String, Object> error = InstallationValidator.validateBeforeUpdateState(installation);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Date date = new Date();
		installation.setModifyDate(date);
		installation.setIsClear(Boolean.TRUE);
		Boolean result = installationService.update(installation);
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(InstallationController.class).getInstallation(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	
	/**
	 * 取消结算安装服务
	 * @param id
	 * @param measure
	 * @return
	 */
	@RequestMapping(value = "/{id}/cancelStatement", method = RequestMethod.PATCH)
	public ResponseEntity<?> cacelStateInstallation(
		@PathVariable("id") String id
	){
		if(StringUtils.isBlank(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_ID_NULL, "安装服务编号为空").build());
		}
		OrderSer installation = installationService.findById(id);
		if (installation == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SERVICE_NULL, "找不到安装服务").build());
		}
		
		Map<String, Object> error = InstallationValidator.validateBeforeUpdateCancelState(installation);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Date date = new Date();
		installation.setModifyDate(date);
		installation.setIsClear(Boolean.FALSE);
		Boolean result = installationService.update(installation);
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(InstallationController.class).getInstallation(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	/**
	 * 上传附件
	 * @param fileType
	 * @param file
	 * @param authentication
	 * @return
	 */
	@RequestMapping(path = "/attachment_upload", method = RequestMethod.POST)
	public ResponseEntity<?> upload(
	    @Param("fileType") String fileType,
	    @RequestParam("file") MultipartFile file,
	    Authentication authentication
   ){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		if (file == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_UPLOAD, "未获得上传文件").build());
		}
		
		FileUploadService fileUploadService = SpringContextHolder.getBean(FileUploadService.class);
		Resource uploadResource = fileUploadService.getResource(file, fileType);
		if(uploadResource == null){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_UPLOAD, "上传失败").build());
		}
		
		String path = null;
		try {
			path = uploadResource.getFile().getPath();
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_UPLOAD, "上传失败").build());
		}
	   
  	    Date date = new Date();
		OrderServiceAttachment attachment = new OrderServiceAttachment();
		attachment.setCreateDate(date);
		attachment.setModifyDate(date);
		attachment.setAttachmentRoot(fileUploadService.getRelativePath(path));
		attachment.setAttachmentSize(String.valueOf(file.getSize()));
		attachment.setAttachmentFilename(file.getOriginalFilename());
		attachment.setUploadDate(date);
		attachment.setUploadPersonId(customUser.getId());
		
		
		Boolean result = orderServiceAttachmentService.create(attachment);
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(OrderServiceAttachmentController.class).getOrderServiceAttachment(attachment.getId())).toUri()).build();
		}
		
	   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_UPLOAD, "上传失败").build());
	}
	
	
	
	/**
	 * 下载附件
	 * @param id
	 * @return
	 */
	@RequestMapping(path = "/{id}/attachment_download", method = RequestMethod.GET)
	public ResponseEntity<?> download(
			@PathVariable("id") String id,
			HttpServletResponse response
	){
		FileUploadService fileUploadService = SpringContextHolder.getBean(FileUploadService.class);
		if(StringUtils.isBlank(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceAttachmentErrorBuilder(OrderServiceAttachmentErrorBuilder.ERROR_ID_NULL, "附件编号为空").build());
		}
		OrderServiceAttachment attachment  = orderServiceAttachmentService.findById(id);
		FileSystemResource resource = new FileSystemResource(fileUploadService.getUploadRootPath() + attachment.getAttachmentRoot());
		try {
			DownResponseBuilder.buildFile(response, resource, attachment.getAttachmentFilename());
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_DOWNLOAD, "下载附件失败").build());
		}
		
		return ResponseEntity.ok(null);
		
	}
	
	
	/**
	 * 删除安装服务
	 * @param id
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteInstallation(
		@PathVariable("id") String id
	){
		Map<String, Object> error = InstallationValidator.validateBeforeDeleteOrderService(id);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		if (installationService.delete(id, new Date())) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_DELETED, "删除错误").build());
	}
	

	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(InstallationController.class).withRel("deliverys"));
		return resource;
	}
	
}