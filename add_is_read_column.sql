-- 添加消息已读状态字段
ALTER TABLE ticket_message ADD COLUMN is_read TINYINT DEFAULT 0 COMMENT '是否已读：0-未读，1-已读';

-- 更新现有消息为已读（可选，根据需求决定）
-- UPDATE ticket_message SET is_read = 1;
