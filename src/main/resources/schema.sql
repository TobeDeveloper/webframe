CREATE TABLE IF NOT EXISTS `customer` (
  `id`        BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `name`      VARCHAR(255) NOT NULL,
  `contact`   VARCHAR(255) NOT NULL,
  `telephone` VARCHAR(255)          DEFAULT NULL,
  `email`     VARCHAR(255)          DEFAULT NULL,
  `remark`    TEXT,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;
