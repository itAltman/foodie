package me.atm.pojo.bo.center;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Altman
 * @date 2019/11/21
 **/
@Data
@Builder
@ApiModel(value = "子订单评价对象")
public class OrderItemsCommentBO {
    @ApiModelProperty(value = "评论id", required = false)
    private String commentId;
    @ApiModelProperty(value = "商品id", required = false)
    private String itemId;
    @ApiModelProperty(value = "商品名称", required = false)
    private String itemName;
    @ApiModelProperty(value = "商品规格id", required = false)
    private String itemSpecId;
    @ApiModelProperty(value = "商品规格名称", required = false)
    private String itemSpecName;
    @ApiModelProperty(value = "评价等级", required = true)
    @NotNull(message = "评价等级 不能为空")
    private Integer commentLevel;
    @ApiModelProperty(value = "评价内容", required = true)
    @NotBlank(message = "评价内容 不能为空")
    private String content;

    @Tolerate
    public OrderItemsCommentBO() {
    }
}
