package com.ripple.friend.model.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 修改队伍请求参数类
 */

@Data
public class TeamUpdateRequest implements Serializable {

    private static final long serialVersionUID = 171302955018157713L;

    /**
     * 主键 id
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
     * 过期时间
     */
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 0-公开、1-私有、2-加密
     */
    private Integer status;

    /**
     * 入队密码
     */
    private String password;

}
