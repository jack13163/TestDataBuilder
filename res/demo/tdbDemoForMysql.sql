-- ----------------------------------------------------------------------
-- MySQL Migration Toolkit
-- SQL Create Script
-- ----------------------------------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS `school`
  CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `school`;
-- -------------------------------------
-- Tables

DROP TABLE IF EXISTS `school`.`class`;
CREATE TABLE `school`.`class` (
  `id` VARCHAR(255) NOT NULL,
  'code' VARCHAR(255) NOT NULL,
  `master` VARCHAR(255) NULL,
  `classRome` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK__class__master__0425A276` FOREIGN KEY `FK__class__master__0425A276` (`master`)
    REFERENCES `school`.`teacher` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `school`.`student`;
CREATE TABLE `school`.`student` (
  `ID` INT(10) NOT NULL AUTO_INCREMENT,
  `no` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `sex` VARCHAR(10) NOT NULL,
  `age` INT(10) NOT NULL,
  `tel` VARCHAR(256) NULL,
  `birthday` DATETIME NULL,
  `classId` VARCHAR(255) NULL,
  `homeAddress` VARCHAR(255) NULL,
  `count` INT(10) NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK__student__classId__07020F21` FOREIGN KEY `FK__student__classId__07020F21` (`classId`)
    REFERENCES `school`.`class` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = INNODB;

DROP TABLE IF EXISTS `school`.`teacher`;
CREATE TABLE `school`.`teacher` (
  `ID` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `sex` VARCHAR(10) NOT NULL,
  `tel` VARCHAR(255) NULL,
  `homeAddress` VARCHAR(255) NULL,
  PRIMARY KEY (`ID`)
)
ENGINE = INNODB;



SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------------
-- EOF

