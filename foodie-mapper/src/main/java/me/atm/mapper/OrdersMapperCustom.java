package me.atm.mapper;

import me.atm.my.mapper.MyMapper;
import me.atm.pojo.Orders;
import me.atm.pojo.vo.MyOrdersVO;
import me.atm.pojo.vo.MySubOrderItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom extends MyMapper<Orders> {
    List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> paramsMap);

//    MySubOrderItemVO getMySubOrders(String orderId);
}