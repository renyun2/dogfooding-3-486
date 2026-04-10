import { createPinia } from 'pinia'

/**
 * Pinia Store 入口
 */
export const pinia = createPinia()

// 导出所有store
export { useAuthStore } from './auth'

export default pinia
