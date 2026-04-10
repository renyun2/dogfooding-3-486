<template>
  <div class="class-list">
    <h1 class="page-title">
      <el-icon><School /></el-icon> 班级管理
    </h1>

    <div class="page-card">
      <div class="action-bar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon> 添加班级
        </el-button>
        <el-input
          v-model="searchQuery"
          placeholder="搜索班级名称..."
          style="width: 300px"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <el-table :data="filteredData" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="班级名称" width="180" />
        <el-table-column prop="description" label="班级描述" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="scope">
            {{ new Date(scope.row.createdAt).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDeleteClass(scope.row)">
              <el-icon><Delete /></el-icon> 删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑班级' : '添加班级'"
      width="500px"
      append-to-body
      @close="handleDialogClose"
    >
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="班级名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入班级名称" />
        </el-form-item>
        <el-form-item label="班级描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            placeholder="请输入班级描述"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitting"> 确定 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { School, Plus, Search, Edit, Delete } from '@element-plus/icons-vue'
import { clazzApi } from '../api/clazz'
import { useCrud } from '../composables/useCrud'

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入班级名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [{ max: 200, message: '描述不能超过 200 个字符', trigger: 'blur' }]
}

// 使用CRUD composable
const {
  loading,
  submitting,
  dialogVisible,
  isEdit,
  dataList,
  searchQuery,
  formRef,
  formData,
  filteredData,
  loadData,
  handleAdd,
  handleEdit,
  handleDelete,
  handleSubmit,
  handleDialogClose
} = useCrud({
  fetchAll: clazzApi.getAll,
  create: (_, data) => clazzApi.create(data),
  update: (_, data) => clazzApi.update(data.id, data),
  delete: clazzApi.delete,
  defaultForm: {
    id: null,
    name: '',
    description: ''
  },
  resourceName: '班级',
  validationRules: rules
})

// 自定义删除处理（带警告信息）
const handleDeleteClass = (row) => {
  handleDelete(row, {
    confirmMessage: `确定要删除班级 "${row.name}" 吗？`,
    warningMessage: '该操作将同时删除该班级的所有学生及其成绩记录！'
  })
}

// 提交表单
const submitForm = () => {
  handleSubmit(async (formData, isEdit) => {
    const api = isEdit ? clazzApi.update : clazzApi.create
    const res = await api(isEdit ? formData.id : formData, formData)
    return res
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.class-list {
  padding: 20px;
}

.page-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
  color: #303133;
  font-size: 24px;
}

.page-card {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
