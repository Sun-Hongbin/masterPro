--数据库初始化脚本

--创建数据库
CREATE DATABASE mcs_system;
--使用数据库
use mcs_system;
--创建用户信息表
CREATE TABLE user(
`id` smallint NOT NULL AUTO_INCREMENT COMMENT '用户id',
`name` varchar(120) NOT NULL COMMENT '用户名',
`user_phone` bigint NOT NULL COMMENT '用户手机号',
`password` char(32) NOT NULL COMMENT '密码',
`email` char(32) NOT NULL COMMENT '邮箱',
`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT'用户创建时间',
PRIMARY KEY (id),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

--初始化数据
insert into
  user(name,user_phone,password,email,create_time)
values
  ('bachelor',15501833823,'123456','420498252@qq.com','2018-11-02 00:00:00'),
  ('master',18500300866,'123456','sunhongbin200808@gmail.com','2018-11-02 00:00:00'),
  ('master2',15510173823,'123456','17125053@bjtu.edi.cn','2018-11-02 00:00:00'),

--用户登录认证相关信息
CREATE TABLE noise_message(
`id` smallint NOT NULL AUTO_INCREMENT COMMENT '噪声数据id',
`user_id` smallint NOT NULL COMMENT '用户id',
`db` smallint NOT NULL DEFAULT -1 COMMENT '分贝值',
`create_time` timestamp NOT NULL COMMENT '上传时间',
PRIMARY KEY(id),
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT charset=utf8 COMMENT='噪声信息表';

--用户登录认证记录
CREATE TABLE login_record(
`user_id` smallint NOT NULL COMMENT '用户id',
`login_time` timestamp NOT NULL COMMENT '登陆时间',
PRIMARY KEY(user_id),
key idx_create_time(login_time)
)ENGINE=InnoDB DEFAULT charset=utf8 COMMENT='用户登录记录表';

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













