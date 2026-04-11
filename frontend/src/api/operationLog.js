import request from './request'

export const operationLogApi = {
    getList(params) {
        return request.get('/api/operation-logs', { params })
    },

    getById(id) {
        return request.get(`/api/operation-logs/${id}`)
    },

    delete(id) {
        return request.delete(`/api/operation-logs/${id}`)
    }
}
