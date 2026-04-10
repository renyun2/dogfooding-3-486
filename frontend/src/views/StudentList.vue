<template>
  <div class="student-list">
    <h1 class="page-title">
      <el-icon><User /></el-icon>
      学生管理
    </h1>

    <div class="page-card">
      <!-- 操作栏 -->
      <div class="action-bar">
        <el-input
          v-model="searchQuery"
          placeholder="搜索学生姓名"
          style="max-width: 300px;"
          clearable
          @clear="loadData"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>

        <el-button type="primary" :icon="Plus" @click="handleAdd">
          添加学生
        </el-button>
      </div>

      <!-- 学生表格 -->
      <el-table
        :data="dataList"
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <el-tag :type="row.gender === '男' ? 'primary' : 'danger'" size="small">
              {{ row.gender }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="email" label="邮箱" min-width="200" show-overflow-tooltip />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="clazz.name" label="班级" width="150" />
        <el-table-column prop="enrollmentDate" label="入学日期" width="120" />
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              :icon="Edit"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              :icon="Delete"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑学生' : '添加学生'"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="90px"
      >
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="formData.age" :min="1" :max="150" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" />
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>

        <el-form-item label="入学日期" prop="enrollmentDate">
          <el-date-picker
            v-model="formData.enrollmentDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="班级" prop="clazzId">
          <el-select v-model="formData.clazzId" placeholder="请选择班级" style="width: 100%">
            <el-option
              v-for="item in clazzes"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { User, Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { studentApi } from '../api/student'
import { getAllClasses } from '../api/clazz'
import { useCrud } from '../composables/useCrud'

// 班级列表
const clazzes = ref([])

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 50, message: '姓名长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  age: [
    { required: true, message: '请输入年龄', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  enrollmentDate: [
    { required: true, message: '请选择入学日期', trigger: 'change' }
  ],
  clazzId: [
    { required: true, message: '请选择班级', trigger: 'change' }
  ]
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
  loadData,
  handleSearch,
  handleAdd,
  handleEdit,
  handleDelete,
  handleSubmit,
  handleDialogClose
} = useCrud({
  fetchAll: studentApi.getAll,
  create: (_, data) => studentApi.create(data),
  update: (_, data) => studentApi.update(data.id, data),
  delete: studentApi.delete,
  search: studentApi.search,
  defaultForm: {
    id: null,
    name: '',
    gender: '男',
    age: 18,
    email: '',
    phone: '',
    enrollmentDate: '',
    clazzId: null
  },
  resourceName: '学生',
  validationRules: rules
})

// 加载班级列表
const loadClasses = async () => {
  try {
    const res = await getAllClasses()
    clazzes.value = res.data || []
  } catch (error) {
    console.error('Failed to load classes:', error)
  }
}

// 自定义删除处理（带警告信息）
const handleDeleteStudent = (row) => {
  handleDelete(row, {
    confirmMessage: `确定要删除学生 "${row.name}" 吗？`,
    warningMessage: '该操作将同时删除其所有成绩记录！'
  })
}

// 提交表单
const submitForm = () => {
  handleSubmit(async (formData, isEdit) => {
    const api = isEdit ? studentApi.update : studentApi.create
    const res = await api(isEdit ? formData.id : formData, formData)
    return res
  })
}

onMounted(() => {
  loadData()
  loadClasses()
})
</script>

<style scoped>
.student-list {
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
</style>
