import { ref } from 'vue'

export function useApi() {
  const loading = ref(false)
  const error = ref(null)

  const execute = async (apiCall, options = {}) => {
    const { handleError = true, defaultValue = null } = options

    loading.value = true
    error.value = null

    try {
      const result = await apiCall()
      return result
    } catch (err) {
      error.value = err
      if (handleError) {
        console.error('API call failed:', err)
      } else {
        throw err
      }
      return defaultValue
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    error,
    execute
  }
}

export function useApiData(fetchFn) {
  const loading = ref(false)
  const data = ref(null)
  const error = ref(null)

  const fetch = async (...args) => {
    loading.value = true
    error.value = null
    try {
      const result = await fetchFn(...args)
      data.value = result.data || null
      return result
    } catch (err) {
      error.value = err
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    data,
    error,
    fetch
  }
}
