import request from './request'

export const login = (username, password) => {
    return request.post('/api/auth/login', { username, password })
}

export const logout = () => {
    return request.post('/api/auth/logout')
}

export const changePassword = (oldPassword, newPassword) => {
    return request.put('/api/auth/password', { oldPassword, newPassword })
}
