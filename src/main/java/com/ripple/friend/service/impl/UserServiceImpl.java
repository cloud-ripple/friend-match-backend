package com.ripple.friend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ripple.friend.common.ErrorCode;
import com.ripple.friend.exception.BusinessException;
import com.ripple.friend.model.domain.User;
import com.ripple.friend.service.UserService;
import com.ripple.friend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ripple.friend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 花海
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2023-11-03 22:03:34
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;
    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "bobo"; //（加密配方）搅屎棍，让密码更加复杂 知道盐才能解密，防解密


    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1. 校验 isAnyBlank方法用于校验字符串是否为 null 、空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.NULL_ERROR, "数据为空"); //有一项不满足校验就返回-1
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度过短");
        }
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号不符合要求");
        }
        //账户不能重复，查询数据库中的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        // 如果该账户已经有人注册了
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号已经存在");
        }

        //星球编号不能重复，二次查询数据库中的用户
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("planetCode", planetCode);
        count = userMapper.selectCount(queryWrapper);
        // 如果该星球编号的账户已经有人注册了
        if (count > 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "星球编号已存在");
        }

        //账户不能包含特殊字符
        // 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String validPattern = "[` ~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符");
        }
        //密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }

        // 2. 加密密码
        // 使用Spring提供的加密工具类，得到加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 插入用户数据到数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据库插入数据失败");
        }
        return user.getId();
        //如果上面没有插入成功，那么用户 主键id 为 null，此时返回 null ，而 UserService 接口中的方该法返回值是 long
        // 会导致数据类型 自动拆箱失败，报错
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验 isAnyBlank方法用于校验字符串是否为 null 、空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            // todo 修改为自定义异常
            throw new BusinessException(ErrorCode.NULL_ERROR, "参数为空");//有一项不满足校验就返回
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        //账户不能包含特殊字符
        // String regEx ="[^a-zA-Z0-9]";    // 只允许字母和数字
        String validPattern = "[` ~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) { // 如果含有特殊字符
            throw new BusinessException(ErrorCode.PARAMS_ERROR, " 账号不能有特殊字符");
        }
        // 2. 加密密码
        // 使用Spring提供的加密工具类，得到加密密码
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3. 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount); //用户输入的账户要等于数据表中的字段
        queryWrapper.eq("userPassword", encryptPassword); //用户输入的密码要等于加密后的密码

        User user = userMapper.selectOne(queryWrapper); //以上作为条件去数据库查询用户
        // 如果用户不存在
        if (null == user) {
            log.info("user login failed, userAccount cannot match userPassword"); //match(匹配)
            throw new BusinessException(ErrorCode.PARAMS_ERROR, " 账号或密码错误");
        }
        // 4. 把该用户信息脱敏
        User safetyUser = getSafetyUser(user);
        // 5. 记录用户的登录状态,将其存放到服务器上(session)，当前只是单个，后面可以做成分布式
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 用户信息脱敏
     *
     * @param user 数据库中查询的用户
     * @return 脱敏处理后的用户
     */
    @Override
    public User getSafetyUser(User user) {
        if (user == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender()); //密码就不用返回了
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime()); // 更新时间、是否被删除不用返回了
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setTags(user.getTags());
        safetyUser.setArea(user.getArea());
        safetyUser.setSelfDesc(user.getSelfDesc());
        safetyUser.setFansNum(user.getFansNum());

        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request 请求对象
     */
    @Override
    public void UserLogout(HttpServletRequest request) {
        // 移除 session 中的登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
    }

    /**
     * 根据多个标签查询用户- 方式1 数据库查询（实现简单）
     */
    @Override
    public List<User> searchUsersByTags(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "");
        }
        // 用于存放脱敏后的用户（返回给前端）
        List<User> safetyUserList = new ArrayList<>();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 拼接 and 查询  like '%java%' and like '%python%
        for (String tagName : tagNameList) {
            queryWrapper = queryWrapper.like("tags", tagName); // like查询叠加
        }
        List<User> userList = userMapper.selectList(queryWrapper);
        //对数据库中查询到的用户进行脱敏处理
        userList.forEach(user -> {
            safetyUserList.add(getSafetyUser(user));
        });

        return safetyUserList;
    }

    /**
     * 根据多个标签查询用户 -方式2 内存计算查询（灵活）在内存中判断是否包含要求的标签
     */
    @Override
    public List<User> searchUsersByTagsComputed(List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "根据标签查询用户-tagNameList为空");
        }
        // 用于存放脱敏后的用户（返回给前端）
        List<User> safetyUserList = new ArrayList<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper); //先查询所有用户
        Gson gson = new Gson();

        // 遍历数据库中查询到的用户
        for (User user : userList) {
            // 获取该用户的所有标签
            String tagsStr = user.getTags(); //标签是个 json字符串 -> ["java", "前端", "python"]
            // 标签判空（可能当前用户没有标签）
            if (StringUtils.isBlank(tagsStr)) {
                continue; // 继续遍历下一个用户，下方代码不再执行
            }
            // 把 json 字符串转化成 java 对象（反序列化），得到的标签放在集合中(标签不能重复) ["java", "前端", "python"] => {"java", "前端", "python"}
            Set<String> tagSet = gson.fromJson(tagsStr, new TypeToken<Set<String>>() {
            }.getType());
            // 对集合tagSet进行判空处理（也可以用if判断）
            // ofNullable方法接收一个可能为空的对象，如果它为空就取后面指定的默认值(创建一个)
            tagSet = Optional.ofNullable(tagSet).orElse(new HashSet<>());
            // 对标签集合与传入进来的标签列表进行对比 看是否每个标签都存在于这个标签集合(当前用户拥有的)
            if (isContainsTagSet(tagSet, tagNameList)) { //如果满足条件
                safetyUserList.add(getSafetyUser(user)); //匹配到的用户，添加到列表准备返回给前端，注意需要对用户脱敏处理
            }
        }

        return safetyUserList;
    }

    // 根据属性名(字段)更新用户信息
    @Override
    public int updateUserByName(User user) {
        int rows = userMapper.updateById(user);
        return rows;
    }

    /**
     * 判断标签列表tagNameList中的每个标签 是否都存在于tagSet标签集合中
     *
     * @param tagSet      用户的所有标签
     * @param tagNameList 传入的标签列表(查询参数)
     * @return false-不满足，true-满足
     */
    public boolean isContainsTagSet(Set<String> tagSet, List<String> tagNameList) {
        // 对标签集合与传入进来的标签列表进行对比
        for (String tagName : tagNameList) {
            // 如果该传入的标签列表中的标签并不存在于这个标签集合(当前用户拥有的)
            if (!tagSet.contains(tagName)) {
                return false;
            }
        }
        return true; //代表(查询参数)在标签集合中都存在
    }


}




