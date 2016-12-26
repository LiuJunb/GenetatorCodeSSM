/*
Navicat MySQL Data Transfer

Source Server         : localhost215
Source Server Version : 50525
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50525
File Encoding         : 65001

Date: 2016-12-26 14:10:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT '',
  `age` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
