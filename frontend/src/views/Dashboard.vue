<template>
  <div class="dashboard">
    <h1 class="page-title">
      <el-icon><DataAnalysis /></el-icon>
      数据概况
    </h1>
    
    <!-- 统计卡片 -->
    <el-row :gutter="24" v-loading="loading">
      <el-col :xs="24" :sm="12" :lg="8">
        <div class="stat-card" style="border-top: 4px solid #667eea;">
          <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
            <el-icon :size="32"><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">学生总数</div>
            <div class="stat-value">{{ stats.studentCount }}</div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="24" :sm="12" :lg="8">
        <div class="stat-card" style="border-top: 4px solid #48bb78;">
          <div class="stat-icon" style="background: linear-gradient(135deg, #48bb78 0%, #38a169 100%);">
            <el-icon :size="32"><Reading /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">课程总数</div>
            <div class="stat-value">{{ stats.courseCount }}</div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="24" :sm="12" :lg="8">
        <div class="stat-card" style="border-top: 4px solid #ed8936;">
          <div class="stat-icon" style="background: linear-gradient(135deg, #ed8936 0%, #dd6b20 100%);">
            <el-icon :size="32"><Memo /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-label">成绩记录</div>
            <div class="stat-value">{{ stats.gradeCount }}</div>
          </div>
        </div>
      </el-col>
    </el-row>
    
    <!-- 快捷入口 -->
    <div class="page-card">
      <h2 style="margin-bottom: 20px; color: #2d3748;">快捷操作</h2>
      <el-row :gutter="16">
        <el-col :xs="24" :sm="12" :md="6">
          <el-button type="primary" size="large" style="width: 100%;" @click="$router.push('/students')">
            <el-icon><Plus /></el-icon>
            添加学生
          </el-button>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <el-button type="success" size="large" style="width: 100%;" @click="$router.push('/courses')">
            <el-icon><Plus /></el-icon>
            添加课程
          </el-button>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <el-button type="warning" size="large" style="width: 100%;" @click="$router.push('/grades')">
            <el-icon><Edit /></el-icon>
            录入成绩
          </el-button>
        </el-col>
        <el-col :xs="24" :sm="12" :md="6">
          <el-button type="info" size="large" style="width: 100%;" @click="loadStats">
            <el-icon><Refresh /></el-icon>
            刷新数据
          </el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { DataAnalysis, User, Reading, Memo, Plus, Edit, Refresh } from '@element-plus/icons-vue'
import { studentApi } from '../api/student'
import { courseApi } from '../api/course'
import { gradeApi } from '../api/grade'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const stats = ref({
  studentCount: 0,
  courseCount: 0,
  gradeCount: 0
})

const loadStats = async () => {
  loading.value = true
  try {
    const [studentRes, courseRes, gradeRes] = await Promise.all([
      studentApi.getCount(),
      courseApi.getCount(),
      gradeApi.getStatistics()
    ])
    
    stats.value = {
      studentCount: studentRes.data || 0,
      courseCount: courseRes.data || 0,
      gradeCount: gradeRes.data?.totalGrades || 0
    }
    
    ElMessage.success('数据加载成功')
  } catch (error) {
    console.error('Failed to load statistics:', error)
    ElMessage.error('数据加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  min-height: calc(100vh - 150px);
}

.stat-card {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
  margin-bottom: 24px;
}

.stat-icon {
  width: 70px;
  height: 70px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  flex-shrink: 0;
}

.stat-content {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #718096;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
  color: #2d3748;
}

.el-button + .el-button {
  margin-left: 0;
  margin-top: 16px;
}

@media (min-width: 640px) {
  .el-button + .el-button {
    margin-top: 0;
  }
}
</style>
