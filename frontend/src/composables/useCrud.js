import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

/**
 * 通用CRUD操作封装
 * @param {Object} options - 配置选项
 * @param {Function} options.fetchAll - 获取所有数据的API函数
 * @param {Function} options.create - 创建数据的API函数
 * @param {Function} options.update - 更新数据的API函数
 * @param {Function} options.delete - 删除数据的API函数
 * @param {Function} options.search - 搜索数据的API函数（可选）
 * @param {Object} options.defaultForm - 表单默认值
 * @param {String} options.resourceName - 资源名称（用于提示信息）
 * @param {Object} options.validationRules - 表单验证规则
 * @returns {Object} CRUD相关状态和方法
 */
export function useCrud(options) {
  const {
    fetchAll,
    create,
    update,
    delete: deleteApi,
    search,
    defaultForm = {},
    resourceName = '数据',
    validationRules = {}
  } = options

  // 状态
  const loading = ref(false)
  const submitting = ref(false)
  const dialogVisible = ref(false)
  const isEdit = ref(false)
  const dataList = ref([])
  const searchQuery = ref('')
  const formRef = ref(null)

  // 表单数据
  const formData = reactive({ ...defaultForm })

  // 过滤后的数据（如果没有提供search API，则在本地过滤）
  const filteredData = computed(() => {
    if (!searchQuery.value || search) return dataList.value
    return dataList.value.filter(item =>
      JSON.stringify(item).toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  })

  // 重置表单
  const resetForm = () => {
    Object.assign(formData, defaultForm)
    if (formRef.value) {
      formRef.value.resetFields()
    }
  }

  // 加载数据
  const loadData = async () => {
    loading.value = true
    try {
      const res = await fetchAll()
      if (res.code === 200) {
        dataList.value = res.data || []
      }
    } catch (error) {
      console.error(`Failed to load ${resourceName}:`, error)
    } finally {
      loading.value = false
    }
  }

  // 搜索
  const handleSearch = async () => {
    if (!searchQuery.value.trim()) {
      loadData()
      return
    }

    if (search) {
      loading.value = true
      try {
        const res = await search(searchQuery.value)
        if (res.code === 200) {
          dataList.value = res.data || []
          ElMessage.success(`找到 ${dataList.value.length} 条记录`)
        }
      } catch (error) {
        console.error('Search failed:', error)
      } finally {
        loading.value = false
      }
    }
  }

  // 打开添加对话框
  const handleAdd = () => {
    isEdit.value = false
    resetForm()
    dialogVisible.value = true
  }

  // 打开编辑对话框
  const handleEdit = (row) => {
    isEdit.value = true
    Object.assign(formData, row)
    dialogVisible.value = true
  }

  // 删除
  const handleDelete = (row, options = {}) => {
    const {
      confirmMessage = `确定要删除该${resourceName}吗？`,
      warningMessage = '此操作不可恢复！'
    } = options

    ElMessageBox.confirm(
      `${confirmMessage}${warningMessage ? '<br><span style="color: #f56c6c;">' + warningMessage + '</span>' : ''}`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }
    )
      .then(async () => {
        try {
          const res = await deleteApi(row.id)
          if (res.code === 200) {
            ElMessage.success('删除成功')
            loadData()
          }
        } catch (error) {
          console.error('Delete failed:', error)
        }
      })
      .catch(() => {})
  }

  // 提交表单
  const handleSubmit = async (customHandler) => {
    if (!formRef.value) return

    await formRef.value.validate(async (valid) => {
      if (!valid) return

      submitting.value = true
      try {
        if (customHandler) {
          await customHandler(formData, isEdit.value)
        } else {
          const api = isEdit.value ? update : create
          const res = await api(isEdit.value ? formData.id : null, formData)
          if (res.code === 200 || res.code === 201) {
            ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
            dialogVisible.value = false
            loadData()
          }
        }
      } catch (error) {
        console.error('Submit failed:', error)
      } finally {
        submitting.value = false
      }
    })
  }

  // 关闭对话框
  const handleDialogClose = () => {
    resetForm()
    dialogVisible.value = false
  }

  return {
    // 状态
    loading,
    submitting,
    dialogVisible,
    isEdit,
    dataList,
    searchQuery,
    formRef,
    formData,
    filteredData,
    rules: validationRules,

    // 方法
    loadData,
    handleSearch,
    handleAdd,
    handleEdit,
    handleDelete,
    handleSubmit,
    handleDialogClose,
    resetForm
  }
}

/**
 * 带分页的CRUD操作封装
 * @param {Object} options - 配置选项
 * @returns {Object} 分页CRUD相关状态和方法
 */
export function useCrudWithPagination(options) {
  const crud = useCrud(options)

  // 分页状态
  const pagination = reactive({
    currentPage: 1,
    pageSize: 10,
    total: 0
  })

  // 分页后的数据
  const paginatedData = computed(() => {
    const start = (pagination.currentPage - 1) * pagination.pageSize
    const end = start + pagination.pageSize
    return crud.filteredData.value.slice(start, end)
  })

  // 页码变化
  const handlePageChange = (page) => {
    pagination.currentPage = page
  }

  // 每页条数变化
  const handleSizeChange = (size) => {
    pagination.pageSize = size
    pagination.currentPage = 1
  }

  return {
    ...crud,
    pagination,
    paginatedData,
    handlePageChange,
    handleSizeChange
  }
}

export default useCrud
