package me.atm.pojo.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

/**
 * 三级分类VO
 *
 * @author Altman
 * @date 2019/11/07
 **/
@Data
@Builder
public class SubCategoryVO {
    private Integer subId;
    private String subName;
    private String subType;
    private Integer subFatherId;

    @Tolerate
    public SubCategoryVO() {
    }
}
