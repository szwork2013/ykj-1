package com.gnet.app.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

import com.gnet.app.orderService.OrderServiceErrorBuilder;
import com.gnet.app.orderService.OrderSer;
import com.gnet.app.orderService.OrderServiceOrderType;
import com.gnet.app.orderService.OrderServiceResource;
import com.gnet.codeword.CodewordGetter;
import com.gnet.app.Constant;
import com.gnet.app.codeword.Codeword;
import com.gnet.app.delivery.DeliveryResourceAssembler;
import com.gnet.app.delivery.DeliveryService;
import com.gnet.app.design.DesignResourceAssembler;
import com.gnet.app.design.DesignService;
import com.gnet.app.installation.InstallationService;
import com.gnet.app.measure.MeasureResourceAssembler;
import com.gnet.app.measure.MeasureService;
import com.gnet.app.orderGood.OrderGood;
import com.gnet.app.orderGood.OrderGoodOrderType;
import com.gnet.app.orderGood.OrderGoodResource;
import com.gnet.app.orderGood.OrderGoodResourceAssembler;
import com.gnet.app.orderGood.OrderGoodService;
import com.gnet.app.orderProcess.OrderProcess;
import com.gnet.app.orderProcess.OrderProcessResource;
import com.gnet.app.orderProcess.OrderProcessResourceAssembler;
import com.gnet.resource.boolResource.BooleanResourceAssembler;
import com.gnet.resource.listResource.ListResourcesAssembler;
import com.gnet.security.user.CustomUser;
import com.gnet.utils.sort.ParamSceneUtils;
import com.gnet.utils.sort.exception.NotFoundOrderDirectionException;
import com.gnet.utils.sort.exception.NotFoundOrderPropertyException;
import com.gnet.utils.spring.SpringContextHolder;


