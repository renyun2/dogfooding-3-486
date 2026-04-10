import request from './request'

/**
 * 认证相关API
 */
export const authApi = {
    // 登录
    login(credentials) {
        return request.post('/api/auth/login', credentials)
    },

    // 登出
    logout() {
        return request.post('/api/auth/logout')
    },

    // 修改密码
    changePassword(data) {
        return request.put('/api/auth/password', data)
    }
}

export default authApi
