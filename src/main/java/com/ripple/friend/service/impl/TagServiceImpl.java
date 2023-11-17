package com.ripple.friend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ripple.friend.common.ErrorCode;
import com.ripple.friend.exception.BusinessException;
import com.ripple.friend.model.domain.Tag;
import com.ripple.friend.model.domain.User;
import com.ripple.friend.service.TagService;
import com.ripple.friend.mapper.TagMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

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




