import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export function useCrud(fetchDataFn, createFn, updateFn, deleteFn, defaultFormData) {
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
      const res = await fetchDataFn()
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

  const handleDelete = (row, nameField = 'name') => {
    ElMessageBox.confirm(
      `确定要删除 "${row[nameField]}" 吗？该操作不可恢复！`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    ).then(async () => {
      try {
        await deleteFn(row.id)
        ElMessage.success('删除成功')
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
          await updateFn(formData.id, formData)
          ElMessage.success('更新成功')
        } else {
          await createFn(formData)
          ElMessage.success('添加成功')
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
