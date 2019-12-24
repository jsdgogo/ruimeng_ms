package com.ruimeng.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.dto.CustomerDto;
import com.ruimeng.dto.OrderDto;
import com.ruimeng.dto.OrderItemDto;
import com.ruimeng.entity.Order;
import com.ruimeng.entity.OrderItem;
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

    @PostMapping("save")
    public Result save(@RequestBody String orderInfo) throws ParseException {
//        TypeReference<KeepState<List<Order>>> type = new TypeReference<KeepState<List<Order>>>() {
//        };
//        KeepState<List<Order>> keepState = JSON.parseObject(content, type);
        OrderDto orderDto = JSON.parseObject(orderInfo, OrderDto.class);
        CustomerDto customer = orderDto.getCustomer();
        List<OrderItemDto> orderItems = orderDto.getOrderItems();
        String createTimeStr = orderDto.getCreateTimeStr();
        Order order = new Order();
        order.setCustomerId(customer.getId());
        order.setCustomerName(customer.getName());
        order.setCreateTime(new Date());
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
        order.setOrderItems(orderItemList);
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        orderService.add(order);
        log.info(orderInfo);
        return Result.error();
    }


    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if (orderService.deleteById(id)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("update")
    public Result update(Order order) {
        order.setUpdateTime(new Date());
        if (orderService.updateById(order)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(PageParam pageParam, String startTime, String endTime) throws ParseException {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(pageParam.getSearch())) {
            queryWrapper.like("search", pageParam.getSearch());
        }
        if (StringUtils.isNotBlank(pageParam.getOrderBy())) {
            queryWrapper.orderBy(true, pageParam.isAscOrDesc(), pageParam.getOrderBy());
        }
        //创建时间大于等于开始时间
        if (StringUtils.isNotBlank(startTime)) {
            Date startDate = DateUtil.stringToDate(startTime);
            queryWrapper.ge("createTime", startDate.getTime());
        }
        //创建时间小于等于结束时间
        if (StringUtils.isNotBlank(endTime)) {
            Date endDate = DateUtil.stringToDate(endTime);
            queryWrapper.le("createTime", endDate.getTime());
        }
        Page<Order> page = new Page<>(pageParam.getIndex(), pageParam.getSize());
        IPage<Order> orderIPage = orderService.page(page, queryWrapper);
        return Result.ok().data("page", orderIPage);
    }

    @GetMapping("getById")
    public Result getById(int id) {
        Order order = orderService.getById(id);
        return Result.ok().data("order", order);
    }
    //excel
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
//                if ((int) map.get("isAllocation") == Order.ISALLOCATION_FALSE) {
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

