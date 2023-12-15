package com.ripple.friend.service;

import com.ripple.friend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 花海
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-11-03 22:03:34
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request      请求
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户信息脱敏
     *
     * @param user 数据库中查询到的用户
     * @return 脱敏处理后的用户
     */
    User getSafetyUser(User user);

    /**
     * 用户注销
     *
     * @param request 请求对象
     * @return
     */
    void UserLogout(HttpServletRequest request);

    /**
     * 根据多个标签查询用户- 方式1 数据库查询（实现简单）
     * @param tagNameList 标签列表(前端传入的标签参数)
     * @return 匹配的用户
     */
    List<User> searchUsersByTags(List<String> tagNameList);

    /**
     * 根据多个标签查询用户-方式2 内存计算查询（灵活）在内存中判断是否包含要求的标签
     * @param tagNameList 标签列表(前端传入的标签参数)
     * @return 匹配的用户
     */
    List<User> searchUsersByTagsComputed(List<String> tagNameList);

    /**
     * 根据属性名(字段)更新用户信息
     * @param user 用户
     * @return
     */
    int updateUserByName(User user);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 判断当前登录用户是否为管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);
}
