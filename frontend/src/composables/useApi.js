import { ref } from 'vue'
import { ElMessage } from 'element-plus'

/**
 * 通用API调用封装
 * 自动处理loading状态和错误提示
 *
 * @param {Function} apiFunction - API调用函数
 * @param {Object} options - 配置选项
 * @param {Boolean} options.showSuccessMsg - 是否显示成功消息
 * @param {String} options.successMsg - 成功消息文本
 * @param {Boolean} options.showErrorMsg - 是否显示错误消息
 * @param {String} options.errorMsg - 错误消息文本
 * @param {Function} options.onSuccess - 成功回调
 * @param {Function} options.onError - 错误回调
 * @returns {Object} API调用相关状态和方法
 */
export function useApi(apiFunction, options = {}) {
  const {
    showSuccessMsg = false,
    successMsg = '操作成功',
    showErrorMsg = true,
    errorMsg = '操作失败',
    onSuccess,
    onError
  } = options

  const loading = ref(false)
  const data = ref(null)
  const error = ref(null)

  const execute = async (...args) => {
    loading.value = true
    error.value = null

    try {
      const response = await apiFunction(...args)
      data.value = response

      if (showSuccessMsg && response.code === 200) {
        ElMessage.success(successMsg)
      }

      if (onSuccess) {
        onSuccess(response)
      }

      return response
    } catch (err) {
      error.value = err

      if (showErrorMsg) {
        const msg = err.response?.data?.message || err.message || errorMsg
        ElMessage.error(msg)
      }

      if (onError) {
        onError(err)
      }

      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    data,
    error,
    execute
  }
}

/**
 * 批量API调用封装
 * @param {Array} apiFunctions - API调用函数数组
 * @param {Object} options - 配置选项
 * @returns {Object} 批量调用相关状态和方法
 */
export function useApiBatch(apiFunctions, options = {}) {
  const { showErrorMsg = true } = options

  const loading = ref(false)
  const results = ref([])
  const errors = ref([])

  const execute = async (...argsArray) => {
    loading.value = true
    results.value = []
    errors.value = []

    try {
      const promises = apiFunctions.map((fn, index) =>
        fn(...(argsArray[index] || [])).catch(err => {
          if (showErrorMsg) {
            ElMessage.error(err.message || '请求失败')
          }
          errors.value.push({ index, error: err })
          return null
        })
      )

      results.value = await Promise.all(promises)
      return results.value
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    results,
    errors,
    execute
  }
}

/**
 * 创建标准CRUD API对象
 * @param {Object} api - 包含CRUD方法的对象
 * @returns {Object} 包装后的CRUD API对象
 */
export function createCrudApi(api) {
  return {
    getAll: (options) => useApi(api.getAll, options),
    getById: (options) => useApi(api.getById, options),
    create: (options = {}) => useApi(api.create, { showSuccessMsg: true, successMsg: '创建成功', ...options }),
    update: (options = {}) => useApi(api.update, { showSuccessMsg: true, successMsg: '更新成功', ...options }),
    delete: (options = {}) => useApi(api.delete, { showSuccessMsg: true, successMsg: '删除成功', ...options }),
    search: (options) => useApi(api.search, options)
  }
}

export default useApi
