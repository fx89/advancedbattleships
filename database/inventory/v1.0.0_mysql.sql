-- USER AND DATABASE ----------------------------------------------------------------------------------
CREATE USER IF NOT EXISTS 'absinventory'@'localhost' IDENTIFIED BY 'inventory';
CREATE DATABASE IF NOT EXISTS absinventory;
USE absinventory;




-- TABLES ---------------------------------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `BATTLESHIP_TEMPLATE` (
	`ID`                       BIGINT NOT NULL AUTO_INCREMENT,
	`UNIQUE_TOKEN`             CHAR(12) BINARY NOT NULL,
	`USER_UNIQUE_TOKEN`        CHAR(12) BINARY NOT NULL,
	`IS_OFFICIAL_TEMPLATE`     BIT NOT NULL DEFAULT FALSE,
	`IS_PUBLIC`                BIT NOT NULL DEFAULT FALSE,
	`IS_VISIBLE_IN_LISTS`      BIT NOT NULL DEFAULT FALSE,
	`NAME`                     VARCHAR(200) NOT NULL,
	`HULL_WIDTH`               INT NOT NULL,
	`HULL_HEIGHT`              INT NOT NULL,
	`HULL_ARRAY`               VARCHAR(4000),
	`COST`                     DOUBLE,
	`ENERGY`                   DOUBLE,
	`FIREPOWER`                DOUBLE
PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS `SUBSYSTEM_TYPE` (
	`ID`     BIGINT NOT NULL AUTO_INCREMENT,
	`NAME`   VARCHAR(200) NOT NULL,
PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS `SUBSYSTEM_REF` (
	`ID`             BIGINT NOT NULL AUTO_INCREMENT,
	`UNIQUE_TOKEN`   CHAR(12) BINARY NOT NULL,
	`NAME`           VARCHAR(200) NOT NULL,
	`TYPE_ID`        BIGINT NOT NULL REFERENCES `SUBSYSTEM_TYPE`(`ID`),
	`COST`           DECIMAL NOT NULL,	
PRIMARY KEY(ID)
);

CREATE TABLE IF NOT EXISTS `STORED_RESOURCE_TYPE` (
	`ID`     BIGINT NOT NULL AUTO_INCREMENT,
	`NAME`   VARCHAR(200) NOT NULL,
PRIMARY KEY (`ID`)
);

CREATE TABLE IF NOT EXISTS `SUBSYSTEM_REF_STORED_RESOURCE_REQ` (
	`ID`                        BIGINT NOT NULL AUTO_INCREMENT,
	`SUBSYSTEM_REF_ID`          BIGINT NOT NULL REFERENCES `SUBSYSTEM_REF`(`ID`),
	`STORED_RESOURCE_TYPE_ID`   BIGINT NOT NULL REFERENCES `STORED_RESOURCE_TYPE`(`ID`),
	`REQUIRED_AMT_PER_SHOT`     DECIMAL NOT NULL,
PRIMARY KEY (`ID`)
);

CREATE TABLE IF NOT EXISTS `SUBSYSTEM_REF_STORAGE` (
	`ID`                        BIGINT NOT NULL AUTO_INCREMENT,
	`SUBSYSTEM_REF_ID`          BIGINT NOT NULL REFERENCES `SUBSYSTEM_REF`(`ID`),
	`STORED_RESOURCE_TYPE_ID`   BIGINT NOT NULL REFERENCES `STORED_RESOURCE_TYPE`(`ID`),
	`INITIAL_AMT`               DECIMAL NOT NULL,
PRIMARY KEY(`ID`)
);

CREATE TABLE IF NOT EXISTS `GENERATED_RESOURCE_TYPE` (
	`ID`     BIGINT NOT NULL AUTO_INCREMENT,
	`NAME`   VARCHAR(200),
PRIMARY KEY(`ID`)
);

CREATE TABLE IF NOT EXISTS `SUBSYSTEM_REF_GENERATED_RESOURCE_REQ` (
	`ID`                           BIGINT NOT NULL AUTO_INCREMENT,
	`SUBSYSTEM_REF_ID`             BIGINT NOT NULL REFERENCES `SUBSYSTEM_REF`(`ID`),
	`GENERATED_RESOURCE_TYPE_ID`   BIGINT NOT NULL REFERENCES `STORED_RESOURCE`(`ID`),
	`REQUIRED_AMT`                 DECIMAL NOT NULL,
PRIMARY KEY(`ID`)
);

CREATE TABLE IF NOT EXISTS `SUBSYSTEM_REF_GENERATED_RESOURCE_SPEC` (
	`ID`                           BIGINT NOT NULL AUTO_INCREMENT,
	`SUBSYSTEM_REF_ID`             BIGINT NOT NULL REFERENCES `SUBSYSTEM_REF`(`ID`),
	`GENERATED_RESOURCE_TYPE_ID`   BIGINT NOT NULL REFERENCES `GENERATED_RESOURCE_TYPE`(`ID`),
	`AMOUNT`                       DECIMAL NOT NULL,
PRIMARY KEY(`ID`)
);

CREATE TABLE IF NOT EXISTS `BATTLESHIP_TEMPLATE_SUBSYSTEM` (
	`ID`                       BIGINT NOT NULL AUTO_INCREMENT,
	`UNIQUE_TOKEN`             CHAR(12) BINARY NOT NULL,
	`BATTLESHIP_TEMPLATE_ID`   BIGINT NOT NULL REFERENCES `BATTLESHIP_TEMPLATE`(`ID`),
	`SUBSYSTEM_REF_ID`         BIGINT NOT NULL REFERENCES `SUBSYSTEM_REF`,
	`POS_X`                    INT NOT NULL,
	`POS_Y`                    INT NOT NULL,
PRIMARY KEY(ID)
);





-- STOCK BATTLESHIP PARTS -----------------------------------------------------------------------------

-- Clean up the tables before (re)inserting the data
TRUNCATE TABLE `BATTLESHIP_TEMPLATE_SUBSYSTEM`;
TRUNCATE TABLE `SUBSYSTEM_REF_GENERATED_RESOURCE_SPEC`;
TRUNCATE TABLE `SUBSYSTEM_REF_GENERATED_RESOURCE_REQ`;
TRUNCATE TABLE `GENERATED_RESOURCE_TYPE`;
TRUNCATE TABLE `SUBSYSTEM_REF_STORAGE`;
TRUNCATE TABLE `SUBSYSTEM_REF_STORED_RESOURCE_REQ`;
TRUNCATE TABLE `STORED_RESOURCE_TYPE`;
TRUNCATE TABLE `SUBSYSTEM_REF`;
TRUNCATE TABLE `SUBSYSTEM_TYPE`;
TRUNCATE TABLE `BATTLESHIP_TEMPLATE`;

-- Add the names first
INSERT INTO `GENERATED_RESOURCE_TYPE`(`NAME`) VALUES ('power');
INSERT INTO `SUBSYSTEM_TYPE`(`NAME`) VALUES ('power systems'), ('weapons systems'), ('storage systems');
INSERT INTO `STORED_RESOURCE_TYPE`(`NAME`) VALUES ('torpedos'), ('railgun slugs'), ('nukes');

-- Define the STANDARD REACTOR
INSERT INTO `SUBSYSTEM_REF`(`UNIQUE_TOKEN`, `NAME`, `TYPE_ID`, `COST`) VALUES (
	'ZZ``````````',
	'standard reactor',
	( SELECT MIN(`ID`) FROM `SUBSYSTEM_TYPE` WHERE `NAME` = 'power systems' ),
	100
);
INSERT INTO `SUBSYSTEM_REF_GENERATED_RESOURCE_SPEC`(`SUBSYSTEM_REF_ID`, `GENERATED_RESOURCE_TYPE_ID`, `AMOUNT`) VALUES (
	( SELECT MIN(`ID`) FROM `SUBSYSTEM_REF` WHERE `NAME` = 'standard reactor' ),
	( SELECT MIN(`ID`) FROM `GENERATED_RESOURCE_TYPE` WHERE `NAME` = 'power' ),
	50
);

-- Define the OVERLOADED REACTOR
INSERT INTO `SUBSYSTEM_REF`(`UNIQUE_TOKEN`, `NAME`, `TYPE_ID`, `COST`) VALUES (
	'ZZ~`````````',
	'overloaded reactor',
	( SELECT MIN(`ID`) FROM `SUBSYSTEM_TYPE` WHERE `NAME` = 'power systems' ),
	200
);
INSERT INTO `SUBSYSTEM_REF_GENERATED_RESOURCE_SPEC`(`SUBSYSTEM_REF_ID`, `GENERATED_RESOURCE_TYPE_ID`, `AMOUNT`) VALUES (
	( SELECT MIN(`ID`) FROM `SUBSYSTEM_REF` WHERE `NAME` = 'overloaded reactor' ),
	( SELECT MIN(`ID`) FROM `GENERATED_RESOURCE_TYPE` WHERE `NAME` = 'power' ),
	100
);

-- Define the small torpedo shelf
INSERT INTO `SUBSYSTEM_REF`(`UNIQUE_TOKEN`, `NAME`, `TYPE_ID`, `COST`) VALUES (
	'ZZ1`````````',
	'small torpedo shelf',
	( SELECT MIN(`ID`) FROM `SUBSYSTEM_TYPE` WHERE `NAME` = 'storage systems' ),
	10
);
INSERT INTO `SUBSYSTEM_REF_STORAGE`(`SUBSYSTEM_REF_ID`, `STORED_RESOURCE_TYPE_ID`, `INITIAL_AMT`) VALUES (
	( SELECT MIN(`ID`) FROM `SUBSYSTEM_REF` WHERE `NAME` = 'small torpedo shelf' ),
	( SELECT MIN(`ID`) FROM `STORED_RESOURCE_TYPE` WHERE `NAME` = 'torpedos' ),
	10
);

-- Define the torpedo launcher
INSERT INTO `SUBSYSTEM_REF`(`UNIQUE_TOKEN`, `NAME`, `TYPE_ID`, `COST`) VALUES (
	'ZZ!`````````',
	'torpedo launcher',
	( SELECT MIN(`ID`) FROM `SUBSYSTEM_TYPE` WHERE `NAME` = 'weapons systems' ),
	20
);
INSERT INTO `SUBSYSTEM_REF_STORED_RESOURCE_REQ`(`SUBSYSTEM_REF_ID`, `STORED_RESOURCE_TYPE_ID`, `REQUIRED_AMT_PER_SHOT`) VALUES (
	( SELECT MIN(`ID`) FROM `SUBSYSTEM_REF` WHERE `NAME` = 'torpedo launcher' ),
	( SELECT MIN(`ID`) FROM `STORED_RESOURCE_TYPE` WHERE `NAME` = 'torpedos' ),
	1
);
INSERT INTO `SUBSYSTEM_REF_GENERATED_RESOURCE_REQ`(`SUBSYSTEM_REF_ID`, `GENERATED_RESOURCE_TYPE_ID`, `REQUIRED_AMT`) VALUES (
	( SELECT MIN(`ID`) FROM `SUBSYSTEM_REF` WHERE `NAME` = 'torpedo launcher' ),
	( SELECT MIN(`ID`) FROM `GENERATED_RESOURCE_TYPE` WHERE `NAME` = 'power' ),
	10
);





-- PRIVILEGES -----------------------------------------------------------------------------------------

GRANT ALL PRIVILEGES ON absinventory.* TO 'absinventory'@'localhost';