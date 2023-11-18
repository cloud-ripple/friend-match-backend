package com.ripple.friend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ripple.friend.mapper.TagMapper;
import com.ripple.friend.model.domain.Tag;
import com.ripple.friend.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 花海
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2023-11-15 20:23:50
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Resource
    private TagMapper tagMapper;


}




