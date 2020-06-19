/**
  创建数据库
 */
drop database if exists `my_blog`;

create database `my_blog`;
-- 切换数据库
use `my_blog`;
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------
/**
  创建表
 */
drop table if exists `tb_user`;
-- （`tb_user`）用户表
create table `tb_user`
(
    `index_id` int(11) auto_increment comment '主键/序列id',
    `user_id` char(32) not null comment '用户uuid',
    `login_user_name` varchar(20) not null unique comment '登陆名',
    `login_user_password` varchar(255) not null comment '登陆密码',
    `nick_name` varchar(20) not null comment '网名/真实名字',
    `sex` int not null comment '性别',
    `user_phone` varchar(20) comment '联系电话',
    `user_email` varchar(30) comment '用户邮箱',
    `user_address` varchar(255) comment '联系地址',
    `profile_picture_url` varchar(255) comment '用户头像地址',
    `is_lock` int not null default 1 comment '是否允许登陆',
    primary key (`index_id`)
) engine=innoDB default charset=utf8;
-- 测试用户
INSERT INTO `tb_user`(`user_id`, `login_user_name`, `login_user_password`, `nick_name`, `sex`, `user_phone`, `user_email`, `user_address`, `profile_picture_url`)
VALUES ('a25f81d2b11111eabe5c8c164501e7ab','admin',md5('admin'),'alone丶star',1,'18791288755','2532446368@qq.com','陕西省榆林市绥德县','/upload/userAvatar/20200618_11115618.jpg');

-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------
drop table if exists `tb_category`;
-- （`tb_category`）分类表
create table `tb_category`
(
    `category_id` int auto_increment comment '主键id',
    `category_name` varchar(30) unique not null comment '分类名',
    `create_time` datetime default current_timestamp comment '添加时间',
    `hot_rank` int comment '火热值',    -- 展示排序使用，添加博客引用一个+1
    `is_delete` int default 0 comment '是否删除', -- 删除：1 未删除：0
    primary key (`category_id`)
) engine=innoDB default charset=utf8;
-- 添加测试
insert into `tb_category`(`category_name`,`hot_rank`,`is_delete`) values ('学习',1,0);
insert into `tb_category`(`category_name`,`hot_rank`,`is_delete`) values ('工作',1,0);
insert into `tb_category`(`category_name`,`hot_rank`,`is_delete`) values ('日常',1,0);

-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------
drop table if exists `tb_tag`;
-- (tb_tag) 标签表
create table `tb_tag`
(
    `tag_id` int auto_increment comment '主键id',
    `tag_name` varchar(50) unique not null comment '标签名',
    `create_time` datetime default current_timestamp comment '添加时间',
    `is_delete` int default 0 comment '是否删除', -- 删除：1 未删除：0
    primary key (`tag_id`)
)engine=innoDB default charset=utf8;
-- 添加测试
insert into `tb_tag`(`tag_name`) values ('Java');
insert into `tb_tag`(`tag_name`) values ('Spring');
insert into `tb_tag`(`tag_name`) values ('MySql');

-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------
drop table if exists `tb_blog`;
-- (tb_blog) 博客表
create table `tb_blog`
(
    `blog_primary_id` bigint(20) auto_increment comment '主键id',
    `blog_id` char(32) not null unique comment '博客uuid',
    `blog_title` varchar(200) not null comment '博客标题',
    `blog_access_url` varchar(200) comment '自定义访问url',
    `blog_cover_image` varchar(200) comment '博客封面图片',
    `blog_content` mediumtext not null comment '博客内容',
    `blog_category_id` int not null comment '分类id',
    `blog_category_name` varchar(30) comment '分类名',
    `blog_tags` varchar(200) not null comment '博客标签',
    `blog_status` int not null default 0 comment '发布状态 0：草稿 1：发布',
    `blog_views` bigint(20) not null default '0' comment '阅读量',
    `allow_comment` int not null default 1 comment '是否允许评论1:允许 0:不允许',
    `is_delete` int default 0 comment '是否删除 删除：1 未删除：0',
    `create_time` datetime default current_timestamp comment '添加时间',
    `update_time` datetime default current_timestamp comment '修改时间',
    primary key (`blog_primary_id`)
)engine=innoDB default charset=utf8;

