/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : localhost:3306
 Source Schema         : ms

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 10/01/2020 16:47:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for bill
-- ----------------------------
DROP TABLE IF EXISTS `bill`;
CREATE TABLE `bill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderTotal` decimal(20, 4) NOT NULL,
  `orderDebt` decimal(20, 4) NULL DEFAULT NULL,
  `totalDebt` decimal(20, 4) NULL DEFAULT NULL,
  `customerId` int(11) NULL DEFAULT NULL,
  `customerName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `paid` decimal(20, 4) NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `emptyBottleTotal` decimal(20, 4) NULL DEFAULT NULL,
  `search` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (concat_ws(' ',ifnull(`customerName`,''))) VIRTUAL NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of bill
-- ----------------------------
INSERT INTO `bill` VALUES (32, 1119000.0000, 1110900.0000, 1128900.0000, 18, '武庚1', 8100.0000, '2020-01-10 15:47:39', '2020-01-10 15:56:46', 18000.0000, DEFAULT);
INSERT INTO `bill` VALUES (33, 10030.0000, 10.0000, 10.0000, 12, '撒甲方', 10020.0000, '2020-01-10 16:44:20', '2020-01-10 16:44:20', 0.0000, DEFAULT);

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
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (12, '撒甲方', '美国', '10086', '123456', '狗思思', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);
INSERT INTO `customer` VALUES (18, '武庚1', '商国', '10086', '123456', '阿狗', '2019-12-10 20:20:57', '2019-12-10 20:20:59', DEFAULT);

-- ----------------------------
-- Table structure for customer_empty_bottle
-- ----------------------------
DROP TABLE IF EXISTS `customer_empty_bottle`;
CREATE TABLE `customer_empty_bottle`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customerName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `search` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (concat_ws(' ',ifnull(`gasCylinderName`,''),ifnull(`customerName`,''))) VIRTUAL NULL,
  `gasCylinderId` int(11) NULL DEFAULT NULL,
  `gasCylinderName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `total` int(11) NULL DEFAULT NULL,
  `customerId` int(11) NULL DEFAULT NULL,
  `sendBackNumber` int(11) NULL DEFAULT NULL,
  `nowNumber` int(11) NULL DEFAULT NULL,
  `price` decimal(10, 4) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer_empty_bottle
-- ----------------------------
INSERT INTO `customer_empty_bottle` VALUES (12, '武庚1', '2020-01-10 11:23:14', '2020-01-10 15:47:14', DEFAULT, 4, '氧气瓶', 100, 18, 10, 90, 100.0000);
INSERT INTO `customer_empty_bottle` VALUES (13, '武庚1', '2020-01-10 11:23:56', '2020-01-10 15:44:21', DEFAULT, 3, '气瓶3', 1000, 18, 1000, 0, 100.0000);

-- ----------------------------
-- Table structure for customer_gas_cylinder
-- ----------------------------
DROP TABLE IF EXISTS `customer_gas_cylinder`;
CREATE TABLE `customer_gas_cylinder`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gasCylinderId` int(11) NOT NULL,
  `gasCylinderName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
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
  `gasCylinderName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gasCylinderId` int(11) NULL DEFAULT NULL,
  `number` int(11) NULL DEFAULT NULL,
  `price` decimal(10, 4) NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `search` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (concat_ws(' ',ifnull(`gasCylinderName`,''))) VIRTUAL NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of empty_bottle
-- ----------------------------
INSERT INTO `empty_bottle` VALUES (1, '气瓶3', 3, 1000, 10.0000, '2020-01-09 21:31:44', '2020-01-10 15:42:47', DEFAULT);

-- ----------------------------
-- Table structure for gas_cylinder
-- ----------------------------
DROP TABLE IF EXISTS `gas_cylinder`;
CREATE TABLE `gas_cylinder`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `inventory` int(11) NOT NULL,
  `price` decimal(11, 4) NOT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `search` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (concat_ws(' ',ifnull(`name`,''),ifnull(`type`,''))) VIRTUAL NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gas_cylinder
-- ----------------------------
INSERT INTO `gas_cylinder` VALUES (1, '气瓶1', '一', 89563, 10.0000, '2019-12-15 19:23:45', '2020-01-10 15:56:46', DEFAULT);
INSERT INTO `gas_cylinder` VALUES (3, '气瓶3', '二', 98690, 11.0000, '2019-12-15 12:14:00', '2020-01-10 15:56:46', DEFAULT);
INSERT INTO `gas_cylinder` VALUES (4, '氧气瓶', NULL, 700, 1000.0000, '2020-01-09 21:37:43', '2020-01-10 16:44:20', DEFAULT);

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `orderId` int(11) NOT NULL,
  `gasCylinderId` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(11, 4) NOT NULL,
  `gasCylinderName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 124 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (107, 27, 1, 100, 100.0000, '气瓶1');
INSERT INTO `order_item` VALUES (109, 28, 3, 100, 100.0000, '气瓶3');
INSERT INTO `order_item` VALUES (120, 29, 1, 10000, 10.0000, '气瓶1');
INSERT INTO `order_item` VALUES (121, 29, 4, 100, 100.0000, '氧气瓶');
INSERT INTO `order_item` VALUES (122, 29, 3, 1000, 999.0000, '气瓶3');
INSERT INTO `order_item` VALUES (123, 30, 4, 100, 0.3000, '氧气瓶');

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `customerId` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `totalPrice` decimal(20, 4) NOT NULL,
  `paid` decimal(20, 4) NOT NULL,
  `customerName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `search` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (concat_ws(' ',ifnull(`customerId`,''),ifnull(`customerName`,''))) VIRTUAL NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (27, 18, 100, 10000.0000, 8000.0000, '武庚1', '2020-01-09 11:00:31', '2020-01-09 14:45:55', DEFAULT);
