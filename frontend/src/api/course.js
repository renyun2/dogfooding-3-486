import request from './request'

export const courseApi = {
    // 获取所有课程
    getAll() {
        return request.get('/api/courses')
    },

    // 根据 ID 获取课程
    getById(id) {
        return request.get(`/api/courses/${id}`)
    },

    // 创建课程
    create(data) {
        return request.post('/api/courses', data)
    },

    // 更新课程
    update(id, data) {
        return request.put(`/api/courses/${id}`, data)
    },

    // 删除课程
    delete(id) {
        return request.delete(`/api/courses/${id}`)
    },

    // 搜索课程
    search(name) {
        return request.get('/api/courses/search', { params: { name } })
    },

    // 获取课程总数
    getCount() {
        return request.get('/api/courses/count')
    }
}
