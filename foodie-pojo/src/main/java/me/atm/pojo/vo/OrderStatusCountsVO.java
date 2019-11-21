package me.atm.pojo.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * 订单状态概览数量VO 
 */
@Data
@Builder
public class OrderStatusCountsVO {
    private Integer waitPayCounts;
    private Integer waitDeliverCounts;
    private Integer waitReceiveCounts;
    private Integer waitCommentCounts;
    @Tolerate
    public OrderStatusCountsVO() {
    }
}