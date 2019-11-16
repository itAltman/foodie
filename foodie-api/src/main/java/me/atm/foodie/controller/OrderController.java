package me.atm.foodie.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.atm.common.utils.JsonResult;
import me.atm.pojo.OrderStatus;
import me.atm.pojo.bo.SubmitOrderBO;
import me.atm.service.OrderService;
import org.springframework.web.bind.annotation.*;

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

        // 3. 调用支付中心进行支付。我这里就不进行支付中心的对接了，因为没有企业资质。
        return JsonResult.ok(orderId);
    }

    @ApiOperation(value = "查询订单支付状态信息", notes = "这里前端可以几秒轮训查一次，看看支付是否完成", httpMethod = "POST")
    @PostMapping("getPaidOrderInfo")
    public JsonResult getPaidOrderInfo(String orderId) {
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return JsonResult.ok(orderStatus);
    }
}
