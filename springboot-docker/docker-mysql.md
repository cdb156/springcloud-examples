Docker 下MySql集群搭建
===================

## Docker 网络设置

Docker创建容器时默认采用bridge网络，自行分配ip，不允许自己指定。

在实际部署中，我们需要指定容器ip，不允许其自行分配ip，尤其是搭建集群时，固定ip是必须的。

### 创建一个新的bridge网络

docker network命令列表：

```shell
docker network create
docker network connect
docker network ls 
docker network rm
docker network disconnect
docker network inspect
```

docker 创建新网络

```shell
# docker 创建一个新的网络
docker network create --driver bridge --subnet=172.18.1.0/16 --gateway=172.18.1.1 mynet-1
# 查看所有网路
docker network ls 

# 查看指定的网络信息
docker network inspect [网络名]

# 指定ip和网络运行容器
docker run --net mynet-1 --ip 172.18.1.2
```





## Docker运行Mysql

目前docker hub 的mysql 版本：

- [`8.0.17`, `8.0`, `8`, `latest`](https://github.com/docker-library/mysql/blob/4af273a07854d7e4b68c5148b8e23b86aa8706e2/8.0/Dockerfile)
- [`5.7.27`, `5.7`, `5`](https://github.com/docker-library/mysql/blob/51f9523ad07abacbce90c43eb27390c1c1f76266/5.7/Dockerfile)
- [`5.6.45`, `5.6`](https://github.com/docker-library/mysql/blob/ed0e47e48b8ca3dbc4d68d68f56384bdd1fb5cdb/5.6/Dockerfile)

推荐采用Mysql 5.7

* [mysql5.7主从复制，主主复制](https://blog.csdn.net/qq_16676539/article/details/81906959)

* [MySQL 5.7半同步复制技术](https://www.cnblogs.com/zero-gg/p/9057092.html)



### 数据库基础概念

#### OLTP和OLAP

- OLTP ：On-Line Transaction Processing联机事务处理过程(OLTP)

  也称为面向交易的处理过程，其基本特征是前台接收的用户数据可以立即传送到计算中心进行处理，并在很短的时间内给出处理结果，是对用户操作快速响应的方式之一。

- OLAP：联机分析处理。作为数据仓库

  OLAP是一种软件技术，它使分析人员能够迅速、一致、交互地从各个方面观察信息，以达到深入理解数据的目的。

- OLTP和OLAP区别

  1、适用人员不同：OLTP主要供基层人员使用，进行一线业务操作。OLAP则是探索并挖掘数据价值，作为企业高层进行决策的参考。

  2、面向内容不同：OLTP面向应用，OLAP面向主题；

  4、数据特点不同：OLTP的数据特点是当前的、最新的、细节的, 二维的、分立的；而OLTP则是历史的, 聚集的, 多维的，集成的, 统一的；

  5、存取能力不同：OLTP可以读/写数十条记录，而OLAP则可以读上百万条记录；

  6、工作事件的复杂度不同：OLTP执行的是简单的事务，而OLAP执行的是复杂任务；

  7、可承载用户数量不同：OLTP的可承载用户数量为上千个，而OLAP则是上百万个；

  8、DB大小不同：OLTP的DB 大小为100GB，而OLAP则可以达到100TB；

  9、执行时间要求不同：OLTP具有实时性，OLAP对时间的要求不严格。

#### DDL和DML

这里解释一下什么是DML和DQL？SQL语言四大分类：DQL、DML、DDL、DCL。

- DQL（Data QueryLanguage）：数据查询语言，比如select查询语句
- DML（Data Manipulation Language）：数据操纵语言，比如insert、delete、update更新语句 。**数据库事务只对这一类操作有效。**
- DDL（）：数据定义语言，比如create/drop/alter等语句
- DCL（）：数据控制语言，比如grant/rollback/commit等语句




### 单机运行mysql

```shell
# 拉取镜像
docker pull mysql:5.7

# 运行单机，root密码123456
docker run --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7

# 指定my.cnf 和 data目录
docker run --restart=always --name mysql -p 3306:3306 \
-v /docker/mysql/conf.d:/etc/mysql/conf.d \
-v /docker/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7
```

#### My.cnf 说明

mysql-utf8.cnf 服务端字符编码格式是：`utf8`

```properties
[client]
port = 3306
default-character-set = utf8

[mysqld]
port = 3306
character-set-server = utf8
bind_address = 0.0.0.0
server-id = 1
default-storage-engine = INNODB
# 交互和非交互超时30秒
interactive_timeout = 1800
wait_timeout = 1800

#最大连接数
max_connections = 400
max_connect_errors = 1000

#是否对sql语句大小写敏感，1表示不敏感
lower_case_table_names = 1

#数据库错误日志文件
log_error = error.log

#开启mysql binlog功能
log-bin = mysqlbin 
binlog_cache_size = 4M  
max_binlog_cache_size = 8M  
max_binlog_size = 1024M  
binlog_format = MIXED  
expire_logs_days = 7

[mysqldump]
quick
max_allowed_packet = 128M

[mysql]
no-auto-rehash
default-character-set = utf8 
```



#### biglog主要用途

binlog 就是 binarylog，即二进制日志。

binlog的两个重要用途：

**1) 主从复制**
对于主从部署的mysql server，master将所有可能更改数据库状态的操作写入binlog并将其同步给slave，后者重放binlog中的操作序列以便使其数据库状态与master达到一致，这样就实现了master-slave的数据同步。

**2) 数据恢复**
数据库恢复时可以回放binlog重建宕机时的数据状态。假设数据库在某个备份点后又运行一段时间后宕机，则server重启后，可以在上次备份点的基础上，通过回放binlog来重做数据以便恢复上次备份点到server宕机这段时间内的数据状态。

注意：binlog只包含可能改变数据库状态的操作，因此，select或show等不会修改数据的操作不会记录在binlog中。

### 主从架构：主写、从读

#### 主库节点-master

* master节点配置：my.cnf
  
  ```properties
  # 机器的唯一标识
  server-id = 1
  # 开启mysql binlog功能
  log-bin = mysqlbin 
  ```

* docker 运行命令

  ```shell
  docker run --restart=always --net mynet-1 --ip 172.18.1.2 \
  --name mysql-master -p 3306:3306 \
  -v /docker/mysql/master/conf.d:/etc/mysql/conf.d \
  -v /docker/mysql/master/data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7
  ```

* 开启slave复制账号

  ```sql
  -- 开启backup 账号
  GRANT REPLICATION SLAVE ON *.* to 'backup'@'%' identified by 'backup';
  -- 查看权限
  show grants for 'backup'@'%';
  flush privileges;
  ```

#### 从库节点-slave

* slave节点配置：my.cnf

  ```properties
  # 机器的唯一标识
  server-id = 2
  # 开启mysql binlog功能
  log-bin = mysqlbin-slave
  relay-log-index = relaylog-index-slave
  relay-log = relaylog-slave
  ```

* docker运行命令

  ```shell
  docker run --restart=always --net mynet-1 --ip 172.18.1.3 \
  --name mysql-slave0 -p 3307:3306 \
  -v /docker/mysql/slave0/conf.d:/etc/mysql/conf.d \
  -v /docker/mysql/slave0/data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7
  ```

* 开启master配置

  ```sql
  -- 开启master节点，host为docker内部地址
  CHANGE MASTER TO 
  MASTER_HOST='172.18.1.2',
  MASTER_PORT=3306,
  MASTER_USER='backup',
  MASTER_PASSWORD='backup';
  
  -- 开启从节点
  START SLAVE;
  -- 关闭从节点
  STOP SLAVE;
  -- 检查是否配置成功
  show slave status;
  show master status;
  ```

  

### 主主架构：双写,双读

#### 主库节点-master1

- master1节点配置：my.cnf

  ```properties
  # 机器的唯一标识
  server-id = 1
  # 开启mysql binlog功能
  log-bin = mysqlbin
  # 步进值auto_imcrement,一般有n台主MySQL就填n
  auto_increment_increment = 2
  # 设定数据库中自动增长的起点，两台mysql的起点必须不同
  auto_increment_offset = 1
  ```

- docker 运行命令

  ```shell
  docker run --restart=always --name mysql-master1 -p 3306:3306 \
  -v /docker/mysql/master1/conf.d:/etc/mysql/conf.d \
  -v /docker/mysql/master1/data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7
  ```

- 开启slave复制账号，注册备份

  ```sql
  -- 开启backup 账号
  GRANT REPLICATION SLAVE ON *.* to 'backup'@'%' identified by 'backup';
  -- 查看权限
  show grants for 'backup'@'%';
  flush privileges;
  
  -- 开启master节点，host为docker内部地址
  CHANGE MASTER TO 
  MASTER_HOST='172.17.0.4',
  MASTER_PORT=3306,
  MASTER_USER='backup',
  MASTER_PASSWORD='backup';
  
  -- 开启从节点
  START SLAVE;
  -- 关闭从节点
  STOP SLAVE;
  -- 检查是否配置成功
  show slave status;
  show master status;
  ```

#### 主库节点-master2
- master2节点配置：my.cnf

  ```properties
  # 机器的唯一标识
  server-id = 2
  # 开启mysql binlog功能
  log-bin = mysqlbin
  # 步进值auto_imcrement,一般有n台主MySQL就填n
  auto_increment_increment = 2
  # 设定数据库中自动增长的起点，两台mysql的起点必须不同
  auto_increment_offset = 2
  ```

- docker 运行命令

  ```shell
  docker run --restart=always --name mysql-master2 -p 3307:3306 \
  -v /docker/mysql/master2/conf.d:/etc/mysql/conf.d \
  -v /docker/mysql/master2/data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7
  ```

- 开启slave复制账号，注册备份

  ```sql
  -- 开启backup 账号
  GRANT REPLICATION SLAVE ON *.* to 'backup'@'%' identified by 'backup';
  -- 查看权限
  show grants for 'backup'@'%';
  flush privileges;
  
  -- 开启master节点，host为docker内部地址
  CHANGE MASTER TO 
  MASTER_HOST='172.17.0.3',
  MASTER_PORT=3306,
  MASTER_USER='backup',
  MASTER_PASSWORD='backup';
  
  -- 开启从节点
  START SLAVE;
  -- 关闭从节点
  STOP SLAVE;
  -- 检查是否配置成功
  show slave status;
  show master status;
  ```


## mysql 系统设置

### 查询日志功能是否开启

```sql
-- 查看日志记录是否开启
SHOW VARIABLES LIKE 'general%';
-- general_log：日志功能是否开启，默认关闭OFF
-- general_log_file：日志文件保存位置

-- 开启日志记录，日志记录很占空间
set GLOBAL general_log='ON';

```



### MySQL 5.7半同步复制

* 异步复制：主库上的事务不会等待从库的确认即返回客户端提交成功！ 
* 同步复制：主库上提交的事务向客户端返回成功之前，需要收到所有从库提交事务的确认信息。
* 半同步复制：异步复制和同步复制的折中，主库上提交事务时，需要等待至少一个从库发来的收到事件确认信息，才向客户端返回成功。

现在我们已经知道，在半同步环境下，主库是在事务提交之后等待Slave ACK，所以才会有数据不一致问题。所以这个Slave ACK在什么时间去等待，也是一个很关键的问题了。因此MySQL针对半同步复制的问题，在5.7.2引入了Loss-less Semi-Synchronous，在调用binlog sync之后，engine层commit之前等待Slave ACK。这样只有在确认Slave收到事务events后，事务才会提交。在commit之前等待Slave ACK，同时可以堆积事务，利于group commit，有利于提升性能。



MySQL 5.7安装半同步模块，命令如下：

```shell
# 主库--安装 semisync_master.so插件 
mysql> install plugin rpl_semi_sync_master soname 'semisync_master.so';

# 启用半同步协议-可行
mysql> set global rpl_semi_sync_master_enabled = 1;

# 主从连接错误的超时时间默认是10s可改为2s
mysql> set global rpl_semi_sync_master_timeout = 2000;

# 永久生效，修改my.cnf
[mysqld]
plugin_load = "rpl_semi_sync_master=semisync_master.so"
rpl_semi_sync_master_enabled = 1
rpl_semi_sync_master_timeout = 1000

# -------------------------------------------#

# 从库--安装 semisync_master.so插件 
mysql> install plugin rpl_semi_sync_slave soname 'semisync_slave.so';
# 启用半同步协议
mysql> set global rpl_semi_sync_slave_enabled = 1;
# 永久生效，修改my.cnf
[mysqld]
plugin_load = "rpl_semi_sync_slave=semisync_slave.so"
rpl_semi_sync_slave_enabled = 1
```



### Mysql集群架构

| 特点 | MGR                                                          | PXC                                                          | MHA                                          |
| :--- | :----------------------------------------------------------- | :----------------------------------------------------------- | -------------------------------------------- |
| 优点 | 原生高可用、数据一致性保证、支持多主                         | 类似MGR                                                      | 成熟稳定、对MySQL侵入小、 宕机后保证数据一致 |
| 缺点 | 太新有BUG（如新加入集群宕机,并行复制有不一致bug）、管理不方便（需配合mysql-shell） | 性能损耗大（降低为1/3）、 大事务会卡住整个集群、需要用第三方发行版MySQL | 选主方式过时、需要配合第三方脚本进行自动切换 |



业界里面用MHA最多，pxc其次，MGR由于比较新还挺少

其他的高可用方案还有共享存储、MMM（淘汰了），Heartbeat+DRBD+MySQL等

个人比较看好中间件（mysql router 或者 proxysql）+MGR的架构

#### MHA

> MHA（Master High Availability）目前在MySQL高可用方面是一个相对成熟的解决方案，它由日本DeNA公司youshimaton（现就职于Facebook公司）开发，是一套优秀的作为MySQL高可用性环境下故障切换和主从提升的高可用软件。在MySQL故障切换过程中，MHA能做到在0~30秒之内自动完成数据库的故障切换操作，并且在进行故障切换的过程中，MHA能在最大程度上保证数据的一致性，以达到真正意义上的高可用。
>
> 

#### PXC

#### MGR

