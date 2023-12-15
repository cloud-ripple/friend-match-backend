package com.ripple.friend.model.request;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 创建队伍请求参数类
 */
@Data
public class TeamAddRequest implements Serializable {


    private static final long serialVersionUID = -2053726492158701743L;
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
     * 入队密码
     */
    private String password;

}
