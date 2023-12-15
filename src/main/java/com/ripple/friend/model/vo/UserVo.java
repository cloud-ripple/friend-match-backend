package com.ripple.friend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户包装类（脱敏）
 *
 */
@Data
public class UserVo implements Serializable {
    private static final long serialVersionUID = -1188031385130061027L;
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别 （1-男，0-女）
     */
    private Integer gender;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0-正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户角色 0-普通用户 1-管理员
     */
    private Integer userRole;

    /**
     * 星球编号
     */
    private String planetCode;

    /**
     * 用户标签列表 json格式字符串 比如： ["java", "python", "小白"]
     */
    private String tags;

    /**
     * 用户所在地区-城市
     */
    private String area;

    /**
     * 用户自我描述信息
     */
    private String selfDesc;

    /**
     * 粉丝数量
     */
    private Long fansNum;


}