package com.ruimeng.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.dto.CustomerDto;
import com.ruimeng.dto.OrderDto;
import com.ruimeng.dto.OrderItemDto;
import com.ruimeng.entity.Customer;
import com.ruimeng.entity.GasCylinder;
import com.ruimeng.entity.Orders;
import com.ruimeng.entity.OrderItem;
import com.ruimeng.service.CustomerService;
import com.ruimeng.service.GasCylinderService;
import com.ruimeng.service.OrderItemService;
import com.ruimeng.service.OrderService;
import com.ruimeng.util.DateUtil;
import com.ruimeng.vo.PageParam;
import com.ruimeng.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author JiangShiDing
 * @since 2019-11-27
 */
@RestController
@RequestMapping("order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private GasCylinderService gasCylinderService;

    @PostMapping("save")
    public Result save(@RequestBody String orderInfo) throws ParseException {
        Map map = JSON.parseObject(orderInfo, Map.class);
        Object object = map.get("orderInfo");
        String string = JSON.toJSONString(object);
        TypeReference<OrderDto> type = new TypeReference<OrderDto>() {};
        OrderDto orderDto = JSON.parseObject(string, type);
        CustomerDto customer = orderDto.getCustomer();
        List<OrderItemDto> orderItems = orderDto.getOrderItems();
        String createTimeStr = orderDto.getCreateTimeStr();
        Orders order = new Orders();
        order.setCustomerId(customer.getCustomerId());
        order.setCustomerName(customer.getCustomerName());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        if (StringUtils.isNotBlank(createTimeStr)) {
            order.setCreateTime(DateUtil.stringToDate(createTimeStr));
        }
        int quantity = 0;
        double totalPrice = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItems) {
            quantity+=orderItemDto.getQuantity();
            totalPrice+=orderItemDto.getPrice()*orderItemDto.getQuantity();
            OrderItem orderItem = new OrderItem();
            orderItem.setGasCylinderId(orderItemDto.getGasCylinderId());
            orderItem.setPrice(orderItemDto.getPrice());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItemList.add(orderItem);
        }
        order.setStatus(Orders.STATUS_NO);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        if (orderService.save(order)){
            for (OrderItem orderItem : orderItemList) {
                orderItem.setOrderId(order.getId());
                GasCylinder gasCylinder = gasCylinderService.getById(orderItem.getGasCylinderId());
                int inventory = gasCylinder.getInventory();
                inventory-=orderItem.getQuantity();
                gasCylinder.setInventory(inventory);
                gasCylinder.setUpdateTime(new Date());
                gasCylinderService.updateById(gasCylinder);
            }
            orderItemService.saveBatch(orderItemList);
            return Result.ok();
        }
        return Result.error();
    }


    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if (orderService.removeById(id)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("update")
    public Result update(@RequestBody String orderInfo) throws ParseException {
        Map map = JSON.parseObject(orderInfo, Map.class);
        Object object = map.get("orderInfo");
        String string = JSON.toJSONString(object);
        TypeReference<OrderDto> type = new TypeReference<OrderDto>() {};
        OrderDto orderDto = JSON.parseObject(string, type);
        CustomerDto customer = orderDto.getCustomer();
        List<OrderItemDto> orderItems = orderDto.getOrderItems();
        String createTimeStr = orderDto.getCreateTimeStr();
        Orders order = new Orders();
        order.setId(orderDto.getId());
        order.setCustomerId(customer.getCustomerId());
        order.setCustomerName(customer.getCustomerName());
        order.setUpdateTime(new Date());
        if (StringUtils.isNotBlank(createTimeStr)) {
            order.setCreateTime(DateUtil.stringToDate(createTimeStr));
        }
        int quantity = 0;
        double totalPrice = 0;
        List<OrderItem> orderItemList = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItems) {
            quantity+=orderItemDto.getQuantity();
            totalPrice+=orderItemDto.getPrice()*orderItemDto.getQuantity();
            OrderItem orderItem = new OrderItem();
            orderItem.setGasCylinderId(orderItemDto.getGasCylinderId());
            orderItem.setPrice(orderItemDto.getPrice());
            orderItem.setOrderId(order.getId());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setId(orderItemDto.getId());
            orderItemList.add(orderItem);
        }
        order.setStatus(Orders.STATUS_NO);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        if (orderService.updateById(order)){
                for (OrderItem orderItem : orderItemList) {
                    OrderItem oldOrderItem = orderItemService.getById(orderItem.getId());
                    GasCylinder gasCylinder = gasCylinderService.getById(orderItem.getGasCylinderId());
                    int inventory = gasCylinder.getInventory();
                    inventory+=oldOrderItem.getQuantity();
                    inventory -= orderItem.getQuantity();
                    gasCylinder.setInventory(inventory);
                    gasCylinder.setUpdateTime(new Date());
                    gasCylinderService.updateById(gasCylinder);
                    orderItemService.updateById(orderItem);
                }
                return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(PageParam pageParam, String beginTime, String endTime) throws ParseException {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getSearch())) {
            queryWrapper.like("search", pageParam.getSearch());
        }
        if (StringUtils.isNotBlank(pageParam.getOrderBy())) {
            queryWrapper.orderBy(true, pageParam.isAscOrDesc(), pageParam.getOrderBy());
        }
        //创建时间大于等于开始时间
        if (StringUtils.isNotBlank(beginTime)) {
            Date startDate = DateUtil.stringToDate(beginTime);
            queryWrapper.ge("createTime", startDate);
        }
        //创建时间小于等于结束时间
        if (StringUtils.isNotBlank(endTime)) {
            Date endDate = DateUtil.stringToDate(endTime);
            queryWrapper.le("createTime", endDate);
        }
        Page<Orders> page = new Page<>(pageParam.getIndex(), pageParam.getSize());
        IPage<Orders> orderIPage = orderService.page(page, queryWrapper);
        return Result.ok().data("page", orderIPage);
    }

    @GetMapping("getById")
    public Result getById(int id) {
        Orders order = orderService.getById(id);
        if (order!=null) {
            OrderDto orderDto = new OrderDto();
            String createTime = DateUtil.dateToString(order.getCreateTime());
            if (StringUtils.isNotBlank(createTime)){
                orderDto.setCreateTimeStr(createTime);
            }
            orderDto.setId(order.getId());
            Customer customer = customerService.getById(order.getCustomerId());
            CustomerDto customerDto = new CustomerDto();
            if (customer != null) {
                customerDto.setCustomerId(customer.getId());
                customerDto.setCustomerName(customer.getName());
                customerDto.setAddress(customer.getAddress());
            }
            orderDto.setCustomer(customerDto);
            QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("orderId", order.getId());
            List<OrderItem> orderItems = orderItemService.list(queryWrapper);
            if (orderItems!=null&&!orderItems.isEmpty()) {
                List<OrderItemDto> orderItemDtos = new ArrayList<>();
                for (OrderItem orderItem : orderItems) {
                    OrderItemDto orderItemDto = new OrderItemDto();
                    orderItemDto.setId(orderItem.getId());
                    orderItemDto.setGasCylinderId(orderItem.getGasCylinderId());
                    orderItemDto.setPrice(orderItem.getPrice());
                    orderItemDto.setQuantity(orderItem.getQuantity());
                    GasCylinder gasCylinder = gasCylinderService.getById(orderItem.getGasCylinderId());
                    if (gasCylinder!=null){
                        orderItemDto.setInventory(gasCylinder.getInventory());
                        orderItemDto.setName(gasCylinder.getName());
                    }
                    orderItemDtos.add(orderItemDto);
                }
                orderDto.setOrderItems(orderItemDtos);
            }
            return Result.ok().data("order", orderDto);
        }
        return Result.error();
    }
    //excel1
