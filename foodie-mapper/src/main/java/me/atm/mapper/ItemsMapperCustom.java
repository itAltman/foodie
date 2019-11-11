package me.atm.mapper;

import me.atm.my.mapper.MyMapper;
import me.atm.pojo.Items;
import me.atm.pojo.vo.SearchItemsVO;
import me.atm.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom extends MyMapper<Items> {
    List<SearchItemsVO> searchItemsByKeywords(@Param("paramsMap") Map<String, Object> map);

    List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);

    List<ShopcartVO> queryItemsBySpecIds(@Param("itemSpecIdList") List<String> itemSpecIdList);
}