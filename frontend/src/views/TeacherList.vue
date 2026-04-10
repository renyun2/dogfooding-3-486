<template>
  <div class="teacher-list">
    <h1 class="page-title">
      <el-icon><User /></el-icon>
      教师管理
    </h1>
    
    <div class="page-card">
      <!-- 操作栏 -->
      <div class="action-bar">
        <el-input
          v-model="searchQuery"
          placeholder="搜索教师姓名"
          style="max-width: 300px;"
          clearable
          @clear="loadTeachers"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button :icon="Search" @click="handleSearch" />
          </template>
        </el-input>
        
        <el-button type="primary" :icon="Plus" @click="handleAdd">
          添加教师
        </el-button>
      </div>
      
      <!-- 教师表格 -->
      <el-table 
        :data="teachers" 
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="teacherNo" label="工号" width="100" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <el-tag :type="row.gender === '男' ? 'primary' : 'danger'" size="small">
              {{ row.gender }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="职称" width="100">
          <template #default="{ row }">
            <el-tag 
              :type="row.title === '教授' ? 'danger' : row.title === '副教授' ? 'warning' : row.title === '讲师' ? 'primary' : 'info'" 
              size="small"
            >
              {{ row.title }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="所属院系" min-width="180" show-overflow-tooltip />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="email" label="邮箱" min-width="200" show-overflow-tooltip />
        <el-table-column prop="hireDate" label="入职日期" width="120" />
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
      :title="isEdit ? '编辑教师' : '添加教师'"
      width="600px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="90px"
      >
        <el-form-item label="工号" prop="teacherNo">
          <el-input v-model="formData.teacherNo" placeholder="请输入工号，如T00001" maxlength="20" />
        </el-form-item>
        
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formData.name" placeholder="请输入姓名" maxlength="50" />
        </el-form-item>
        
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="formData.gender">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="职称" prop="title">
          <el-select v-model="formData.title" placeholder="请选择职称" style="width: 100%">
            <el-option label="教授" value="教授" />
            <el-option label="副教授" value="副教授" />
            <el-option label="讲师" value="讲师" />
            <el-option label="助教" value="助教" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="所属院系" prop="department">
          <el-input v-model="formData.department" placeholder="请输入所属院系" maxlength="100" />
        </el-form-item>
        
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入联系电话" maxlength="20" />
        </el-form-item>
        
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" maxlength="100" />
        </el-form-item>
        
        <el-form-item label="入职日期" prop="hireDate">
          <el-date-picker
            v-model="formData.hireDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
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
import { User, Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { teacherApi } from '../api/teacher'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const submitting = ref(false)
const teachers = ref([])
const searchQuery = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  teacherNo: '',
  name: '',
  gender: '男',
  title: '讲师',
  department: '',
  phone: '',
  email: '',
  hireDate: ''
})

const rules = {
  teacherNo: [
    { required: true, message: '请输入工号', trigger: 'blur' },
    { pattern: /^T\d{5}$/, message: '工号格式必须为T+5位数字，如T00001', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 50, message: '姓名长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  title: [
    { required: true, message: '请选择职称', trigger: 'change' }
  ],
  department: [
    { required: true, message: '请输入所属院系', trigger: 'blur' },
    { min: 2, max: 100, message: '院系名称长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  hireDate: [
    { required: true, message: '请选择入职日期', trigger: 'change' }
  ]
}

const loadTeachers = async () => {
  loading.value = true
  try {
    const res = await teacherApi.getAll()
    teachers.value = res.data || []
  } catch (error) {
    console.error('Failed to load teachers:', error)
    ElMessage.error('加载教师列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = async () => {
  if (!searchQuery.value.trim()) {
    loadTeachers()
    return
  }
  
  loading.value = true
  try {
    const res = await teacherApi.search(searchQuery.value)
    teachers.value = res.data || []
    ElMessage.success(`找到 ${teachers.value.length} 条记录`)
  } catch (error) {
    console.error('Search failed:', error)
    ElMessage.error('搜索失败')
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
    `确定要删除教师 "${row.name}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await teacherApi.delete(row.id)
      ElMessage.success('删除成功')
      loadTeachers()
    } catch (error) {
      console.error('Delete failed:', error)
      ElMessage.error('删除失败')
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
        await teacherApi.update(formData.id, formData)
        ElMessage.success('更新成功')
      } else {
        await teacherApi.create(formData)
        ElMessage.success('添加成功')
      }
      dialogVisible.value = false
      loadTeachers()
    } catch (error) {
      console.error('Submit failed:', error)
      ElMessage.error(error.response?.data?.message || '提交失败，请检查工号和邮箱是否重复')
    } finally {
      submitting.value = false
    }
  })
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    teacherNo: '',
    name: '',
    gender: '男',
    title: '讲师',
    department: '',
    phone: '',
    email: '',
    hireDate: ''
  })
  formRef.value?.resetFields()
}

onMounted(() => {
  loadTeachers()
})
</script>

<style scoped>
.teacher-list {
  min-height: calc(100vh - 150px);
}
</style>
