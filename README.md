# cloudin-data

#### 介绍
datax整合任务调度
项目分为三部分 
1.arrange 调度datax服务 支持master/slave 自动选举leader执行定时任务
2.ui
3.datax 内置datax 异步执行arrange分配的任务 支持多节点注册到arrange

#### 软件架构
软件架构说明
![输入图片说明](https://images.gitee.com/uploads/images/2021/0715/100349_dfd6044d_9012733.png "未命名文件-2.png")

#### 安装教程
安装mysql 8.x 5.x请修改 application.yml配置
1.  单机部署
修改arrange 模块的application.yml ,application-dev.yml
修改datax 模块的application.yml ,application-dev.yml
2. docker部署
修改根目录下docker-compose.yml
