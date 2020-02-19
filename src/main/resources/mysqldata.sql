/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.24-log : Database - seckill
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`seckill` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `seckill`;

/*Table structure for table `item` */

DROP TABLE IF EXISTS `item`;

CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `price` double(10,0) NOT NULL DEFAULT '0',
  `description` varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `sales` int(11) NOT NULL DEFAULT '0',
  `img_url` varchar(500) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `item` */

insert  into `item`(`id`,`title`,`price`,`description`,`sales`,`img_url`) values (2,'苹果手机',99,'最好用的苹果手机',101,'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1574779579262&di=0c1f681edd9aff87ebce4f045892a80c&imgtype=0&src=http%3A%2F%2Fimg13.360buyimg.com%2Fn1%2Fs450x450_jfs%2Ft16108%2F78%2F1657603428%2F244727%2Fe48eae1%2F5a66ef48N387aa1f9.jpg'),(5,'苹果手机8',88,'最好用的欣赏裒辑',201,'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1574779588207&di=db4b4451d9cd0f3927cca2b3a8d21dbe&imgtype=0&src=http%3A%2F%2Fm.360buyimg.com%2Fmobilecms%2Fs800x671_jfs%2Ft6010%2F99%2F7560437314%2F56562%2Fcdbed730%2F597ed19cN3a3f6689.jpg%2521q70.jpg');

/*Table structure for table `item_stock` */

DROP TABLE IF EXISTS `item_stock`;

CREATE TABLE `item_stock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stock` int(11) NOT NULL DEFAULT '0',
  `item_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `item_stock` */

insert  into `item_stock`(`id`,`stock`,`item_id`) values (2,97,2),(3,99,5);

/*Table structure for table `order_info` */

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info` (
  `id` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `user_id` int(11) NOT NULL DEFAULT '0',
  `item_id` int(11) NOT NULL DEFAULT '0',
  `item_price` double NOT NULL DEFAULT '0',
  `amount` int(11) NOT NULL DEFAULT '0',
  `order_price` double NOT NULL DEFAULT '0',
  `promo_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `order_info` */

insert  into `order_info`(`id`,`user_id`,`item_id`,`item_price`,`amount`,`order_price`,`promo_id`) values ('2019112920000000',3,2,99,1,99,0),('2019112930000000',3,5,88,1,88,0),('2019120140000000',3,2,100,1,100,1);

/*Table structure for table `promo` */

DROP TABLE IF EXISTS `promo`;

CREATE TABLE `promo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `promo_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `start_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `item_id` int(11) NOT NULL DEFAULT '0',
  `promo_item_price` double NOT NULL DEFAULT '0',
  `end_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `promo` */

insert  into `promo`(`id`,`promo_name`,`start_date`,`item_id`,`promo_item_price`,`end_date`) values (1,'iphone8秒杀活动','2019-12-01 13:27:17',2,100,'2019-12-25 11:21:54');

/*Table structure for table `sequence_info` */

DROP TABLE IF EXISTS `sequence_info`;

CREATE TABLE `sequence_info` (
  `name` varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `crrent_value` int(11) NOT NULL DEFAULT '0',
  `step` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `sequence_info` */

insert  into `sequence_info`(`name`,`crrent_value`,`step`) values ('order_info',5,1);

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `gender` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1 男性 2女性',
  `age` int(11) NOT NULL DEFAULT '0',
  `telphone` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `register_mode` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '注册方式',
  `third_party_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '第三方id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `telphone_unique_index` (`telphone`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `user_info` */

insert  into `user_info`(`id`,`name`,`gender`,`age`,`telphone`,`register_mode`,`third_party_id`) values (1,'Hellen',0,24,'12345678','weChat','fssdfsadf'),(3,'swadian',1,27,'123456','byphone','');

/*Table structure for table `user_password` */

DROP TABLE IF EXISTS `user_password`;

CREATE TABLE `user_password` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `encrpt_password` varchar(128) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '加密密码',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `user_password` */

insert  into `user_password`(`id`,`encrpt_password`,`user_id`) values (1,'sfsdfsadfasdf',1),(3,'4QrcOUm6Wau+VuBX8g+IPg==',3);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
