import request from './request'

export const teacherApi = {
    getAll() {
        return request.get('/api/teachers')
    },

    getById(id) {
        return request.get(`/api/teachers/${id}`)
    },

    create(data) {
        return request.post('/api/teachers', data)
    },

    update(id, data) {
        return request.put(`/api/teachers/${id}`, data)
    },

    delete(id) {
        return request.delete(`/api/teachers/${id}`)
    },

    search(name) {
        return request.get('/api/teachers/search', { params: { name } })
    },

    getByDepartment(department) {
        return request.get(`/api/teachers/department/${department}`)
    },

    getCount() {
        return request.get('/api/teachers/count')
    }
}
