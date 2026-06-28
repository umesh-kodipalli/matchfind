-- Reference schema for person-backend.
-- Not executed automatically — Hibernate creates/updates this table at
-- startup because spring.jpa.hibernate.ddl-auto=update. This file is here
-- so you can inspect or manually provision the schema if you'd rather not
-- rely on Hibernate to do it (e.g. in a locked-down production database).

CREATE DATABASE IF NOT EXISTS persondb
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE persondb;

CREATE TABLE IF NOT EXISTS person_names (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    person1    VARCHAR(255) NOT NULL,
    person2    VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL
);
