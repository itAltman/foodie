package me.atm.pojo.vo;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import me.atm.pojo.Items;
import me.atm.pojo.ItemsImg;
import me.atm.pojo.ItemsParam;
import me.atm.pojo.ItemsSpec;

import java.util.List;

/**
 * 商品信息VO
 *
 * @author Altman
 * @date 2019/11/08
 **/
@Data
@Builder
public class ItemInfoVO {
    // 商品详情
    private Items item;
    // 商品主图
    private List<ItemsImg> itemImgList;
    // 商品规格
    private List<ItemsSpec> itemSpecList;
    // 商品参数
    private ItemsParam itemParams;

    @Tolerate
    public ItemInfoVO() {
    }
}
