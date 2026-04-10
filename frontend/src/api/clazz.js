import request from './request'

export function getAllClasses() {
    return request({
        url: '/api/classes',
        method: 'get'
    })
}

export function getClassById(id) {
    return request({
        url: `/api/classes/${id}`,
        method: 'get'
    })
}

export function createClass(data) {
    return request({
        url: '/api/classes',
        method: 'post',
        data
    })
}

export function updateClass(id, data) {
    return request({
        url: `/api/classes/${id}`,
        method: 'put',
        data
    })
}

export function deleteClass(id) {
    return request({
        url: `/api/classes/${id}`,
        method: 'delete'
    })
}
