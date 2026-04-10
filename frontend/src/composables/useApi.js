import { ref } from 'vue'

export function useApi(apiFunction) {
  const loading = ref(false)
  const error = ref(null)
  const data = ref(null)

  const execute = async (...args) => {
    loading.value = true
    error.value = null
    try {
      const result = await apiFunction(...args)
      data.value = result.data || result
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
    error,
    data,
    execute
  }
}

export function useApiList(apiFunction) {
  const loading = ref(false)
  const list = ref([])

  const fetch = async (...args) => {
    loading.value = true
    try {
      const result = await apiFunction(...args)
      list.value = result.data || []
      return result
    } catch (err) {
      list.value = []
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    list,
    fetch
  }
}
