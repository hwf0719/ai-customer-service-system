-- AI智能客服工单系统 数据库表结构

CREATE DATABASE IF NOT EXISTS ai_customer_service DEFAULT charset utf8mb4;

USE ai_customer_service;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `nickname` VARCHAR(50),
    `email` VARCHAR(100),
    `phone` VARCHAR(20),
    `avatar` VARCHAR(255),
    `role` VARCHAR(20) DEFAULT 'USER' COMMENT '角色：ADMIN/AGENT/USER',
    `deleted` INT DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 工单表
CREATE TABLE IF NOT EXISTS `ticket` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `description` TEXT,
    `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING/PROCESSING/RESOLVED/CLOSED',
    `priority` VARCHAR(20) DEFAULT 'MEDIUM' COMMENT '优先级：LOW/MEDIUM/HIGH/URGENT',
    `category` VARCHAR(50) COMMENT 'AI自动分类',
    `sentiment` VARCHAR(20) COMMENT 'AI情绪分析',
    `user_id` BIGINT NOT NULL COMMENT '提交用户',
    `agent_id` BIGINT COMMENT '处理客服',
    `summary` TEXT COMMENT 'AI生成摘要',
    `deleted` INT DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_agent_id` (`agent_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 工单消息表
CREATE TABLE IF NOT EXISTS `ticket_message` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `ticket_id` BIGINT NOT NULL,
    `sender_id` BIGINT NOT NULL,
    `sender_type` VARCHAR(20) NOT NULL COMMENT '发送者：USER/AGENT/AI',
    `content` TEXT NOT NULL,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_ticket_id` (`ticket_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 知识库表
CREATE TABLE IF NOT EXISTS `knowledge` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `question` VARCHAR(500) NOT NULL,
    `answer` TEXT NOT NULL,
    `category` VARCHAR(50),
    `vector_id` VARCHAR(100) COMMENT 'Chroma向量ID',
    `deleted` INT DEFAULT 0,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;