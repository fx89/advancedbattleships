CREATE USER IF NOT EXISTS 'abschat'@'localhost' IDENTIFIED BY 'chat';
CREATE DATABASE IF NOT EXISTS abschat;
USE abschat;

CREATE TABLE IF NOT EXISTS `CHAT_CHANNEL` (
    `ID`                 BIGINT        NOT NULL AUTO_INCREMENT,
    `NAME`               VARCHAR( 200) UNIQUE NOT NULL,
    `PARTY_UNIQUE_TOKEN` CHAR   (  12) BINARY UNIQUE NOT NULL,
    `IS_PRIVATE`         BOOLEAN       NOT NULL DEFAULT TRUE,
PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS `CHAT_CHANNEL_BAN` (
    `ID`                    BIGINT        NOT NULL AUTO_INCREMENT,
    `CHAT_CHANNEL_ID`       BIGINT        NOT NULL REFERENCES `CHAT_CHANNEL`(`ID`),
    `USER_UNIQUE_TOKEN`     CHAR   (  12) BINARY UNIQUE NOT NULL,
    `IS_PERMANENT`          BOOLEAN       NOT NULL DEFAULT TRUE,
    `TIME_WHEN_LIFTED`      DATETIME      NOT NULL,
PRIMARY KEY(ID)
);

INSERT INTO `CHAT_CHANNEL`(`NAME`, `PARTY_UNIQUE_TOKEN`, `IS_PRIVATE`) VALUES
	('General', 'WQ``````````', FALSE),
	('Support', 'WR``````````', FALSE)
;

COMMIT;

GRANT ALL PRIVILEGES ON `abschat`.* TO 'abschat'@'localhost';