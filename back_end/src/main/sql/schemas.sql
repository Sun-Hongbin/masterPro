--数据库初始化脚本

--创建数据库
CREATE DATABASE mcs_system;
--使用数据库
use mcs_system;

CREATE TABLE `noise_message` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT COMMENT '噪声数据id',
  `user_id` smallint(6) NOT NULL COMMENT '用户id',
  `db` smallint(6) NOT NULL DEFAULT '-1' COMMENT '分贝值',
  `longtitude` double(13,10) NOT NULL DEFAULT '-1.0000000000' COMMENT '进度',
  `latitude` double(13,10) NOT NULL DEFAULT '-1.0000000000' COMMENT '纬度',
  `collect_time` bigint(20) NOT NULL COMMENT '采集时间',
  `upload_time` bigint(20) NOT NULL COMMENT '上传时间',
  PRIMARY KEY (`id`),
  KEY `FK_STUDENT_idx` (`user_id`),
  CONSTRAINT `FK_USER` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='噪声信息表';

CREATE TABLE `user` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(120) NOT NULL COMMENT '用户名',
  `user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
  `password` char(32) NOT NULL COMMENT '密码',
  `email` char(32) NOT NULL COMMENT '邮箱',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='用户信息表';

CREATE TABLE `login_record` (
  `id` smallint(6) NOT NULL AUTO_INCREMENT COMMENT '登录记录的id',
  `user_id` smallint(6) NOT NULL COMMENT '用户id',
  `login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '登陆时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_RECORD` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户登录记录表';

--初始化数据
insert into
  user(name,user_phone,password,email,create_time)
values
  ('bachelor',15501833823,'123456','420498252@qq.com','1566341271888'),
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













