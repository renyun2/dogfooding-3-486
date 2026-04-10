import request from './request'

export const studentApi = {
    // 获取所有学生
    getAll() {
        return request.get('/api/students')
    },

    // 根据 ID 获取学生
    getById(id) {
        return request.get(`/api/students/${id}`)
    },

    // 创建学生
    create(data) {
        return request.post('/api/students', data)
    },

    // 更新学生
    update(id, data) {
        return request.put(`/api/students/${id}`, data)
    },

    // 删除学生
    delete(id) {
        return request.delete(`/api/students/${id}`)
    },

    // 搜索学生
    search(name) {
        return request.get('/api/students/search', { params: { name } })
    },

    // 获取学生总数
    getCount() {
        return request.get('/api/students/count')
    }
}
