package me.atm.pojo.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * 用于展示商品评价数量的vo
 */
@Data
@Builder
public class CommentLevelCountsVO {
    public Integer totalCounts;
    public Integer goodCounts;
    public Integer normalCounts;
    public Integer badCounts;

    @Tolerate
    public CommentLevelCountsVO() {

    }
}
