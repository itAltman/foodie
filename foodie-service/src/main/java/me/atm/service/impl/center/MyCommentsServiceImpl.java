package me.atm.service.impl.center;

import com.github.pagehelper.PageHelper;
import me.atm.common.enums.YesOrNoEnum;
import me.atm.common.utils.PagedGridResult;
import me.atm.mapper.*;
import me.atm.pojo.ItemsComments;
import me.atm.pojo.OrderItems;
import me.atm.pojo.OrderStatus;
import me.atm.pojo.Orders;
import me.atm.pojo.bo.center.OrderItemsCommentBO;
import me.atm.service.center.MyCommentsService;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyCommentsServiceImpl extends BaseService implements MyCommentsService {

    @Resource
    private OrderItemsMapper orderItemsMapper;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderStatusMapper orderStatusMapper;

    @Resource
    private Sid sid;

    @Resource
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    public List<OrderItems> getPendingComment(String orderId) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);
        return orderItemsMapper.select(orderItems);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList) {
        // 保存评价信息
        commentList.forEach(bo -> bo.setCommentId(sid.nextShort()));
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("commentList", commentList);
        itemsCommentsMapperCustom.saveComments(map);

        // 修改订单 评价字段
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(YesOrNoEnum.YES.type);
        ordersMapper.updateByPrimaryKeySelective(orders);

        // 修改订单状态
        OrderStatus os = new OrderStatus();
        os.setOrderId(orderId);
        os.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(os);
    }

    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("userId",userId);
        List<ItemsComments> itemsCommentList = itemsCommentsMapperCustom.queryMyComments(map);
        return setterPagedGrid(itemsCommentList, page);
    }
}