INSERT INTO `orders` VALUES (28, 12, 100, 10000.0000, 10000.0000, '撒甲方', '2020-01-09 21:19:35', '2020-01-09 21:20:06', DEFAULT);
INSERT INTO `orders` VALUES (29, 18, 11100, 1109000.0000, 100.0000, '武庚1', '2020-01-10 11:14:37', '2020-01-10 15:56:46', DEFAULT);
INSERT INTO `orders` VALUES (30, 12, 100, 30.0000, 20.0000, '撒甲方', '2020-01-10 16:44:20', '2020-01-10 16:44:20', DEFAULT);

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
  `position` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  `search` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci GENERATED ALWAYS AS (concat_ws(' ',ifnull(`name`,''))) VIRTUAL NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of staff
-- ----------------------------
INSERT INTO `staff` VALUES (1, '王启年', 48, '100001', '10', 10000, '打工的', '2019-12-15 21:22:47', '2019-12-30 20:44:08', DEFAULT);
INSERT INTO `staff` VALUES (2, '1', 1, '1', '1', 1, '1', '2019-12-24 21:16:19', '2019-12-24 21:16:19', DEFAULT);
INSERT INTO `staff` VALUES (3, '10', 100, '100', '100', 100, '100', '2019-12-24 21:17:24', '2019-12-24 21:17:24', DEFAULT);
INSERT INTO `staff` VALUES (4, NULL, 0, NULL, NULL, 0, '0', '2019-12-24 21:24:46', '2019-12-24 21:24:46', DEFAULT);
INSERT INTO `staff` VALUES (5, '10', 10, '10', '10', 10, '10', '2019-12-24 21:24:46', '2019-12-24 21:24:46', DEFAULT);
INSERT INTO `staff` VALUES (6, NULL, 0, NULL, NULL, 0, NULL, '2019-12-24 21:29:26', '2019-12-24 21:29:26', DEFAULT);
INSERT INTO `staff` VALUES (7, '||ヽ(*￣▽￣*)ノミ|Ю', 12, '1800', '10', 1000, NULL, '2019-12-24 21:29:27', '2019-12-24 21:29:27', DEFAULT);
INSERT INTO `staff` VALUES (8, NULL, 0, NULL, NULL, 0, NULL, '2019-12-24 21:30:29', '2019-12-24 21:30:29', DEFAULT);
INSERT INTO `staff` VALUES (9, '10', 0, NULL, NULL, 0, NULL, '2019-12-24 21:30:29', '2019-12-24 21:30:29', DEFAULT);
INSERT INTO `staff` VALUES (10, NULL, 0, NULL, NULL, 0, NULL, '2019-12-24 21:32:17', '2019-12-24 21:32:17', DEFAULT);
INSERT INTO `staff` VALUES (11, '10', 0, NULL, NULL, 0, NULL, '2019-12-24 21:32:17', '2019-12-24 21:32:17', DEFAULT);
INSERT INTO `staff` VALUES (12, NULL, 0, NULL, NULL, 0, NULL, '2019-12-24 22:02:16', '2019-12-24 22:02:16', DEFAULT);
INSERT INTO `staff` VALUES (13, '1', 1, '1', '1', 1, NULL, '2019-12-24 22:02:16', '2019-12-24 22:02:16', DEFAULT);
INSERT INTO `staff` VALUES (14, NULL, 0, NULL, NULL, 0, NULL, '2019-12-30 20:28:39', '2019-12-30 20:28:39', DEFAULT);
INSERT INTO `staff` VALUES (15, '1', 1, '1', '1', 1, NULL, '2019-12-30 20:28:39', '2019-12-30 20:28:39', DEFAULT);
INSERT INTO `staff` VALUES (16, NULL, 0, NULL, NULL, 0, NULL, '2019-12-30 20:29:07', '2019-12-30 20:29:07', DEFAULT);
INSERT INTO `staff` VALUES (17, '1', 1, '1', '1', 1, NULL, '2019-12-30 20:29:07', '2019-12-30 20:29:07', DEFAULT);
INSERT INTO `staff` VALUES (18, NULL, 0, NULL, NULL, 0, NULL, '2019-12-30 20:33:27', '2019-12-30 20:33:27', DEFAULT);
INSERT INTO `staff` VALUES (19, '1', 2, '4', '3', 5, NULL, '2019-12-30 20:33:27', '2019-12-30 20:33:27', DEFAULT);
INSERT INTO `staff` VALUES (20, NULL, 0, NULL, NULL, 0, NULL, '2019-12-30 20:38:38', '2019-12-30 20:38:38', DEFAULT);
INSERT INTO `staff` VALUES (21, '1', 2, '4', '3', 5, NULL, '2019-12-30 20:38:38', '2019-12-30 20:38:38', DEFAULT);
INSERT INTO `staff` VALUES (22, '23', 0, '31', '3', 0, '13', '2019-12-30 20:45:31', '2019-12-30 20:47:16', DEFAULT);
INSERT INTO `staff` VALUES (23, '12', 123, '12', '123', 132, '13', '2019-12-30 20:45:31', '2019-12-30 20:45:31', DEFAULT);
INSERT INTO `staff` VALUES (24, '123', 123, '12', '13', 31, '3', '2019-12-30 20:46:58', '2019-12-30 20:46:58', DEFAULT);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `loginName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `createTime` datetime(0) NULL DEFAULT NULL,
  `updateTime` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '超级管理员', 'admin', 'admin', '2019-12-09 20:38:36', '2019-12-21 19:27:36');

SET FOREIGN_KEY_CHECKS = 1;
