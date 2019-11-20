package me.atm.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Date;

/**
 * 用户中心 - 我的订单列表 子订单
 *
 * @author Altman
 * @date 2019/11/19
 **/
@ApiModel(value = "子订单VO", description = "用户中心 - 订单管理的子订单信息")
@Data
@Builder
@AllArgsConstructor
public class MySubOrderItemVO {
    @ApiModelProperty(value = "商品id")
    private String itemId;
    @ApiModelProperty(value = "商品图片")
    private String itemImg;
    @ApiModelProperty(value = "商品名称")
    private String itemName;
    @ApiModelProperty(value = "商品规格名称")
    private String itemSpecName;
    @ApiModelProperty(value = "购买数量")
    private Integer buyCounts;
    @ApiModelProperty(value = "单价")
    private Integer price;

    @Tolerate
    public MySubOrderItemVO() {
    }
}
