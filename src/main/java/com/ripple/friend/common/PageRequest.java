package com.ripple.friend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @description 通用分页请求参数类
 */

@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 6687647558595851237L;

    /**
     * 页面大小
     */
    protected int pageSize = 10;

    /**
     * 当前页码
     */
    protected int pageNum = 1;

}
