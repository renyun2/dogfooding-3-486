<template>
  <div class="course-list">
    <h1 class="page-title">
      <el-icon><Reading /></el-icon>
      课程管理
    </h1>
    
    <div class="page-card">
      <!-- 操作栏 -->
      <div class="action-bar">
        <el-input
          v-model="searchQuery"
          placeholder="搜索课程名称"
          style="max-width: 300px;"
          clearable
          @clear="loadCourses"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>
        
        <el-button type="primary" :icon="Plus" @click="handleAdd">
          添加课程
        </el-button>
      </div>
      
      <!-- 课程表格 -->
      <el-table 
        :data="courses" 
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="课程名称" min-width="150" />
        <el-table-column prop="credits" label="学分" width="100">
          <template #default="{ row }">
            <el-tag type="success">{{ row.credits }} 学分</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="teacher.name" label="授课教师" width="120" />
        <el-table-column prop="description" label="课程描述" min-width="200" show-overflow-tooltip />
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
      :title="isEdit ? '编辑课程' : '添加课程'"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="90px"
      >
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入课程名称" />
        </el-form-item>
        
        <el-form-item label="学分" prop="credits">
          <el-input-number v-model="formData.credits" :min="1" :max="10" />
        </el-form-item>
        
        <el-form-item label="授课教师" prop="teacherId">
          <el-select v-model="formData.teacherId" placeholder="请选择授课教师" style="width: 100%">
            <el-option
              v-for="item in teachers"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="课程描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="请输入课程描述"
            maxlength="500"
            show-word-limit
          />
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
import { Reading, Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { courseApi } from '../api/course'
import { teacherApi } from '../api/teacher'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const submitting = ref(false)
const courses = ref([])
const teachers = ref([])
const searchQuery = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  name: '',
  credits: 3,
  teacherId: null,
  description: ''
})

const rules = {
  name: [
    { required: true, message: '请输入课程名称', trigger: 'blur' },
    { min: 2, max: 100, message: '课程名称长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  credits: [
    { required: true, message: '请输入学分', trigger: 'blur' }
  ],
  teacherId: [
    { required: true, message: '请选择授课教师', trigger: 'change' }
  ],
  description: [
    { max: 500, message: '课程描述不能超过 500 个字符', trigger: 'blur' }
  ]
}

const loadCourses = async () => {
  loading.value = true
  try {
    const res = await courseApi.getAll()
    courses.value = res.data || []
  } catch (error) {
    console.error('Failed to load courses:', error)
  } finally {
    loading.value = false
  }
}

const loadTeachers = async () => {
  try {
    const res = await teacherApi.getAll()
    teachers.value = res.data || []
  } catch (error) {
    console.error('Failed to load teachers:', error)
  }
}

const handleSearch = async () => {
  if (!searchQuery.value.trim()) {
    loadCourses()
    return
  }
  
  loading.value = true
  try {
    const res = await courseApi.search(searchQuery.value)
    courses.value = res.data || []
    ElMessage.success(`找到 ${courses.value.length} 条记录`)
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
    `确定要删除课程 "${row.name}" 吗？该操作将同时删除所有该课程的成绩记录！`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await courseApi.delete(row.id)
      ElMessage.success('删除成功')
      loadCourses()
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
        await courseApi.update(formData.id, formData)
        ElMessage.success('更新成功')
      } else {
        await courseApi.create(formData)
        ElMessage.success('添加成功')
      }
      dialogVisible.value = false
      loadCourses()
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
    credits: 3,
    teacherId: null,
    description: ''
  })
  formRef.value?.resetFields()
}

onMounted(() => {
  loadCourses()
  loadTeachers()
})
</script>

<style scoped>
.course-list {
  min-height: calc(100vh - 150px);
}
</style>
