package me.atm.foodie.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import me.atm.common.enums.OrderStatusEnum;
import me.atm.common.utils.JsonResult;
import me.atm.pojo.OrderStatus;
import me.atm.pojo.bo.SubmitOrderBO;
import me.atm.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Altman
 * @date 2019/11/16
 **/
@Api(value = "订单模块", tags = {"订单/支付模块相关接口"})
@RestController
@RequestMapping("/orders")
public class OrderController {

    /**
     * 使用定时任务关闭超期未支付订单，会存在的弊端：
     * 1. 会有时间差，程序不严谨
     *      10:39下单，11:00检查不足1小时，12:00检查，超过1小时多余39分钟
     * 2. 不支持集群
     *      单机没毛病，使用集群后，就会有多个定时任务
     *      解决方案：只使用一台计算机节点，单独用来运行所有的定时任务
     * 3. 会对数据库全表搜索，及其影响数据库性能：select * from order where orderStatus = 10;
     * 定时任务，仅仅只适用于小型轻量级项目，传统项目
     *
     * 后续课程会涉及到消息队列：MQ-> RabbitMQ, RocketMQ, Kafka, ZeroMQ...
     *      延时任务（队列）
     *      10:12分下单的，未付款（10）状态，11:12分检查，如果当前状态还是10，则直接关闭订单即可
     */

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

    @ApiOperation(value = "通知订单支付", notes = "订单支付成功后，由支付中心调用此方法通知订单支付成功。（未对接支付中心需要手动调用）", httpMethod = "POST")
    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(@ApiParam(value = "订单编号") @RequestParam String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @ApiOperation(value = "查询订单支付状态信息", notes = "这里前端可以几秒轮训查一次，看看支付是否完成", httpMethod = "POST")
    @PostMapping("getPaidOrderInfo")
    public JsonResult getPaidOrderInfo(String orderId) {
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return JsonResult.ok(orderStatus);
    }
}
