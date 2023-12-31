package com.ripple.friend.controller;


import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.ripple.friend.common.BaseResponse;
import com.ripple.friend.common.ErrorCode;
import com.ripple.friend.common.ResultUtils;
import com.ripple.friend.exception.BusinessException;
import com.ripple.friend.model.domain.Tag;
import com.ripple.friend.service.TagService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author 花海
 * @date 2023/11/23
 * @description
 */

@RestController
@RequestMapping
@Slf4j
public class TagController {

    @Resource
    private TagService tagService;

    // 获取所有父标签-即所有分类
    @ApiOperation("获取所有父标签分类")
    @GetMapping("/parentTags")
    public BaseResponse<List<Tag>> selectParentTags() {
        log.info("查询所有父标签分类..");
        return ResultUtils.success(tagService.selectParentTags());
    }

    // 根据分类名称来查询该类别下的所有子标签
    @GetMapping("/categoryTags")
    @ApiOperation("根据分类名查询所有子标签")
    public BaseResponse<List<Tag>> selectTagsByCategory(String category) {
        if (StringUtils.isBlank(category)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "标签分类参数为空");
        }
        log.info("根据分类名查询子标签: {}", category);
        List<Tag> tagsList = tagService.selectTagsByCategory(category);
        return ResultUtils.success(tagsList);
    }

    // 查询所有子标签，isParent字段  0-不是父标签，1-父标签
    @GetMapping("/childTags")
    @ApiOperation("查询所有子标签")
    @ApiImplicitParam(name = "isParent字段  0-不是父标签，1-父标签")
    public BaseResponse<List<Tag>> selectChildTags() {
        log.info("查询所有子标签..");
        return ResultUtils.success(tagService.selectChildTags());
    }

}
