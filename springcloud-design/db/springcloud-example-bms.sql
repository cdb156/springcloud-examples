/*
 Navicat Premium Data Transfer

 Source Server         : mysql-docker-master
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : master.docker:3306
 Source Schema         : springcloud-example-bms

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 07/08/2019 10:30:33
*/

-- ----------------------------
-- Table structure for bms_user
-- ----------------------------
DROP TABLE IF EXISTS `bms_user`;
CREATE TABLE `bms_user` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `account` varchar(100) DEFAULT NULL COMMENT '账号',
  `name` varchar(100) DEFAULT NULL COMMENT '姓名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐值',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `photo` varchar(200) DEFAULT NULL COMMENT '头像图片路径',
  `status` int(1) DEFAULT '1' COMMENT '状态（0：禁用，1：启用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_host` varchar(50) DEFAULT NULL COMMENT '创建host',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_host` varchar(50) DEFAULT NULL COMMENT '更新host',
   PRIMARY KEY (`pkid`) USING BTREE,
   UNIQUE KEY `un_account` (`account`) USING BTREE COMMENT '账号唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='后台管理-用户表';


-- ----------------------------
-- Table structure for bms_menu
-- ----------------------------
DROP TABLE IF EXISTS `bms_menu`;
CREATE TABLE `bms_menu` (
  `pkid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级ID',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `path` varchar(200) DEFAULT NULL COMMENT '菜单路径',
  `perms` varchar(200) DEFAULT NULL COMMENT '权限授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(1) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT '0' COMMENT '排序序号',
  `status` int(1) DEFAULT '1' COMMENT '状态（0：禁用，1：启用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_host` varchar(50) DEFAULT NULL COMMENT '创建host',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_host` varchar(50) DEFAULT NULL COMMENT '更新host',
   PRIMARY KEY (`pkid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 COMMENT='后台管理-菜单表';
