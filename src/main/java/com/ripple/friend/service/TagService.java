package com.ripple.friend.service;

import com.ripple.friend.model.domain.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
* @author 花海
* @description 针对表【tag(标签)】的数据库操作Service
* @createDate 2023-11-15 20:23:50
*/
public interface TagService extends IService<Tag> {

    /**
     * 查询所有父标签-即所有分类  0-不是父标签，1-是父标签
     * @return
     */
    List<Tag> selectParentTags();

    /**
     * 根据分类名称来查询该类别下的所有子标签
     *
     * @param category 标签分类
     * @return 该类别下的所有子标签
     */
    List<Tag> selectTagsByCategory(String category);

    /**
     * 查询所有子标签-即不是父标签的标签，isParent字段  0-不是父标签，1-父标签
     * @return 所有子标签
     */
    List<Tag> selectChildTags();

}
