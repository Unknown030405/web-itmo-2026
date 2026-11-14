-- Схема БД проекта. Выполняется автоматически один раз — при создании пустой базы.
-- Каждое изменение структуры (CREATE TABLE, ALTER TABLE, индексы) выполните на своей базе
-- (Adminer или консоль) и допишите тот же SQL в конец этого файла.

CREATE TABLE `User` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `login`        VARCHAR(255) NOT NULL,
    `passwordSha`  VARCHAR(255) NOT NULL,
    `creationTime` DATETIME     NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_User_login` (`login`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- Email
ALTER TABLE `User`
    ADD COLUMN `email` VARCHAR(255) NOT NULL,
    ADD UNIQUE KEY `uk_User_email` (`email`);

-- Enter / Logout
CREATE TABLE `Event` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `userId`       BIGINT       NOT NULL,
    `type`         VARCHAR(8)   NOT NULL,
    `creationTime` DATETIME(6)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_Event_user_time` (`userId`, `creationTime`, `id`),
    CONSTRAINT `fk_Event_userId` FOREIGN KEY (`userId`) REFERENCES `User` (`id`),
    CONSTRAINT `chk_Event_type` CHECK (`type` IN ('ENTER', 'LOGOUT'))
);

-- Talks
CREATE TABLE `Talk` (
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `sourceUserId` BIGINT      NOT NULL,
    `targetUserId` BIGINT      NOT NULL,
    `text`         TEXT        NOT NULL,
    `creationTime` DATETIME(6) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_Talk_source_time` (`sourceUserId`, `creationTime`, `id`),
    KEY `idx_Talk_target_time` (`targetUserId`, `creationTime`, `id`),
    CONSTRAINT `fk_Talk_sourceUserId` FOREIGN KEY (`sourceUserId`) REFERENCES `User` (`id`),
    CONSTRAINT `fk_Talk_targetUserId` FOREIGN KEY (`targetUserId`) REFERENCES `User` (`id`)
);

-- Articles
CREATE TABLE `Article` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT,
    `userId`       BIGINT       NOT NULL,
    `title`        VARCHAR(255) NOT NULL,
    `text`         TEXT         NOT NULL,
    `creationTime` DATETIME(6)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_Article_user_time` (`userId`, `creationTime`, `id`),
    KEY `idx_Article_time` (`creationTime`, `id`),
    CONSTRAINT `fk_Article_userId` FOREIGN KEY (`userId`) REFERENCES `User` (`id`)
);

-- Hide / Show
ALTER TABLE `Article`
    ADD COLUMN `hidden` BOOLEAN NOT NULL DEFAULT FALSE,
    ADD KEY `idx_Article_hidden_time` (`hidden`, `creationTime`, `id`),
    ADD CONSTRAINT `chk_Article_hidden` CHECK (`hidden` IN (FALSE, TRUE)),
    DROP INDEX `idx_Article_time`;

-- Admin and roles
CREATE TABLE `Role` (
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(64) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_Role_name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `Privilege` (
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `code` VARCHAR(64) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_Privilege_code` (`code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `UserRole` (
    `userId` BIGINT NOT NULL,
    `roleId` BIGINT NOT NULL,
    PRIMARY KEY (`userId`, `roleId`),
    KEY `idx_UserRole_roleId` (`roleId`),
    CONSTRAINT `fk_UserRole_userId` FOREIGN KEY (`userId`) REFERENCES `User` (`id`),
    CONSTRAINT `fk_UserRole_roleId` FOREIGN KEY (`roleId`) REFERENCES `Role` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `RolePrivilege` (
    `roleId`      BIGINT NOT NULL,
    `privilegeId` BIGINT NOT NULL,
    PRIMARY KEY (`roleId`, `privilegeId`),
    KEY `idx_RolePrivilege_privilegeId` (`privilegeId`),
    CONSTRAINT `fk_RolePrivilege_roleId` FOREIGN KEY (`roleId`) REFERENCES `Role` (`id`),
    CONSTRAINT `fk_RolePrivilege_privilegeId` FOREIGN KEY (`privilegeId`) REFERENCES `Privilege` (`id`)
);

INSERT INTO `Role` (`name`) VALUES ('USER'), ('ADMIN');
INSERT INTO `Privilege` (`code`) VALUES ('SEND_TALK'), ('CREATE_ARTICLE'), ('MANAGE_USERS');
INSERT INTO `RolePrivilege` (`roleId`, `privilegeId`)
SELECT r.`id`, p.`id`
FROM `Role` r JOIN `Privilege` p
    ON (r.`name` = 'USER' AND p.`code` IN ('SEND_TALK', 'CREATE_ARTICLE'))
    OR (r.`name` = 'ADMIN' AND p.`code` = 'MANAGE_USERS');

INSERT INTO `UserRole` (`userId`, `roleId`)
SELECT u.`id`, r.`id` FROM `User` u JOIN `Role` r ON r.`name` = 'USER';
