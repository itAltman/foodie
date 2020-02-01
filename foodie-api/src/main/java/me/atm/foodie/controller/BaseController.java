package me.atm.foodie.controller;

import me.atm.common.utils.JsonResult;
import me.atm.common.utils.RedisOperator;
import me.atm.pojo.Orders;
import me.atm.pojo.Users;
import me.atm.pojo.vo.UsersVO;
import me.atm.service.center.MyOrdersService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.UUID;

@Controller
public class BaseController {

    public static final String FOODIE_SHOPCART = "shopcart";
    public static final String REDIS_USER_TOKEN = "redis_user_token";

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    @Resource
    public RedisOperator redisOperator;

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


    /**
     * 转换用户信息
     * @param users : 数据库中用户信息
     * @return 处理掉铭感字段的用户信息
     * @date 2020/1/15
     */
    public UsersVO convertUsersVO(Users users) {
        String userToken = UUID.randomUUID().toString().trim();

        redisOperator.set(REDIS_USER_TOKEN + ":" + users.getId(), userToken);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users, usersVO);
        return usersVO;
    }
}
