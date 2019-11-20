package me.atm.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Date;
import java.util.List;

/**
 * 订单 VO
 *
 * @author Altman
 * @date 2019/11/11
 **/
@ApiModel(value = "订单VO", description = "用户中心 - 订单管理的订单信息")
@Data
@Builder
public class MyOrdersVO {
    @ApiModelProperty(value = "订单id", example = "1911167RGCNA1WX4")
    private String orderId;
    @ApiModelProperty(value = "成交时间", notes = "返回时间")
    private Date createdTime;
    @ApiModelProperty(value = "支付方式", notes = "返回数值。1-微信，2-支付宝")
    private Integer payMethod;
    @ApiModelProperty(value = "合计价格", notes = "10000代表100元")
    private Integer realPayAmount;
    @ApiModelProperty(value = "运费", notes = "10000代表100元")
    private Integer postAmount;
    @ApiModelProperty(value = "是否评价", notes = "返回数值。1：已评价，0：未评价")
    private Integer isComment;
    @ApiModelProperty(value = "订单状态", notes = "返回数值。10-待付款，20-已付款、待发货，30-已发货、待收货，40-交易成功，50-交易关闭")
    private Integer orderStatus;
    @ApiModelProperty(value = "子订单信息")
    private List<MySubOrderItemVO> subOrderItemList;

    @Tolerate
    public MyOrdersVO() {
    }
}