//    @GetMapping("exportCustomerOrder")
//    public void exportCustomerOrder(String keepshopId, String startTimeStr, String endTimeStr,
//                                    HttpServletResponse response) {
//        Date startTime = null;
//        Date endTime = null;
//        Workbook workbook = null;
//        try {
//            if (StringUtils.isNotBlank(startTimeStr)) {
//                startTime = StringToDateUtil.stringToDate(startTimeStr);
//            }
//            if (StringUtils.isNotBlank(endTimeStr)) {
//                endTime = StringToDateUtil.stringToDate(endTimeStr);
//            }
//            int loginShopId = Integer.parseInt(keepshopId);
//            List<Map<String, Object>> list = orderService.findOrderItem(Integer.parseInt(keepshopId), startTime,
//                    endTime);
//            List<Map<String, Object>> newList = new ArrayList<>();
//            for (Map<String, Object> map : list) {
//                if ((int) map.get("isAllocation") == Orders.ISALLOCATION_FALSE) {
//                    setMapParams(map, loginShopId);
//                    newList.add(map);
//                }
//            }
//            String[] consumerTitles = { "订单ID", "订单日期", "终端客户", "客户ERP编码", "平台商品ID", "产品外部编码", "产品名称", "规格", "包装", "厂家",
//                    "批准文号", "批号", "效期", "单位", "单价", "数量", "金额", "活动类型", "订单类型", "调拨价", "送货店铺", "自营店铺", "支付方式", "订单状态",
//                    "erp单号反馈" };
//            String[] params = { "orderId", "createTime", "createrName", "createrErpNumber", "itemId", "commoditCode",
//                    "title1", "title2", "largeScale", "manufacturer", "licenseNumber", "batchCode", "expiryTime",
//                    "basicUnit", "calcuPrice", "quantity", "total", "proType", "logisType", "transferPrice", "shopName",
//                    "logisShopName", "payType", "status", "" };
//            workbook = ExcelUtil.createExecl(consumerTitles, params, newList);
//            String fileName = "终端订单明细查询.xlsx"; // 创建文件名
//            String fileNameURL = URLEncoder.encode(fileName, "UTF-8");
//            response.setContentType("application/ms-excel;charset=UTF-8"); // 设置ContentType
//            response.setHeader("Content-disposition",
//                    "attachment;filename=" + fileNameURL + ";" + "filename*=utf-8''" + fileNameURL);
//            workbook.write(response.getOutputStream());
//        } catch (Exception e) {
//            log.error("异常", e);
//        } finally {
//            if (workbook != null) {
//                try {
//                    workbook.close();
//                } catch (IOException e) {
//                    log.error("异常", e);
//                }
//            }
//        }
//    }
}

