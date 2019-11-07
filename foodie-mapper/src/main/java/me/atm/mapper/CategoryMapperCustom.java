package me.atm.mapper;

import me.atm.my.mapper.MyMapper;
import me.atm.pojo.Category;
import me.atm.pojo.vo.CategoryVO;
import me.atm.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 一般自定义的 Mapper 会重新创建一个新类。以防万一使用 generator 自动生成之后把自己原来的逻辑 mapper 替换掉了
 */
public interface CategoryMapperCustom extends MyMapper<Category> {
    List<CategoryVO> getSubCatList(Integer rootCatId);

    List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}