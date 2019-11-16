package me.atm.service;

import me.atm.common.utils.PagedGridResult;
import me.atm.pojo.*;
import me.atm.pojo.bo.SubmitOrderBO;
import me.atm.pojo.vo.CommentLevelCountsVO;
import me.atm.pojo.vo.ShopcartVO;

import java.util.List;

/**
 * @author Altman
 * @date 2019/11/16
 **/
public interface OrderService {

    /**
     * 创建订单
     *
     * @param submitOrderBo : 提交订单对象
     * @return 订单编号
     * @author Altman
     * @date 2019-11-16
     */
    String createOrder(SubmitOrderBO submitOrderBo);

    /**
     * 创建订单
     *
     * @param orderId : 订单id
     * @return 订单状态对象
     * @author Altman
     * @date 2019-11-16
     */
    OrderStatus queryOrderStatusInfo(String orderId);
}
