import request from './request'

export const operationLogApi = {
    getAll(page = 0, size = 10) {
        return request.get(`/api/operation-logs?page=${page}&size=${size}`)
    },

    getById(id) {
        return request.get(`/api/operation-logs/${id}`)
    },

    search(params) {
        const query = new URLSearchParams()
        if (params.operator) query.append('operator', params.operator)
        if (params.module) query.append('module', params.module)
        if (params.startTime) query.append('startTime', params.startTime)
        if (params.endTime) query.append('endTime', params.endTime)
        if (params.isSuccess !== undefined && params.isSuccess !== null) {
            query.append('isSuccess', params.isSuccess)
        }
        query.append('page', params.page || 0)
        query.append('size', params.size || 10)
        return request.get(`/api/operation-logs/search?${query.toString()}`)
    },

    getModules() {
        return request.get('/api/operation-logs/modules')
    }
}
