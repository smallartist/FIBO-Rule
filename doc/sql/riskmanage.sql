/*
Navicat MySQL Data Transfer

Source Server         : enginex
Source Server Version : 50724
Source Host           : 47.102.125.25:3306
Source Database       : riskmanage

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2022-06-20 10:30:04
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for organ_46_b_112
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_112`;
CREATE TABLE `organ_46_b_112` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '704',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_top4` (`t0`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for organ_46_b_113
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_113`;
CREATE TABLE `organ_46_b_113` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '703',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_top4` (`t0`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for organ_46_b_114
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_114`;
CREATE TABLE `organ_46_b_114` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '703',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_top4` (`t0`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for organ_46_b_115
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_115`;
CREATE TABLE `organ_46_b_115` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '698',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_top4` (`t0`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=329 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for organ_46_b_148
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_148`;
CREATE TABLE `organ_46_b_148` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1067',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_b_149
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_149`;
CREATE TABLE `organ_46_b_149` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1055',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_b_151
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_151`;
CREATE TABLE `organ_46_b_151` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1070',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_b_152
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_152`;
CREATE TABLE `organ_46_b_152` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1070',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_b_153
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_153`;
CREATE TABLE `organ_46_b_153` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1080',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_b_156
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_156`;
CREATE TABLE `organ_46_b_156` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1077',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_b_157
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_157`;
CREATE TABLE `organ_46_b_157` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1055',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_b_158
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_b_158`;
CREATE TABLE `organ_46_b_158` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1087',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_list_db_148_16
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_list_db_148_16`;
CREATE TABLE `organ_46_list_db_148_16` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1067',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_list_db_148_17
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_list_db_148_17`;
CREATE TABLE `organ_46_list_db_148_17` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1072',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_list_db_150_10
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_list_db_150_10`;
CREATE TABLE `organ_46_list_db_150_10` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1067',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_list_db_150_11
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_list_db_150_11`;
CREATE TABLE `organ_46_list_db_150_11` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1067',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_list_db_150_12
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_list_db_150_12`;
CREATE TABLE `organ_46_list_db_150_12` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1067',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_list_db_150_13
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_list_db_150_13`;
CREATE TABLE `organ_46_list_db_150_13` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1067',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_list_db_150_14
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_list_db_150_14`;
CREATE TABLE `organ_46_list_db_150_14` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1067',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_list_db_150_15
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_list_db_150_15`;
CREATE TABLE `organ_46_list_db_150_15` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1067',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_list_db_150_8
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_list_db_150_8`;
CREATE TABLE `organ_46_list_db_150_8` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1067',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_list_db_150_9
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_list_db_150_9`;
CREATE TABLE `organ_46_list_db_150_9` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1067',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for organ_46_w_150
-- ----------------------------
DROP TABLE IF EXISTS `organ_46_w_150`;
CREATE TABLE `organ_46_w_150` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `t0` varchar(100) DEFAULT NULL COMMENT '1067',
  `t1` varchar(100) DEFAULT NULL,
  `t2` varchar(100) DEFAULT NULL,
  `t3` varchar(100) DEFAULT NULL,
  `t4` varchar(100) DEFAULT NULL,
  `t5` varchar(100) DEFAULT NULL,
  `t6` varchar(100) DEFAULT NULL,
  `t7` varchar(100) DEFAULT NULL,
  `t8` varchar(100) DEFAULT NULL,
  `t9` varchar(100) DEFAULT NULL,
  `t10` varchar(100) DEFAULT NULL,
  `t11` varchar(100) DEFAULT NULL,
  `t12` varchar(100) DEFAULT NULL,
  `t13` varchar(100) DEFAULT NULL,
  `t14` varchar(100) DEFAULT NULL,
  `t15` varchar(100) DEFAULT NULL,
  `t16` varchar(100) DEFAULT NULL,
  `t17` varchar(100) DEFAULT NULL,
  `t18` varchar(100) DEFAULT NULL,
  `t19` varchar(100) DEFAULT NULL,
  `user_id` int(11) NOT NULL COMMENT '创建人编号',
  `nick_name` varchar(50) DEFAULT NULL COMMENT '创建人昵称',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_top4` (`t0`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_analyse_decision_result
-- ----------------------------
DROP TABLE IF EXISTS `t_analyse_decision_result`;
CREATE TABLE `t_analyse_decision_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `call_date` datetime NOT NULL COMMENT '调用时间',
  `engine_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎id',
  `engine_name` varchar(255) DEFAULT NULL COMMENT '引擎名',
  `engine_description` varchar(255) DEFAULT NULL COMMENT '引擎表述',
  `version_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎版本id',
  `version_code` varchar(255) DEFAULT NULL COMMENT '引擎版本code',
  `result` varchar(255) DEFAULT NULL COMMENT '结果',
  `result_count` int(11) NOT NULL DEFAULT '0' COMMENT '次数',
  `organ_id` int(11) NOT NULL DEFAULT '0' COMMENT '组织id',
  `create_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建者id',
  `update_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '修改者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_analyse_decision_tables
-- ----------------------------
DROP TABLE IF EXISTS `t_analyse_decision_tables`;
CREATE TABLE `t_analyse_decision_tables` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `call_date` datetime NOT NULL COMMENT '调用时间',
  `engine_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎id',
  `engine_name` varchar(255) DEFAULT NULL COMMENT '引擎名',
  `engine_description` varchar(255) DEFAULT NULL COMMENT '引擎表述',
  `version_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎版本id',
  `version_code` varchar(255) DEFAULT NULL COMMENT '引擎版本code',
  `decison_tables_id` int(11) NOT NULL COMMENT '决策表id',
  `decison_tables_name` varchar(255) DEFAULT NULL COMMENT '决策表名',
  `decison_tables_version_id` int(11) NOT NULL DEFAULT '0' COMMENT '决策表版本id',
  `decison_tables_version_code` varchar(255) DEFAULT NULL COMMENT '决策表版本code',
  `result` varchar(255) DEFAULT NULL COMMENT '结果',
  `result_count` int(11) NOT NULL DEFAULT '0' COMMENT '次数',
  `organ_id` int(11) NOT NULL DEFAULT '0' COMMENT '组织id',
  `create_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建者id',
  `update_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '修改者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_analyse_engine_call
-- ----------------------------
DROP TABLE IF EXISTS `t_analyse_engine_call`;
CREATE TABLE `t_analyse_engine_call` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '引擎调用表主键id',
  `call_date` datetime NOT NULL COMMENT '调用时间',
  `engine_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎id',
  `engine_name` varchar(255) DEFAULT NULL COMMENT '引擎名',
  `engine_description` varchar(255) DEFAULT NULL COMMENT '引擎表述',
  `version_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎版本id',
  `version_code` varchar(255) DEFAULT NULL COMMENT '引擎版本code',
  `call_count` int(11) NOT NULL DEFAULT '0' COMMENT '调用次数',
  `organ_id` int(11) NOT NULL DEFAULT '0' COMMENT '组织id',
  `create_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建者id',
  `update_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '修改者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2048 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_analyse_engine_node
-- ----------------------------
DROP TABLE IF EXISTS `t_analyse_engine_node`;
CREATE TABLE `t_analyse_engine_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `call_date` datetime NOT NULL COMMENT '调用时间',
  `engine_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎id',
  `engine_name` varchar(255) DEFAULT NULL COMMENT '引擎名',
  `engine_description` varchar(255) DEFAULT NULL COMMENT '引擎表述',
  `version_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎版本id',
  `version_code` varchar(255) DEFAULT NULL COMMENT '引擎版本code',
  `node_id` int(11) NOT NULL COMMENT '节点id',
  `node_name` varchar(255) DEFAULT NULL COMMENT '节点名',
  `pass_count` int(11) NOT NULL DEFAULT '0' COMMENT '经过次数',
  `organ_id` int(11) NOT NULL DEFAULT '0' COMMENT '组织id',
  `create_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建者id',
  `update_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '修改者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_analyse_engine_summary
-- ----------------------------
DROP TABLE IF EXISTS `t_analyse_engine_summary`;
CREATE TABLE `t_analyse_engine_summary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '引擎概况主键id',
  `engine_version_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎版本id',
  `engine_name` varchar(255) DEFAULT NULL COMMENT '引擎名称',
  `statistics_dimension` varchar(255) NOT NULL COMMENT '统计维度（1.调用次数 engine_call,2.决策结果 decision_result,3规则命中 rule_hit 4.评分卡 scorecard 5.决策表 decision_tables6.名单库 list_db 7.节点命中 node_hit）',
  `statistics_count` int(11) NOT NULL DEFAULT '0' COMMENT '统计数量（截至创建日期的当天的总数）',
  `organ_id` int(11) NOT NULL DEFAULT '0' COMMENT '组织id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5509 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_analyse_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_analyse_rule`;
CREATE TABLE `t_analyse_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `call_date` datetime NOT NULL COMMENT '调用时间',
  `engine_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎id',
  `engine_name` varchar(255) DEFAULT NULL COMMENT '引擎名',
  `engine_description` varchar(255) DEFAULT NULL COMMENT '引擎表述',
  `version_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎版本id',
  `version_code` varchar(255) DEFAULT NULL COMMENT '引擎版本code',
  `rule_id` int(11) NOT NULL COMMENT '规则id',
  `rule_name` varchar(255) DEFAULT NULL COMMENT '规则名',
  `rule_version_id` int(11) DEFAULT NULL COMMENT '规则版本id',
  `rule_version_code` varchar(255) DEFAULT NULL COMMENT '规则版本code',
  `hit_count` int(11) NOT NULL DEFAULT '0' COMMENT '命中次数',
  `organ_id` int(11) NOT NULL DEFAULT '0' COMMENT '组织id',
  `create_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建者id',
  `update_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '修改者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_analyse_scorecard
-- ----------------------------
DROP TABLE IF EXISTS `t_analyse_scorecard`;
CREATE TABLE `t_analyse_scorecard` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `call_date` datetime NOT NULL COMMENT '调用时间',
  `engine_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎id',
  `engine_name` varchar(255) DEFAULT NULL COMMENT '引擎名',
  `engine_description` varchar(255) DEFAULT NULL COMMENT '引擎表述',
  `version_id` int(11) NOT NULL DEFAULT '0' COMMENT '引擎版本id',
  `version_code` varchar(255) DEFAULT NULL COMMENT '引擎版本code',
  `scorecard_id` int(11) NOT NULL COMMENT '评分卡id',
  `scorecard_name` varchar(255) DEFAULT NULL COMMENT '评分卡名',
  `scorecard_version_id` int(11) NOT NULL DEFAULT '0' COMMENT '评分卡版本id',
  `scorecard_version_code` varchar(255) DEFAULT NULL COMMENT '评分卡版本code',
  `result` varchar(255) DEFAULT NULL COMMENT '结果',
  `result_count` int(11) NOT NULL DEFAULT '0' COMMENT '次数',
  `organ_id` int(11) NOT NULL DEFAULT '0' COMMENT '组织id',
  `create_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '创建者id',
  `update_user_id` int(11) NOT NULL DEFAULT '0' COMMENT '修改者id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_approval
-- ----------------------------
DROP TABLE IF EXISTS `t_approval`;
CREATE TABLE `t_approval` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `apply_type` varchar(255) DEFAULT NULL,
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `create_user_name` varchar(255) DEFAULT NULL COMMENT '创建人名称',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改人id',
  `update_user_name` varchar(255) DEFAULT NULL COMMENT '修改人名称',
  `organ_id` int(11) DEFAULT NULL COMMENT '组织id',
  `apply_status` smallint(1) NOT NULL DEFAULT '0' COMMENT '申请单的状态：（-1取消申请。 0 待审批，1 审批通过，2 审批不通过）',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态：0冻结 -1 删除 1正常',
  `approval_user_id` int(11) DEFAULT NULL COMMENT '审批人id',
  `approval_user_name` varchar(255) DEFAULT NULL COMMENT '审批人名称',
  `approval_time` datetime DEFAULT NULL COMMENT '审批时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `apply_detail` json DEFAULT NULL COMMENT '申请详情',
  `apply_desc` json DEFAULT NULL COMMENT '申请描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_approval_config
-- ----------------------------
DROP TABLE IF EXISTS `t_approval_config`;
CREATE TABLE `t_approval_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `approval_type` varchar(255) DEFAULT NULL COMMENT '审批类型',
  `approval_name` varchar(255) DEFAULT NULL COMMENT '审批名称',
  `approval_desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `approval_status` int(1) DEFAULT NULL COMMENT '审批状态（1开启，0关闭，-1删除）',
  `organ_id` int(11) DEFAULT NULL COMMENT '组织id',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改人id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `organ_approval_type` (`organ_id`,`approval_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_app_template
-- ----------------------------
DROP TABLE IF EXISTS `t_app_template`;
CREATE TABLE `t_app_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `template_code` varchar(100) NOT NULL COMMENT '模板编码',
  `push_subject` varchar(200) NOT NULL COMMENT '推送标题',
  `push_content` varchar(500) NOT NULL COMMENT '推送内容',
  `click_action` int(2) NOT NULL DEFAULT '1' COMMENT '点击推送后动作 1：唤醒应用，2：打开指定链接，3：自定义跳转',
  `jump_url` varchar(200) DEFAULT NULL COMMENT '点击推送后跳转地址',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态 0：无效，1：有效',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `modifier` int(11) DEFAULT NULL COMMENT '修改人',
  `organ_id` int(11) NOT NULL COMMENT '组织编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `template_code` (`template_code`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='APP推送模板表';

-- ----------------------------
-- Table structure for t_base_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_base_rule`;
CREATE TABLE `t_base_rule` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `rule_type` enum('strategy_complex_rule','strategy_decision_tables','strategy_decision_tree','strategy_custom_output','engine_node_group','engine_node_decision_operation','engine_terminal_condition','strategy_tag_condition','data_flow_engine') NOT NULL COMMENT '规则类型：',
  `organ_id` int(11) NOT NULL COMMENT '组织id',
  `create_user_id` int(11) NOT NULL COMMENT '创建人id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_base_rule_condition
-- ----------------------------
DROP TABLE IF EXISTS `t_base_rule_condition`;
CREATE TABLE `t_base_rule_condition` (
  `id` bigint(20) NOT NULL COMMENT '自增主键',
  `rule_id` bigint(20) NOT NULL COMMENT '规则id',
  `cond_type` int(11) NOT NULL COMMENT '规则节点的类型：1-关系节点，2-表达式节点 3-for表达式 4-for的结果项 5条件组 6条件组的结果条件',
  `logic` enum('leaf','&&','||','for','condGroup','') DEFAULT NULL COMMENT '逻辑符号：leaf、&&、||、for、condGroup',
  `left_type` int(2) DEFAULT NULL COMMENT '条件左边值的类型',
  `left_id` int(11) DEFAULT NULL COMMENT '左边条件引用的指标id',
  `left_value` varchar(255) DEFAULT NULL COMMENT '左边的值',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作符',
  `right_type` int(2) DEFAULT NULL COMMENT '右边值类型',
  `right_id` int(11) DEFAULT NULL COMMENT '右边值引用的指标id',
  `right_value` varchar(255) DEFAULT NULL COMMENT '右边的值',
  `parent_id` bigint(20) NOT NULL COMMENT '父id',
  PRIMARY KEY (`id`),
  KEY `rule_id_idx` (`rule_id`) USING BTREE COMMENT '规则id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_business_event_log
-- ----------------------------
DROP TABLE IF EXISTS `t_business_event_log`;
CREATE TABLE `t_business_event_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `batch_no` varchar(100) NOT NULL COMMENT '批次号',
  `event_request_id` varchar(100) NOT NULL COMMENT '事件流水id',
  `event_id` varchar(100) NOT NULL COMMENT '事件ID',
  `event_name` varchar(100) NOT NULL COMMENT '事件名称',
  `template_id` varchar(100) NOT NULL COMMENT '模板ID',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `business_name` varchar(100) NOT NULL COMMENT '业务类型名称',
  `business_code` varchar(100) NOT NULL COMMENT '业务类型编码',
  `business_child_name` varchar(100) NOT NULL COMMENT '业务子类型名称',
  `business_child_code` varchar(100) NOT NULL COMMENT '业务子类型编码',
  `customer_name` varchar(100) DEFAULT NULL COMMENT '客户姓名',
  `customer_mobile` varchar(100) NOT NULL COMMENT '客户手机号',
  `policy_no` varchar(100) DEFAULT NULL COMMENT '保单号',
  `send_platform` varchar(100) DEFAULT NULL COMMENT '发送平台',
  `call_start_time` datetime NOT NULL COMMENT '规则执行开始时间',
  `call_end_time` datetime NOT NULL COMMENT '规则执行结束时间',
  `call_time` int(10) NOT NULL COMMENT '耗时，单位毫秒',
  `call_status` int(2) NOT NULL DEFAULT '1' COMMENT '执行结果 1:成功，0:失败',
  `rule_log_ids` varchar(200) DEFAULT NULL COMMENT '规则执行记录表ids',
  `organ_id` int(11) NOT NULL COMMENT '企业编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=464 DEFAULT CHARSET=utf8 COMMENT='事件调用记录';

-- ----------------------------
-- Table structure for t_business_rule_log
-- ----------------------------
DROP TABLE IF EXISTS `t_business_rule_log`;
CREATE TABLE `t_business_rule_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `batch_no` varchar(100) NOT NULL COMMENT '批次号',
  `rule_name` varchar(100) NOT NULL COMMENT '规则名称',
  `rule_code` varchar(100) NOT NULL COMMENT '规则编号',
  `rule_type` varchar(100) NOT NULL COMMENT '规则类型',
  `business_name` varchar(100) NOT NULL COMMENT '业务类型名称',
  `business_code` varchar(100) NOT NULL COMMENT '业务类型编码',
  `business_child_name` varchar(100) NOT NULL COMMENT '业务子类型名称',
  `business_child_code` varchar(100) NOT NULL COMMENT '业务子类型编码',
  `rule_result` varchar(1000) DEFAULT NULL COMMENT '规则输出结果',
  `rule_description` varchar(500) DEFAULT NULL COMMENT '规则描述',
  `execute_switch` int(2) DEFAULT NULL COMMENT '执行开关 1：开启，0：关闭',
  `valid_start_time` datetime DEFAULT NULL COMMENT '执行有效期，开始时间',
  `valid_end_time` datetime DEFAULT NULL COMMENT '执行有效期，结束时间',
  `organ_id` int(11) NOT NULL COMMENT '企业编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=301 DEFAULT CHARSET=utf8 COMMENT='规则执行记录';

-- ----------------------------
-- Table structure for t_business_rule_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_business_rule_rel`;
CREATE TABLE `t_business_rule_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_name` varchar(100) NOT NULL COMMENT '业务类型名称',
  `business_code` varchar(100) NOT NULL COMMENT '业务类型编码',
  `business_child_name` varchar(100) NOT NULL COMMENT '业务子类型名称',
  `business_child_code` varchar(100) NOT NULL COMMENT '业务子类型编码',
  `send_type` varchar(50) NOT NULL COMMENT '发送方式：自动/手动',
  `is_unsubscribe` varchar(50) NOT NULL COMMENT '是否取消订阅',
  `event_type` varchar(50) NOT NULL COMMENT '事件类型：通知/待办/系统类',
  `backlog` varchar(200) DEFAULT NULL COMMENT '待办任务',
  `is_manual_intervention` varchar(50) DEFAULT NULL COMMENT '是否需人工干预',
  `rule_info` varchar(2000) DEFAULT NULL COMMENT '5大类规则信息',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态 0：无效，1：有效',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `modifier` int(11) DEFAULT NULL COMMENT '修改人',
  `organ_id` int(11) NOT NULL COMMENT '企业编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='业务类型与规则关联表';

-- ----------------------------
-- Table structure for t_data_clean
-- ----------------------------
DROP TABLE IF EXISTS `t_data_clean`;
CREATE TABLE `t_data_clean` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(200) DEFAULT NULL COMMENT '集合操作名称',
  `code` varchar(200) DEFAULT NULL COMMENT '集合操作代码',
  `description` text COMMENT '集合操作描述',
  `op_type` int(4) DEFAULT NULL COMMENT '集合操作类型：1 集合规则，2 集合处理',
  `folder_id` int(11) NOT NULL DEFAULT '0' COMMENT '文件夹id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态：0 停用 1 启用 -1删除（默认启用）',
  `start_time` datetime DEFAULT NULL COMMENT '有效期开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `create_user_id` int(11) NOT NULL COMMENT '创建者id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改者id',
  `organ_id` int(11) NOT NULL COMMENT '组织id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_data_clean_block
-- ----------------------------
DROP TABLE IF EXISTS `t_data_clean_block`;
CREATE TABLE `t_data_clean_block` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `data_clean_version_id` int(11) DEFAULT NULL COMMENT '版本id',
  `name` varchar(100) DEFAULT NULL COMMENT '条件区域名称',
  `op_type` varchar(50) DEFAULT NULL COMMENT '选择来源：original原数据，data_op原数据操作，handle_collection选择集合',
  `handle_collection` varchar(100) DEFAULT NULL COMMENT '处理集合',
  `group_fields` text COMMENT '分组字段',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=283 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_data_clean_condition
-- ----------------------------
DROP TABLE IF EXISTS `t_data_clean_condition`;
CREATE TABLE `t_data_clean_condition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `data_clean_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作的id',
  `data_clean_version_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作版本的id',
  `data_clean_block_id` int(11) NOT NULL DEFAULT '0' COMMENT '条件区域块id',
  `logical` varchar(10) DEFAULT NULL COMMENT '关系节点的逻辑符号：&&（并关系），||（或关系）',
  `op_type` varchar(50) DEFAULT NULL COMMENT '选择来源：original原数据，data_op原数据操作，handle_collection选择集合',
  `op_key` varchar(255) DEFAULT NULL COMMENT '计算的参数key{循环中对象的key}',
  `operator` varchar(50) DEFAULT NULL COMMENT '表达式节点的操作符',
  `variable_type` int(2) DEFAULT NULL COMMENT '变量类型，1常量，2变量 3自定义',
  `variable_value` text COMMENT '表达式节点对应字段的限定值',
  `parent_id` int(11) DEFAULT '0' COMMENT '父节点的id 此项为0的是根节点',
  `condition_type` int(2) NOT NULL COMMENT '规则节点的类型：1-关系节点，2-表达式节点',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=561 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_data_clean_filter_condition
-- ----------------------------
DROP TABLE IF EXISTS `t_data_clean_filter_condition`;
CREATE TABLE `t_data_clean_filter_condition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `data_clean_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作的id',
  `data_clean_version_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作版本的id',
  `data_clean_block_id` int(11) NOT NULL DEFAULT '0' COMMENT '条件区域块id',
  `filter_type` varchar(50) DEFAULT NULL COMMENT '条件类型：选择集合处过滤：input、集合处理规则处过滤:result。',
  `logical` varchar(10) DEFAULT NULL COMMENT '关系节点的逻辑符号：&&（并关系），||（或关系）',
  `op_type` varchar(50) DEFAULT NULL COMMENT '选择来源：original原数据，data_op原数据操作，handle_collection选择集合',
  `op_key` varchar(100) DEFAULT NULL COMMENT '计算的参数key{循环中对象的key}',
  `operator` varchar(50) DEFAULT NULL COMMENT '表达式节点的操作符',
  `variable_type` int(2) DEFAULT '1' COMMENT '变量类型，1常量，2变量 3自定义',
  `variable_value` text COMMENT '表达式节点对应字段的限定值',
  `parent_id` int(11) DEFAULT '0' COMMENT '父节点的id 此项为0的是根节点',
  `condition_type` int(2) NOT NULL COMMENT '规则节点的类型：1-关系节点，2-表达式节点',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=442 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_data_clean_original_data_op
-- ----------------------------
DROP TABLE IF EXISTS `t_data_clean_original_data_op`;
CREATE TABLE `t_data_clean_original_data_op` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `data_clean_version_id` bigint(20) NOT NULL COMMENT '数据清洗的版本id',
  `op_type` varchar(20) DEFAULT NULL COMMENT '操作类型：1.首元素：first_element,2.尾元素：last_element,3.迭代处理：iteration ',
  `op_field` varchar(100) DEFAULT NULL COMMENT '操作字段：此字段为数据清洗版本中存储的源数据指标内部字段',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_data_clean_output
-- ----------------------------
DROP TABLE IF EXISTS `t_data_clean_output`;
CREATE TABLE `t_data_clean_output` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `data_clean_version_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作版本id',
  `data_clean_condition_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作条件根节点id',
  `data_clean_block_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作块id',
  `output_type` int(11) DEFAULT NULL COMMENT '输出类型：1 命中输出，2未命中输出,3默认输出',
  `output_key` varchar(200) DEFAULT NULL COMMENT '输出的key',
  `op_type` varchar(100) DEFAULT NULL COMMENT '选择来源：original原数据，data_op原数据操作，handle_collection选择集合',
  `output_value` text COMMENT '输出的值',
  `variable_type` int(11) DEFAULT NULL COMMENT '输出类型： 1.常量 2.变量 3.自定义',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=413 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_data_clean_version
-- ----------------------------
DROP TABLE IF EXISTS `t_data_clean_version`;
CREATE TABLE `t_data_clean_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据清洗版本表id',
  `data_clean_id` int(11) NOT NULL COMMENT '数据清洗id',
  `version_code` varchar(50) NOT NULL COMMENT '版本code',
  `description` varchar(200) DEFAULT NULL COMMENT '版本描述',
  `input_field_en` varchar(200) DEFAULT NULL COMMENT '数据源数组或者map的en',
  `input_field_type` varchar(200) DEFAULT NULL COMMENT '数据源类型：map、list',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 ，1启用，0停用',
  `result_field_en` varchar(255) DEFAULT NULL COMMENT '存放执行结果的变量',
  `organ_id` int(11) NOT NULL COMMENT '所属组织id',
  `create_user_id` int(20) NOT NULL COMMENT '创建者id',
  `update_user_id` int(20) DEFAULT NULL COMMENT '修改者id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `snapshot` json DEFAULT NULL COMMENT '集合操作版本配置快照',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_decision_tables
-- ----------------------------
DROP TABLE IF EXISTS `t_decision_tables`;
CREATE TABLE `t_decision_tables` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '决策表id',
  `parent_id` int(11) DEFAULT NULL COMMENT '文件夹id',
  `name` varchar(100) DEFAULT NULL COMMENT '决策表名称',
  `code` varchar(50) DEFAULT NULL COMMENT '决策表代码(英文)',
  `description` text COMMENT '决策表描述',
  `version` varchar(10) DEFAULT NULL COMMENT '决策表版本',
  `status` int(2) DEFAULT '1' COMMENT '决策表状态：0 停用 1 启用 -1删除（默认启用）',
  `creator` int(20) DEFAULT NULL COMMENT '决策表创建者',
  `modifier` int(20) DEFAULT NULL COMMENT '决策表修改者',
  `organ_id` int(11) DEFAULT NULL COMMENT '所属组织id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `result_field_en` varchar(255) DEFAULT NULL COMMENT '存放执行结果的变量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_decision_tables_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_decision_tables_detail`;
CREATE TABLE `t_decision_tables_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '详情节点id',
  `decision_tables_id` int(11) DEFAULT NULL COMMENT '决策表id',
  `version_id` int(11) NOT NULL DEFAULT '0' COMMENT '决策表版本id',
  `dimensionality` int(2) DEFAULT NULL COMMENT '条件维度(1-左侧，2-顶部)',
  `field_id` int(11) DEFAULT NULL COMMENT '字段id',
  `field_en` varchar(50) DEFAULT NULL COMMENT '字段en',
  `parent_id` int(11) DEFAULT NULL COMMENT '父节点id',
  `logical` varchar(50) DEFAULT NULL COMMENT '逻辑关系，如（&&，||）',
  `type` int(2) DEFAULT NULL COMMENT '节点类型：1-普通节点，2-叶子节点',
  `index_value` int(11) DEFAULT NULL COMMENT '所在维度的值，不能为负数',
  `content` text COMMENT '执行串',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5535 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_decision_tables_detail_condition
-- ----------------------------
DROP TABLE IF EXISTS `t_decision_tables_detail_condition`;
CREATE TABLE `t_decision_tables_detail_condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '决策表详情条件id',
  `detail_id` int(11) DEFAULT NULL COMMENT '详情id',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作符',
  `variable_type` int(2) DEFAULT NULL COMMENT '变量类型：1常量，2变量',
  `field_value` longtext NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6856 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_decision_tables_result
-- ----------------------------
DROP TABLE IF EXISTS `t_decision_tables_result`;
CREATE TABLE `t_decision_tables_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '决策表结果集id',
  `decision_tables_id` int(11) DEFAULT NULL COMMENT '决策表id',
  `version_id` int(11) NOT NULL DEFAULT '0' COMMENT '决策表版本id',
  `rows` int(11) DEFAULT NULL COMMENT '行数',
  `columns` int(11) DEFAULT NULL COMMENT '列数',
  `result_value` longtext COMMENT '结果集二维数组',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=258 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_decision_tables_version
-- ----------------------------
DROP TABLE IF EXISTS `t_decision_tables_version`;
CREATE TABLE `t_decision_tables_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '版本主鍵id',
  `decision_tables_id` int(11) NOT NULL COMMENT '决策表id',
  `version_code` varchar(255) NOT NULL COMMENT '版本号',
  `description` varchar(255) NOT NULL COMMENT '版本描述',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 ，1启用，0停用',
  `result_field_en` varchar(255) DEFAULT NULL COMMENT '存放执行结果的变量',
  `organ_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属组织id',
  `create_user_id` int(20) NOT NULL DEFAULT '0' COMMENT '创建者id',
  `update_user_id` int(20) NOT NULL DEFAULT '0' COMMENT '修改者id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `snapshot` json DEFAULT NULL COMMENT '决策表版本配置快照',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_decision_tree
-- ----------------------------
DROP TABLE IF EXISTS `t_decision_tree`;
CREATE TABLE `t_decision_tree` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '决策树主表id',
  `code` varchar(255) NOT NULL COMMENT '决策树code',
  `name` varchar(255) NOT NULL COMMENT '决策树名称',
  `description` varchar(255) DEFAULT NULL COMMENT '决策树描述',
  `folder_id` int(11) NOT NULL DEFAULT '0' COMMENT '文件夹id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态：0 停用 1 启用 -1删除（默认启用）',
  `create_user_id` int(11) NOT NULL COMMENT '创建者id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改者id',
  `organ_id` int(11) NOT NULL COMMENT '组织id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_decision_tree_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_decision_tree_detail`;
CREATE TABLE `t_decision_tree_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '决策树详情id',
  `decision_tree_version_id` int(11) DEFAULT NULL COMMENT '决策表id',
  `field_id` int(11) DEFAULT NULL COMMENT '字段id',
  `field_en` varchar(50) DEFAULT NULL COMMENT '字段en',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父节点id',
  `logical` varchar(50) DEFAULT NULL COMMENT '逻辑关系，如（&&，||）',
  `node_type` int(2) DEFAULT NULL COMMENT '节点类型：1-普通节点，2-叶子节点',
  `result_value` text COMMENT '叶子节点对应决策结果值',
  `variable_type` int(4) NOT NULL COMMENT '结果值1、常量 2、变量 3、自定义',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=248 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_decision_tree_detail_condition
-- ----------------------------
DROP TABLE IF EXISTS `t_decision_tree_detail_condition`;
CREATE TABLE `t_decision_tree_detail_condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '决策树详情条件id',
  `detail_id` int(11) NOT NULL COMMENT '详情id',
  `operator` varchar(50) NOT NULL COMMENT '操作符',
  `variable_type` int(2) NOT NULL DEFAULT '1' COMMENT '变量类型：1常量，2变量',
  `field_value` text NOT NULL COMMENT '变量值（常量为值，变量为字段en）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=296 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_decision_tree_version
-- ----------------------------
DROP TABLE IF EXISTS `t_decision_tree_version`;
CREATE TABLE `t_decision_tree_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '决策树版本表id',
  `decision_tree_id` int(11) NOT NULL COMMENT '决策树id',
  `version_code` varchar(255) NOT NULL COMMENT '版本code',
  `description` varchar(255) DEFAULT NULL COMMENT '版本描述',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 ，1启用，0停用',
  `result_field_en` varchar(255) DEFAULT NULL COMMENT '存放执行结果的变量',
  `organ_id` int(11) NOT NULL COMMENT '所属组织id',
  `create_user_id` int(20) NOT NULL COMMENT '创建者id',
  `update_user_id` int(20) DEFAULT NULL COMMENT '修改者id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `snapshot` json DEFAULT NULL COMMENT '决策树版本配置快照',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `t_dictionary`;
CREATE TABLE `t_dictionary` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `dict_key` varchar(200) NOT NULL COMMENT '字典key',
  `dict_value` text COMMENT '字典value',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_idx` (`dict_key`) USING BTREE COMMENT '字典key唯一'
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_email_template
-- ----------------------------
DROP TABLE IF EXISTS `t_email_template`;
CREATE TABLE `t_email_template` (
  `template_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `subject` varchar(100) NOT NULL COMMENT '标题',
  `nid` varchar(100) NOT NULL DEFAULT '' COMMENT '模板唯一的nid',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '模板状态，1:有效、0:无效',
  `address` varchar(200) NOT NULL COMMENT '收件地址',
  `content` text NOT NULL COMMENT '模板内容',
  `use_type` tinyint(2) NOT NULL DEFAULT '1' COMMENT '发送类型，1:通知',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`template_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='邮件模板';

-- ----------------------------
-- Table structure for t_engine
-- ----------------------------
DROP TABLE IF EXISTS `t_engine`;
CREATE TABLE `t_engine` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(64) DEFAULT NULL COMMENT '编号',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `engine_type` varchar(50) DEFAULT 'rule_engine' COMMENT '引擎类型：',
  `status` int(11) DEFAULT NULL COMMENT '状态：0被删除1正常使用',
  `create_datetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_datetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `organ_id` int(11) DEFAULT NULL COMMENT '企业编号',
  `user_id` int(11) DEFAULT NULL COMMENT '修改人',
  `callback_type` int(4) DEFAULT '1' COMMENT '调用方式 1：同步，2：异步',
  `callback_url` varchar(200) DEFAULT NULL COMMENT '回调地址',
  `exception_callback_url` varchar(200) DEFAULT NULL COMMENT '异常回调地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=358 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_engine_node
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_node`;
CREATE TABLE `t_engine_node` (
  `node_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '节点信息编号',
  `version_id` int(11) DEFAULT NULL COMMENT '版本编号',
  `node_name` varchar(100) NOT NULL COMMENT '节点名称',
  `node_code` varchar(100) NOT NULL COMMENT '节点代号',
  `node_order` int(11) DEFAULT NULL COMMENT '节点顺序',
  `node_type` int(11) DEFAULT NULL COMMENT '节点类型',
  `node_json` longtext COMMENT '节点信息',
  `node_x` decimal(7,2) DEFAULT NULL COMMENT '节点横坐标',
  `node_y` decimal(7,2) DEFAULT NULL COMMENT '节点纵坐标',
  `node_script` text COMMENT '节点脚本',
  `next_nodes` longtext COMMENT '下个节点(可能是多个)',
  `params` longtext COMMENT '节点用到的参数列表',
  `parent_id` varchar(100) DEFAULT NULL COMMENT '上一个节点的id，多个节点逗号分隔',
  `snapshot` json DEFAULT NULL COMMENT '节点配置快照',
  PRIMARY KEY (`node_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4955 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='存储版本模型的信息包括(使用的参数，节点的位置，节点的执行逻辑)';

-- ----------------------------
-- Table structure for t_engine_version
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_version`;
CREATE TABLE `t_engine_version` (
  `version_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '版本编号',
  `engine_id` int(11) DEFAULT NULL COMMENT '引擎编号',
  `engine_type` varchar(50) DEFAULT NULL COMMENT '引擎类型',
  `version` int(11) NOT NULL COMMENT '版本',
  `sub_version` int(5) DEFAULT NULL COMMENT '子版本',
  `boot_state` smallint(1) NOT NULL COMMENT '是否部署(0:未部署1:正在运行,2申请部署)',
  `status` smallint(1) DEFAULT NULL COMMENT '是否删除(0:在回收站中,可恢复,1:正常,2彻底删除)',
  `layout` smallint(1) DEFAULT NULL COMMENT '布局方式(1,2,预留通用布局方式,0自定义布局)',
  `user_id` int(11) DEFAULT NULL COMMENT '创建者',
  `create_time` varchar(100) DEFAULT NULL COMMENT '创建时间',
  `latest_user` int(11) DEFAULT NULL COMMENT '最新修改者',
  `latest_time` varchar(100) DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`version_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=679 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='不同场景下的多个模型版本';

-- ----------------------------
-- Table structure for t_engine_version_content
-- ----------------------------
DROP TABLE IF EXISTS `t_engine_version_content`;
CREATE TABLE `t_engine_version_content` (
  `engine_version_id` int(11) NOT NULL COMMENT '引擎版本id',
  `engine_type` varchar(50) DEFAULT NULL COMMENT '引擎类型',
  `engine_content` text COMMENT '引擎内容',
  `engine_script` text COMMENT '引擎执行内容',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改人id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`engine_version_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_field
-- ----------------------------
DROP TABLE IF EXISTS `t_field`;
CREATE TABLE `t_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字段编号',
  `field_en` varchar(100) NOT NULL DEFAULT '' COMMENT '字段英文名',
  `field_cn` varchar(100) NOT NULL COMMENT '字段中文名',
  `field_typeid` int(11) NOT NULL COMMENT '字段类型编号,来源t_fieldtype表主键',
  `value_type` int(4) DEFAULT NULL COMMENT '字段存值类型,待选(0),数值型(1),字符型(2),枚举型(3),小数型(4),原数组形(5),json形(6)',
  `value_scope` varchar(255) DEFAULT NULL COMMENT '字段约束范围',
  `is_derivative` int(4) NOT NULL DEFAULT '0' COMMENT '是否衍生字段，0代表不是，1代表是，默认不是(0)',
  `is_output` int(4) NOT NULL DEFAULT '0' COMMENT '是否输出字段，0代表不是，1代表是，默认不是(0)',
  `is_common` int(4) NOT NULL DEFAULT '0' COMMENT '是否组织定义的通用字段，0代表不是，1代表是，默认不是(0)',
  `formula` text COMMENT '公式',
  `formula_show` text COMMENT '公式回显信息存值',
  `used_fieldid` varchar(200) DEFAULT NULL COMMENT '该衍生字段引用的字段id，逗号分割',
  `orig_fieldid` varchar(200) DEFAULT NULL COMMENT '衍生字段用到的所有原生字段编号，逗号分割',
  `author` int(11) NOT NULL COMMENT '创建人',
  `organ_id` int(11) DEFAULT NULL COMMENT '组织id',
  `created` datetime NOT NULL COMMENT '创建时间',
  `is_use_sql` tinyint(1) DEFAULT '0' COMMENT '是否使用sql',
  `data_source_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `sql_statement` text COMMENT 'sql语句',
  `sql_variable` varchar(200) DEFAULT NULL COMMENT 'sql变量配置',
  `is_interface` tinyint(1) DEFAULT '0' COMMENT '是否使用接口',
  `interface_id` int(11) DEFAULT NULL COMMENT '接口id',
  `interface_parse_field` varchar(100) DEFAULT NULL COMMENT '接口指标解析字段',
  `json_value` longtext COMMENT 'json类型对应的json值',
  `dict_variable` text COMMENT '字典变量如：日期字符串',
  `mq_source_id` int(11) DEFAULT NULL COMMENT '消息队列源id',
  `source_type` int(11) DEFAULT '1' COMMENT '指标的类型：(默认)1基础类型，2sql类型，3衍生类型，4.接口 5.常量指标，6实时指标',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1157 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户字段表';

-- ----------------------------
-- Table structure for t_field_call_log
-- ----------------------------
DROP TABLE IF EXISTS `t_field_call_log`;
CREATE TABLE `t_field_call_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `field_id` int(11) NOT NULL COMMENT '指标id',
  `field_type` enum('basics','const','database','interface','derive') DEFAULT NULL COMMENT '指标类型',
  `source_type` varchar(20) DEFAULT NULL COMMENT '数据源类型 mysql、redis、interface',
  `source_id` int(11) DEFAULT NULL COMMENT '数据源id',
  `input_param` text COMMENT '入参',
  `field_value` text COMMENT '获取到的指标值',
  `duration` int(11) NOT NULL COMMENT '消耗时长，单位：毫秒',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `field_idx` (`field_id`,`create_time`) USING BTREE COMMENT '指标id索引'
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_field_condition
-- ----------------------------
DROP TABLE IF EXISTS `t_field_condition`;
CREATE TABLE `t_field_condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '条件编号',
  `field_id` int(11) NOT NULL COMMENT '字段字段编号',
  `condition_value` varchar(100) NOT NULL COMMENT '字段条件值',
  `content` text NOT NULL COMMENT '字段条件区域设置json格式',
  `cond_field_id` int(11) DEFAULT NULL,
  `cond_field_operator` varchar(100) DEFAULT NULL,
  `cond_field_value` varchar(100) DEFAULT NULL,
  `cond_field_logical` varchar(100) DEFAULT NULL,
  `created` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户字段条件区域设置表';

-- ----------------------------
-- Table structure for t_field_data_source
-- ----------------------------
DROP TABLE IF EXISTS `t_field_data_source`;
CREATE TABLE `t_field_data_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '数据库连接名称',
  `type` varchar(50) NOT NULL DEFAULT 'MySQL' COMMENT '数据源类型：MySQL、Oracle、SQLServer、Hive、Spark、Redis',
  `url` varchar(200) DEFAULT NULL COMMENT '连接地址',
  `host` varchar(200) DEFAULT NULL COMMENT '数据库地址',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `port` varchar(100) DEFAULT NULL COMMENT '端口',
  `db_name` varchar(100) DEFAULT NULL COMMENT '数据库名称',
  `spark_home` varchar(200) DEFAULT NULL COMMENT 'spark路径',
  `app_name` varchar(200) DEFAULT NULL COMMENT 'spark应用程序的名称',
  `master_url` varchar(200) DEFAULT NULL COMMENT 'spark地址',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态 0：无效，1：有效',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `modifier` int(11) DEFAULT NULL COMMENT '修改人',
  `organ_id` int(11) NOT NULL COMMENT '企业编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='指标数据源表';

-- ----------------------------
-- Table structure for t_field_interface
-- ----------------------------
DROP TABLE IF EXISTS `t_field_interface`;
CREATE TABLE `t_field_interface` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '接口id',
  `name` varchar(20) DEFAULT NULL COMMENT '接口名称',
  `url` varchar(200) DEFAULT NULL COMMENT '请求地址',
  `method` char(10) DEFAULT NULL COMMENT '请求方法',
  `request_headers` varchar(200) DEFAULT NULL COMMENT '请求体类型',
  `request_body` text COMMENT '请求参数体',
  `bind_param` text COMMENT '绑定参数',
  `response_body` text COMMENT '请求响应体',
  `creator` int(11) NOT NULL COMMENT '创建者id',
  `modifier` int(11) DEFAULT NULL COMMENT '修改者id',
  `organ_id` int(11) DEFAULT NULL COMMENT '企业编号',
  `status` int(11) DEFAULT NULL COMMENT '请求状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `request_type` varchar(20) DEFAULT NULL COMMENT '请求体类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_field_mq_source
-- ----------------------------
DROP TABLE IF EXISTS `t_field_mq_source`;
CREATE TABLE `t_field_mq_source` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(100) NOT NULL COMMENT '消息队列连接名称',
  `type` varchar(50) NOT NULL DEFAULT 'kafka' COMMENT '消息队列类型：kafka',
  `server_addrs` varchar(200) DEFAULT NULL COMMENT '连接地址',
  `topic` varchar(200) DEFAULT NULL COMMENT '数据库地址',
  `group_id` varchar(100) DEFAULT NULL COMMENT '用户名',
  `auto_offset_reset` varchar(100) DEFAULT 'earliest' COMMENT 'offset设置:earliest，latest，none',
  `enable_auto_commit` tinyint(1) DEFAULT '1' COMMENT '自动提交0:false 1:true',
  `timeout` int(11) DEFAULT '20000' COMMENT '超时时间',
  `auto_commit_interval` int(11) DEFAULT '100' COMMENT '自动提交延时',
  `concurrency` int(2) DEFAULT '1' COMMENT '消费线程数',
  `key_deserializer` varchar(200) DEFAULT 'org.apache.kafka.common.serialization.StringDeserializer' COMMENT 'key解析器默认：StringDeserializer',
  `value_deserializer` varchar(200) DEFAULT 'org.apache.kafka.common.serialization.StringDeserializer' COMMENT 'value解码器默认：StringDeserializer',
  `message_body` text COMMENT '消息体类型',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态 0：无效，1：有效，-1删除',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `modifier` int(11) DEFAULT NULL COMMENT '修改人',
  `organ_id` int(11) NOT NULL COMMENT '企业编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_field_type
-- ----------------------------
DROP TABLE IF EXISTS `t_field_type`;
CREATE TABLE `t_field_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字段类型编号',
  `field_type` varchar(100) NOT NULL COMMENT '字段类型名',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父节点编号',
  `is_common` int(4) NOT NULL DEFAULT '0' COMMENT '是否组织定义的通用字段类型',
  `type` int(4) DEFAULT '1' COMMENT '指标的类型：(默认)1基础类型，2sql类型，3衍生类型，4.接口 5.常量指标',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=430 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字段类型表';

-- ----------------------------
-- Table structure for t_field_type_user_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_field_type_user_rel`;
CREATE TABLE `t_field_type_user_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `field_typeid` int(11) NOT NULL COMMENT '字段类型编号',
  `organ_id` int(11) NOT NULL COMMENT '归属的组织编号',
  `engine_id` int(11) DEFAULT NULL COMMENT '引擎表主键id，用来与引擎绑定，该字段为空代表组织内通用字段',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '启用、删除标志，启用用1表示，删除用-1表示，默认启用',
  `user_id` int(11) NOT NULL COMMENT '创建该字段类型的用户编号',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '该字段分类的创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3006 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字段类型表';

-- ----------------------------
-- Table structure for t_field_user_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_field_user_rel`;
CREATE TABLE `t_field_user_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户字段映射关系表主键',
  `field_id` int(11) NOT NULL COMMENT '字段编号',
  `organ_id` int(11) DEFAULT NULL COMMENT '归属的组织编号',
  `engine_id` int(11) DEFAULT NULL COMMENT '引擎表主键id，用来与引擎绑定，该字段为空代表组织内通用字段',
  `user_id` int(11) NOT NULL COMMENT '创建或修改该字段的用户编号',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '启用停用删除标志，启用用1表示，停用用0表示，删除用-1表示，默认启用',
  `created` datetime NOT NULL COMMENT '创建时间',
  `updated` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10767 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户字段关联关系表';

-- ----------------------------
-- Table structure for t_knowledge_tree
-- ----------------------------
DROP TABLE IF EXISTS `t_knowledge_tree`;
CREATE TABLE `t_knowledge_tree` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) DEFAULT NULL COMMENT '目录名称',
  `parent_id` int(11) DEFAULT NULL COMMENT '父节点id',
  `user_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `organ_id` int(11) DEFAULT NULL COMMENT '组织id',
  `engine_id` int(11) DEFAULT NULL COMMENT '引擎id',
  `status` int(2) DEFAULT NULL COMMENT '状态    0：停用  ，1：启用 ，-1 ：删除',
  `type` int(2) DEFAULT NULL COMMENT '目录类型  0 : 系统的目录  1：组织的目录 2： 引擎的目录',
  `tree_type` int(2) DEFAULT NULL COMMENT '树形分类：0：基础规则树  1：评分卡的树 2：回收站的树 3：决策表树，4:决策树的树5：复杂规则树,6.名单库，7脚本规则集',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `updated` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1530 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='知识库目录表';

-- ----------------------------
-- Table structure for t_list_db
-- ----------------------------
DROP TABLE IF EXISTS `t_list_db`;
CREATE TABLE `t_list_db` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '名单库编号',
  `list_code` varchar(50) DEFAULT NULL COMMENT '所属文件夹id',
  `list_type` varchar(10) NOT NULL COMMENT '名单库区分,用b表示黑名单,w表示白名单,其它待扩展',
  `list_name` varchar(100) NOT NULL COMMENT '名单库名称',
  `data_source` int(4) DEFAULT NULL COMMENT '数据来源：外部黑(白)名单（1）、内部黑(白)名单（2）、待选（0）',
  `list_attr` varchar(100) DEFAULT NULL COMMENT '名单库类型属性,用户输入',
  `list_desc` varchar(100) DEFAULT NULL COMMENT '名单库描述',
  `table_column` varchar(200) NOT NULL COMMENT '名单库表中列字段，字段id逗号分隔',
  `match_type` int(4) DEFAULT NULL COMMENT '检索匹配类型，精确匹配（1），模糊匹配（0）',
  `query_type` int(4) DEFAULT NULL COMMENT '查询字段间逻辑，and（1），or（0）',
  `query_field` varchar(200) DEFAULT NULL COMMENT '查询主键，字段编号逗号分割',
  `organ_id` int(11) DEFAULT NULL COMMENT '归属的组织编号',
  `status` int(4) NOT NULL DEFAULT '0' COMMENT '启用（1），停用（0），删除（-1）',
  `user_id` int(11) NOT NULL COMMENT '创建该名单的用户编号',
  `created` datetime NOT NULL COMMENT '创建时间',
  `result_field_en` varchar(255) DEFAULT NULL COMMENT '名单库命中结果的en',
  `snapshot` json DEFAULT NULL COMMENT '名单库版本配置快照',
  `folder_id` int(11) NOT NULL DEFAULT '0' COMMENT '所属文件夹id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='黑白名单库配置记录表';

-- ----------------------------
-- Table structure for t_list_db_version
-- ----------------------------
DROP TABLE IF EXISTS `t_list_db_version`;
CREATE TABLE `t_list_db_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '名单库版本表id',
  `list_db_id` int(11) NOT NULL COMMENT '名单库id',
  `version_code` varchar(255) DEFAULT NULL COMMENT '版本号',
  `description` varchar(255) DEFAULT NULL COMMENT '版本描述',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 ，1启用，0停用',
  `result_field_en` varchar(255) DEFAULT NULL COMMENT '固定输出的指标en',
  `table_column` varchar(200) NOT NULL COMMENT '名单库表中列字段，字段id逗号分隔',
  `match_type` int(4) DEFAULT NULL COMMENT '检索匹配类型，精确匹配（1），模糊匹配（0）',
  `query_type` int(4) DEFAULT NULL COMMENT '查询字段间逻辑，and（1），or（0）',
  `query_field` varchar(200) DEFAULT NULL COMMENT '查询主键，字段编号逗号分割',
  `organ_id` int(11) NOT NULL COMMENT '所属组织id',
  `create_user_id` int(11) NOT NULL COMMENT '创建者id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改者id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `snapshot` json DEFAULT NULL COMMENT '名单库版本配置快照',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_list_operation
-- ----------------------------
DROP TABLE IF EXISTS `t_list_operation`;
CREATE TABLE `t_list_operation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(200) DEFAULT NULL COMMENT '集合操作名称',
  `code` varchar(200) DEFAULT NULL COMMENT '集合操作代码',
  `description` text COMMENT '集合操作描述',
  `op_type` int(4) DEFAULT NULL COMMENT '集合操作类型：1 集合规则，2 集合处理',
  `folder_id` int(11) NOT NULL DEFAULT '0' COMMENT '文件夹id',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态：0 停用 1 启用 -1删除（默认启用）',
  `start_time` datetime DEFAULT NULL COMMENT '有效期开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `create_user_id` int(11) NOT NULL COMMENT '创建者id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改者id',
  `organ_id` int(11) NOT NULL COMMENT '组织id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_list_operation_block
-- ----------------------------
DROP TABLE IF EXISTS `t_list_operation_block`;
CREATE TABLE `t_list_operation_block` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `list_op_version_id` int(11) DEFAULT NULL COMMENT '版本id',
  `name` varchar(100) DEFAULT NULL COMMENT '条件区域名称',
  `group_fields` text COMMENT '分组字段',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=224 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_list_operation_condition
-- ----------------------------
DROP TABLE IF EXISTS `t_list_operation_condition`;
CREATE TABLE `t_list_operation_condition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `list_op_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作的id',
  `list_op_version_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作版本的id',
  `list_op_block_id` int(11) NOT NULL DEFAULT '0' COMMENT '条件区域块id',
  `logical` varchar(10) DEFAULT NULL COMMENT '关系节点的逻辑符号：&&（并关系），||（或关系）',
  `op_type` varchar(50) DEFAULT NULL COMMENT '计算维度：1 count 2distinct_count 3 max  4min  5avg 6custom',
  `op_key` varchar(255) DEFAULT NULL COMMENT '计算的参数key{循环中对象的key}',
  `operator` varchar(50) DEFAULT NULL COMMENT '表达式节点的操作符',
  `variable_type` int(2) DEFAULT NULL COMMENT '变量类型，1常量，2变量 3自定义',
  `variable_value` text COMMENT '表达式节点对应字段的限定值',
  `parent_id` int(11) DEFAULT '0' COMMENT '父节点的id 此项为0的是根节点',
  `condition_type` int(2) NOT NULL COMMENT '规则节点的类型：1-关系节点，2-表达式节点',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=442 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_list_operation_filter_condition
-- ----------------------------
DROP TABLE IF EXISTS `t_list_operation_filter_condition`;
CREATE TABLE `t_list_operation_filter_condition` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `list_op_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作的id',
  `list_op_version_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作版本的id',
  `list_op_block_id` int(11) NOT NULL DEFAULT '0' COMMENT '条件区域块id',
  `logical` varchar(10) DEFAULT NULL COMMENT '关系节点的逻辑符号：&&（并关系），||（或关系）',
  `op_type` varchar(50) DEFAULT NULL COMMENT '计算维度：1 count 2distinct_count 3 max  4min  5avg 6custom',
  `op_key` varchar(255) DEFAULT NULL COMMENT '计算的参数key{循环中对象的key}',
  `operator` varchar(50) DEFAULT NULL COMMENT '表达式节点的操作符',
  `variable_type` int(2) DEFAULT '1' COMMENT '变量类型，1常量，2变量 3自定义',
  `variable_value` text COMMENT '表达式节点对应字段的限定值',
  `parent_id` int(11) DEFAULT '0' COMMENT '父节点的id 此项为0的是根节点',
  `condition_type` int(2) NOT NULL COMMENT '规则节点的类型：1-关系节点，2-表达式节点',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=232 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_list_operation_output
-- ----------------------------
DROP TABLE IF EXISTS `t_list_operation_output`;
CREATE TABLE `t_list_operation_output` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `list_op_version_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作版本id',
  `list_op_condition_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作条件根节点id',
  `list_op_block_id` int(11) NOT NULL DEFAULT '0' COMMENT '列表操作块id',
  `output_type` int(11) DEFAULT NULL COMMENT '输出类型：1 命中输出，2未命中输出,3默认输出',
  `output_key` varchar(255) DEFAULT NULL COMMENT '输出的key',
  `output_op` varchar(50) DEFAULT NULL COMMENT '输出操作：1 count 2count 去重 3 max  4min  5avg ,6 list_element',
  `output_op_key` varchar(255) DEFAULT NULL COMMENT '操作字段',
  `output_value` text COMMENT '输出的值',
  `variable_type` int(11) DEFAULT NULL COMMENT '输出类型： 1.常量 2.变量 3.自定义',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=361 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_list_operation_version
-- ----------------------------
DROP TABLE IF EXISTS `t_list_operation_version`;
CREATE TABLE `t_list_operation_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '集合操作版本表id',
  `list_op_id` int(11) NOT NULL COMMENT '集合操作id',
  `version_code` varchar(255) NOT NULL COMMENT '版本code',
  `description` varchar(255) DEFAULT NULL COMMENT '版本描述',
  `input_field_en` varchar(255) DEFAULT NULL COMMENT '数据源数组或者map的en',
  `input_field_type` varchar(255) DEFAULT NULL COMMENT '数据源类型：map、list',
  `group_fields` varchar(255) DEFAULT NULL COMMENT '分组指标列表,逗号分割的en列表',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 ，1启用，0停用',
  `result_field_en` varchar(255) DEFAULT NULL COMMENT '存放执行结果的变量',
  `organ_id` int(11) NOT NULL COMMENT '所属组织id',
  `create_user_id` int(20) NOT NULL COMMENT '创建者id',
  `update_user_id` int(20) DEFAULT NULL COMMENT '修改者id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `snapshot` json DEFAULT NULL COMMENT '集合操作版本配置快照',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_logger
-- ----------------------------
DROP TABLE IF EXISTS `t_logger`;
CREATE TABLE `t_logger` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `op_type` varchar(200) DEFAULT NULL COMMENT '操作类型',
  `op_name` varchar(500) DEFAULT NULL COMMENT '操作名称',
  `op_user_id` int(11) DEFAULT NULL COMMENT '操作人员id',
  `organ_id` int(11) DEFAULT NULL COMMENT '组织id',
  `method` varchar(500) DEFAULT NULL COMMENT '方法名',
  `request_path` text COMMENT '请求地址',
  `request_param` text COMMENT '请求参数',
  `response_param` longtext COMMENT '响应参数',
  `ip` varchar(200) DEFAULT NULL COMMENT 'ip地址',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31583 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_machine_learning_models
-- ----------------------------
DROP TABLE IF EXISTS `t_machine_learning_models`;
CREATE TABLE `t_machine_learning_models` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `model_name` varchar(100) NOT NULL COMMENT '模型名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `model_type` varchar(50) NOT NULL DEFAULT 'pmml' COMMENT '模型类型',
  `file_name` varchar(100) NOT NULL COMMENT '模型文件名称',
  `file_path` varchar(200) NOT NULL COMMENT '模型文件路径',
  `model_field` varchar(500) NOT NULL DEFAULT '' COMMENT '模型解析字段',
  `mapping_field` varchar(500) NOT NULL DEFAULT '' COMMENT '模型字段对应的系统指标字段',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态 0：无效，1：有效',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `modifier` int(11) DEFAULT NULL COMMENT '修改人',
  `organ_id` int(11) NOT NULL COMMENT '企业编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `result_field_en` varchar(255) DEFAULT NULL COMMENT '模型执行结果的en',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_marketing_engine_node_date_result
-- ----------------------------
DROP TABLE IF EXISTS `t_marketing_engine_node_date_result`;
CREATE TABLE `t_marketing_engine_node_date_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `engine_id` int(11) NOT NULL COMMENT '引擎id',
  `engine_version_id` int(11) NOT NULL COMMENT '引擎版本id',
  `engine_name` varchar(100) NOT NULL COMMENT '引擎名称',
  `node_id` int(11) NOT NULL COMMENT '节点id',
  `current_date` date NOT NULL COMMENT '当前日期',
  `enter_num` int(11) NOT NULL DEFAULT '0' COMMENT '累计进入数',
  `touch_num` int(11) NOT NULL DEFAULT '0' COMMENT '累计触发数',
  `complete_num` int(11) NOT NULL DEFAULT '0' COMMENT '累计目标完成数',
  `complete_rate` float(4,2) NOT NULL DEFAULT '0.00' COMMENT '目标完成率',
  `organ_id` int(11) NOT NULL COMMENT '组织编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='营销引擎节点当天结果表';

-- ----------------------------
-- Table structure for t_marketing_engine_node_result
-- ----------------------------
DROP TABLE IF EXISTS `t_marketing_engine_node_result`;
CREATE TABLE `t_marketing_engine_node_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `engine_id` int(11) NOT NULL COMMENT '引擎id',
  `engine_version_id` int(11) NOT NULL COMMENT '引擎版本id',
  `engine_name` varchar(100) NOT NULL COMMENT '引擎名称',
  `node_id` int(11) NOT NULL COMMENT '节点id',
  `enter_num` int(11) NOT NULL DEFAULT '0' COMMENT '累计进入数',
  `touch_num` int(11) NOT NULL DEFAULT '0' COMMENT '累计触发数',
  `complete_num` int(11) NOT NULL DEFAULT '0' COMMENT '累计目标完成数',
  `complete_rate` float(4,2) NOT NULL DEFAULT '0.00' COMMENT '目标完成率',
  `organ_id` int(11) NOT NULL COMMENT '组织编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='营销引擎节点结果表';

-- ----------------------------
-- Table structure for t_marketing_engine_result
-- ----------------------------
DROP TABLE IF EXISTS `t_marketing_engine_result`;
CREATE TABLE `t_marketing_engine_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `engine_id` int(11) NOT NULL COMMENT '引擎id',
  `engine_version_id` int(11) NOT NULL COMMENT '引擎版本id',
  `engine_name` varchar(100) NOT NULL COMMENT '引擎名称',
  `enter_num` int(11) NOT NULL DEFAULT '0' COMMENT '累计进入数',
  `touch_num` int(11) NOT NULL DEFAULT '0' COMMENT '累计触发数',
  `complete_num` int(11) NOT NULL DEFAULT '0' COMMENT '累计目标完成数',
  `complete_rate` float(4,2) NOT NULL DEFAULT '0.00' COMMENT '目标完成率',
  `organ_id` int(11) NOT NULL COMMENT '组织编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `engine_version_id` (`engine_version_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='营销引擎结果表';

-- ----------------------------
-- Table structure for t_message_send_record
-- ----------------------------
DROP TABLE IF EXISTS `t_message_send_record`;
CREATE TABLE `t_message_send_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `touch_type` varchar(30) NOT NULL COMMENT '触达方式 Sms、App、WebHook、WeChat',
  `user_id` int(11) NOT NULL COMMENT '触达用户id',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `template_code` varchar(100) NOT NULL COMMENT '模板编码',
  `send_status` int(2) NOT NULL DEFAULT '0' COMMENT '发送状态 0：未处理，1：已发送，2：发送成功，-1：发送失败',
  `send_content` varchar(500) NOT NULL COMMENT '发送内容',
  `organ_id` int(11) NOT NULL COMMENT '组织编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息发送记录表';

-- ----------------------------
-- Table structure for t_monitor_engine
-- ----------------------------
DROP TABLE IF EXISTS `t_monitor_engine`;
CREATE TABLE `t_monitor_engine` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_id` varchar(200) DEFAULT NULL COMMENT '业务id',
  `monitor_parent_id` varchar(50) NOT NULL COMMENT '执行结果id',
  `engine_id` int(11) DEFAULT NULL COMMENT '引擎id',
  `engine_name` varchar(200) DEFAULT NULL,
  `organ_id` int(11) DEFAULT NULL COMMENT '组织id',
  `engine_version_id` int(11) DEFAULT NULL COMMENT '引擎版本id',
  `process` varchar(500) DEFAULT NULL COMMENT '决策流执行轨迹',
  `snapshot` json DEFAULT NULL COMMENT '决策引擎快照信息',
  `input` text COMMENT '入参',
  `output` longtext COMMENT '出参',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='决策流监控';

-- ----------------------------
-- Table structure for t_monitor_node
-- ----------------------------
DROP TABLE IF EXISTS `t_monitor_node`;
CREATE TABLE `t_monitor_node` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_id` varchar(200) DEFAULT NULL COMMENT '业务id',
  `monitor_parent_id` varchar(50) NOT NULL COMMENT '引擎监控id',
  `engine_id` int(11) DEFAULT NULL COMMENT '引擎id',
  `organ_id` int(11) DEFAULT NULL COMMENT '组织id',
  `engine_version_id` int(11) DEFAULT NULL COMMENT '引擎版本id',
  `node_id` int(11) DEFAULT NULL COMMENT '节点id',
  `node_name` varchar(200) DEFAULT NULL COMMENT '节点名称',
  `node_type` varchar(4) DEFAULT NULL COMMENT '节点类型',
  `snapshot` json DEFAULT NULL COMMENT '决策引擎快照信息',
  `input` text COMMENT '入参',
  `output` longtext COMMENT '出参',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=266 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='决策流节点层面监控';

-- ----------------------------
-- Table structure for t_monitor_strategy
-- ----------------------------
DROP TABLE IF EXISTS `t_monitor_strategy`;
CREATE TABLE `t_monitor_strategy` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_id` varchar(200) DEFAULT NULL COMMENT '业务id',
  `monitor_parent_id` varchar(50) NOT NULL COMMENT '引擎监控id',
  `engine_id` int(11) DEFAULT NULL COMMENT '引擎id',
  `organ_id` int(11) DEFAULT NULL COMMENT '组织id',
  `engine_version_id` int(11) DEFAULT NULL COMMENT '引擎版本id',
  `node_id` int(11) DEFAULT NULL COMMENT '节点id',
  `node_type` varchar(4) DEFAULT NULL COMMENT '节点类型',
  `strategy_id` int(11) DEFAULT NULL COMMENT '策略id',
  `strategy_name` varchar(200) DEFAULT NULL COMMENT '策略名称',
  `strategy_type` varchar(4) DEFAULT NULL COMMENT '策略类型',
  `snapshot` json DEFAULT NULL COMMENT '决策引擎快照信息',
  `input` longtext COMMENT '入参',
  `output` longtext COMMENT '出参',
  `result` longtext COMMENT '执行输出结果',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='决策流策略层面监控';

-- ----------------------------
-- Table structure for t_organization
-- ----------------------------
DROP TABLE IF EXISTS `t_organization`;
CREATE TABLE `t_organization` (
  `organ_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '组织编号',
  `name` varchar(100) NOT NULL COMMENT '组织名称',
  `code` varchar(100) NOT NULL COMMENT '组织代号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `telephone` varchar(100) DEFAULT NULL COMMENT '电话',
  `status` tinyint(1) DEFAULT '1' COMMENT '0禁用1启用',
  `author` varchar(100) DEFAULT NULL COMMENT '创建者',
  `birth` datetime DEFAULT NULL COMMENT '创建时间',
  `token` varchar(100) DEFAULT NULL COMMENT '唯一标识',
  PRIMARY KEY (`organ_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_personas_engine_result
-- ----------------------------
DROP TABLE IF EXISTS `t_personas_engine_result`;
CREATE TABLE `t_personas_engine_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `engine_id` int(11) NOT NULL COMMENT '引擎id',
  `engine_version_id` int(11) NOT NULL COMMENT '引擎版本id',
  `engine_name` varchar(100) NOT NULL COMMENT '引擎名称',
  `batch_no` int(11) NOT NULL COMMENT '批次号',
  `batch_num` int(11) NOT NULL COMMENT '批次数据量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_personas_engine_result_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_personas_engine_result_detail`;
CREATE TABLE `t_personas_engine_result_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `engine_id` int(11) NOT NULL COMMENT '引擎id\r\n\r\n',
  `engine_version_id` int(11) NOT NULL COMMENT '引擎版本id\r\n',
  `batch_no` int(11) NOT NULL COMMENT '批次号',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  `tag_version_id` int(11) DEFAULT NULL COMMENT '标签版本id',
  `tag_value` varchar(100) NOT NULL DEFAULT '' COMMENT '标签值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource` (
  `resource_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资源编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '分配人',
  `name` varchar(100) NOT NULL COMMENT '资源名称',
  `code` varchar(100) DEFAULT NULL COMMENT '资源代号',
  `url` varchar(100) DEFAULT NULL COMMENT '资源路径',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父节点',
  `des` varchar(100) DEFAULT NULL COMMENT '资源描述',
  `resource_system` enum('EngineX','DataX','Auth','ModuleX') NOT NULL DEFAULT 'EngineX' COMMENT '资源所在系统',
  `birth` datetime DEFAULT NULL COMMENT '创建时间',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `sort` int(4) DEFAULT '0' COMMENT '菜单顺序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_resultset
-- ----------------------------
DROP TABLE IF EXISTS `t_resultset`;
CREATE TABLE `t_resultset` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` varchar(200) DEFAULT NULL COMMENT '用户id',
  `pid` varchar(200) DEFAULT NULL COMMENT '项目id',
  `input` text COMMENT '入参',
  `output` longtext COMMENT '出参',
  `create_datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间',
  `result` varchar(200) DEFAULT NULL COMMENT '1,通过。2，拒绝',
  `engine_id` int(11) DEFAULT NULL COMMENT '引擎编号',
  `engine_version` int(11) DEFAULT NULL COMMENT '引擎版本',
  `uuid` varchar(11) DEFAULT NULL COMMENT '用户id',
  `engine_name` varchar(200) DEFAULT NULL COMMENT '引擎名称',
  `engine_code` varchar(64) DEFAULT NULL COMMENT '引擎编号',
  `type` int(2) DEFAULT NULL COMMENT '1.页面填写 2.api',
  `sub_version` int(2) DEFAULT NULL COMMENT '子版本',
  `scorecardscore` varchar(10) DEFAULT NULL COMMENT '评分卡评分',
  `batch_no` varchar(200) DEFAULT NULL COMMENT '引擎批量测试批次号',
  `datilResult` varchar(200) DEFAULT NULL COMMENT '决策详情',
  `hbase_row_key` varchar(30) DEFAULT NULL COMMENT 'hbase行键',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `create_datetime` (`create_datetime`)
) ENGINE=InnoDB AUTO_INCREMENT=21693 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色编号',
  `organ_id` bigint(20) DEFAULT NULL COMMENT '组织编号',
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `role_code` varchar(100) DEFAULT NULL COMMENT '角色代号',
  `role_desc` varchar(100) DEFAULT NULL COMMENT '角色描述',
  `author` varchar(100) DEFAULT NULL COMMENT '创建者',
  `birth` datetime DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '0禁用1启用',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_role_resource_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_role_resource_rel`;
CREATE TABLE `t_role_resource_rel` (
  `rel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色id',
  `resource_id` bigint(20) DEFAULT NULL COMMENT '资源id',
  PRIMARY KEY (`rel_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4803 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_rule`;
CREATE TABLE `t_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(200) DEFAULT NULL COMMENT '规则名称',
  `code` varchar(200) DEFAULT NULL COMMENT '规则代码',
  `description` text COMMENT '规则描述',
  `priority` int(4) DEFAULT NULL COMMENT '规则优先级',
  `parent_id` int(11) DEFAULT NULL COMMENT '父节点id',
  `author` int(11) DEFAULT NULL COMMENT '创建人id',
  `user_id` int(11) DEFAULT NULL COMMENT '修改人id',
  `organ_id` int(11) DEFAULT NULL COMMENT '组织id',
  `engine_id` int(11) DEFAULT NULL,
  `status` int(2) DEFAULT NULL COMMENT '状态    0 :停用 ，1 : 启用，-1：删除  ',
  `type` int(2) DEFAULT NULL COMMENT '规则类型  0 : 系统的规则  1：组织的规则 2： 引擎的规则',
  `is_non` int(2) DEFAULT NULL COMMENT '逻辑关系“非”，0：否 ，1：是',
  `content` longtext COMMENT '规则具体内容',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `rule_type` smallint(2) DEFAULT NULL COMMENT '0硬性拒绝规则1加减分规则',
  `rule_audit` smallint(2) DEFAULT NULL,
  `score` int(11) DEFAULT NULL COMMENT '得分',
  `score_field_en` varchar(255) DEFAULT NULL COMMENT '接收规则得分的字段en',
  `last_logical` varchar(50) DEFAULT NULL COMMENT '逻辑关系符',
  `difficulty` int(2) NOT NULL DEFAULT '1' COMMENT '1-简单规则，2-复杂规则，3-脚本规则',
  `script_type` varchar(255) DEFAULT NULL COMMENT '脚本类型：groovy，python，js',
  `result_field_en` varchar(255) DEFAULT NULL COMMENT '接收规则命中结果的字段en',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1089 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='组织规则维护表';

-- ----------------------------
-- Table structure for t_rule_condition
-- ----------------------------
DROP TABLE IF EXISTS `t_rule_condition`;
CREATE TABLE `t_rule_condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_id` int(11) NOT NULL COMMENT '规则表的id',
  `version_id` int(11) NOT NULL DEFAULT '0' COMMENT '规则版本的id',
  `logical` varchar(10) DEFAULT NULL COMMENT '关系节点的逻辑符号：&&（并关系），||（或关系）',
  `field_id` int(11) DEFAULT NULL COMMENT '表达式节点对应的字段id',
  `field_en` varchar(255) DEFAULT NULL COMMENT '指标的en，或者json的某个key形如（obj.a.b）',
  `field_type` int(2) DEFAULT '2' COMMENT '指标类型1.中间变量，2.入参变量',
  `operator` varchar(50) DEFAULT NULL COMMENT '表达式节点的操作符',
  `variable_type` int(2) DEFAULT NULL COMMENT '变量类型，1常量，2变量',
  `field_value` varchar(50) DEFAULT NULL COMMENT '表达式节点对应字段的限定值',
  `execution_logic` text COMMENT '执行逻辑，每个表达式对应一个用于执行器执行逻辑',
  `parent_id` int(11) DEFAULT NULL COMMENT '父节点的id 此项为0的是根节点',
  `condition_type` int(2) NOT NULL COMMENT '规则节点的类型：1-关系节点，2-表达式节点 3-for表达式 4-for的结果项 5条件组 6条件组的结果条件',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5758 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_rule_loop_group_action
-- ----------------------------
DROP TABLE IF EXISTS `t_rule_loop_group_action`;
CREATE TABLE `t_rule_loop_group_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '循环组动作表主键',
  `condition_for_id` int(11) NOT NULL COMMENT '对应条件表中for的id',
  `condition_group_id` int(11) NOT NULL COMMENT '对应条件表中条件id',
  `action_type` int(3) DEFAULT NULL COMMENT '动作类型 1-求和，2-赋值，3-输出变量，4-输出常量 5-过滤 6添加元素',
  `action_key` varchar(255) DEFAULT NULL COMMENT '动作的key',
  `action_value` varchar(255) DEFAULT NULL COMMENT '动作的value',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=807 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_rule_script_version
-- ----------------------------
DROP TABLE IF EXISTS `t_rule_script_version`;
CREATE TABLE `t_rule_script_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键：规则版本id',
  `rule_id` int(11) NOT NULL COMMENT '规则id',
  `version_code` varchar(255) NOT NULL COMMENT '版本号',
  `description` varchar(255) NOT NULL COMMENT '版本描述',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 ，1启用，0停用',
  `script_type` varchar(255) DEFAULT NULL COMMENT '脚本类型：groovy，python，js',
  `script_content` longtext COMMENT '脚本规则集内容json，包含脚本内容和脚本所用字段两个值',
  `organ_id` int(11) NOT NULL COMMENT '组织id',
  `create_user_id` int(11) NOT NULL COMMENT '创建者id',
  `update_user_id` int(11) NOT NULL COMMENT '修改者id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `snapshot` json DEFAULT NULL COMMENT '规则版本配置快照',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_rule_version
-- ----------------------------
DROP TABLE IF EXISTS `t_rule_version`;
CREATE TABLE `t_rule_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键：规则版本id',
  `rule_id` int(11) NOT NULL COMMENT '规则id',
  `version_code` varchar(255) NOT NULL COMMENT '版本号',
  `description` varchar(255) NOT NULL COMMENT '版本描述',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 ，1启用，0停用',
  `result_field_en` varchar(255) DEFAULT NULL COMMENT '存放执行结果的变量',
  `score` int(11) NOT NULL COMMENT '规则分数',
  `score_field_en` varchar(255) DEFAULT NULL COMMENT '存放得分的变量',
  `organ_id` int(11) NOT NULL COMMENT '组织id',
  `create_user_id` int(11) NOT NULL COMMENT '创建者id',
  `update_user_id` int(11) NOT NULL COMMENT '修改者id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `snapshot` json DEFAULT NULL COMMENT '规则版本配置快照',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_scorecard
-- ----------------------------
DROP TABLE IF EXISTS `t_scorecard`;
CREATE TABLE `t_scorecard` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `code` varchar(200) DEFAULT NULL COMMENT '代码',
  `description` text COMMENT '描述',
  `version` varchar(500) DEFAULT NULL COMMENT '版本号',
  `parent_id` int(11) DEFAULT NULL COMMENT '父节点id',
  `author` int(11) DEFAULT NULL COMMENT '创建人id',
  `user_id` int(11) DEFAULT NULL COMMENT '修改人id',
  `organ_id` int(11) DEFAULT NULL COMMENT '组织id',
  `engine_id` int(11) DEFAULT NULL COMMENT '引擎id',
  `type` int(2) DEFAULT NULL COMMENT '评分卡类型  0：系统的评分卡  1：组织的评分卡  2：引擎的评分卡',
  `status` int(2) DEFAULT NULL COMMENT '状态  0：停用 ，1：启用，-1：删除',
  `score` longtext COMMENT '得分',
  `pd` longtext,
  `odds` longtext,
  `score_calculate_type` int(2) DEFAULT '1' COMMENT '得分计算方式',
  `result_field_en` varchar(255) DEFAULT NULL COMMENT '存放执行结果的变量',
  `created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='评分卡维护表';

-- ----------------------------
-- Table structure for t_scorecard_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_scorecard_detail`;
CREATE TABLE `t_scorecard_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '内容id',
  `dimension_id` int(11) DEFAULT NULL COMMENT '评分卡id',
  `field_id` int(11) DEFAULT NULL COMMENT '指标id',
  `parent_id` int(11) DEFAULT NULL COMMENT '父节点id',
  `type` int(11) DEFAULT NULL COMMENT '0是根，1是叶子',
  `score` decimal(10,2) DEFAULT NULL COMMENT '分数：type为1时有值',
  `coefficient` decimal(10,2) DEFAULT NULL COMMENT '系数：type为2时有值',
  `custom` longtext COMMENT '自定义：存储自定义公式',
  `calculate_type` int(11) DEFAULT NULL COMMENT '计算方式：1为score，2为coefficient，3为自定义custom',
  `logical` varchar(50) DEFAULT NULL COMMENT '逻辑运算符(一对二时才有)，表示区间时使用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=948 DEFAULT CHARSET=utf8 COMMENT='评分卡明细表';

-- ----------------------------
-- Table structure for t_scorecard_detail_condition
-- ----------------------------
DROP TABLE IF EXISTS `t_scorecard_detail_condition`;
CREATE TABLE `t_scorecard_detail_condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `detail_id` int(11) DEFAULT NULL COMMENT '内容id',
  `operator` varchar(20) DEFAULT NULL COMMENT '关系运算符',
  `field_value` varchar(20) DEFAULT NULL COMMENT '指标的值(实参)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1517 DEFAULT CHARSET=utf8 COMMENT='评分卡明细表的condition表';

-- ----------------------------
-- Table structure for t_scorecard_dimension
-- ----------------------------
DROP TABLE IF EXISTS `t_scorecard_dimension`;
CREATE TABLE `t_scorecard_dimension` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `card_id` int(11) DEFAULT NULL COMMENT '评分卡id',
  `version_id` int(11) NOT NULL DEFAULT '0' COMMENT '评分卡版本id',
  `dimension_name` varchar(30) DEFAULT NULL COMMENT '维度名称',
  `weight` decimal(7,2) DEFAULT NULL COMMENT '权重',
  `execute_type` varchar(30) DEFAULT NULL COMMENT '执行方式 预留字段',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=589 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_scorecard_version
-- ----------------------------
DROP TABLE IF EXISTS `t_scorecard_version`;
CREATE TABLE `t_scorecard_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键：评分卡版本id',
  `scorecard_id` int(11) NOT NULL COMMENT '评分卡id',
  `version_code` varchar(255) NOT NULL COMMENT '版本号',
  `description` varchar(255) NOT NULL COMMENT '版本描述',
  `status` int(4) NOT NULL DEFAULT '1' COMMENT '状态：-1删除 ，1启用，0停用',
  `score_calculate_type` int(4) NOT NULL COMMENT '得分计算方式',
  `result_field_en` varchar(255) NOT NULL COMMENT '存放执行结果的变量',
  `organ_id` int(11) NOT NULL COMMENT '组织id',
  `create_user_id` int(11) NOT NULL COMMENT '创建者id',
  `update_user_id` int(11) NOT NULL COMMENT '修改者id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `snapshot` json DEFAULT NULL COMMENT '评分卡版本配置快照',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_template`;
CREATE TABLE `t_sms_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `template_code` varchar(100) NOT NULL COMMENT '模板编码',
  `sms_type` varchar(50) NOT NULL COMMENT '短信类型',
  `sms_sign` varchar(100) NOT NULL COMMENT '短信签名',
  `template_content` varchar(500) NOT NULL COMMENT '模板内容',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态 0：无效，1：有效',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `modifier` int(11) DEFAULT NULL COMMENT '修改人',
  `organ_id` int(11) NOT NULL COMMENT '组织编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `template_code` (`template_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='短信模板表';

-- ----------------------------
-- Table structure for t_strategy_output
-- ----------------------------
DROP TABLE IF EXISTS `t_strategy_output`;
CREATE TABLE `t_strategy_output` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `field_id` int(11) NOT NULL COMMENT '字段id',
  `field_en` varchar(50) NOT NULL COMMENT '字段的en',
  `field_value` longtext NOT NULL,
  `variable_type` int(2) NOT NULL DEFAULT '1' COMMENT '字段值的类型：1 常量、2 变量、3自定义',
  `strategy_id` int(11) DEFAULT NULL,
  `strategy_type` varchar(20) DEFAULT NULL,
  `out_condition` longtext COMMENT '输出条件，为null则无条件',
  `out_type` varchar(255) DEFAULT NULL COMMENT '输出的类型：success成功时候输出，fail 失败时候的输出',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7213 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='策略中心自定义输出表';

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `tag_code` varchar(100) NOT NULL COMMENT '标签代码',
  `tag_name` varchar(100) NOT NULL COMMENT '标签名称',
  `tag_desc` varchar(500) DEFAULT NULL COMMENT '标签描述',
  `folder_id` int(11) DEFAULT NULL COMMENT '文件夹id',
  `status` int(2) DEFAULT NULL COMMENT '状态：1正常，-1删除 0停用',
  `organ_id` int(11) NOT NULL COMMENT '组织id',
  `create_user_id` int(11) NOT NULL COMMENT '创建人id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改人id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_tag_version
-- ----------------------------
DROP TABLE IF EXISTS `t_tag_version`;
CREATE TABLE `t_tag_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '版本主键id',
  `tag_id` int(11) DEFAULT NULL COMMENT '标签id',
  `version_code` varchar(50) NOT NULL COMMENT '版本code',
  `version_name` varchar(50) DEFAULT NULL COMMENT '版本名称',
  `description` varchar(500) DEFAULT NULL COMMENT '版本描述',
  `status` int(2) NOT NULL COMMENT '状态：1正常，-1删除 0停用',
  `organ_id` int(11) DEFAULT NULL COMMENT '组织id',
  `create_user_id` int(11) NOT NULL COMMENT '创建人id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改人id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `snapshot` text COMMENT '快照',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_tag_version_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_tag_version_detail`;
CREATE TABLE `t_tag_version_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `tag_version_id` int(11) NOT NULL COMMENT '版本主表id',
  `tag_value` varchar(200) NOT NULL COMMENT '标签值',
  `tag_value_desc` varchar(500) DEFAULT NULL COMMENT '标签值描述',
  `tag_rule_id` bigint(20) DEFAULT NULL COMMENT '条件块id',
  `organ_id` int(11) NOT NULL COMMENT '组织id',
  `create_user_id` int(11) NOT NULL COMMENT '创建人id',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改人id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `organ_id` bigint(20) DEFAULT NULL COMMENT '组织编号',
  `employee_id` varchar(100) DEFAULT NULL COMMENT '员工编号',
  `account` varchar(100) NOT NULL COMMENT '账户（手机或者邮箱）',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nick_name` varchar(100) NOT NULL COMMENT '昵称',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `cellphone` varchar(100) DEFAULT NULL COMMENT '手机号',
  `qq` varchar(100) DEFAULT NULL COMMENT 'qq',
  `latest_time` varchar(100) DEFAULT NULL COMMENT '最后登录时间',
  `latest_ip` varchar(100) DEFAULT NULL COMMENT '最后登录ip',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT '1' COMMENT '0禁用，1启用,-1假删',
  `birth` datetime DEFAULT NULL COMMENT '创建时间',
  `author` varchar(100) DEFAULT NULL COMMENT '创建者',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role_rel`;
CREATE TABLE `t_user_role_rel` (
  `rel_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关系主键',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户编号',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色编号',
  `organ_id` bigint(20) DEFAULT NULL COMMENT '公司编号',
  `status` int(10) DEFAULT '1' COMMENT '1.启用。0.停用。-1.删除',
  PRIMARY KEY (`rel_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for t_webhook_template
-- ----------------------------
DROP TABLE IF EXISTS `t_webhook_template`;
CREATE TABLE `t_webhook_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `template_code` varchar(100) NOT NULL COMMENT '模板编码',
  `request_url` varchar(200) NOT NULL COMMENT '请求地址',
  `request_params` varchar(200) DEFAULT NULL COMMENT '请求参数（json格式）',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态 0：无效，1：有效',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `modifier` int(11) DEFAULT NULL COMMENT '修改人',
  `organ_id` int(11) NOT NULL COMMENT '组织编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `template_code` (`template_code`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COMMENT='webhook模板表';

-- ----------------------------
-- Table structure for t_wechat_template
-- ----------------------------
DROP TABLE IF EXISTS `t_wechat_template`;
CREATE TABLE `t_wechat_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `template_name` varchar(100) NOT NULL COMMENT '模板名称',
  `template_code` varchar(100) NOT NULL COMMENT '模板编码',
  `template_content` varchar(500) NOT NULL COMMENT '模板内容',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '状态 0：无效，1：有效',
  `creator` int(11) DEFAULT NULL COMMENT '创建人',
  `modifier` int(11) DEFAULT NULL COMMENT '修改人',
  `organ_id` int(11) NOT NULL COMMENT '组织编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `template_code` (`template_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='微信服务号模板表';
