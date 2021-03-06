package com.ruimeng.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.dto.CustomerDto;
import com.ruimeng.dto.OrderDto;
import com.ruimeng.dto.OrderExcelDto;
import com.ruimeng.dto.OrderItemDto;
import com.ruimeng.entity.*;
import com.ruimeng.service.*;
import com.ruimeng.util.DateUtil;
import com.ruimeng.util.ExcelUtil;
import com.ruimeng.vo.PageParam;
import com.ruimeng.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
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
@Transactional
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private GasCylinderService gasCylinderService;

    @Autowired
    private BillService billService;


    @PostMapping("save")
    public Result save(@RequestBody String orderInfo) throws ParseException {
        Map map = JSON.parseObject(orderInfo, Map.class);
        Object object = map.get("orderInfo");
        String string = JSON.toJSONString(object);
        TypeReference<OrderDto> type = new TypeReference<OrderDto>() {
        };
        OrderDto orderDto = JSON.parseObject(string, type);
        CustomerDto customer = orderDto.getCustomer();
        List<OrderItemDto> orderItems = orderDto.getOrderItems();
        String createTimeStr = orderDto.getCreateTimeStr();
        Orders order = new Orders();
        order.setPaid(orderDto.getPaid());
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
            quantity += orderItemDto.getQuantity();
            double total = orderItemDto.getPrice() * orderItemDto.getQuantity();
            totalPrice += total;
            OrderItem orderItem = new OrderItem();
            orderItem.setGasCylinderId(orderItemDto.getGasCylinderId());
            orderItem.setPrice(orderItemDto.getPrice());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItemList.add(orderItem);
        }
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setOrderDebt(totalPrice - order.getPaid());
        if (orderService.save(order)) {
            QueryWrapper<Bill> billQueryWrapper = new QueryWrapper<>();
            billQueryWrapper.eq("customerId", order.getCustomerId());
            QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
            ordersQueryWrapper.eq("customerId", order.getCustomerId());
            for (OrderItem orderItem : orderItemList) {
                orderItem.setOrderId(order.getId());
                GasCylinder gasCylinder = gasCylinderService.getById(orderItem.getGasCylinderId());
                orderItem.setGasCylinderName(gasCylinder.getName());
                int inventory = gasCylinder.getInventory();
                inventory -= orderItem.getQuantity();
                gasCylinder.setInventory(inventory);
                gasCylinder.setUpdateTime(new Date());
                gasCylinderService.updateById(gasCylinder);
            }
            orderItemService.saveBatch(orderItemList);

            List<Orders> orders = orderService.list(ordersQueryWrapper);
            Bill oldBill = billService.getOne(billQueryWrapper);
            Bill newBill = new Bill();
            double orderTotal = 0;
            double paid = 0;
            for (Orders orderByCustomerId : orders) {
                orderTotal += orderByCustomerId.getTotalPrice();
                paid += orderByCustomerId.getPaid();
            }
            newBill.setCreateTime(new Date());
            newBill.setUpdateTime(new Date());
            newBill.setCustomerId(order.getCustomerId());
            newBill.setCustomerName(order.getCustomerName());
            if (oldBill != null) {
                newBill.setEmptyBottleTotal(oldBill.getEmptyBottleTotal());
                newBill.setCreateTime(oldBill.getCreateTime());
            }
            newBill.setOrderTotal(orderTotal);
            newBill.setPaid(paid);
            double orderDebt = orderTotal - paid;
            newBill.setOrderDebt(orderDebt);
            double totalDebt = orderDebt + newBill.getEmptyBottleTotal();
            newBill.setTotalDebt(totalDebt);
            billService.remove(billQueryWrapper);
            billService.save(newBill);
            return Result.ok();
        }
        return Result.error();
    }


    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if (orderService.removeById(id)) {
            QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("orderId", id);
            List<OrderItem> orderItems = orderItemService.list(queryWrapper);
            for (OrderItem orderItem : orderItems) {
                GasCylinder gasCylinder = gasCylinderService.getById(orderItem.getGasCylinderId());
                int inventory = gasCylinder.getInventory();
                inventory -= orderItem.getQuantity();
                gasCylinder.setInventory(inventory);
                gasCylinder.setUpdateTime(new Date());
                gasCylinderService.updateById(gasCylinder);
            }
            orderItemService.remove(queryWrapper);
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("update")
    public Result update(@RequestBody String orderInfo) throws ParseException {
        Map map = JSON.parseObject(orderInfo, Map.class);
        Object object = map.get("orderInfo");
        String string = JSON.toJSONString(object);
        TypeReference<OrderDto> type = new TypeReference<OrderDto>() {
        };
        OrderDto orderDto = JSON.parseObject(string, type);
        CustomerDto customer = orderDto.getCustomer();
        List<OrderItemDto> orderItems = orderDto.getOrderItems();
        String createTimeStr = orderDto.getCreateTimeStr();
        Orders order = new Orders();
        order.setPaid(orderDto.getPaid());
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
            quantity += orderItemDto.getQuantity();
            totalPrice += orderItemDto.getPrice() * orderItemDto.getQuantity();
            OrderItem orderItem = new OrderItem();
            orderItem.setGasCylinderId(orderItemDto.getGasCylinderId());
            orderItem.setPrice(orderItemDto.getPrice());
            orderItem.setOrderId(order.getId());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setId(orderItemDto.getId());
            orderItemList.add(orderItem);

            OrderItem oldOrderItem = orderItemService.getById(orderItem.getId());
            GasCylinder gasCylinder = gasCylinderService.getById(orderItem.getGasCylinderId());
            orderItem.setGasCylinderName(gasCylinder.getName());
            int inventory = gasCylinder.getInventory();
            if (oldOrderItem != null) {
                inventory += oldOrderItem.getQuantity();
            }
            inventory -= orderItem.getQuantity();
            gasCylinder.setInventory(inventory);
            gasCylinder.setUpdateTime(new Date());
            gasCylinderService.updateById(gasCylinder);
        }
        order.setQuantity(quantity);
        order.setTotalPrice(totalPrice);
        order.setOrderDebt(totalPrice - order.getPaid());
        if (orderService.updateById(order)) {
            QueryWrapper<Bill> billQueryWrapper = new QueryWrapper<>();
            billQueryWrapper.eq("customerId", order.getCustomerId());

            QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
            ordersQueryWrapper.eq("customerId", order.getCustomerId());

            QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
            orderItemQueryWrapper.eq("orderId", order.getId());

            orderItemService.remove(orderItemQueryWrapper);
            orderItemService.saveBatch(orderItemList);

            List<Orders> orders = orderService.list(ordersQueryWrapper);
            Bill oldBill = billService.getOne(billQueryWrapper);
            Bill newBill = new Bill();
            double orderTotal = 0;
            double paid = 0;
            for (Orders orderByCustomerId : orders) {
                orderTotal += orderByCustomerId.getTotalPrice();
                paid += orderByCustomerId.getPaid();
            }
            newBill.setCreateTime(new Date());
            newBill.setUpdateTime(new Date());
            newBill.setCustomerId(order.getCustomerId());
            newBill.setCustomerName(order.getCustomerName());
            if (oldBill != null) {
                newBill.setEmptyBottleTotal(oldBill.getEmptyBottleTotal());
                newBill.setCreateTime(oldBill.getCreateTime());
            }
            newBill.setOrderTotal(orderTotal);
            newBill.setPaid(paid);
            double orderDebt = orderTotal - paid;
            newBill.setOrderDebt(orderDebt);
            double totalDebt = orderDebt + newBill.getEmptyBottleTotal();
            newBill.setTotalDebt(totalDebt);
            billService.remove(billQueryWrapper);
            billService.save(newBill);
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
        }else {
            queryWrapper.orderBy(true,false,"createTime");
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
        if (order != null) {
            OrderDto orderDto = new OrderDto();
            String createTime = DateUtil.dateToString(order.getCreateTime());
            if (StringUtils.isNotBlank(createTime)) {
                orderDto.setCreateTimeStr(createTime);
            }
            orderDto.setId(order.getId());
            orderDto.setPaid(order.getPaid());
            orderDto.setTotal(order.getTotalPrice());
            orderDto.setOrderDebt(order.getOrderDebt());
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
            if (orderItems != null && !orderItems.isEmpty()) {
                List<OrderItemDto> orderItemDtos = new ArrayList<>();
                for (OrderItem orderItem : orderItems) {
                    OrderItemDto orderItemDto = new OrderItemDto();
                    orderItemDto.setId(orderItem.getId());
                    orderItemDto.setGasCylinderId(orderItem.getGasCylinderId());
                    orderItemDto.setPrice(orderItem.getPrice());
                    orderItemDto.setQuantity(orderItem.getQuantity());
                    GasCylinder gasCylinder = gasCylinderService.getById(orderItem.getGasCylinderId());
                    if (gasCylinder != null) {
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

    @GetMapping("exportOrder")
    public void exportOrder(String beginTime, String endTime,String search,
                            HttpServletResponse response) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        Workbook workbook = null;
        try {
            if (StringUtils.isNotBlank(search)) {
                queryWrapper.like("search", search);
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
            List<Orders> orders = orderService.list(queryWrapper);
            QueryWrapper<OrderItem> orderItemQueryWrapper = new QueryWrapper<>();
            List<OrderExcelDto> orderExcelDtos = new ArrayList<>();
            for (Orders order : orders) {
                queryWrapper.eq("orderId", order.getId());
                List<OrderItem> orderItems = orderItemService.list(orderItemQueryWrapper);
                for (OrderItem orderItem : orderItems) {
                    OrderExcelDto orderExcelDto = new OrderExcelDto();
                    orderExcelDto.setId(order.getId());
                    orderExcelDto.setCustomerId(order.getCustomerId());
                    orderExcelDto.setCustomerName(order.getCustomerName());
                    orderExcelDto.setCreateTime(order.getCreateTime());
                    orderExcelDto.setGasCylinderId(orderItem.getGasCylinderId());
                    orderExcelDto.setGasCylinderName(orderItem.getGasCylinderName());
                    orderExcelDto.setPrice(orderItem.getPrice());
                    orderExcelDto.setTotal(order.getQuantity());
                    orderExcelDto.setQuantity(orderItem.getQuantity());
                    orderExcelDto.setTotalPrice(order.getTotalPrice());
                    orderExcelDtos.add(orderExcelDto);
                }
            }
            String[] orderTitles = {"订单单号", "订单日期", "客户id", "客户名称", "气瓶id", "气瓶类型", "购买数量", "单价", "订单总数量", "订单总金额"};
            String[] params = {"id", "createTime", "customerId", "customerName", "gasCylinderId", "gasCylinderName",
                    "quantity", "price", "total", "totalPrice"};
            workbook = ExcelUtil.createExecl(orderTitles, params, orderExcelDtos);
            String fileName = "订单详情报表.xlsx"; // 创建文件名
            String fileNameURL = URLEncoder.encode(fileName, "UTF-8");
            response.setContentType("application/ms-excel;charset=UTF-8"); // 设置ContentType
            response.setHeader("Content-disposition",
                    "attachment;filename=" + fileNameURL + ";" + "filename*=utf-8''" + fileNameURL);
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("异常", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.error("异常", e);
                }
            }
        }
    }
}

