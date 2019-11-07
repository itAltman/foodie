package me.atm.pojo.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.List;

/**
 * 推荐商品VO
 *
 * @author Altman
 * @date 2019/11/07
 **/
@Data
@Builder
public class NewItemsVO {
    private Integer rootCatId;
    private String rootCatName;
    private String slogan;
    private String catImage;
    private String bgColor;
    private List<SimpleItemVO> simpleItemList;

    @Tolerate
    public NewItemsVO() {
    }
}
