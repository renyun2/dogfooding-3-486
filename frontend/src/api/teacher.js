import request from './request'

export const teacherApi = {
    // 获取所有教师
    getAll() {
        return request.get('/api/teachers')
    },

    // 根据 ID 获取教师
    getById(id) {
        return request.get(`/api/teachers/${id}`)
    },

    // 创建教师
    create(data) {
        return request.post('/api/teachers', data)
    },

    // 更新教师
    update(id, data) {
        return request.put(`/api/teachers/${id}`, data)
    },

    // 删除教师
    delete(id) {
        return request.delete(`/api/teachers/${id}`)
    },

    // 搜索教师
    search(name) {
        return request.get('/api/teachers/search', { params: { name } })
    },

    // 获取教师总数
    getCount() {
        return request.get('/api/teachers/count')
    }
}
