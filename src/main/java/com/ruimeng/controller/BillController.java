package com.ruimeng.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruimeng.dto.BillDto;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
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
@RequestMapping("bill")
@Slf4j
public class BillController {

    @Autowired
    private BillService billService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerEmptyBottleService customerEmptyBottleService;
    @Autowired
    private OrderItemService orderItemService;

    @PostMapping("save")
    public Result save(Bill bill) {
        bill.setCreateTime(new Date());
        bill.setUpdateTime(new Date());
        if (billService.save(bill)) {
            return Result.ok();
        }
        return Result.error();
    }

    @GetMapping("deleteById")
    public Result deleteById(int id) {
        if (billService.removeById(id)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("update")
    public Result update(Bill bill) {
        bill.setUpdateTime(new Date());
        if (billService.updateById(bill)) {
            return Result.ok();
        }
        return Result.error();
    }

    @PostMapping("findByPage")
    public Result findByPage(PageParam pageParam, String beginTime, String endTime) throws ParseException {
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
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
        Page<Bill> page = new Page<>(pageParam.getIndex(), pageParam.getSize());
        IPage<Bill> orderIPage = billService.page(page, queryWrapper);
        return Result.ok().data("page", orderIPage);
    }

    @GetMapping("getById")
    public Result getById(int id) {
        Bill bill = billService.getById(id);
        int customerId = bill.getCustomerId();
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.eq("customerId", customerId);
        List<Orders> orders = orderService.list(ordersQueryWrapper);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Orders order : orders) {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setCreateTimeStr(DateUtil.dateToString(order.getCreateTime()));
            orderDto.setPaid(order.getPaid());
            orderDto.setTotal(order.getTotalPrice());
            orderDto.setNumber(order.getQuantity());
            orderDto.setOrderDebt(order.getOrderDebt());
            orderDto.setUpdateTime(DateUtil.dateToString(order.getUpdateTime()));
            orderDtos.add(orderDto);
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
                    orderItemDto.setName(orderItem.getGasCylinderName());
                    orderItemDto.setGasCylinderId(orderItem.getGasCylinderId());
                    orderItemDtos.add(orderItemDto);
                }
                orderDto.setOrderItems(orderItemDtos);
            }
        }
        QueryWrapper<CustomerEmptyBottle> customerEmptyBottleQueryWrapper = new QueryWrapper<>();
        List<CustomerEmptyBottle> customerEmptyBottles = customerEmptyBottleService.list(customerEmptyBottleQueryWrapper);
        BillDto billDto = new BillDto();
        billDto.setId(bill.getId());
        billDto.setCreateTime(bill.getCreateTime());
        billDto.setCustomerEmptyBottles(customerEmptyBottles);
        billDto.setCustomerName(bill.getCustomerName());
        billDto.setEmptyBottleTotal(bill.getEmptyBottleTotal());
        billDto.setOrders(orderDtos);
        billDto.setOrderDebt(bill.getOrderDebt());
        billDto.setOrderTotal(bill.getOrderTotal());
        billDto.setPaid(bill.getPaid());
        billDto.setTotalDebt(bill.getTotalDebt());
        return Result.ok().data("bill", billDto);
    }

    @GetMapping("exportBill")
    public void exportBill(String beginTime, String endTime,String search,
                           HttpServletResponse response) {
        QueryWrapper<Bill> queryWrapper = new QueryWrapper<>();
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
            List<Bill> bills = billService.list(queryWrapper);
            String[] billTitles = {"欠款id", "欠款日期", "客户id", "客户名称", "总欠款", "订单欠款", "订单总金额", "已付款金额"};
            String[] params = {"id", "createTime", "customerId", "customerName", "totalDebt", "orderDebt",
                    "orderTotal", "paid"};
            workbook = ExcelUtil.createExecl(billTitles, params, bills);
            String fileName = "欠款详情报表.xlsx"; // 创建文件名
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

    @GetMapping("getDetails")
    public Result getDetails(int id) {
        Bill bill = billService.getById(id);
        int customerId = bill.getCustomerId();
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.eq("customerId", customerId);
        List<Orders> orders = orderService.list(ordersQueryWrapper);
        for (Orders order : orders) {
            QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("orderId", order.getId());
            List<OrderItem> orderItems = orderItemService.list(queryWrapper);
        }
        QueryWrapper<CustomerEmptyBottle> customerEmptyBottleQueryWrapper = new QueryWrapper<>();
        List<CustomerEmptyBottle> customerEmptyBottles = customerEmptyBottleService.list(customerEmptyBottleQueryWrapper);
        return Result.ok().data("bill", bill);
    }
}

