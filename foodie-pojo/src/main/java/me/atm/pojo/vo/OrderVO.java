package me.atm.pojo.vo;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import me.atm.pojo.bo.ShopcartBO;

import java.util.List;

@Data
@Builder
public class OrderVO {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;

    private List<ShopcartBO> toBeRemovedShopcatdList;

    @Tolerate
    public OrderVO() {
    }
}