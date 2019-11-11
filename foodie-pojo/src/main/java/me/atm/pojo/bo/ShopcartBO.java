package me.atm.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 购物车商品 BO
 *
 * @author Altman
 * @date 2019/11/11
 **/
@ApiModel(value = "购物车商品BO", description = "添加商品到购物车中需要使用到的前端请求对象")
@Data
public class ShopcartBO {
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
    @ApiModelProperty(value = "购物车的数量", required = true)
    private Integer buyCounts;
    @ApiModelProperty(value = "商品折扣价", required = true)
    private String priceDiscount;
    @ApiModelProperty(value = "商品原价", required = true)
    private String priceNormal;
}
