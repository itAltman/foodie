package me.atm.pojo.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.Date;

/**
 * 用于展示商品评价的VO
 */
@Data
@Builder
public class ItemCommentVO {

    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nickname;

    @Tolerate
    public ItemCommentVO() {

    }
}
