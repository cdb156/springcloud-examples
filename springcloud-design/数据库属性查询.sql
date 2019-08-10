show global status like 'uptime';

show VARIABLES;

-- 查看字符集
SHOW VARIABLES LIKE 'character%';

-- 查看排序规则
SHOW VARIABLES LIKE 'collation%';

-- 查看bin log 日志是否开启
SHOW VARIABLES LIKE 'binlog%'; 

-- 
SHOW CHARACTER SET  LIKE 'utf8';

-- 慢查询语句
SHOW VARIABLES LIKE '%slow%';

set slow_query_log = true;

show grants for 'root'@'%';

show database;
