import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export function useCrud(apiService, options = {}) {
  const {
    defaultFormData = {},
    confirmMessage = '确定要删除这条记录吗？',
    successMessages = {
      create: '添加成功',
      update: '更新成功',
      delete: '删除成功'
    }
  } = options

  const loading = ref(false)
  const submitting = ref(false)
  const dataList = ref([])
  const dialogVisible = ref(false)
  const isEdit = ref(false)
  const formRef = ref(null)

  const formData = reactive({ ...defaultFormData })

  const loadData = async () => {
    loading.value = true
    try {
      const res = await apiService.getAll()
      dataList.value = res.data || []
    } catch (error) {
      console.error('Failed to load data:', error)
    } finally {
      loading.value = false
    }
  }

  const handleAdd = () => {
    isEdit.value = false
    resetForm()
    dialogVisible.value = true
  }

  const handleEdit = (row) => {
    isEdit.value = true
    Object.assign(formData, row)
    dialogVisible.value = true
  }

  const handleDelete = (row, displayField = 'name') => {
    ElMessageBox.confirm(
      confirmMessage.replace('{name}', row[displayField]),
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    ).then(async () => {
      try {
        await apiService.delete(row.id)
        ElMessage.success(successMessages.delete)
        loadData()
      } catch (error) {
        console.error('Delete failed:', error)
      }
    }).catch(() => {})
  }

  const handleSubmit = async () => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
      if (!valid) return

      submitting.value = true
      try {
        if (isEdit.value) {
          await apiService.update(formData.id, formData)
          ElMessage.success(successMessages.update)
        } else {
          await apiService.create(formData)
          ElMessage.success(successMessages.create)
        }
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error('Submit failed:', error)
      } finally {
        submitting.value = false
      }
    })
  }

  const resetForm = () => {
    Object.keys(defaultFormData).forEach(key => {
      formData[key] = defaultFormData[key]
    })
    formRef.value?.resetFields()
  }

  return {
    loading,
    submitting,
    dataList,
    dialogVisible,
    isEdit,
    formRef,
    formData,
    loadData,
    handleAdd,
    handleEdit,
    handleDelete,
    handleSubmit,
    resetForm
  }
}
