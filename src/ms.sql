/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : ms

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 19/12/2019 01:09:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `wechatId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `linkman` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `search` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (concat_ws(' ',ifnull(`name`,''),ifnull(`phone`,''),ifnull(`linkman`,''),ifnull(`address`,''),ifnull(`wechatId`,''))) VIRTUAL NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (8, '撒甲方', '商国', '10086', '123456', '思思', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);
INSERT INTO `customer` VALUES (9, '撒经济', '商国', '10086', '123456', '思思', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);
INSERT INTO `customer` VALUES (11, '撒甲方', '商国', '10086', '123456', '狗思思', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);
INSERT INTO `customer` VALUES (12, '撒甲方', '美国', '10086', '123456', '狗思思', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);
INSERT INTO `customer` VALUES (14, '撒甲方', '程度', '10086', '123456', '狗思思', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);
INSERT INTO `customer` VALUES (15, '方瑞盟', '两个', '10086', '123456', '思思', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);
INSERT INTO `customer` VALUES (16, '武庚11', '商国', '10086', '123456', '阿狗', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);
INSERT INTO `customer` VALUES (17, '武庚1111', '商国', '10086', '123456', '阿狗', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);
INSERT INTO `customer` VALUES (18, '武庚1', '商国', '10086', '123456', '阿狗', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);
INSERT INTO `customer` VALUES (19, '武庚2222', '商国', '10086', '123456', '阿狗', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);

-- ----------------------------
-- Table structure for customer_empty_bottle
-- ----------------------------
DROP TABLE IF EXISTS `customer_empty_bottle`;
CREATE TABLE `customer_empty_bottle`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `emptyBottleId` int(11) NOT NULL,
  `total` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `customerId` int(11) NOT NULL,
  `customerName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sendBackNumber` int(11) NOT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for empty_bottle
-- ----------------------------
DROP TABLE IF EXISTS `empty_bottle`;
CREATE TABLE `empty_bottle`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `number` int(11) NOT NULL,
  `price` decimal(11, 0) NOT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `search` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (concat_ws(' ',ifnull(`name`,''),ifnull(`type`,''))) VIRTUAL NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of empty_bottle
-- ----------------------------
INSERT INTO `empty_bottle` VALUES (1, '空瓶1', '一', 1000, 10, '2019-12-15 19:21:35', '2019-12-15 19:21:37', DEFAULT);

-- ----------------------------
-- Table structure for gas_cylinder
-- ----------------------------
DROP TABLE IF EXISTS `gas_cylinder`;
CREATE TABLE `gas_cylinder`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `inventory` int(11) NOT NULL,
  `price` decimal(11, 0) NOT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `search` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (concat_ws(' ',ifnull(`name`,''),ifnull(`type`,''))) VIRTUAL NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gas_cylinder
-- ----------------------------
INSERT INTO `gas_cylinder` VALUES (1, '气瓶1', '一', 1000, 10, '2019-12-15 19:23:45', '2019-12-15 19:23:47', DEFAULT);
INSERT INTO `gas_cylinder` VALUES (3, '气瓶3', '二', 1000, 10, '2019-12-15 12:14:00', '2019-12-15 12:14:13', DEFAULT);

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customerId` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `totalPrice` decimal(11, 0) NOT NULL,
  `status` int(11) NOT NULL,
  `paid` int(11) NOT NULL,
  `customerName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `search` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (concat_ws(' ',ifnull(`customerId`,''),ifnull(`customerName`,''))) VIRTUAL NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (1, 1, 10, 10000, 1, 2, '武庚武庚武庚武庚武庚武庚', '测试', '2019-12-15 21:20:41', '2019-12-15 21:20:44', DEFAULT);

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderId` int(11) NOT NULL,
  `gasCylinderId` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(11, 0) NOT NULL,
  `paid` int(11) NOT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for staff
-- ----------------------------
DROP TABLE IF EXISTS `staff`;
CREATE TABLE `staff`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `age` int(11) NOT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `workYears` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `salary` decimal(10, 0) NULL DEFAULT NULL,
  `level` int(11) NOT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `search` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (concat_ws(' ',ifnull(`name`,''))) VIRTUAL NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of staff
-- ----------------------------
INSERT INTO `staff` VALUES (1, '王启年', 48, '100001', '10', 10000, 1, '2019-12-15 21:22:47', '2019-12-15 21:22:50', DEFAULT);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `loginName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `level` int(11) NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '管理员', 'admin', '123456', 1, '2019-12-09 20:38:36', '2019-12-09 20:38:39');

-- ----------------------------
-- Table structure for word_lib
-- ----------------------------
DROP TABLE IF EXISTS `word_lib`;
CREATE TABLE `word_lib`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of word_lib
-- ----------------------------
INSERT INTO `word_lib` VALUES (1, '职位级别', 'zjl', '总经理', '公司总经理', '2019-11-27 21:39:45', '2019-11-27 21:39:49');
INSERT INTO `word_lib` VALUES (2, '职位级别', 'fjl', '副经理', '公司副经理', '2019-12-03 19:37:04', '2019-12-03 19:37:07');

SET FOREIGN_KEY_CHECKS = 1;
