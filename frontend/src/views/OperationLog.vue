<template>
  <div class="operation-log">
    <h1 class="page-title">
      <el-icon><Document /></el-icon>
      操作日志
    </h1>
    
    <div class="page-card">
      <div class="search-bar">
        <el-date-picker
          v-model="dateRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          style="width: 350px;"
          @change="handleSearch"
        />
        
        <el-input
          v-model="searchForm.operator"
          placeholder="操作人"
          style="width: 150px;"
          clearable
          @clear="handleSearch"
        />
        
        <el-select
          v-model="searchForm.module"
          placeholder="操作模块"
          style="width: 150px;"
          clearable
          @clear="handleSearch"
        >
          <el-option label="课程管理" value="课程管理" />
          <el-option label="学生管理" value="学生管理" />
          <el-option label="班级管理" value="班级管理" />
          <el-option label="成绩管理" value="成绩管理" />
        </el-select>
        
        <el-button type="primary" :icon="Search" @click="handleSearch">
          搜索
        </el-button>
        
        <el-button :icon="Refresh" @click="handleReset">
          重置
        </el-button>
      </div>
      
      <el-table 
        :data="logList" 
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="module" label="操作模块" width="120">
          <template #default="{ row }">
            <el-tag size="small">{{ row.module }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.type === '新增'" type="success" size="small">新增</el-tag>
            <el-tag v-else-if="row.type === '更新'" type="warning" size="small">更新</el-tag>
            <el-tag v-else-if="row.type === '删除'" type="danger" size="small">删除</el-tag>
            <el-tag v-else size="small">{{ row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="method" label="请求方法" width="100" />
        <el-table-column prop="url" label="请求URL" min-width="180" show-overflow-tooltip />
        <el-table-column prop="ipAddress" label="IP地址" width="130" />
        <el-table-column prop="operationTime" label="操作时间" width="170" />
        <el-table-column prop="duration" label="耗时(ms)" width="100" />
        <el-table-column prop="success" label="状态" width="90">
          <template #default="{ row }">
            <el-tag v-if="row.success" type="success" size="small">成功</el-tag>
            <el-tag v-else type="danger" size="small">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button 
              type="primary" 
              size="small" 
              :icon="View"
              @click="handleView(row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSearch"
          @current-change="handleSearch"
        />
      </div>
    </div>
    
    <el-dialog
      v-model="detailVisible"
      title="操作日志详情"
      width="800px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="ID" :span="2">{{ currentLog.id }}</el-descriptions-item>
        <el-descriptions-item label="操作人">{{ currentLog.operator }}</el-descriptions-item>
        <el-descriptions-item label="操作模块">{{ currentLog.module }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">{{ currentLog.type }}</el-descriptions-item>
        <el-descriptions-item label="请求方法">{{ currentLog.method }}</el-descriptions-item>
        <el-descriptions-item label="请求URL" :span="2">{{ currentLog.url }}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ currentLog.ipAddress }}</el-descriptions-item>
        <el-descriptions-item label="操作时间">{{ currentLog.operationTime }}</el-descriptions-item>
        <el-descriptions-item label="耗时">{{ currentLog.duration }} ms</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="currentLog.success" type="success">成功</el-tag>
          <el-tag v-else type="danger">失败</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <el-input
            v-model="currentLog.params"
            type="textarea"
            :rows="4"
            readonly
          />
        </el-descriptions-item>
        <el-descriptions-item v-if="currentLog.errorMessage" label="错误信息" :span="2">
          <el-input
            v-model="currentLog.errorMessage"
            type="textarea"
            :rows="3"
            readonly
          />
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Document, Search, Refresh, View } from '@element-plus/icons-vue'
import { operationLogApi } from '../api/operationLog'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const logList = ref([])
const total = ref(0)
const currentPage = ref(0)
const pageSize = ref(10)
const dateRange = ref([])
const detailVisible = ref(false)
const currentLog = reactive({})

const searchForm = reactive({
  operator: '',
  module: ''
})

const loadLogs = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      operator: searchForm.operator || undefined,
      module: searchForm.module || undefined
    }
    
    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0].toISOString()
      params.endTime = dateRange.value[1].toISOString()
    }
    
    const res = await operationLogApi.getList(params)
    logList.value = res.data || []
    total.value = res.total || 0
  } catch (error) {
    console.error('Failed to load logs:', error)
    ElMessage.error('加载日志失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 0
  loadLogs()
}

const handleReset = () => {
  dateRange.value = []
  searchForm.operator = ''
  searchForm.module = ''
  currentPage.value = 0
  loadLogs()
}

const handleView = async (row) => {
  try {
    const res = await operationLogApi.getById(row.id)
    Object.assign(currentLog, res.data)
    detailVisible.value = true
  } catch (error) {
    console.error('Failed to load log detail:', error)
    ElMessage.error('加载详情失败')
  }
}

onMounted(() => {
  loadLogs()
})
</script>

<style scoped>
.operation-log {
  min-height: calc(100vh - 150px);
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
