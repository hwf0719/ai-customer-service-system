-- 初始化数据

-- 插入管理员账号（密码：admin123）
INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('admin', 'admin123', '系统管理员', 'ADMIN');

-- 插入测试客服账号
INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('agent01', 'agent123', '客服小王', 'AGENT');

-- 插入测试用户账号
INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('user01', 'user123', '张三', 'USER'),
('user02', 'user123', '李四', 'USER');
