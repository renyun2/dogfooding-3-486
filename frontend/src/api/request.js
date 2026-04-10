import axios from 'axios'
import { ElMessage } from 'element-plus'

const baseURL = window.location.hostname === 'localhost'
    ? 'http://localhost:8247'
    : 'http://backend:8080'

const request = axios.create({
    baseURL: baseURL,
    timeout: 10000
})

request.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`
        }
        return config
    },
    error => {
        console.error('Request error:', error)
        return Promise.reject(error)
    }
)

request.interceptors.response.use(
    response => {
        const res = response.data
        if (res.code && res.code !== 200 && res.code !== 201) {
            ElMessage.error(res.message || '请求失败')
            return Promise.reject(new Error(res.message || '请求失败'))
        }
        return res
    },
    error => {
        if (error.response?.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('username')
            ElMessage.error('登录已过期，请重新登录')
            window.location.href = '/login'
            return Promise.reject(error)
        }
        console.error('Response error:', error)
        ElMessage.error(error.response?.data?.message || error.message || '网络请求失败，请检查后端服务')
        return Promise.reject(error)
    }
)

export default request
