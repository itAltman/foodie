package me.atm.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import me.atm.common.enums.OrderStatusEnum;
import me.atm.common.enums.YesOrNoEnum;
import me.atm.mapper.OrderItemsMapper;
import me.atm.mapper.OrderStatusMapper;
import me.atm.mapper.OrdersMapper;
import me.atm.pojo.*;
import me.atm.pojo.bo.ShopcartBO;
import me.atm.pojo.bo.SubmitOrderBO;
import me.atm.pojo.vo.MerchantOrdersVO;
import me.atm.pojo.vo.OrderVO;
import me.atm.service.AddressService;
import me.atm.service.ItemService;
import me.atm.service.OrderService;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Altman
 * @date 2019/11/16
 **/
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderItemsMapper orderItemsMapper;

    @Resource
    private OrderStatusMapper orderStatusMapper;

    @Resource
    private AddressService addressService;

    @Resource
    private ItemService itemService;

    @Resource
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(List<ShopcartBO> shopcartList, SubmitOrderBO submitOrderBo) {
        String userId = submitOrderBo.getUserId();
        String itemSpecIds = submitOrderBo.getItemSpecIds();
        String addressId = submitOrderBo.getAddressId();
        Integer payMethod = submitOrderBo.getPayMethod();
        String leftMsg = submitOrderBo.getLeftMsg();
        String orderId = sid.nextShort();
        // 包邮费用设置为0
        Integer postAmount = 0;

        UserAddress address = addressService.queryUserAddres(userId, addressId);

        // 1.新增订单
        Orders parentOrder = new Orders();
        parentOrder.setId(orderId);
        parentOrder.setUserId(userId);
        parentOrder.setReceiverName(address.getReceiver());
        parentOrder.setReceiverMobile(address.getMobile());
        parentOrder.setReceiverAddress(address.getProvince() + " "
                + address.getCity() + " "
                + address.getDistrict() + " "
                + address.getDetail());
        // 这里得从子订单的商品中累加求得
//        orders.setTotalAmount(0);
//        orders.setRealPayAmount(0);
        parentOrder.setPostAmount(0); // 邮费设置为0，代表目前包邮
        parentOrder.setPayMethod(payMethod);
        parentOrder.setLeftMsg(leftMsg);
        parentOrder.setIsComment(YesOrNoEnum.NO.type);
        parentOrder.setIsDelete(YesOrNoEnum.NO.type);
        parentOrder.setCreatedTime(new Date());
        parentOrder.setUpdatedTime(new Date());

        // 2.新增每个规格的商品。子订单表
        Splitter.on(",").split(itemSpecIds);
        List<String> strings = Splitter.on(",").splitToList(itemSpecIds);
        Integer totalAmount = 0;    // 商品原价累计
        Integer realPayAmount = 0;  // 优惠后的实际支付价格累计
        List<ShopcartBO> toBeRemovedShopcatdList = Lists.newArrayList();
        for (String itemSpecId : strings) {
            ShopcartBO cartItem = getBuyCountsFromShopcart(shopcartList, itemSpecId);
            // 整合redis后，商品购买的数量重新从redis的购物车中获取
            // TODO 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = cartItem.getBuyCounts();
            toBeRemovedShopcatdList.add(cartItem);

            // 2.1 根据规格id，查询规格的具体信息，主要获取价格
            ItemsSpec itemSpec = itemService.queryItemSpecById(itemSpecId);
            totalAmount += itemSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemSpec.getPriceDiscount() * buyCounts;

            // 2.2 根据商品id，获得商品信息以及商品图片
            String itemId = itemSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            // 2.3 循环保存子订单数据到数据库
            String subOrderId = sid.nextShort();
            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(itemSpec.getName());
            subOrderItem.setPrice(itemSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);

            // 2.4 在用户提交订单以后，规格表中需要扣除库存
            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
        }

        parentOrder.setTotalAmount(totalAmount);
        parentOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(parentOrder);

        // 3.新增订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        // 4. 构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        // 5. 构建自定义订单vo
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);
        orderVO.setToBeRemovedShopcatdList(toBeRemovedShopcatdList);

        return orderVO;
    }

    /**
     * 从redis中的购物车里获取商品，目的：counts
     * @param shopcartList
     * @param specId
     * @return
     */
    private ShopcartBO getBuyCountsFromShopcart(List<ShopcartBO> shopcartList, String specId) {
        for (ShopcartBO cart : shopcartList) {
            if (cart.getSpecId().equals(specId)) {
                return cart;
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {

        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    public static void main(String[] args) {
        String itemSpecIds = "1,2,3";
        List<String> strings = Splitter.on(",").splitToList(itemSpecIds);
        strings.forEach(s -> System.out.println(s));
    }
}
