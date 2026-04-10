import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useApi } from './useApi'

export function useCrud(api, options = {}) {
  const {
    immediate = true,
    defaultForm = {},
    confirmDelete = true,
    deleteMessage = '确定要删除吗？'
  } = options

  const { loading, execute, confirm } = useApi()
  const dataList = ref([])
  const dialogVisible = ref(false)
  const isEdit = ref(false)
  const formData = reactive({ ...defaultForm })
  const submitting = ref(false)

  const loadData = async () => {
    const { success, data } = await execute(() => api.getAll())
    if (success) {
      dataList.value = data?.data || []
    }
  }

  const handleAdd = () => {
    isEdit.value = false
    Object.assign(formData, defaultForm)
    dialogVisible.value = true
  }

  const handleEdit = (row) => {
    isEdit.value = true
    Object.assign(formData, row)
    dialogVisible.value = true
  }

  const handleDelete = async (row, idField = 'id') => {
    if (confirmDelete) {
      const confirmed = await confirm(
        typeof deleteMessage === 'function' ? deleteMessage(row) : deleteMessage
      )
      if (!confirmed) return
    }

    const { success } = await execute(
      () => api.delete(row[idField]),
      { successMessage: '删除成功' }
    )
    
    if (success) {
      loadData()
    }
  }

  const handleSubmit = async (validateFn) => {
    if (validateFn) {
      const valid = await validateFn()
      if (!valid) return
    }

    submitting.value = true
    try {
      const action = isEdit.value
        ? () => api.update(formData.id, formData)
        : () => api.create(formData)

      const { success } = await execute(action, {
        successMessage: isEdit.value ? '更新成功' : '添加成功'
      })

      if (success) {
        dialogVisible.value = false
        loadData()
      }
    } finally {
      submitting.value = false
    }
  }

  const resetForm = (formRef) => {
    Object.assign(formData, defaultForm)
    formRef?.value?.resetFields()
  }

  onMounted(() => {
    if (immediate) {
      loadData()
    }
  })

  return {
    loading,
    dataList,
    dialogVisible,
    isEdit,
    formData,
    submitting,
    loadData,
    handleAdd,
    handleEdit,
    handleDelete,
    handleSubmit,
    resetForm
  }
}
