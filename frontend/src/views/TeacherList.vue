<template>
  <div class="teacher-list">
    <h1 class="page-title">
      <el-icon><UserFilled /></el-icon>
      教师管理
    </h1>
    
    <div class="page-card">
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
      
      <el-table 
        :data="paginatedTeachers" 
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="employeeId" label="工号" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="80">
          <template #default="{ row }">
            <el-tag :type="row.gender === '男' ? 'primary' : 'danger'" size="small">
              {{ row.gender }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="职称" width="120" />
        <el-table-column prop="department" label="所属院系" min-width="150" show-overflow-tooltip />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="email" label="邮箱" min-width="180" show-overflow-tooltip />
        <el-table-column prop="hireDate" label="入职日期" width="110" />
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
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="filteredTeachers.length"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
    
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑教师' : '添加教师'"
      width="650px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="90px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="工号" prop="employeeId">
              <el-input v-model="formData.employeeId" placeholder="请输入工号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="formData.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="formData.gender">
                <el-radio label="男">男</el-radio>
                <el-radio label="女">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期" prop="hireDate">
              <el-date-picker
                v-model="formData.hireDate"
                type="date"
                placeholder="选择入职日期"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职称" prop="title">
              <el-select v-model="formData.title" placeholder="请选择职称" style="width: 100%">
                <el-option label="助教" value="助教" />
                <el-option label="讲师" value="讲师" />
                <el-option label="副教授" value="副教授" />
                <el-option label="教授" value="教授" />
                <el-option label="特聘教授" value="特聘教授" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属院系" prop="department">
              <el-input v-model="formData.department" placeholder="请输入所属院系" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="formData.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="formData.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
        </el-row>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { UserFilled, Search, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { teacherApi } from '../api/teacher'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const submitting = ref(false)
const teachers = ref([])
const searchQuery = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)

const formData = reactive({
  id: null,
  employeeId: '',
  name: '',
  gender: '男',
  title: '',
  department: '',
  phone: '',
  email: '',
  hireDate: ''
})

const rules = {
  employeeId: [
    { required: true, message: '请输入工号', trigger: 'blur' },
    { min: 3, max: 20, message: '工号长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 50, message: '姓名长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  hireDate: [
    { required: true, message: '请选择入职日期', trigger: 'change' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入有效的手机号码', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ]
}

const filteredTeachers = computed(() => {
  if (!searchQuery.value.trim()) {
    return teachers.value
  }
  return teachers.value.filter(t => 
    t.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const paginatedTeachers = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filteredTeachers.value.slice(start, end)
})

const loadTeachers = async () => {
  loading.value = true
  try {
    const res = await teacherApi.getAll()
    teachers.value = res.data || []
  } catch (error) {
    console.error('Failed to load teachers:', error)
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
    currentPage.value = 1
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
    } finally {
      submitting.value = false
    }
  })
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    employeeId: '',
    name: '',
    gender: '男',
    title: '',
    department: '',
    phone: '',
    email: '',
    hireDate: ''
  })
  formRef.value?.resetFields()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
}

const handleCurrentChange = (val) => {
  currentPage.value = val
}

onMounted(() => {
  loadTeachers()
})
</script>

<style scoped>
.teacher-list {
  min-height: calc(100vh - 150px);
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
