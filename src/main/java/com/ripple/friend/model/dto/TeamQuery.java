package com.ripple.friend.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ripple.friend.common.PageRequest;
import lombok.Data;

import java.io.Serializable;


/**
 * @description 队伍信息查询类
 * <p>
 * 为什么需要请求参数封装类？
 * 1. 请求参数名称和实体类不一样
 * 2. 有一些参数用不到，如果要自动生成接口文档，会增加理解成本
 * 3. 可能有些字段需要隐藏，不能返回给前端、或者有些字段、方法是不关心的
 */
@Data
public class TeamQuery extends PageRequest implements Serializable {


    private static final long serialVersionUID = -2914896010669653828L;
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
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
     * 创建人id
     */
    private Long userId;

    /**
     * 0-公开、1-私有、2-加密
     */
    private Integer status;
}
