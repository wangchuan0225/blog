-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`
(
    `id`     bigint                                  NOT NULL,
    `name`   varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `phone`  varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `salary` double                                  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_hafqwjqe2e9bcpgyj6evm52ap` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;