import { ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export function useApi() {
  const loading = ref(false)
  const error = ref(null)

  const execute = async (apiCall, options = {}) => {
    const {
      successMessage = '',
      errorMessage = '操作失败',
      showSuccess = false,
      showError = true
    } = options

    loading.value = true
    error.value = null

    try {
      const result = await apiCall()
      
      if (showSuccess && successMessage) {
        ElMessage.success(successMessage)
      }
      
      return { success: true, data: result, error: null }
    } catch (err) {
      error.value = err
      if (showError) {
        ElMessage.error(err.response?.data?.message || err.message || errorMessage)
      }
      return { success: false, data: null, error: err }
    } finally {
      loading.value = false
    }
  }

  const confirm = async (message, title = '确认') => {
    try {
      await ElMessageBox.confirm(message, title, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      return true
    } catch {
      return false
    }
  }

  return {
    loading,
    error,
    execute,
    confirm
  }
}
