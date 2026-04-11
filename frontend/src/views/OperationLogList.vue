<template>
  <div class="operation-log-list">
    <h1 class="page-title">
      <el-icon><Document /></el-icon>
      操作日志
    </h1>
    
    <div class="page-card">
      <div class="filter-bar">
        <el-input
          v-model="filters.operator"
          placeholder="操作人"
          clearable
          style="width: 150px;"
        />
        
        <el-select
          v-model="filters.module"
          placeholder="操作模块"
          clearable
          style="width: 150px;"
        >
          <el-option
            v-for="item in modules"
            :key="item"
            :label="item"
            :value="item"
          />
        </el-select>
        
        <el-select
          v-model="filters.isSuccess"
          placeholder="执行状态"
          clearable
          style="width: 120px;"
        >
          <el-option label="成功" :value="true" />
          <el-option label="失败" :value="false" />
        </el-select>
        
        <el-date-picker
          v-model="filters.timeRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="YYYY-MM-DD HH:mm:ss"
          style="width: 360px;"
        />
        
        <el-button type="primary" :icon="Search" @click="handleSearch">
          搜索
        </el-button>
        
        <el-button :icon="Refresh" @click="handleReset">
          重置
        </el-button>
      </div>
      
      <el-table 
        :data="logs" 
        v-loading="loading"
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="operator" label="操作人" width="120" />
        <el-table-column prop="module" label="操作模块" width="120" />
        <el-table-column prop="operationType" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getOperationTypeTag(row.operationType)" size="small">
              {{ row.operationType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="requestMethod" label="请求方法" width="90">
          <template #default="{ row }">
            <el-tag :type="getMethodTag(row.requestMethod)" size="small">
              {{ row.requestMethod }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="requestUrl" label="请求URL" min-width="180" show-overflow-tooltip />
        <el-table-column prop="ipAddress" label="IP地址" width="130" />
        <el-table-column prop="operationTime" label="操作时间" width="170" />
        <el-table-column prop="duration" label="耗时(ms)" width="100" />
        <el-table-column prop="isSuccess" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.isSuccess ? 'success' : 'danger'" size="small">
              {{ row.isSuccess ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button 
              type="primary" 
              size="small" 
              :icon="View"
              @click="handleViewDetail(row)"
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
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>
    
    <el-dialog
      v-model="detailVisible"
      title="操作日志详情"
      width="700px"
    >
      <el-descriptions :column="2" border v-if="currentLog">
        <el-descriptions-item label="日志ID">{{ currentLog.id }}</el-descriptions-item>
        <el-descriptions-item label="操作人">{{ currentLog.operator }}</el-descriptions-item>
        <el-descriptions-item label="操作模块">{{ currentLog.module }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">{{ currentLog.operationType }}</el-descriptions-item>
        <el-descriptions-item label="请求方法">
          <el-tag :type="getMethodTag(currentLog.requestMethod)" size="small">
            {{ currentLog.requestMethod }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="执行状态">
          <el-tag :type="currentLog.isSuccess ? 'success' : 'danger'" size="small">
            {{ currentLog.isSuccess ? '成功' : '失败' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="请求URL" :span="2">{{ currentLog.requestUrl }}</el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ currentLog.ipAddress }}</el-descriptions-item>
        <el-descriptions-item label="耗时">{{ currentLog.duration }} ms</el-descriptions-item>
        <el-descriptions-item label="操作时间" :span="2">{{ currentLog.operationTime }}</el-descriptions-item>
        <el-descriptions-item label="请求参数" :span="2">
          <div class="json-viewer">
            <pre>{{ formatJson(currentLog.requestParams) }}</pre>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="错误信息" :span="2" v-if="!currentLog.isSuccess && currentLog.errorMsg">
          <div class="error-msg">{{ currentLog.errorMsg }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Document, Search, Refresh, View } from '@element-plus/icons-vue'
import { operationLogApi } from '../api/operationLog'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const logs = ref([])
const modules = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const detailVisible = ref(false)
const currentLog = ref(null)

const filters = reactive({
  operator: '',
  module: '',
  isSuccess: null,
  timeRange: null
})

const loadLogs = async () => {
  loading.value = true
  try {
    const res = await operationLogApi.getAll(currentPage.value - 1, pageSize.value)
    logs.value = res.data.content || []
    total.value = res.data.totalElements || 0
  } catch (error) {
    console.error('Failed to load logs:', error)
    ElMessage.error('加载日志失败')
  } finally {
    loading.value = false
  }
}

const loadModules = async () => {
  try {
    const res = await operationLogApi.getModules()
    modules.value = res.data || []
  } catch (error) {
    console.error('Failed to load modules:', error)
  }
}

const handleSearch = async () => {
  loading.value = true
  try {
    const params = {
      operator: filters.operator || undefined,
      module: filters.module || undefined,
      isSuccess: filters.isSuccess,
      startTime: filters.timeRange ? filters.timeRange[0] : undefined,
      endTime: filters.timeRange ? filters.timeRange[1] : undefined,
      page: currentPage.value - 1,
      size: pageSize.value
    }
    const res = await operationLogApi.search(params)
    logs.value = res.data.content || []
    total.value = res.data.totalElements || 0
  } catch (error) {
    console.error('Search failed:', error)
    ElMessage.error('搜索失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  filters.operator = ''
  filters.module = ''
  filters.isSuccess = null
  filters.timeRange = null
  currentPage.value = 1
  loadLogs()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  handleSearch()
}

const handlePageChange = (page) => {
  currentPage.value = page
  handleSearch()
}

const handleViewDetail = (row) => {
  currentLog.value = row
  detailVisible.value = true
}

const getMethodTag = (method) => {
  const map = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger'
  }
  return map[method] || 'info'
}

const getOperationTypeTag = (type) => {
  const map = {
    '查询': 'info',
    '新增': 'success',
    '更新': 'warning',
    '删除': 'danger',
    '登录': 'primary',
    '退出': 'info',
    '修改密码': 'warning'
  }
  return map[type] || 'info'
}

const formatJson = (str) => {
  if (!str) return '无'
  try {
    return JSON.stringify(JSON.parse(str), null, 2)
  } catch {
    return str
  }
}

onMounted(() => {
  loadLogs()
  loadModules()
})
</script>

<style scoped>
.operation-log-list {
  min-height: calc(100vh - 150px);
}

.filter-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.json-viewer {
  max-height: 200px;
  overflow: auto;
  background: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
}

.json-viewer pre {
  margin: 0;
  font-size: 12px;
  white-space: pre-wrap;
  word-break: break-all;
}

.error-msg {
  color: #f56c6c;
  background: #fef0f0;
  padding: 10px;
  border-radius: 4px;
}
</style>
