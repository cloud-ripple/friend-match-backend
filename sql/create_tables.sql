create table if not exists user
(
    id           bigint auto_increment comment '主键ID'
        primary key,
    username     varchar(256) default ''                null comment '用户昵称',
    userAccount  varchar(256)                           null comment '账号',
    avatarUrl    varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别',
    userPassword varchar(512)                           not null comment '密码',
    phone        varchar(128)                           null comment '电话',
    email        varchar(512)                           null comment '邮箱',
    userStatus   int          default 0                 not null comment '状态 0-正常',
    createTime   datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    userRole     int          default 0                 not null comment '用户角色 0-普通用户 1-管理员',
    planetCode   varchar(512)                           null comment '星球编号',
    tags         varchar(1024)                          null comment '标签列表'
)
    comment '用户' charset = utf8;


create table if not exists tag
(
    id         bigint auto_increment comment '主键ID'
        primary key,
    tagName    varchar(256)                       null comment '标签名',
    userId     bigint                             null comment '用户id',
    parentId   bigint                             null comment '父标签id',
    isParent   tinyint                            null comment '0-不是父标签，1-父标签',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '标签' charset = utf8;

-- 创建索引
create unique index tag_index_tagName
    on tag (tagName)
    comment '标签名唯一索引';
create index tag_index_userId
    on tag (userId)
    comment '用户id索引';

insert into user
values (null, '韩立', 'hanli',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/076bd9ed-6d76-4a27-ad8d-72bbd837eb13.jpeg',
        0, '11111111', null, null, 0, now(), now(), 0, 1, 4, '["网上冲浪","萌新"]', '上海市', '修仙', 0),
       (null, '历飞雨', 'lifeiyu',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/09876f4f-f931-4f44-a189-e8fc5a68e28b.jpeg',
        0, '11111111', null, null, 0, now(), now(), 0, 1, 5, '["大二","跑步","前端"]', '深圳市', '暂时没有个性签名', 0),
       (null, '钟吾', 'zhongwu',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/0b52f8e9-5d81-4a62-b4bf-b248aff63d3d.jpeg',
        1, '11111111', null, null, 0, now(), now(), 0, 1, 6, '["小白"]', '上海市', '暂时没有个性签名', 0),
       (null, '李化元', 'lihuayuan',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/0edc4e29-bdad-4234-9426-11d87622fcf8.jpeg',
        1, '11111111', null, null, 0, now(), now(), 0, 1, 7, '["网上冲浪","羽毛球"]', '苏州市', '暂时没有个性签名', 0),
       (null, '王禅', 'hanli',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/076bd9ed-6d76-4a27-ad8d-72bbd837eb13.jpeg',
        1, '11111111', null, null, 0, now(), now(), 0, 1, 8, '["网上冲浪","python"]', '南京市', '暂时没有个性签名', 0),
       (null, '云露', 'hanli',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/22a55abb-f1c0-4651-ac71-d6d78589bdaa.jpeg',
        0, '11111111', null, null, 0, now(), now(), 0, 1, 9, '["网上冲浪","凡人修仙传"]', '广州', '暂时没有个性签名',
        0),
       (null, '陈巧倩', 'hanli',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/34840766-5364-4496-9d02-5a93bc3dedc5.jpeg',
        0, '11111111', null, null, 0, now(), now(), 0, 1, 10, '["网上冲浪","武动乾坤","小白"]', '北京市',
        '暂时没有个性签名', 0);

