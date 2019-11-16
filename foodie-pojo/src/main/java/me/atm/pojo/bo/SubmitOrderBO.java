package me.atm.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用于创建订单的BO对象
 */
@Data
@ApiModel(value = "用于创建订单的BO对象 - {\"userId\":\"1911064TAGPGNZ9P\",\"itemSpecIds\":\"cake-1004-spec-2,cake-1006-spec-3\",\"addressId\":\"191112BZY3B99GC0\",\"payMethod\":1,\"leftMsg\":\"bbb\"}")
public class SubmitOrderBO {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "商品规格的id集合。多个用,分隔")
    private String itemSpecIds;
    @ApiModelProperty(value = "收货地址id")
    private String addressId;
    @ApiModelProperty(value = "支付方式。1-微信，2-支付宝。")
    private Integer payMethod;
    @ApiModelProperty(value = "用户留言")
    private String leftMsg;
}
