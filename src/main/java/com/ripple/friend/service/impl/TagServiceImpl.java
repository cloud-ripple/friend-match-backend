package com.ripple.friend.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ripple.friend.common.ErrorCode;
import com.ripple.friend.exception.BusinessException;
import com.ripple.friend.mapper.TagMapper;
import com.ripple.friend.model.domain.Tag;
import com.ripple.friend.service.TagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 花海
 * @description 针对表【tag(标签)】的数据库操作Service实现
 * @createDate 2023-11-15 20:23:50
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    @Resource
    private TagMapper tagMapper;


    // 查询所有父标签
    @Override
    public List<Tag> selectParentTags() {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper = queryWrapper.eq("isParent", 1); // 0-不是父标签，1-是父标签
        // select * from tag where isParent = 1
        List<Tag> parentTagsList = tagMapper.selectList(queryWrapper);
        // 如果没有查询到，返回新的集合(避免空指针)
        if (CollectionUtils.isEmpty(parentTagsList)) {
            return new ArrayList<Tag>();
        }
        return parentTagsList;
    }

    // 根据分类名称来查询该类别下的所有子标签
    @Override
    public List<Tag> selectTagsByCategory(String category) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        // 根据category，isParent 字段从数据库中查询所有数据，0-不是父标签，1-是父标签
        queryWrapper.eq("category", category); // select * from tag where category = '' and isParent = 0
        queryWrapper.eq("isParent", 0);

        List<Tag> tagsList = tagMapper.selectList(queryWrapper);
        // 如果没有查询到，返回新的集合(避免空指针)
        if (CollectionUtils.isEmpty(tagsList)) {
            return new ArrayList<Tag>();
        }

        return tagsList;
    }

    // 查询所有子标签-即不是父标签的标签，isParent字段  0-不是父标签，1-父标签
    @Override
    public List<Tag> selectChildTags() {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        // 根据cisParent 字段从数据库中查询
        queryWrapper.eq("isParent", 0); // select * from tag where isParent = 0
        List<Tag> tagsList = tagMapper.selectList(queryWrapper);
        // 如果没有查询到，返回新的集合(避免空指针)
        if (CollectionUtils.isEmpty(tagsList)) {
            return new ArrayList<Tag>();
        }

        return tagsList;
    }
}




