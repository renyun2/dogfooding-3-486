<template>
  <div class="class-list">
    <h1 class="page-title">
      <el-icon><School /></el-icon> 班级管理
    </h1>

    <div class="page-card">

      <div class="action-bar">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon> 添加班级
        </el-button>
        <el-input
          v-model="searchQuery"
          placeholder="搜索班级名称..."
          style="width: 300px"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <el-table :data="filteredClasses" v-loading="loading" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="班级名称" width="180" />
        <el-table-column prop="description" label="班级描述" show-overflow-tooltip />
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="scope">
            {{ new Date(scope.row.createdAt).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">
              <el-icon><Delete /></el-icon> 删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑班级' : '添加班级'"
      width="500px"
      append-to-body
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="班级名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入班级名称" />
        </el-form-item>
        <el-form-item label="班级描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入班级描述"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm" :loading="submitLoading"> 确定 </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { School, Plus, Search, Edit, Delete } from '@element-plus/icons-vue'
import { getAllClasses, createClass, updateClass, deleteClass } from '../api/clazz'

const loading = ref(false)
const submitLoading = ref(false)
const classes = ref([])
const searchQuery = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  name: '',
  description: ''
})

const rules = {
  name: [
    { required: true, message: '请输入班级名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [{ max: 200, message: '描述不能超过 200 个字符', trigger: 'blur' }]
}

const filteredClasses = computed(() => {
  if (!searchQuery.value) return classes.value
  return classes.value.filter((item) =>
    item.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const fetchClasses = async () => {
  loading.value = true
  try {
    const res = await getAllClasses()
    if (res.code === 200) {
      classes.value = res.data
    }
  } catch (error) {
    console.error('Fetch classes error:', error)
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, name: '', description: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, { ...row })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除班级 "${row.name}" 吗？该操作将同时删除该班级的所有学生及其成绩记录！`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
    .then(async () => {
      try {
        const res = await deleteClass(row.id)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          fetchClasses()
        }
      } catch (error) {
        console.error('Delete class error:', error)
      }
    })
    .catch(() => {})
}

const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        const res = isEdit.value ? await updateClass(form.id, form) : await createClass(form)
        if (res.code === 200 || res.code === 201) {
          ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
          dialogVisible.value = false
          fetchClasses()
        }
      } catch (error) {
        console.error('Submit class error:', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

onMounted(() => {
  fetchClasses()
})
</script>

<style scoped>
.class-list {
  padding: 20px;
}
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style>
