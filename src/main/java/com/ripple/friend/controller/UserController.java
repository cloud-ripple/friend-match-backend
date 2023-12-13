package com.ripple.friend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ripple.friend.common.BaseResponse;
import com.ripple.friend.common.ErrorCode;
import com.ripple.friend.common.ResultUtils;
import com.ripple.friend.exception.BusinessException;
import com.ripple.friend.model.domain.User;
import com.ripple.friend.model.request.UserLoginRequest;
import com.ripple.friend.model.request.UserRegisterRequest;
import com.ripple.friend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ripple.friend.constant.UserConstant.ADMIN_ROLE;
import static com.ripple.friend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 花海
 * @date 2023/11/5
 * @description 用户接口
 */

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR, "");
        }
        log.info("用户注册请求参数，{}", userRegisterRequest);
        // 前端传递过来的所有请求参数封装在 UserRegisterRequest 实体类中
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        // 简单校验，如果这3个请求参数有一个为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return ResultUtils.error(ErrorCode.NULL_ERROR, "");
        }
        // 调用服务层注册方法
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.NULL_ERROR, "数据为空");
        }
        log.info("用户登录请求参数：{}", userLoginRequest);
        // 前端传递过来的 账户、密码 参数封装在 UserLoginRequest 实体类中，而Http请求对象封装在 HttpServletRequest
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 简单校验，如果这2个请求参数有一个为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public void userLogout(HttpServletRequest request) {
        log.info("退出登录...,{}", request.getSession().getAttribute(USER_LOGIN_STATE));
        if (request == null) {
            return;
        }
        userService.UserLogout(request);
    }


    // 获取当前登录用户
    @GetMapping("/current")
    public User getCurrentUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return user;
    }

    // 根据用户名模糊查询
    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        log.info("查询用户参数 username：{}", username);
        // 仅管理员可查询
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "仅管理员可查询"); //如果不是管理员返回空列表
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // isNotBlank 判断该字符串长度是否为0、为空
        if (StringUtils.isNotBlank((username))) {
            queryWrapper.like("username", username); //模糊查询
        }

        // 把用户敏感信息过滤掉，不要返回给前端
        List<User> userList = userService.list(queryWrapper); //从数据库中查询到的用户
        List<User> safetyUsersList = new ArrayList<>(); // 脱敏处理后的用户

        for (User user : userList) {
            safetyUsersList.add(userService.getSafetyUser(user));
        }
        return safetyUsersList;
    }

    // 根据id删除用户
    @PostMapping("/delete")
    public boolean deleteUser(long id, HttpServletRequest request) {
        log.info("删除用户参数 id：{}", id);
        // 仅管理员可删除
        if (!isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "仅管理员可删除");
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该删除的用户id不合法");
        }
        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     *
     * @param request 请求对象
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        User userObj = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null || userObj.getUserRole() != ADMIN_ROLE) {
            throw new BusinessException(ErrorCode.NO_AUTH, "用户未登录或非管理员");
        }
        return true;
    }

    // 根据多个标签搜索用户 - 内存计算方式
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(name = "tagNameList") @RequestBody List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "标签查询参数为空");
        }
        log.info("根据多个标签搜索用户：{}", tagNameList);
        List<User> userList = userService.searchUsersByTagsComputed(tagNameList);
        return ResultUtils.success(userList);
    }

    // 根据属性名(字段)更新用户信息
    @PostMapping("/update")
    public BaseResponse updateUserById(@RequestBody User user) {
        if (user == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "更新的用户参数为空");
        }
        log.info("更新用户：{}", user);
        boolean update = userService.updateById(user);
        return update == true ? ResultUtils.success(null) : ResultUtils.error(ErrorCode.SYSTEM_ERROR, "用户信息更新失败");
    }

    // 主页推荐用户 - 相似度匹配
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        //设计缓存 key，让不同的用户看到的数据不同
        String redisKey = String.format("friend:user:recommend:%s", loginUser.getId());
        // 先判断如有缓存，直接读缓存用户数据，就不用从数据库中查询了，还需要判断缓存中的用户分页当前页码是否和下一次请求的页码相同，因为主页用了分页组件
        // 否则只会返回显示第一次缓存的分页用户(第1页)
        Page<User> userPage = (Page<User>) redisTemplate.opsForValue().get(redisKey);
        if (userPage != null && userPage.getCurrent() == pageNum) {
            return ResultUtils.success(userPage);
        }
        // 数据库中查询用户，分页查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        // 把查询到的用户写入缓存，注意key过期时间 40s后数据就不存在了，如果不设置缓存过期时间，会导致缓存只进不出，无限增加
        /*
            redis 内存不能无限缓存数据，底层有一个内存溢出淘汰策略，会删除掉一些重要数据，有不确定性。
            所以在写入缓存数据的时候必须设置过期时间
         */
        try {
            redisTemplate.opsForValue().set(redisKey, userPage, 40000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("分页查询用户写入缓存出错了 redis set key error", e);
        }

        return ResultUtils.success(userPage);
    }

}
