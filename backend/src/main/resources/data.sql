INSERT INTO classes (name, description, created_at, updated_at) VALUES
('软件工程 2024-01', '软件工程专业 1 班', NOW(), NOW()),
('计算机科学 2024-02', '计算机科学与技术专业 2 班', NOW(), NOW()),
('人工智能 2024-03', '人工智能实验班', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 然后插入学生数据，并关联班级
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

-- 插入教师数据
INSERT INTO teachers (teacher_no, name, gender, title, department, phone, email, hire_date, created_at, updated_at) VALUES
('T2024001', '张教授', '男', '教授', '数学学院', '13800138011', 'zhangprof@example.com', '2015-09-01', NOW(), NOW()),
('T2024002', '李老师', '女', '副教授', '外国语学院', '13800138012', 'lili@example.com', '2018-03-15', NOW(), NOW()),
('T2024003', '王教授', '男', '教授', '计算机学院', '13800138013', 'wangprof@example.com', '2012-07-01', NOW(), NOW()),
('T2024004', '赵教授', '男', '教授', '计算机学院', '13800138014', 'zhaoprof@example.com', '2010-09-01', NOW(), NOW()),
('T2024005', '钱教授', '女', '教授', '计算机学院', '13800138015', 'qianprof@example.com', '2014-03-01', NOW(), NOW()),
('T2024006', '孙老师', '女', '讲师', '软件学院', '13800138016', 'sunli@example.com', '2020-09-01', NOW(), NOW()),
('T2024007', '周教授', '男', '副教授', '软件学院', '13800138017', 'zhouprof@example.com', '2016-07-01', NOW(), NOW()),
('T2024008', '吴老师', '男', '讲师', '人工智能学院', '13800138018', 'wuli@example.com', '2021-09-01', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 插入课程数据，关联教师
INSERT INTO courses (name, credits, instructor, description, teacher_id) VALUES
('高等数学', 4, '张教授', '微积分、线性代数等数学基础课程', 1),
('大学英语', 3, '李老师', '英语听说读写综合能力培养', 2),
('计算机基础', 3, '王教授', 'Python程序设计和算法基础', 3),
('数据结构', 4, '赵教授', '线性表、树、图等数据结构及算法', 4),
('数据库原理', 3, '钱教授', '关系型数据库设计与SQL查询', 5),
('Web开发', 3, '孙老师', 'HTML、CSS、JavaScript全栈开发', 6),
('软件工程', 3, '周教授', '软件开发生命周期与项目管理', 7)
ON DUPLICATE KEY UPDATE name=name;

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
