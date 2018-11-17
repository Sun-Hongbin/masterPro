--数据库初始化脚本

--创建数据库
CREATE DATABASE mcs_system;
--使用数据库
use mcs_system;

CREATE TABLE `user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(20) NOT NULL COMMENT '用户名',
  `user_phone` BIGINT(20) NOT NULL COMMENT '用户手机号',
  `password` char(32) NOT NULL COMMENT '密码',
  `email` char(32) NOT NULL COMMENT '邮箱',
  `create_time` bigint NOT NULL COMMENT '用户创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

CREATE TABLE `task_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '本条任务的Id',
  `publisher_id` BIGINT(20) NOT NULL COMMENT '用户id',
  `ctime` BIGINT(20) NOT NULL COMMENT '发布时间',
  `utime` BIGINT(20) NULL COMMENT '更新时间',
  `task_description` VARCHAR(150) NOT NULL COMMENT '任务描述',
  `task_location` VARCHAR(20) NOT NULL COMMENT '任务要求的执行地点',
  `task_longitude` decimal (15,8) NOT NULL DEFAULT '0.00000000' COMMENT '任务地点对应的经度',
  `task_latitude` decimal (15,8) NOT NULL DEFAULT '0.00000000' COMMENT '任务地点对应的纬度',
  `task_execute_time` TIMESTAMP NOT NULL COMMENT '任务要求的执行时间',
  PRIMARY KEY (`id`),
  KEY `FK_PUBLISHER_idx` (`publisher_id`),
  CONSTRAINT `FK_RECORD` FOREIGN KEY (`publisher_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务发布记录表';

CREATE TABLE `noise_message` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '噪声数据id',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户id',
  `db` smallint(6) NOT NULL DEFAULT '0' COMMENT '分贝值',
  `mp3_file` MEDIUMBLOB NULL COMMENT '噪声音频',
  `longitude` decimal (15,8) NOT NULL DEFAULT '0.00000000' COMMENT '经度',
  `latitude` decimal (15,8) NOT NULL DEFAULT '0.00000000' COMMENT '纬度',
  `collect_time` BIGINT(20) NOT NULL COMMENT '采集时间',
  `upload_time` BIGINT(20) NOT NULL COMMENT '上传时间',
  `task_id` BIGINT(20) NULL COMMENT '参与的任务Id',
  PRIMARY KEY (`id`),
  KEY `FK_STUDENT_idx` (`user_id`),
  CONSTRAINT `FK_USER` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_TASK` FOREIGN KEY (`task_id`) REFERENCES `task_record` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='噪声信息表';



--初始化数据
insert into
  user(name,user_phone,password,email,create_time)
values
  ('bachelor',15501833823,'123456','420498251@qq.com','1660844471901'),
  ('master',18500300866,'123456','sunhongbin200808@gmail.com','156504447789'),
  ('master2',15510173823,'123456','17125053@bjtu.edi.cn','1660844471901');


-- 连接数据库控制台
mysql -uroot -p

--为什么手写DDL
--记录每次上线的DDL修改
--上线v1.1
ALTER TABLE user
DROP INDEX idx_create_time,
ADD index idx_c_s(start_time,create_time);

--上线v1.2
--ddl

--这是决定存储需要占用多少字节，那么后边的数字(M)代表什么意思呢
tinyint(M), M默认为4;
SMALLINT(M), M默认为6;
MEDIUMINT(M), M默认为9;
INT(M),M默认为11;
BIGINT(M),M默认为20.
--M表示最大显示宽度, 建表若设置了zerofill(0填充), 会在数字前面补充0.












