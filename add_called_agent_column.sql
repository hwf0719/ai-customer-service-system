-- 添加呼叫人工客服标记字段
ALTER TABLE ticket ADD COLUMN called_agent INT DEFAULT 0 COMMENT '是否呼叫过人工客服：0-未呼叫，1-已呼叫';
