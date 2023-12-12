package com.ripple.friend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ripple.friend.model.domain.User;
import com.ripple.friend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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

    @Resource
    private RedissonClient redissonClient;

    // 重要用户(id)，此处可以从数据库或者缓存中去动态查询修改
    private List<Long> mainUserList = Arrays.asList(2L, 13L);

    /**
     * 每天缓存推荐用户
     */
    // cron 表达式忘了可以查，此处代表每年月天 23:59:0 时刻执行该方法一次
    @Scheduled(cron = "0 10 17 * * *")
    synchronized public void doCacheRecommendUser() {
        // 分布式锁，根据指定的 Key 获取一个锁对象
        RLock rLock = redissonClient.getLock("friend:precache:lock");
        try {
            // 只有一个线程能获取到锁，并能执行下面
            // 第一个参数0，为等待时间，执行该方法时进行抢锁操作，只会抢一次，抢不到就放弃，不会等待
            // 第二个参数30000，为锁的(redis key)的过期时间，30s 后该锁不存在
            if (rLock.tryLock(0, 30000, TimeUnit.MILLISECONDS)) {
                System.out.println("get Lock: " + Thread.currentThread().getId());
                // todo 分布式锁导致的其它服务器数据不统一的问题或者多个 redis 里的数据不一致
                // 给指定的重要用户提前预热缓存，这样每当这些用户第一次登录进入主页时数据加载速度都会很快
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
        } catch (Exception e) {
            log.error("获取锁失败 redis lock error", e);
        } finally {
            // 只能释放自己的锁，过呢据当前线程 id 来判断
            if (rLock.isHeldByCurrentThread()) {
                // 执行完资源操作后，必须释放该锁
                rLock.unlock();
                System.out.println("un Lock: " + Thread.currentThread().getId());
            }
        }

    }

}
