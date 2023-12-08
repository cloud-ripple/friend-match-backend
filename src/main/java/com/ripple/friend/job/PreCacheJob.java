package com.ripple.friend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ripple.friend.model.domain.User;
import com.ripple.friend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description 预热缓存任务
 */

@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 重要用户(id)，此处可以从数据库或者缓存中去动态查询修改
    private List<Long> mainUserList = Arrays.asList(2L, 13L);

    /**
     * 每天缓存推荐用户
     */
    // cron 表达式忘了可以查，此处代表每年月天 23:59:0 时刻执行该方法一次
    @Scheduled(cron = "0 10 17 * * *")
    public void doCacheRecommendUser() {
        // 给用户推荐
        for (Long userId : mainUserList) {
            //设计缓存 key，让不同的用户看到的数据不同
            String redisKey = String.format("friend:user:recommend:%s", userId);
            // 数据库中查询用户，分页查询
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            Page<User> userPage = userService.page(new Page<>(1, 20), queryWrapper);
            // 把查询到的用户写入缓存，注意key过期时间 40s后数据就不存在了
            try {
                redisTemplate.opsForValue().set(redisKey, userPage, 40000, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                log.error("分页查询用户写入缓存出错了 redis set key error", e);
            }
        }

    }

}
