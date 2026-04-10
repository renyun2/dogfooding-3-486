import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')

  const isLoggedIn = computed(() => !!token.value)

  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUsername = (newUsername) => {
    username.value = newUsername
    localStorage.setItem('username', newUsername)
  }

  const login = (tokenValue, usernameValue) => {
    setToken(tokenValue)
    setUsername(usernameValue)
  }

  const logout = () => {
    token.value = ''
    username.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('username')
  }

  return {
    token,
    username,
    isLoggedIn,
    setToken,
    setUsername,
    login,
    logout
  }
})
