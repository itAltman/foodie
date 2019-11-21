package me.atm.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Date;

/**
 * 评价 VO
 *
 * @author Altman
 * @date 2019/11/21
 **/
@ApiModel(value = "我的评价VO", description = "我的评价")
@Data
@Builder
public class MyCommentVO {
    @ApiModelProperty(value = "评价id")
    private String commentId;
    @ApiModelProperty(value = "成交时间")
    private Date createdTime;
    @ApiModelProperty(value = "评价内容")
    private String content;
    @ApiModelProperty(value = "商品id")
    private String itemId;
    @ApiModelProperty(value = "商品名称")
    private String itemName;
    @ApiModelProperty(value = "商品规格名称")
    private String specName;
    @ApiModelProperty(value = "商品图片url")
    private String itemImg;

    @Tolerate
    public MyCommentVO() {
    }
}
