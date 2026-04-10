import request from './request'

/**
 * 班级管理API
 */
export const clazzApi = {
    // 获取所有班级
    getAll() {
        return request.get('/api/classes')
    },

    // 根据ID获取班级
    getById(id) {
        return request.get(`/api/classes/${id}`)
    },

    // 创建班级
    create(data) {
        return request.post('/api/classes', data)
    },

    // 更新班级
    update(id, data) {
        return request.put(`/api/classes/${id}`, data)
    },

    // 删除班级
    delete(id) {
        return request.delete(`/api/classes/${id}`)
    }
}

// 为了保持向后兼容，保留原有导出
export const getAllClasses = clazzApi.getAll
export const getClassById = clazzApi.getById
export const createClass = clazzApi.create
export const updateClass = clazzApi.update
export const deleteClass = clazzApi.delete

export default clazzApi
