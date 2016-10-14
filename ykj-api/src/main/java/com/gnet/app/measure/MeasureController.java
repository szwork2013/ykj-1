package com.gnet.app.measure;

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


@RepositoryRestController
@ExposesResourceFor(OrderSer.class)
@RequestMapping("/api/measures")
public class MeasureController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private MeasureService measureService;
	@Autowired
	private OrderServiceAttachmentService orderServiceAttachmentService;

	/**
	 * 测量详细信息
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeasure(
		@PathVariable("id") String id
	){
		if(StringUtils.isBlank(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_ID_NULL, "测量服务编号为空").build());
		}
		OrderSer measure = measureService.findById(id);
		if (measure == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SERVICE_NULL, "找不到测量服务").build());
		}
		
		MeasureResourceAssembler orderServiceResourceAssembler = new MeasureResourceAssembler();
		OrderServiceResource orderServiceResource = orderServiceResourceAssembler.toResource(measure);
		
		return ResponseEntity.ok(orderServiceResource);
	}
	
	
	/**
	 * 增加测量服务
	 * @param measure
	 * @param authentication
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createMeasure(
		@RequestBody OrderSer measure,
		Authentication authentication
	) {
		CustomUser customUser = (CustomUser) authentication.getPrincipal(); 
		Clerk clerk = customUser.getClerk();
		Date date = new Date();
		measure.setCreateDate(date);
		measure.setModifyDate(date);
		measure.setType(OrderSer.TYPE_MEASURE);
		measure.setIsDel(Boolean.FALSE);
		measure.setIsClear(Boolean.FALSE);
		
		Map<String, Object> error = MeasureValidator.validateBeforeCreateMeasure(measure, clerk.getBusinessId());
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		Boolean result = measureService.create(measure);
		
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(MeasureController.class).getMeasure(measure.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_CREATED, "创建错误").build());
	}
	
	
	
	/**
	 * 更新测量服务
	 * @param id
	 * @param measure
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateMeasure(
		@PathVariable("id") String id,
		@RequestBody OrderSer measure,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal(); 
		Clerk clerk = customUser.getClerk();
		Date date = new Date();
		measure.setModifyDate(date);
		measure.setId(id);
		
		Map<String, Object> error = MeasureValidator.validateBeforeUpdateMeasure(measure, clerk.getBusinessId());
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = measureService.update(measure);
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(MeasureController.class).getMeasure(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	/**
	 * 结算测量服务
	 * @param id
	 * @param measure
	 * @return
	 */
	@RequestMapping(value = "/{id}/statement", method = RequestMethod.PATCH)
	public ResponseEntity<?> stateMeasure(
		@PathVariable("id") String id
	){
		if(StringUtils.isBlank(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_ID_NULL, "测量服务编号为空").build());
		}
		OrderSer measure = measureService.findById(id);
		if (measure == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SERVICE_NULL, "找不到测量服务").build());
		}
		
		Map<String, Object> error = MeasureValidator.validateBeforeUpdateState(measure);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Date date = new Date();
		measure.setModifyDate(date);
		measure.setIsClear(Boolean.TRUE);
		Boolean result = measureService.update(measure);
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(MeasureController.class).getMeasure(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	
	/**
	 * 取消结算测量服务
	 * @param id
	 * @param measure
	 * @return
	 */
	@RequestMapping(value = "/{id}/cancelStatement", method = RequestMethod.PATCH)
	public ResponseEntity<?> cacelStateMeasure(
		@PathVariable("id") String id
	){
		if(StringUtils.isBlank(id)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_ID_NULL, "测量服务编号为空").build());
		}
		OrderSer measure = measureService.findById(id);
		if (measure == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SERVICE_NULL, "找不到测量服务").build());
		}
		
		Map<String, Object> error = MeasureValidator.validateBeforeUpdateCancelState(measure);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Date date = new Date();
		measure.setModifyDate(date);
		measure.setIsClear(Boolean.FALSE);
		Boolean result = measureService.update(measure);
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(MeasureController.class).getMeasure(id)).toUri()).build();
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
	 * 删除测量服务
	 * @param id
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeasure(
		@PathVariable("id") String id
	){
		Map<String, Object> error = MeasureValidator.validateBeforeDeleteOrderService(id);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		if (measureService.delete(id, new Date())) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_DELETED, "删除错误").build());
	}
	

	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(MeasureController.class).withRel("measures"));
		return resource;
	}
	
}