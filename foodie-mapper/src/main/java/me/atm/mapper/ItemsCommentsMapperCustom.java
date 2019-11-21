package me.atm.mapper;

import me.atm.my.mapper.MyMapper;
import me.atm.pojo.ItemsComments;
import me.atm.pojo.vo.ItemCommentVO;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    List<ItemCommentVO> queryItemComments(Map<String, Object> map);

    void saveComments(Map<String, Object> map);

    List<ItemsComments> queryMyComments(Map<String, Object> map);
}