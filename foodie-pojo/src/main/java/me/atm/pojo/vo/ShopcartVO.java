package me.atm.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 购物车商品 VO
 *
 * @author Altman
 * @date 2019/11/11
 **/
@ApiModel(value = "购物车商品VO", description = "刷新购物车中的商品对象")
@Data
@Builder
public class ShopcartVO {
    @ApiModelProperty(value = "商品id", required = true, example = "cake-1001")
    private String itemId;
    @ApiModelProperty(value = "商品主图url", required = true, example = "http://xxx.png")
    private String itemImgUrl;
    @ApiModelProperty(value = "商品名称", required = true)
    private String itemName;
    @ApiModelProperty(value = "规格id", required = true)
    private String specId;
    @ApiModelProperty(value = "规格名称", required = true)
    private String specName;
    @ApiModelProperty(value = "商品折扣价", required = true)
    private String priceDiscount;
    @ApiModelProperty(value = "商品原价", required = true)
    private String priceNormal;
}
