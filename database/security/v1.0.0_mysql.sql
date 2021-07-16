CREATE USER IF NOT EXISTS 'abssecurity'@'localhost' IDENTIFIED BY 'security';
CREATE DATABASE IF NOT EXISTS abssecurity;
USE abssecurity;

CREATE TABLE IF NOT EXISTS `USER` (
    `ID`                      BIGINT        NOT NULL AUTO_INCREMENT,
    `UNIQUE_TOKEN`            CHAR   (  12) BINARY UNIQUE NOT NULL,
    `NAME`                    VARCHAR( 200) NOT NULL,
    `PICTURE_URL`             VARCHAR(4000),
    `PRIMARY_EMAIL_ADDRESS`   VARCHAR( 200) NOT NULL,
    `NICK_NAME`               VARCHAR( 200) NOT NULL UNIQUE,
	`IS_FIRST_LOGIN`          BIT           NOT NULL,
PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS `USER_LOGIN_SOURCE` (
    `ID`            BIGINT       NOT NULL AUTO_INCREMENT,
    `USER_ID`       BIGINT       NOT NULL REFERENCES `USER`(`ID`),
    `SOURCE_ID`     TINYINT      NOT NULL,
    `LOGIN_TOKEN`   VARCHAR(200) NOT NULL,
PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS `UGROUP` (
    `ID`            BIGINT NOT NULL AUTO_INCREMENT,
    `NAME`          VARCHAR( 200) NOT NULL UNIQUE,
	`DESCRIPTION`   VARCHAR(4000),
PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS `AUTHORITY` (
	`ID`            BIGINT NOT NULL AUTO_INCREMENT,
	`NAME`          VARCHAR( 200) NOT NULL,
	`DESCRIPTION`   VARCHAR(4000),
PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS `USER_GROUP` (
    `ID`         BIGINT NOT NULL AUTO_INCREMENT,
	`USER_ID`    BIGINT NOT NULL REFERENCES `USER`(`ID`),
	`GROUP_ID`   BIGINT NOT NULL REFERENCES `UGROUP`(`ID`),
PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS `GROUP_AUTHORITY` (
    `ID`             BIGINT NOT NULL AUTO_INCREMENT,
	`GROUP_ID`       BIGINT NOT NULL REFERENCES `UGROUP`(`ID`),
	`AUTHORITY_ID`   BIGINT NOT NULL REFERENCES `AUTHORITY`(`ID`),
PRIMARY KEY(ID)
);

TRUNCATE TABLE `USER_GROUP`;
TRUNCATE TABLE `GROUP_AUTHORITY`;
TRUNCATE TABLE `USER_LOGIN_SOURCE`;
TRUNCATE TABLE `USER`;
TRUNCATE TABLE `UGROUP`;
TRUNCATE TABLE `AUTHORITY`;


INSERT INTO `USER`(
	`UNIQUE_TOKEN`,
	`NAME`,
	`PRIMARY_EMAIL_ADDRESS`,
	`NICK_NAME`,
	`IS_FIRST_LOGIN`
) VALUES (
	'````````````',
	'ABSSYSTEM',
	'',
	'ABSSYSTEM',
	0
);

INSERT INTO `UGROUP`(
	`NAME`,
	`DESCRIPTION`
) VALUES
 ('SYSTEM', 'Group reserved for system users and batch jobs')
,('USERS' , 'Regular users group')
,('ADMIN' , 'System administrators')
;

INSERT INTO `USER_GROUP`(
	`USER_ID`,
	`GROUP_ID`
) VALUES (
	( SELECT MIN(`ID`) FROM `USER` WHERE `UNIQUE_TOKEN` = '````````````' ),
	( SELECT MIN(`ID`) FROM `UGROUP` WHERE `NAME` = 'SYSTEM' )
);

INSERT INTO `AUTHORITY`(
	`NAME`,
	`DESCRIPTION`
) VALUES
 ('BULK_OPERATIONS'      , 'Run bulk operations')
,('ADMINISTRATIVE_TASKS' , 'Perform administrative tasks')
,('ACCESS_SENSITIVE_DATA', 'Access to personal user information, as well as other sensitive data')
;

INSERT INTO `GROUP_AUTHORITY`(
	`GROUP_ID`,
	`AUTHORITY_ID`
) VALUES
(
	( SELECT MIN(`ID`) FROM `UGROUP` WHERE `NAME` = 'SYSTEM' ),
	( SELECT MIN(`ID`) FROM `AUTHORITY` WHERE `NAME` = 'BULK_OPERATIONS' )
),
(
	( SELECT MIN(`ID`) FROM `UGROUP` WHERE `NAME` = 'SYSTEM' ),
	( SELECT MIN(`ID`) FROM `AUTHORITY` WHERE `NAME` = 'ADMINISTRATIVE_TASKS' )
),
(
	( SELECT MIN(`ID`) FROM `UGROUP` WHERE `NAME` = 'SYSTEM' ),
	( SELECT MIN(`ID`) FROM `AUTHORITY` WHERE `NAME` = 'ACCESS_SENSITIVE_DATA' )
),
(
	( SELECT MIN(`ID`) FROM `UGROUP` WHERE `NAME` = 'ADMIN' ),
	( SELECT MIN(`ID`) FROM `AUTHORITY` WHERE `NAME` = 'ADMINISTRATIVE_TASKS' )
),
(
	( SELECT MIN(`ID`) FROM `UGROUP` WHERE `NAME` = 'ADMIN' ),
	( SELECT MIN(`ID`) FROM `AUTHORITY` WHERE `NAME` = 'ACCESS_SENSITIVE_DATA' )
)
;

GRANT ALL PRIVILEGES ON `abssecurity` TO 'abssecurity'@'localhost';