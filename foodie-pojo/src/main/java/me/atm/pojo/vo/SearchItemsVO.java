package me.atm.pojo.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * 用于展示商品搜索列表结果的VO
 */
@Data
@Builder
public class SearchItemsVO {

    private String itemId;
    private String itemName;
    private int sellCounts;
    private String imgUrl;
    private int price;

    @Tolerate
    public SearchItemsVO() {
    }
}
