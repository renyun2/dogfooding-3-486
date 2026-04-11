<template>
  <div class="operation-log">
    <h1 class="page-title">
      <el-icon><Document /></el-icon>
      操作日志
    </h1>

    <div class="page-card">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="操作人">
          <el-input v-model="searchForm.operator" placeholder="请输入操作人" clearable />
        </el-form-item>
        <el-form-item label="操作模块">
          <el-select v-model="searchForm.module" placeholder="请选择模块" clearable style="width: 150px;">
            <el-option label="学生管理" value="学生管理" />
            <el-option label="课程管理" value="课程管理" />
            <el-option label="班级管理" value="班级管理" />
            <el-option label="成绩管理" value="成绩管理" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DDTHH:mm:ss"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="page-card">
      <el-table
        :data="logList"
        v-loading="loading"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="operator" label="操作人" width="120" />
        <el-table-column prop="operationModule" label="操作模块" width="120" />
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getOperationTypeType(row.operationType)"
              size="small"
            >
              {{ row.operationType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="requestMethod" label="请求方法" width="90" align="center">
          <template #default="{ row }">
            <el-tag
              :type="getMethodType(row.requestMethod)"
              size="small"
              effect="plain"
            >
              {{ row.requestMethod }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="requestUrl" label="请求URL" min-width="200" show-overflow-tooltip />
        <el-table-column prop="ipAddress" label="IP地址" width="130" />
        <el-table-column prop="executionTime" label="耗时" width="90" align="center">
          <template #default="{ row }">
            <span :class="getExecutionTimeClass(row.executionTime)">
              {{ row.executionTime }}ms
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="success" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.success ? 'success' : 'danger'" size="small">
              {{ row.success ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationTime" label="操作时间" width="170" />
        <el-table-column label="操作" width="100" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleViewDetail(row)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <el-dialog
      v-model="detailDialogVisible"
      title="日志详情"
      width="700px"
      :close-on-click-modal="false"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="操作人" :span="1">{{ currentLog.operator }}</el-descriptions-item>
        <el-descriptions-item label="操作时间" :span="1">{{ currentLog.operationTime }}</el-descriptions-item>
        <el-descriptions-item label="操作模块" :span="1">{{ currentLog.operationModule }}</el-descriptions-item>
        <el-descriptions-item label="操作类型" :span="1">
          <el-tag :type="getOperationTypeType(currentLog.operationType)" size="small">
            {{ currentLog.operationType }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="请求方法" :span="1">
          <el-tag :type="getMethodType(currentLog.requestMethod)" size="small" effect="plain">
            {{ currentLog.requestMethod }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="执行耗时" :span="1">{{ currentLog.executionTime }}ms</el-descriptions-item>
        <el-descriptions-item label="请求URL" :span="2">{{ currentLog.requestUrl }}</el-descriptions-item>
        <el-descriptions-item label="IP地址" :span="1">{{ currentLog.ipAddress }}</el-descriptions-item>
        <el-descriptions-item label="操作状态" :span="1">
          <el-tag :type="currentLog.success ? 'success' : 'danger'">
            {{ currentLog.success ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <pre class="json-preview">{{ formatJson(currentLog.requestParams) }}</pre>
        </el-descriptions-item>
        <el-descriptions-item v-if="!currentLog.success" label="错误信息" :span="2">
          <el-alert :title="currentLog.errorMessage" type="error" :closable="false" />
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Document, Search, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { operationLogApi } from '../api/operationLog'

const loading = ref(false)
const logList = ref([])
const detailDialogVisible = ref(false)
const currentLog = ref({})

const searchForm = reactive({
  operator: '',
  module: '',
  timeRange: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const getOperationTypeType = (type) => {
  const typeMap = {
    '新增': 'success',
    '修改': 'warning',
    '删除': 'danger',
    '查询': 'info'
  }
  return typeMap[type] || 'info'
}

const getMethodType = (method) => {
  const methodMap = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger'
  }
  return methodMap[method] || 'info'
}

const getExecutionTimeClass = (time) => {
  if (time < 100) return 'time-fast'
  if (time < 500) return 'time-normal'
  return 'time-slow'
}

const formatJson = (json) => {
  if (!json) return '无'
  try {
    return JSON.stringify(JSON.parse(json), null, 2)
  } catch {
    return json
  }
}

const loadLogs = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page - 1,
      size: pagination.size,
      operator: searchForm.operator || undefined,
      module: searchForm.module || undefined
    }

    if (searchForm.timeRange && searchForm.timeRange.length === 2) {
      params.startTime = searchForm.timeRange[0]
      params.endTime = searchForm.timeRange[1]
    }

    const res = await operationLogApi.getLogs(params)
    logList.value = res.data
    pagination.total = res.total
  } catch (error) {
    console.error('加载日志失败:', error)
    ElMessage.error('加载日志失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadLogs()
}

const handleReset = () => {
  searchForm.operator = ''
  searchForm.module = ''
  searchForm.timeRange = null
  pagination.page = 1
  loadLogs()
}

const handlePageChange = (page) => {
  pagination.page = page
  loadLogs()
}

const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadLogs()
}

const handleViewDetail = (row) => {
  currentLog.value = { ...row }
  detailDialogVisible.value = true
}

onMounted(() => {
  loadLogs()
})
</script>

<style scoped>
.search-form {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.json-preview {
  background-color: #f5f7fa;
  padding: 12px;
  border-radius: 4px;
  max-height: 300px;
  overflow: auto;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  margin: 0;
  white-space: pre-wrap;
  word-break: break-all;
}

.time-fast {
  color: #67c23a;
}

.time-normal {
  color: #e6a23c;
}

.time-slow {
  color: #f56c6c;
}
</style>
