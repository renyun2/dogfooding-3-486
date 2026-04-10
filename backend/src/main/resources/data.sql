INSERT INTO classes (name, description, created_at, updated_at) VALUES
('软件工程 2024-01', '软件工程专业 1 班', NOW(), NOW()),
('计算机科学 2024-02', '计算机科学与技术专业 2 班', NOW(), NOW()),
('人工智能 2024-03', '人工智能实验班', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO students (name, gender, age, email, phone, enrollment_date, clazz_id, created_at, updated_at) VALUES
('张三', '男', 20, 'zhangsan@example.com', '13800138001', '2024-09-01', 1, NOW(), NOW()),
('李四', '女', 19, 'lisi@example.com', '13800138002', '2024-09-01', 1, NOW(), NOW()),
('王五', '男', 21, 'wangwu@example.com', '13800138003', '2024-09-01', 2, NOW(), NOW()),
('赵六', '女', 20, 'zhaoliu@example.com', '13800138004', '2024-09-01', 2, NOW(), NOW()),
('钱七', '男', 22, 'qianqi@example.com', '13800138005', '2024-09-01', 3, NOW(), NOW()),
('孙八', '女', 19, 'sunba@example.com', '13800138006', '2024-09-01', 1, NOW(), NOW()),
('周九', '男', 20, 'zhoujiu@example.com', '13800138007', '2024-09-01', 2, NOW(), NOW()),
('吴十', '女', 21, 'wushi@example.com', '13800138008', '2024-09-01', 3, NOW(), NOW()),
('郑十一', '男', 20, 'zheng@example.com', '13800138009', '2024-09-01', 1, NOW(), NOW()),
('王十二', '女', 19, 'wang12@example.com', '13800138010', '2024-09-01', 2, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO teachers (employee_id, name, gender, title, department, phone, email, hire_date, created_at, updated_at) VALUES
('T001', '张明华', '男', '教授', '数学与统计学院', '13900139001', 'zhangmh@university.edu.cn', '2010-09-01', NOW(), NOW()),
('T002', '李芳', '女', '副教授', '外国语学院', '13900139002', 'lifang@university.edu.cn', '2012-09-01', NOW(), NOW()),
('T003', '王建国', '男', '教授', '计算机科学与技术学院', '13900139003', 'wangjg@university.edu.cn', '2008-09-01', NOW(), NOW()),
('T004', '赵雪梅', '女', '副教授', '计算机科学与技术学院', '13900139004', 'zhaoxm@university.edu.cn', '2015-09-01', NOW(), NOW()),
('T005', '钱伟', '男', '教授', '计算机科学与技术学院', '13900139005', 'qianwei@university.edu.cn', '2011-09-01', NOW(), NOW()),
('T006', '孙丽', '女', '讲师', '软件工程学院', '13900139006', 'sunli@university.edu.cn', '2018-09-01', NOW(), NOW()),
('T007', '周强', '男', '教授', '软件工程学院', '13900139007', 'zhouqiang@university.edu.cn', '2009-09-01', NOW(), NOW()),
('T008', '吴敏', '女', '副教授', '人工智能学院', '13900139008', 'wumin@university.edu.cn', '2014-09-01', NOW(), NOW()),
('T009', '郑涛', '男', '讲师', '人工智能学院', '13900139009', 'zhengtao@university.edu.cn', '2020-09-01', NOW(), NOW()),
('T010', '陈静', '女', '特聘教授', '人工智能学院', '13900139010', 'chenjing@university.edu.cn', '2022-09-01', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

INSERT INTO courses (name, credits, instructor, description, teacher_id) VALUES
('高等数学', 4, '张明华', '微积分、线性代数等数学基础课程', 1),
('大学英语', 3, '李芳', '英语听说读写综合能力培养', 2),
('计算机基础', 3, '王建国', 'Python程序设计和算法基础', 3),
('数据结构', 4, '赵雪梅', '线性表、树、图等数据结构及算法', 4),
('数据库原理', 3, '钱伟', '关系型数据库设计与SQL查询', 5),
('Web开发', 3, '孙丽', 'HTML、CSS、JavaScript全栈开发', 6),
('软件工程', 3, '周强', '软件开发生命周期与项目管理', 7)
ON DUPLICATE KEY UPDATE instructor=VALUES(instructor), teacher_id=VALUES(teacher_id);

-- 插入成绩数据
INSERT INTO grades (student_id, course_id, score) VALUES
(1, 1, 85.5),
(1, 2, 90.0),
(1, 3, 88.0),
(2, 1, 92.0),
(2, 2, 87.5),
(2, 4, 91.0),
(3, 1, 78.0),
(3, 3, 82.5),
(3, 5, 85.0),
(4, 2, 95.0),
(4, 4, 89.5),
(4, 6, 93.0),
(5, 1, 86.0),
(5, 3, 84.5),
(5, 5, 88.5),
(6, 2, 91.5),
(6, 4, 87.0),
(6, 7, 90.0),
(7, 1, 80.0),
(7, 3, 83.0),
(8, 2, 88.5),
(8, 5, 86.0),
(9, 4, 92.5),
(9, 6, 89.0),
(10, 1, 87.5),
(10, 7, 91.0)
ON DUPLICATE KEY UPDATE score=score;

-- 初始化管理员密码配置（仅首次插入，后续可通过接口修改）
INSERT INTO system_config (config_key, config_value) VALUES
('admin_password', 'admin123456')
ON DUPLICATE KEY UPDATE config_key = config_key;
