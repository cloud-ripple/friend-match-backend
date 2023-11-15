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