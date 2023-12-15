package com.ripple.friend.model.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 队伍和用户信息封装类（脱敏）
 */

@Data
public class TeamUserVo implements Serializable {

    private static final long serialVersionUID = -9021230642438819959L;
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 队伍名
     */
    private String teamName;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 创建人id
     */
    private Long userId;

    /**
     * 0-公开、1-私有、2-加密
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 队伍创建人信息
     */
    UserVo createUser;

}
