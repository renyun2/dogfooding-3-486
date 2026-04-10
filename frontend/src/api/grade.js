import request from './request'

export const gradeApi = {
    // 获取所有成绩
    getAll() {
        return request.get('/api/grades')
    },

    // 根据 ID 获取成绩
    getById(id) {
        return request.get(`/api/grades/${id}`)
    },

    // 获取学生的所有成绩
    getByStudentId(studentId) {
        return request.get(`/api/grades/student/${studentId}`)
    },

    // 获取课程的所有成绩
    getByCourseId(courseId) {
        return request.get(`/api/grades/course/${courseId}`)
    },

    // 创建成绩
    create(data) {
        return request.post('/api/grades', data)
    },

    // 更新成绩
    update(id, data) {
        return request.put(`/api/grades/${id}`, data)
    },

    // 删除成绩
    delete(id) {
        return request.delete(`/api/grades/${id}`)
    },

    // 获取统计信息
    getStatistics() {
        return request.get('/api/grades/statistics')
    },

    // 获取学生平均分
    getStudentAverage(studentId) {
        return request.get(`/api/grades/average/student/${studentId}`)
    },

    // 获取课程平均分
    getCourseAverage(courseId) {
        return request.get(`/api/grades/average/course/${courseId}`)
    }
}
