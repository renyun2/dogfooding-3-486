import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi } from '../api/auth'

/**
 * 认证状态管理Store
 */
export const useAuthStore = defineStore('auth', () => {
  // State
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')

  // Getters
  const isLoggedIn = computed(() => !!token.value)

  // Actions
  /**
   * 用户登录
   * @param {string} user - 用户名
   * @param {string} pass - 密码
   * @returns {Promise<boolean>} 登录是否成功
   */
  const login = async (user, pass) => {
    try {
      const res = await authApi.login({ username: user, password: pass })
      if (res.code === 200) {
        token.value = res.data.token
        username.value = res.data.username
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('username', res.data.username)
        return true
      }
      return false
    } catch (error) {
      console.error('Login failed:', error)
      return false
    }
  }

  /**
   * 用户登出
   */
  const logout = async () => {
    try {
      await authApi.logout()
    } catch (error) {
      console.error('Logout error:', error)
    } finally {
      clearAuth()
    }
  }

  /**
   * 清除认证信息
   */
  const clearAuth = () => {
    token.value = ''
    username.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('username')
  }

  /**
   * 修改密码
   * @param {string} oldPassword - 原密码
   * @param {string} newPassword - 新密码
   * @returns {Promise<boolean>} 修改是否成功
   */
  const changePassword = async (oldPassword, newPassword) => {
    try {
      const res = await authApi.changePassword({ oldPassword, newPassword })
      return res.code === 200
    } catch (error) {
      console.error('Change password failed:', error)
      return false
    }
  }

  /**
   * 从localStorage恢复登录状态
   */
  const restoreAuth = () => {
    const storedToken = localStorage.getItem('token')
    const storedUsername = localStorage.getItem('username')
    if (storedToken && storedUsername) {
      token.value = storedToken
      username.value = storedUsername
    }
  }

  return {
    token,
    username,
    isLoggedIn,
    login,
    logout,
    clearAuth,
    changePassword,
    restoreAuth
  }
})

export default useAuthStore
