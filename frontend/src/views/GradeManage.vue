<template>
  <div class="grade-manage">
    <h1 class="page-title">
      <el-icon><Memo /></el-icon>
      成绩管理
    </h1>
    
    <div class="page-card">
      <!-- 操作栏 -->
      <div class="action-bar">
        <div style="display: flex; gap: 16px;">
          <el-select v-model="filterStudent" placeholder="按学生筛选" clearable @change="handleFilter" style="width: 200px;">
            <el-option
              v-for="student in students"
              :key="student.id"
              :label="student.name"
              :value="student.id"
            />
          </el-select>
          
          <el-select v-model="filterCourse" placeholder="按课程筛选" clearable @change="handleFilter" style="width: 200px;">
            <el-option
              v-for="course in courses"
              :key="course.id"
              :label="course.name"
              :value="course.id"
            />
          </el-select>
        </div>
        
        <el-button type="primary" :icon="Plus" @click="handleAdd">
          添加成绩
        </el-button>
      </div>
      
      <!-- 成绩表格 -->
      <el-table 
        :data="displayGrades" 
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="学生姓名" width="120">
          <template #default="{ row }">
            {{ getStudentName(row.studentId) }}
          </template>
        </el-table-column>
        <el-table-column label="课程名称" min-width="150">
          <template #default="{ row }">
            {{ getCourseName(row.courseId) }}
          </template>
        </el-table-column>
        <el-table-column prop="score" label="成绩" width="120">
          <template #default="{ row }">
            <el-tag :type="getScoreType(row.score)" size="large">
              {{ row.score }} 分
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="等级" width="100">
          <template #default="{ row }">
            {{ getGradeLevel(row.score) }}
          </template>
        </el-table-column>
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
      :title="isEdit ? '编辑成绩' : '添加成绩'"
      width="500px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="90px"
      >
        <el-form-item label="学生" prop="studentId">
          <el-select v-model="formData.studentId" placeholder="请选择学生" :disabled="isEdit" style="width: 100%;">
            <el-option
              v-for="student in students"
              :key="student.id"
              :label="`${student.name} (${student.email})`"
              :value="student.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="课程" prop="courseId">
          <el-select v-model="formData.courseId" placeholder="请选择课程" :disabled="isEdit" style="width: 100%;">
            <el-option
              v-for="course in courses"
              :key="course.id"
              :label="`${course.name} (${course.credits}学分)`"
              :value="course.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="成绩" prop="score">
          <el-input-number
            v-model="formData.score"
            :min="0"
            :max="100"
            :precision="2"
            :step="0.5"
            style="width: 100%;"
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
import { ref, reactive, computed, onMounted } from 'vue'
import { Memo, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { gradeApi } from '../api/grade'
import { studentApi } from '../api/student'
import { courseApi } from '../api/course'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const submitting = ref(false)
const grades = ref([])
const students = ref([])
const courses = ref([])
const filterStudent = ref(null)
const filterCourse = ref(null)
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const formData = reactive({
  id: null,
  studentId: null,
  courseId: null,
  score: 0
})

const rules = {
  studentId: [
    { required: true, message: '请选择学生', trigger: 'change' }
  ],
  courseId: [
    { required: true, message: '请选择课程', trigger: 'change' }
  ],
  score: [
    { required: true, message: '请输入成绩', trigger: 'blur' }
  ]
}

const displayGrades = computed(() => {
  let result = grades.value
  
  if (filterStudent.value) {
    result = result.filter(g => g.studentId === filterStudent.value)
  }
  
  if (filterCourse.value) {
    result = result.filter(g => g.courseId === filterCourse.value)
  }
  
  return result
})

const getStudentName = (studentId) => {
  const student = students.value.find(s => s.id === studentId)
  return student ? student.name : '未知'
}

const getCourseName = (courseId) => {
  const course = courses.value.find(c => c.id === courseId)
  return course ? course.name : '未知'
}

const getScoreType = (score) => {
  if (score >= 90) return 'success'
  if (score >= 80) return 'primary'
  if (score >= 70) return 'warning'
  if (score >= 60) return 'info'
  return 'danger'
}

const getGradeLevel = (score) => {
  if (score >= 90) return '优秀'
  if (score >= 80) return '良好'
  if (score >= 70) return '中等'
  if (score >= 60) return '及格'
  return '不及格'
}

const loadGrades = async () => {
  loading.value = true
  try {
    const res = await gradeApi.getAll()
    grades.value = res.data || []
  } catch (error) {
    console.error('Failed to load grades:', error)
  } finally {
    loading.value = false
  }
}

const loadStudents = async () => {
  try {
    const res = await studentApi.getAll()
    students.value = res.data || []
  } catch (error) {
    console.error('Failed to load students:', error)
  }
}

const loadCourses = async () => {
  try {
    const res = await courseApi.getAll()
    courses.value = res.data || []
  } catch (error) {
    console.error('Failed to load courses:', error)
  }
}

const handleFilter = () => {
  // 计算属性会自动处理过滤
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
  const studentName = getStudentName(row.studentId)
  const courseName = getCourseName(row.courseId)
  
  ElMessageBox.confirm(
    `确定要删除 "${studentName}" 在 "${courseName}" 的成绩吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async () => {
    try {
      await gradeApi.delete(row.id)
      ElMessage.success('删除成功')
      loadGrades()
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
        await gradeApi.update(formData.id, formData)
        ElMessage.success('更新成功')
      } else {
        await gradeApi.create(formData)
        ElMessage.success('添加成功')
      }
      dialogVisible.value = false
      loadGrades()
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
    studentId: null,
    courseId: null,
    score: 0
  })
  formRef.value?.resetFields()
}

onMounted(() => {
  Promise.all([loadGrades(), loadStudents(), loadCourses()])
})
</script>

<style scoped>
.grade-manage {
  min-height: calc(100vh - 150px);
}
</style>
