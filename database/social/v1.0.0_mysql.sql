CREATE USER IF NOT EXISTS 'abssocial'@'localhost' IDENTIFIED BY 'social';
CREATE DATABASE IF NOT EXISTS abssocial;
USE abssocial;

CREATE TABLE IF NOT EXISTS `USER_FRIEND` (
	`ID`                       BIGINT        NOT NULL AUTO_INCREMENT,
	`USER_UNIQUE_TOKEN`        CHAR   (  12) BINARY UNIQUE NOT NULL,
	`FRIEND_USER_UNIQUE_TOKEN` CHAR   (  12) BINARY UNIQUE NOT NULL,
PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS `PARTY` (
	`ID`                 BIGINT        NOT NULL AUTO_INCREMENT,
	`PARTY_UNIQUE_TOKEN` CHAR   (  12) BINARY UNIQUE NOT NULL,
	`NAME`               VARCHAR( 200) UNIQUE NOT NULL,
PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS `USER_PARTY` (
	`ID`                 BIGINT        NOT NULL AUTO_INCREMENT,
	`USER_UNIQUE_TOKEN`  CHAR   (  12) BINARY UNIQUE NOT NULL,
	`PARTY_ID`           BIGINT REFERENCES `PARTY `(`ID`),
PRIMARY KEY(ID)
);

INSERT INTO `PARTY`(`PARTY_UNIQUE_TOKEN`, `NAME`) VALUES('WQ``````````', 'Public Party');
INSERT INTO `PARTY`(`PARTY_UNIQUE_TOKEN`, `NAME`) VALUES('WR``````````', 'Ops Party');
COMMIT;

GRANT ALL PRIVILEGES ON `abssocial`.* TO 'abssocial'@'localhost';