insert into `tb_blog`(`blog_primary_id`,`blog_id`, `blog_title`, `blog_access_url`, `blog_cover_image`, `blog_content`, `blog_category_id`, `blog_category_name`, `blog_tags`,`blog_status`,`blog_views`,`allow_comment`,`is_delete`,`create_time`,`update_time`)
VALUES (1,'3f5eb0c467984b9d8ad4295fab3f63fd','大家好，我是阿航','about','/upload/cover/20200618_11313437.jpg','# 关于我
我是阿航，一名苦逼的大专生，软件技术专业，是个小白，也没啥经历；
著名''哲学家''吕子乔曾说过：火车是向前开的，去哪儿并不重要，关键在于窗外的风景。
在做这个博客之前，我天真的以为自己所学的都掌握了，之后才发现我错了，我就是一个活生生的例子，所以切记！学习不能图快，一步一脚印。
我相信这世上没人是真正完美的，所以我们要做的仅仅只是努力。

> 你以为挑起生活的担子是勇气，其实去过自己真正想要的生活才更需要勇气。——萨姆门德斯
> 未曾失败的人恐怕也未曾成功过——佚名

![可爱狗子](/upload/6month/pg-7c62a692b3e14a8ca6e49946a85a1cb8.jpg "可爱狗子")',3,'日常','你好,世界',1,0,0,0,'2020-06-18 11:31:36','2020-06-18 11:31:36');
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `tb_blog_tag_relation`;
-- 博客 标签关系表
CREATE TABLE `tb_blog_tag_relation` (
                                        `relation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系表id',
                                        `blog_id` bigint(20) NOT NULL COMMENT '博客id',
                                        `tag_id` int(11) NOT NULL COMMENT '标签id',
                                        `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                                        PRIMARY KEY (`relation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into `tb_blog_tag_relation`(`relation_id`, `blog_id`, `tag_id`, `create_time`) VALUES (1,1,4,'2020-06-18 11:31:36');
insert into `tb_blog_tag_relation`(`relation_id`, `blog_id`, `tag_id`, `create_time`) VALUES (2,1,5,'2020-06-18 11:31:36');
-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------
drop table if exists `tb_blog_user_relation`;
-- 博客 标签关系表
CREATE TABLE `tb_blog_user_relation` (
                                         `relation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系表id',
                                         `blog_id` char(32) NOT NULL COMMENT '博客id',
                                         `user_id` char(32) NOT NULL COMMENT '用户id',
                                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                                         PRIMARY KEY (`relation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into `tb_blog_user_relation`(`relation_id`, `blog_id`, `user_id`, `create_time`) VALUES (1,'3f5eb0c467984b9d8ad4295fab3f63fd','a25f81d2b11111eabe5c8c164501e7ab','2020-06-18 11:31:36');

-- ---------------------------------------------------------------------------------------------------------------------------------------------------------------

DROP TABLE IF EXISTS `tb_config`;
-- 博客 配置表
create table `tb_config`
(
    `config_id` int auto_increment comment '主键id',
    `config_name` varchar(100) not null default '' comment '配置项名称',
    `config_value` varchar(200) not null default '' comment '配置项的值',
    `create_time` datetime default current_timestamp comment '添加时间',
    `update_time` datetime default current_timestamp comment '修改时间',
    primary key (`config_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
/**
  站点名称 websiteName
  站点描述 websiteDescription
  站点logo websiteLogoUrl
  网站图标 websiteIconUrl

  底部about  footerAbout
  底部备案号  footerCaseNumber
  底部Copy Right footerCopyRight
  底部Powered By footerPoweredBy
  底部Powered By URL footerPoweredByUrl
 */
insert into `tb_config`(`config_name`,`config_value`) values ('websiteName','My blog');
insert into `tb_config`(`config_name`,`config_value`) values ('websiteDescription','My blog是我的练手网站，由springboot+mysql+thymeleaf开发');
insert into `tb_config`(`config_name`,`config_value`) values ('websiteLogoUrl','/admin/dist/img/logo2.png');
insert into `tb_config`(`config_name`,`config_value`) values ('websiteIconUrl','/admin/dist/img/favicon.png');
insert into `tb_config`(`config_name`,`config_value`) values ('websiteHeaderTitle','凡事不要想得太复杂，手握的太紧，东西会碎，手会疼。');
insert into `tb_config`(`config_name`,`config_value`) values ('websiteCoverUrl','/blog/amaze/images/header.jpg');
insert into `tb_config`(`config_name`,`config_value`) values ('footerAbout','My blog');
insert into `tb_config`(`config_name`,`config_value`) values ('footerCaseNumber','备案号现在没有');
insert into `tb_config`(`config_name`,`config_value`) values ('footerCopyRight','阿航');
insert into `tb_config`(`config_name`,`config_value`) values ('footerPoweredBy','GitHub');
insert into `tb_config`(`config_name`,`config_value`) values ('footerPoweredByUrl','这写GitHub网址');