@RepositoryRestController
@ExposesResourceFor(Order.class)
@RequestMapping("/api/orders")
public class OrderController implements ResourceProcessor<RepositoryLinksResource> {

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderGoodService orderGoodService;
	@Autowired
	private MeasureService measureService;
	@Autowired
	private DesignService designService;
	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private InstallationService installationService;
	@Autowired
	private PagedResourcesAssembler<Order> pagedResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<Order> listResourcesAssembler;
	@Autowired
	private PagedResourcesAssembler<OrderSer> pagedServiceResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<OrderSer> listServiceResourcesAssembler;
	@Autowired
	private PagedResourcesAssembler<OrderGood> pagedOrderGoodResourcesAssembler;
	@Autowired
	private ListResourcesAssembler<OrderGood> listOrderGoodResourcesAssembler;
	
	

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getOrders(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll,
		Authentication authentication
	) {
		return searchOrders(pageable, isAll, null, null, null, null, null, null, null, authentication);
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ResponseEntity<?> searchOrders(
		@PageableDefault Pageable pageable,
		@RequestParam(name = "isall", required = false) Boolean isAll,
		@RequestParam(name = "type", required = false) Integer type,
		@RequestParam(name = "orderSource", required = false) Integer orderSource,
		@RequestParam(name = "orderResponsibleName", required = false) String orderResponsibleName,
		@RequestParam(name = "customerName", required = false) String customerName,
		@RequestParam(name = "startOrderDate", required = false) String startOrderDate,
		@RequestParam(name = "endOrderDate", required = false) String endOrderDate,
		@RequestParam(name = "mutiSearchColumn", required = false) String mutiSearchColumn,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, OrderOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		
		// 判断是否分页
		Resources<OrderResource> resources = null;
		if (isAll != null && isAll) {
			List<Order> orders = orderService.findAll(customUser.getClerk(), type, orderSource, orderResponsibleName,
					customerName, startOrderDate, endOrderDate, mutiSearchColumn, orderList);
			
			orderService.addSearchExtraData(orders);
			resources = listResourcesAssembler.toResource(orders, new OrderResourceAssembler());
		} else {
			Page<Order> orders = orderService.paginationAll(customUser.getClerk(), pageable, type, orderSource, 
					orderResponsibleName, customerName, startOrderDate, endOrderDate, mutiSearchColumn, orderList);
			
			orderService.addSearchExtraData(orders.getContent());
			resources = pagedResourcesAssembler.toResource(orders, new OrderResourceAssembler());
		}
		
		return ResponseEntity.ok(resources);
	}
	
	/**
	 * 返回订单的测量列表
	 * @param pageable
	 * @param isAll
	 * @return
	 */
	@RequestMapping(value = "/{id}/measures",  method = RequestMethod.GET)
	public ResponseEntity<?> getMeasures(
		@PageableDefault Pageable pageable,
		@PathVariable("id") String orderId,
		@RequestParam(name = "isall", required = false) Boolean isAll
	) {
		if(StringUtils.isBlank(orderId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_ID_NULL, "订单编号为空").build());
		}
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, OrderServiceOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		// 判断是否分页
		Resources<OrderServiceResource> resources = null;
		if (isAll != null && isAll) {
			List<OrderSer> measures = measureService.findAll(orderList, orderId, OrderSer.TYPE_MEASURE);
			
			MeasureResourceAssembler orderServiceResourceAssembler = new MeasureResourceAssembler();
			resources = listServiceResourcesAssembler.toResource(measures, orderServiceResourceAssembler);
		} else {
			Page<OrderSer> measures = measureService.pagination(pageable, orderList, orderId, OrderSer.TYPE_MEASURE);
		
			MeasureResourceAssembler orderServiceResourceAssembler = new MeasureResourceAssembler();
			resources = pagedServiceResourcesAssembler.toResource(measures, orderServiceResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	/**
	 * 返回订单的设计列表
	 * @param pageable
	 * @param isAll
	 * @return
	 */
	@RequestMapping(value = "/{id}/designs",  method = RequestMethod.GET)
	public ResponseEntity<?> getDesigns(
		@PageableDefault Pageable pageable,
		@PathVariable("id") String orderId,
		@RequestParam(name = "isall", required = false) Boolean isAll
	) {
		if(StringUtils.isBlank(orderId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_ID_NULL, "订单编号为空").build());
		}
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, OrderServiceOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		// 判断是否分页
		Resources<OrderServiceResource> resources = null;
		if (isAll != null && isAll) {
			List<OrderSer> designs = designService.findAll(orderList, orderId, OrderSer.TYPE_DESIGN);
			
			DesignResourceAssembler orderServiceResourceAssembler = new DesignResourceAssembler();
			resources = listServiceResourcesAssembler.toResource(designs, orderServiceResourceAssembler);
		} else {
			Page<OrderSer> designs = designService.pagination(pageable, orderList, orderId, OrderSer.TYPE_DESIGN);
		
			DesignResourceAssembler orderServiceResourceAssembler = new DesignResourceAssembler();
			resources = pagedServiceResourcesAssembler.toResource(designs, orderServiceResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	
	/**
	 * 返回订单的送货服务列表
	 * @param pageable
	 * @param orderId
	 * @param isAll
	 * @return
	 */
	@RequestMapping(value = "/{id}/deliverys",  method = RequestMethod.GET)
	public ResponseEntity<?> getDelivery(
		@PageableDefault Pageable pageable,
		@PathVariable("id") String orderId,
		@RequestParam(name = "isall", required = false) Boolean isAll
	) {
		if(StringUtils.isBlank(orderId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_ID_NULL, "订单编号为空").build());
		}
		
		Order order = orderService.findById(orderId);
		Map<String, Object> error = OrderValidator.validateBeforeDelivery(order);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, OrderServiceOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		// 判断是否分页
		Resources<OrderServiceResource> resources = null;
		if (isAll != null && isAll) {
			List<OrderSer> deliverys = deliveryService.findAll(orderList, orderId, OrderSer.TYPE_DELIVERY);
			
			DeliveryResourceAssembler orderServiceResourceAssembler = new DeliveryResourceAssembler();
			resources = listServiceResourcesAssembler.toResource(deliverys, orderServiceResourceAssembler);
		} else {
			Page<OrderSer> deliverys = deliveryService.pagination(pageable, orderList, orderId, OrderSer.TYPE_DELIVERY);
		
			DeliveryResourceAssembler orderServiceResourceAssembler = new DeliveryResourceAssembler();
			resources = pagedServiceResourcesAssembler.toResource(deliverys, orderServiceResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	/**
	 * 返回订单的安装服务列表
	 * @param pageable
	 * @param orderId
	 * @param isAll
	 * @return
	 */
	@RequestMapping(value = "/{id}/installations", method = RequestMethod.GET)
	public ResponseEntity<?> getInstallations(
		@PageableDefault Pageable pageable,
		@PathVariable("id") String orderId,
		@RequestParam(name = "isall", required = false) Boolean isAll
	) {
		if(StringUtils.isBlank(orderId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_ID_NULL, "订单编号为空").build());
		}
		
		Order order = orderService.findById(orderId);
		Map<String, Object> error = OrderValidator.validateBeforeInstallation(order);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, OrderServiceOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		// 判断是否分页
		Resources<OrderServiceResource> resources = null;
		if (isAll != null && isAll) {
			List<OrderSer> installations = installationService.findAll(orderList, orderId, OrderSer.TYPE_INSTALLATION);
			
			DeliveryResourceAssembler orderServiceResourceAssembler = new DeliveryResourceAssembler();
			resources = listServiceResourcesAssembler.toResource(installations, orderServiceResourceAssembler);
		} else {
			Page<OrderSer> installations = installationService.pagination(pageable, orderList, orderId, OrderSer.TYPE_INSTALLATION);
		
			DeliveryResourceAssembler orderServiceResourceAssembler = new DeliveryResourceAssembler();
			resources = pagedServiceResourcesAssembler.toResource(installations, orderServiceResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	
	/**
	 * 返回订单的未送货数量大于0的商品列表
	 * @param pageable
	 * @param isAll
	 * @return
	 */
	@RequestMapping(value = "/{id}/goods_shipment",  method = RequestMethod.GET)
	public ResponseEntity<?> getGoodsShipment(
		@PageableDefault Pageable pageable,
		@PathVariable("id") String orderId,
		@RequestParam(name = "isall", required = false) Boolean isAll
	) {
		if(StringUtils.isBlank(orderId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_ID_NULL, "订单编号为空").build());
		}
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, OrderGoodOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		// 判断是否分页
		Resources<OrderGoodResource> resources = null;
		if (isAll != null && isAll) {
			List<OrderGood> goods = orderGoodService.findAll(orderList, orderId, OrderSer.TYPE_DELIVERY);
			
			OrderGoodResourceAssembler orderGoodResourceAssembler = new OrderGoodResourceAssembler();
			resources = listOrderGoodResourcesAssembler.toResource(goods, orderGoodResourceAssembler);
		} else {
			Page<OrderGood> goods = orderGoodService.pagination(pageable, orderList, orderId, OrderSer.TYPE_DELIVERY);
		
			OrderGoodResourceAssembler orderGoodResourceAssembler = new OrderGoodResourceAssembler();
			resources = pagedOrderGoodResourcesAssembler.toResource(goods, orderGoodResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	
	/**
	 * 返回订单的未安装数量大于0的商品信息
	 * @param pageable
	 * @param isAll
	 * @return
	 */
	@RequestMapping(value = "/{id}/uninstall_goods",  method = RequestMethod.GET)
	public ResponseEntity<?> getGoodsUninstall(
		@PageableDefault Pageable pageable,
		@PathVariable("id") String orderId,
		@RequestParam(name = "isall", required = false) Boolean isAll
	) {
		if(StringUtils.isBlank(orderId)){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_ID_NULL, "订单编号为空").build());
		}
		List<String> orderList = null;
		
		// 排序处理
		try {
			orderList = ParamSceneUtils.toOrder(pageable, OrderGoodOrderType.class);
		} catch (NotFoundOrderPropertyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_PROPERTY_NOTFOUND, "排序字段不符合要求").build());
		} catch (NotFoundOrderDirectionException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_SORT_DIRECTION_NOTFOUND, "排序方向不符合要求").build());
		}
		
		// 判断是否分页
		Resources<OrderGoodResource> resources = null;
		if (isAll != null && isAll) {
			List<OrderGood> goods = orderGoodService.findAll(orderList, orderId, OrderSer.TYPE_INSTALLATION);
			
			OrderGoodResourceAssembler orderGoodResourceAssembler = new OrderGoodResourceAssembler();
			resources = listOrderGoodResourcesAssembler.toResource(goods, orderGoodResourceAssembler);
		} else {
			Page<OrderGood> goods = orderGoodService.pagination(pageable, orderList, orderId, OrderSer.TYPE_INSTALLATION);
		
			OrderGoodResourceAssembler orderGoodResourceAssembler = new OrderGoodResourceAssembler();
			resources = pagedOrderGoodResourcesAssembler.toResource(goods, orderGoodResourceAssembler);
		}
		
		return ResponseEntity.ok(resources);
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOrder(
		@PathVariable("id") String id
	){
		Order order = orderService.findById(id);
		if (order == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_ORDER_NULL, "找不到该订单").build());
		}
		
		OrderResourceAssembler orderResourceAssembler = new OrderResourceAssembler();
		OrderResource orderResource = orderResourceAssembler.toResource(order);
		
		return ResponseEntity.ok(orderResource);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createOrder(
		@RequestBody Order order,
		Authentication authentication
	) {
		
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		order.setBusinessId(customUser.getClerk().getBusinessId());
		
		Map<String, Object> error = OrderValidator.validateBeforeCreateOrder(order);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = orderService.create(order, customUser.getClerk().getId());
		if (result) {
			return ResponseEntity.created(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(OrderController.class).getOrder(order.getId())).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_CREATED, "创建错误").build());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateOrder(
		@PathVariable("id") String id,
		@RequestBody Order order,
		Authentication authentication
	){
		
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		order.setId(id);
		
		Map<String, Object> error = OrderValidator.validateBeforeUpdateOrder(order);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		Boolean result = orderService.update(order, customUser.getClerk().getId());
		
		if (result) {
			return ResponseEntity.noContent().location(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(OrderController.class).getOrder(id)).toUri()).build();
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_EDITED, "更新错误").build());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteOrder(
		@PathVariable("id") String id,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		
		if (orderService.delete(id, new Date(), customUser.getClerk().getId())) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_DELETED, "删除错误").build());
	}
	
	/**
	 * 获得订单下的订单商品列表
	 * @return
	 */
	@RequestMapping(value = "/{id}/goods", method = RequestMethod.GET)
	public ResponseEntity<?> selectOrderGoods(
		@PathVariable("id") String id
	){
		List<OrderGood> list = orderGoodService.selectAllUnderOrderWithGoodInfo(id);
		Resources<OrderGoodResource> resources = new ListResourcesAssembler<OrderGood>().toResource(list, new OrderGoodResourceAssembler());
		return ResponseEntity.ok(resources);
	}
	
	/**
	 * 批量更新订单商品
	 * 
	 * @param orderGoods
	 * @return
	 */
	@RequestMapping(value = "/{id}/goods/batch_update", method = RequestMethod.POST)
	public ResponseEntity<?> updateOrderGoods(
		@PathVariable("id") String id,
		@RequestBody List<OrderGood> orderGoods,
		Authentication authentication
	){
		
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		
		Map<String, Object> error = OrderValidator.validateUpdateOrderGoods(orderGoods);
		if (error != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderErrorBuilder(Integer.valueOf(error.get("code").toString()), error.get("msg").toString()).build());
		}
		
		if (orderGoodService.batchSaveOrUpdate(orderGoods, id, customUser.getClerk().getId())) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderErrorBuilder(OrderErrorBuilder.ERROR_DELETED, "删除错误").build());
	}
	
