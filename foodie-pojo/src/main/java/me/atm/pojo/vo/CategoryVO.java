package me.atm.pojo.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.util.List;

/**
 * 二级分类VO
 *
 * @author Altman
 * @date 2019/11/07
 **/
@Data
@Builder
public class CategoryVO {
    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;

    // 三级分类vo list
    private List<SubCategoryVO> subCatList;

    @Tolerate
    public CategoryVO() {
    }
}
