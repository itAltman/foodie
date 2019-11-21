package me.atm.service.center;

import me.atm.common.utils.PagedGridResult;
import me.atm.pojo.OrderItems;
import me.atm.pojo.bo.center.OrderItemsCommentBO;

import java.util.List;

/**
 * @author Altman
 * @date 2019/11/21
 **/
public interface MyCommentsService {
    /**
     * 查询待评价的子订单
     *
     * @param orderId : 订单id
     * @return
     * @author Altman
     * @date 2019-11-21
     */
    List<OrderItems> getPendingComment(String orderId);

    /**
     * 保存评论列表
     *
     * @param orderId     : 订单id
     * @param userId      : 用户id
     * @param commentList : 子订单对象集合
     * @return
     * @date: 2019-11-21
     */
    void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);

    /**
     * 查询我的评价列表
     *
     * @param userId   :
     * @param page     :
     * @param pageSize :
     * @return
     * @date: 2019-11-21
     */
    PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);
}
