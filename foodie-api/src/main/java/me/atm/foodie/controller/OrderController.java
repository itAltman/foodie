package me.atm.foodie.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.atm.common.utils.JsonResult;
import me.atm.pojo.bo.SubmitOrderBO;
import me.atm.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Altman
 * @date 2019/11/16
 **/
@Api(value = "订单模块", tags = {"订单模块相关接口"})
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Resource
    private OrderService orderService;

    @ApiOperation(value = "创建订单", notes = "提交订单按钮触发", httpMethod = "POST")
    @PostMapping("create")
    public JsonResult createOrders(@RequestBody SubmitOrderBO submitOrderBo) {
        // 1. 创建订单
        String orderId = orderService.createOrder(submitOrderBo);

        // TODO 2. 创建成功之后清空redis中购物车

        // 3. 调用支付中心进行支付
        return JsonResult.ok(orderId);
    }
}
