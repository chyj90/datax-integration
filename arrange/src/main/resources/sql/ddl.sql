create schema if not exists cloudin collate utf8mb4_0900_ai_ci;

create table if not exists t_cfg_pipeline
(
	seq_id int auto_increment comment '主键'
		primary key,
	name varchar(255) null comment '流水线名称',
	cron varchar(255) null comment 'cron表达式',
	owner int null comment '所有者 对应t_ci_name seq_id',
	status tinyint(1) null comment '开始状态 0：关闭 1：开启'
)
comment '流水线配置表';

create table if not exists t_cfg_pipeline_task
(
	seq_id int auto_increment comment '主键'
		primary key,
	pipeline_id int not null comment '流水线id',
	task_id int not null comment '任务id',
	order_no int null comment '执行顺序 作废'
)
comment '流水线-任务关系表';

create table if not exists t_cfg_task
(
	seq_id int auto_increment comment '主键'
		primary key,
	name varchar(255) null comment '任务名',
	json_str varchar(15000) null comment '任务详情',
	owner int not null comment '对应t_ci_user表seq_id',
	cron varchar(255) null comment 'cron表达式',
	status tinyint(1) null comment '是否启用 0 不启用 1 启用'
)
comment '任务表';

create table if not exists t_sys_role
(
	seq_id int auto_increment comment '主键'
		primary key,
	role_name varchar(255) null comment '角色名',
	constraint t_ci_role_role_name_uindex
		unique (role_name)
)
comment '角色表';

create table if not exists t_sys_user
(
	seq_id int auto_increment comment '主键'
		primary key,
	username varchar(255) not null comment '登陆账号',
	password varchar(255) not null comment 'md5密码',
	nickname varchar(255) not null comment '昵称-显示名',
	constraint t_ci_user_username_uindex
		unique (username)
)
comment '用户表';

create table if not exists t_sys_user_role
(
	seq_id int auto_increment comment '主键'
		primary key,
	user_seq_id int not null comment '用户表主键',
	role_seq_id int not null comment '角色表主键'
)
comment '用户-角色关系表';

alter table cloudin.t_sys_user
	add status int null comment '2 禁用 1 正常';

