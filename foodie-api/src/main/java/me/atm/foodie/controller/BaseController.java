package me.atm.foodie.controller;

import me.atm.common.utils.JsonResult;
import me.atm.pojo.Orders;
import me.atm.service.center.MyOrdersService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class BaseController {

    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    @Resource
    public MyOrdersService myOrdersService;

    /**
     * 用于验证用户和订单是否有关联关系，避免非法用户调用
     *
     * @return
     */
    public JsonResult checkUserOrder(String userId, String orderId) {
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return JsonResult.errorMsg("订单不存在！");
        }
        return JsonResult.ok(order);
    }
}
