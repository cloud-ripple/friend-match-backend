create table if not exists user
(
    id           bigint auto_increment comment '主键ID'
        primary key,
    username     varchar(256) default ''                null comment '用户昵称',
    userAccount  varchar(256)                           null comment '账号',
    avatarUrl    varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别 (1-男，0-女)',
    userPassword varchar(512)                           not null comment '密码',
    phone        varchar(128)                           null comment '电话',
    email        varchar(512)                           null comment '邮箱',
    userStatus   int          default 0                 not null comment '状态 0-正常',
    createTime   datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    userRole     int          default 0                 not null comment '用户角色 0-普通用户 1-管理员',
    planetCode   varchar(512)                           null comment '星球编号',
    tags         varchar(1024)                          null comment '标签 json 列表',
    area         varchar(512)                           null comment '用户所在地区-城市',
    selfDesc     varchar(255) default ''                null comment '用户自我描述信息',
    fansNum      bigint       default 0                 null comment '粉丝数量'
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

insert into user
values (null, '假用户', 'test1',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/19.jpeg',
        0, '11111111', null, null, 0, now(), now(), 0, 1, 17, '["网上冲浪","萌新"]', '上海市', '哈哈哈', 0),
       (null, '假用户', 'test2',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/19.jpeg',
        0, '11111111', null, null, 0, now(), now(), 0, 1, 18, '["大二","跑步","前端"]', '深圳市', '暂时没有个性签名',
        0),
       (null, '假用户', 'test3',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/19.jpeg',
        1, '11111111', null, null, 0, now(), now(), 0, 1, 19, '["小白","男"]', '上海市', '暂时没有个性签名', 0),
       (null, '假用户', 'test4',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/19.jpeg',
        1, '11111111', null, null, 0, now(), now(), 0, 1, 20, '["男","网上冲浪","羽毛球"]', '苏州市',
        '暂时没有个性签名', 0),
       (null, '假用户', 'test5',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/19.jpeg',
        1, '11111111', null, null, 0, now(), now(), 0, 1, 21, '["男","网上冲浪","python"]', '南京市',
        '暂时没有个性签名', 0),
       (null, '假用户', 'test6',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/19.jpeg',
        0, '11111111', null, null, 0, now(), now(), 0, 1, 22, '["网上冲浪","男"]', '广州', '好好好',
        0),
       (null, '假用户', 'test7',
        'https://bobo-tlias.oss-cn-hangzhou.aliyuncs.com/19.jpeg',
        0, '11111111', null, null, 0, now(), now(), 0, 1, 23, '["网上冲浪","女","小白"]', '北京市',
        '暂时没有个性签名', 0);

-- 父标签分类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (null, '计算机', 2, 2, '计算机', 1, now(), now(), 0),
       (null, '个性', 3, 3, '个性', 1, now(), now(), 0),
       (null, '身份', 4, 4, '身份', 1, now(), now(), 0),
       (null, '特殊经历', 5, 5, '特殊经历', 1, now(), now(), 0),
       (null, '阅读', 2, 6, '阅读', 1, now(), now(), 0),
       (null, '音乐', 3, 7, '音乐', 1, now(), now(), 0),
       (null, '影视', 7, 8, '影视', 1, now(), now(), 0),
       (null, '游戏', 6, 9, '游戏', 1, now(), now(), 0),
       (null, '运动', 5, 10, '运动', 1, now(), now(), 0),
       (null, '线下活动', 2, 11, '线下活动', 1, now(), now(), 0);

-- 子标签-热门类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (20, 'ikun', 2, 1, '热门', 0, now(), now(), 0),
       (null, '萌新', 3, 1, '热门', 0, now(), now(), 0),
       (null, '爱追剧', 4, 1, '热门', 0, now(), now(), 0),
       (null, '网上冲浪', 5, 1, '热门', 0, now(), now(), 0),
       (null, '铁憨憨', 2, 1, '热门', 0, now(), now(), 0),
       (null, '间歇性高冷', 3, 1, '热门', 0, now(), now(), 0),
       (null, '退堂鼓选手', 7, 1, '热门', 0, now(), now(), 0),
       (null, '中二病', 6, 1, '热门', 0, now(), now(), 0),
       (null, '理想主义者', 5, 1, '热门', 0, now(), now(), 0),
       (null, '一醒就犯困', 3, 1, '热门', 0, now(), now(), 0),
       (null, '懒', 3, 1, '热门', 0, now(), now(), 0),
       (null, '声优', 7, 1, '热门', 0, now(), now(), 0),
       (null, '学霸', 6, 1, '热门', 0, now(), now(), 0),
       (null, '学渣', 5, 1, '热门', 0, now(), now(), 0),
       (null, '佛系', 2, 1, '热门', 0, now(), now(), 0);

-- 子标签-计算机类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (null, 'java', 10, 2, '计算机', 0, now(), now(), 0),
       (null, 'vue', 2, 2, '计算机', 0, now(), now(), 0),
       (null, 'react', 6, 2, '计算机', 0, now(), now(), 0),
       (null, '后端开发', 7, 2, '计算机', 0, now(), now(), 0),
       (null, '前端开发', 2, 2, '计算机', 0, now(), now(), 0),
       (null, 'python', 9, 2, '计算机', 0, now(), now(), 0),
       (null, 'c++', 7, 2, '计算机', 0, now(), now(), 0),
       (null, '编程小白', 6, 2, '计算机', 0, now(), now(), 0);

-- 子标签-个性类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (null, '天然呆', 10, 3, '个性', 0, now(), now(), 0),
       (null, '话唠子', 2, 3, '个性', 0, now(), now(), 0),
       (null, '独处的时候最开心', 6, 3, '个性', 0, now(), now(), 0),
       (null, '半年至少旅行一次', 7, 3, '个性', 0, now(), now(), 0),
       (null, '偶尔搓个麻将', 2, 3, '个性', 0, now(), now(), 0),
       (null, '运动神经满分', 9, 3, '个性', 0, now(), now(), 0),
       (null, '球场少不了我', 7, 3, '个性', 0, now(), now(), 0),
       (null, '肥宅二次元', 8, 3, '个性', 0, now(), now(), 0),
       (null, '社交达人', 3, 3, '个性', 0, now(), now(), 0),
       (null, '穿搭犹豫症', 9, 3, '个性', 0, now(), now(), 0),
       (null, '社恐', 4, 3, '个性', 0, now(), now(), 0),
       (null, '吸猫吸狗', 6, 3, '个性', 0, now(), now(), 0);


-- 子标签-身份类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (null, '在校生', 10, 4, '身份', 0, now(), now(), 0),
       (null, '家里蹲', 2, 4, '身份', 0, now(), now(), 0),
       (null, '大三及以下', 4, 4, '身份', 0, now(), now(), 0),
       (null, '大四', 4, 4, '身份', 0, now(), now(), 0),
       (null, '研究生', 3, 4, '身份', 0, now(), now(), 0),
       (null, '留学生', 9, 4, '身份', 0, now(), now(), 0),
       (null, '求职中', 5, 4, '身份', 0, now(), now(), 0),
       (null, '实习中', 9, 4, '身份', 0, now(), now(), 0),
       (null, '上班族', 3, 4, '身份', 0, now(), now(), 0),
       (null, '化妆师', 9, 4, '身份', 0, now(), now(), 0),
       (null, '摄影师', 2, 4, '身份', 0, now(), now(), 0),
       (null, '咖啡师', 6, 4, '身份', 0, now(), now(), 0),
       (null, '老板', 2, 4, '身份', 0, now(), now(), 0),
       (null, 'HR', 2, 4, '身份', 0, now(), now(), 0),
       (null, '模特', 8, 4, '身份', 0, now(), now(), 0),
       (null, '销售', 3, 4, '身份', 0, now(), now(), 0),
       (null, '美术生', 3, 4, '身份', 0, now(), now(), 0),
       (null, '舞蹈生', 2, 4, '身份', 0, now(), now(), 0),
       (null, '厨师', 8, 4, '身份', 0, now(), now(), 0),
       (null, '导游', 8, 4, '身份', 0, now(), now(), 0);


-- 子标签-特殊经历类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (null, '创过业', 10, 5, '特殊经历', 0, now(), now(), 0),
       (null, '露宿街头', 2, 5, '特殊经历', 0, now(), now(), 0),
       (null, '减肥超20斤', 5, 5, '特殊经历', 0, now(), now(), 0),
       (null, '写过歌', 4, 5, '特殊经历', 0, now(), now(), 0),
       (null, '组过乐队', 3, 5, '特殊经历', 0, now(), now(), 0),
       (null, '出国留学', 9, 5, '特殊经历', 0, now(), now(), 0),
       (null, '玩过蹦极', 5, 5, '特殊经历', 0, now(), now(), 0);


-- 子标签-音乐类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (null, '邓紫棋', 10, 7, '音乐', 0, now(), now(), 0),
       (null, '王贰浪', 2, 7, '音乐', 0, now(), now(), 0),
       (null, '五月天', 5, 7, '音乐', 0, now(), now(), 0),
       (null, '张碧晨', 4, 7, '音乐', 0, now(), now(), 0),
       (null, '傅如乔', 3, 7, '音乐', 0, now(), now(), 0),
       (null, '刘大拿', 9, 7, '音乐', 0, now(), now(), 0),
       (null, '周杰伦', 5, 7, '音乐', 0, now(), now(), 0);


-- 子标签-阅读类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (null, '忠实纸质读者', 10, 6, '阅读', 0, now(), now(), 0),
       (null, '爱逛书店', 3, 6, '阅读', 0, now(), now(), 0),
       (null, '历史类', 4, 6, '阅读', 0, now(), now(), 0),
       (null, '散文类', 4, 6, '阅读', 0, now(), now(), 0),
       (null, '小说类', 6, 6, '阅读', 0, now(), now(), 0);

-- 子标签-影视类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (null, '3D动画', 9, 8, '阅读', 0, now(), now(), 0),
       (null, '美剧', 3, 8, '阅读', 0, now(), now(), 0),
       (null, '韩剧', 4, 8, '阅读', 0, now(), now(), 0),
       (null, '凡人修仙传', 4, 8, '阅读', 0, now(), now(), 0),
       (null, '完美世界', 6, 8, '阅读', 0, now(), now(), 0),
       (null, '吞噬星空', 3, 8, '阅读', 0, now(), now(), 0),
       (null, '武动乾坤', 4, 8, '阅读', 0, now(), now(), 0),
       (null, '仙逆', 4, 8, '阅读', 0, now(), now(), 0),
       (null, '斗破苍穹', 6, 8, '阅读', 0, now(), now(), 0);

-- 子标签-游戏类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (null, '王者荣耀', 9, 9, '游戏', 0, now(), now(), 0),
       (null, '吃鸡', 3, 9, '游戏', 0, now(), now(), 0),
       (null, 'LOL', 4, 9, '游戏', 0, now(), now(), 0),
       (null, '原神', 4, 9, '游戏', 0, now(), now(), 0),
       (null, 'csgo', 9, 9, '游戏', 0, now(), now(), 0),
       (null, '端游', 3, 9, '游戏', 0, now(), now(), 0),
       (null, '手游', 4, 9, '游戏', 0, now(), now(), 0);

-- 子标签-运动类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (null, '跑步', 9, 10, '运动', 0, now(), now(), 0),
       (null, '网球', 3, 10, '运动', 0, now(), now(), 0),
       (null, '羽毛球', 4, 10, '运动', 0, now(), now(), 0),
       (null, '乒乓球', 4, 10, '运动', 0, now(), now(), 0),
       (null, '篮球', 4, 10, '运动', 0, now(), now(), 0),
       (null, '爬山', 3, 10, '运动', 0, now(), now(), 0),
       (null, '游泳', 3, 10, '运动', 0, now(), now(), 0),
       (null, '骑行', 4, 10, '运动', 0, now(), now(), 0);

-- 子标签-线下活动类
insert into tag (id, tagName, userId, parentId, category, isParent, createTime, updateTime, isDelete)
values (null, '脱口秀', 9, 11, '线下活动', 0, now(), now(), 0),
       (null, '密室逃脱', 3, 11, '线下活动', 0, now(), now(), 0),
       (null, '剧本杀', 4, 11, '线下活动', 0, now(), now(), 0),
       (null, '野外露营', 4, 11, '线下活动', 0, now(), now(), 0);

-- 队伍表
create table if not exists team
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    teamName    varchar(256)                       not null comment '队伍名',
    description varchar(256)                       null comment '描述',
    maxNum      int      default 1                 not null comment '最大人数',
    expireTime  datetime                           null comment '过期时间',
    userId      bigint                             not null comment '创建人id',
    status      int      default 0                 not null comment '0-公开、1-私有、2-加密',
    password    varchar(512)                       null comment '入队密码',
    createTime  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除'

)
    comment '队伍' charset = utf8;


-- 用户-队伍表（用户与队伍的关系）
create table if not exists user_team
(
    id         bigint auto_increment comment '主键ID'
        primary key,
    userId     bigint                             not null comment '创建人id',
    teamId     bigint                             not null comment '队伍id',
    joinTime   datetime                           null comment '加入时间',
    createTime datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除'
)
    comment '用户-队伍关系' charset = utf8;

-- 查询队伍和已加入队伍成员的信息
select *
from team t
         left join user_team ut on t.id = ut.teamId
         left join user u on ut.userId = u.id;

-- 查询队伍和创建人的信息
select *
from team t
         left join user u on t.userId = u.id;














