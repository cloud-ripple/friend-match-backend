package com.ripple.friend.service;

import com.ripple.friend.model.domain.Tag;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 花海
 * @date 2023/11/23
 * @description
 */
@SpringBootTest
@Slf4j
class TagServiceTest {

    @Resource
    private TagService tagService;

    // 查询所有父标签
    @Test
    void selectParentTags() {
        List<Tag> parentTags = tagService.selectParentTags();
        Assertions.assertNotNull(parentTags); //只要查出来的数据不为空就可以
        log.info("查询所有父标签：{}", parentTags);
    }

    // 根据分类名称来查询该类别下的所有子标签
    @Test
    void selectTagsByCategory() {
        List<Tag> tagsList = tagService.selectTagsByCategory("热门");
        Assertions.assertNotNull(tagsList); //只要查出来的数据不为空就可以
        log.info("根据分类查询所有子标签：{}", tagsList);
    }


}