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
          @clear="loadStudents"
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
        :data="students" 
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
      @close="resetForm"
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
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { User, Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { studentApi } from '../api/student'
import { getAllClasses } from '../api/clazz'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const submitting = ref(false)
const students = ref([])
const clazzes = ref([])
const searchQuery = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  name: '',
  gender: '男',
  age: 18,
  email: '',
  phone: '',
  enrollmentDate: '',
  clazzId: null
})

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

const loadStudents = async () => {
  loading.value = true
  try {
    const res = await studentApi.getAll()
    students.value = res.data || []
  } catch (error) {
    console.error('Failed to load students:', error)
  } finally {
    loading.value = false
  }
}

const loadClasses = async () => {
  try {
    const res = await getAllClasses()
    clazzes.value = res.data || []
  } catch (error) {
    console.error('Failed to load classes:', error)
  }
}

const handleSearch = async () => {
  if (!searchQuery.value.trim()) {
    loadStudents()
    return
  }
  
  loading.value = true
  try {
    const res = await studentApi.search(searchQuery.value)
    students.value = res.data || []
    ElMessage.success(`找到 ${students.value.length} 条记录`)
  } catch (error) {
    console.error('Search failed:', error)
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

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除学生 "${row.name}" 吗？该操作将同时删除其所有成绩记录！`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await studentApi.delete(row.id)
      ElMessage.success('删除成功')
      loadStudents()
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
        await studentApi.update(formData.id, formData)
        ElMessage.success('更新成功')
      } else {
        await studentApi.create(formData)
        ElMessage.success('添加成功')
      }
      dialogVisible.value = false
      loadStudents()
    } catch (error) {
      console.error('Submit failed:', error)
    } finally {
      submitting.value = false
    }
  })
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    name: '',
    gender: '男',
    age: 18,
    email: '',
    phone: '',
    enrollmentDate: '',
    clazzId: null
  })
  formRef.value?.resetFields()
}

onMounted(() => {
  loadStudents()
  loadClasses()
})
</script>

<style scoped>
.student-list {
  min-height: calc(100vh - 150px);
}
</style>