	/**
	 * 获得订单状态信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/statuses", method = RequestMethod.GET)
	public ResponseEntity<?> getStatuses(
		@PathVariable("id") String id
	){
		List<OrderProcess> orderProcesses = orderService.getProcessWithOrderInfo(id);
		Resources<OrderProcessResource> resources = new ListResourcesAssembler<OrderProcess>().toResource(orderProcesses, new OrderProcessResourceAssembler());
		return ResponseEntity.ok(resources);
	}
	
	/**
	 * 订单退单
	 * 退单条件1: 所有服务全部完成
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/return_result", method = RequestMethod.PATCH)
	public ResponseEntity<?> getReturnResult(
		@PathVariable("id") String id,
		Authentication authentication
	){
		
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		
		if (!orderService.orderServiceAllFinish(id)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_DELETED, "订单下中的服务未全部完成").build());
		}
		
		if (orderService.returnOrder(id, customUser.getClerk().getId())) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_EDITED, "退单失败").build());
	}
	
	/**
	 * 完成订单
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/finish_result", method = RequestMethod.PATCH)
	public ResponseEntity<?> getFinishResult(
		@PathVariable("id") String id,
		Authentication authentication
	){
		CustomUser customUser = (CustomUser) authentication.getPrincipal();
		
		OrderSer orderSer = orderService.getServiceWithoutMaintenanceUnFinish(id);
		CodewordGetter codewordGetter = SpringContextHolder.getBean(CodewordGetter.class);
		if (orderSer != null) {
			Codeword serviceTypeCodeword = codewordGetter.getCodewordByKey(Constant.ORDER_SERVICE_TYPE, orderSer.getType().toString());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_EDITED, "订单下中的" + serviceTypeCodeword.getValue() + "服务未全部完成").build());
		}
		
		if (orderService.finishOrder(id, customUser.getClerk().getId())) {
			BooleanResourceAssembler booleanResourceAssembler = new BooleanResourceAssembler();
			return ResponseEntity.ok(booleanResourceAssembler.toResource(Boolean.TRUE));
		}
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderServiceErrorBuilder(OrderServiceErrorBuilder.ERROR_EDITED, "完成订单失败").build());
	}
	
	
	@Override
	public RepositoryLinksResource process(RepositoryLinksResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(OrderController.class).withRel("orders"));
		return resource;
	}
	
}