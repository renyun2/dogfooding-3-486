import request from './request'

export const operationLogApi = {
    getLogs(params) {
        return request.get('/api/operation-logs', { params })
    },

    getRecentLogs() {
        return request.get('/api/operation-logs/recent')
    },

    getLogById(id) {
        return request.get(`/api/operation-logs/${id}`)
    }
}
