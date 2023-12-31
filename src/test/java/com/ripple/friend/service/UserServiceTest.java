package com.ripple.friend.service;

import com.ripple.friend.model.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author boboking
 * @date 2023/11/3
 * @description 用户服务测试
 */
@SpringBootTest
@Slf4j
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("bobo");
        user.setUserAccount("123");
        user.setAvatarUrl("https://c-ssl.duitang.com/uploads/blog/202201/23/20220123222213_2899a.jpeg");
        user.setGender(0);
        user.setUserPassword("123");
        user.setPhone("123456");
        user.setEmail("789");

        boolean result = userService.save(user);
        System.out.println("用户id：" + user.getId());
        // 断言
        Assertions.assertTrue(result);
    }

    // 用户注册测试
    @Test
    void userRegister() {
        String userAccount = "ripple";
        String userPassword = "";
        String checkPassword = "123456";
        String planetCode = "1";
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "bo"; // 测试账户长度不小于 4 位数
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "ripple"; // 测试密码位数 不小于8
        userPassword = "123456";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "ripp le"; // 测试特殊空格字符
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "ripple"; // 测试密码和校验密码是否一致
        userPassword = "12345678";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);

        userAccount = "yupi"; // 测试正常密码
        userPassword = "123456789";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        Assertions.assertEquals(-1, result);
    }

    // 测试根据标签查询用户
    @Test
    void searchUsersByTags() {
        List<String> tagNameList = Arrays.asList("java", "python","c++");
        List<User> userList = userService.searchUsersByTags(tagNameList);
        Assertions.assertNotNull(userList); //只要不为空就可以
        log.info("根据标签列表参数查询到的用户：{}",userList);
    }
}