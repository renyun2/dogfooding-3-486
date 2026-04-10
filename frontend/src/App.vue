<template>
  <el-container class="app-container">
    <el-header v-if="isAuthenticated" class="app-header">
      <div class="header-content">
        <div class="logo">
          <el-icon :size="28"><School /></el-icon>
          <h1>学生管理系统</h1>
        </div>
        <nav class="nav-menu">
          <router-link to="/" class="nav-item">
            <el-icon><Monitor /></el-icon>
            <span>仪表盘</span>
          </router-link>
          <router-link to="/classes" class="nav-item">
            <el-icon><School /></el-icon>
            <span>班级管理</span>
          </router-link>
          <router-link to="/students" class="nav-item">
            <el-icon><User /></el-icon>
            <span>学生管理</span>
          </router-link>
          <router-link to="/teachers" class="nav-item">
            <el-icon><TeacherIcon /></el-icon>
            <span>教师管理</span>
          </router-link>
          <router-link to="/courses" class="nav-item">
            <el-icon><Reading /></el-icon>
            <span>课程管理</span>
          </router-link>
          <router-link to="/grades" class="nav-item">
            <el-icon><TrendCharts /></el-icon>
            <span>成绩管理</span>
          </router-link>
        </nav>
        <div class="header-right">
          <span class="username">
            <el-icon><UserFilled /></el-icon>
            {{ currentUsername }}
          </span>
          <el-tooltip content="修改密码" placement="bottom">
            <el-button class="icon-btn" @click="pwdDialogVisible = true" text circle>
              <el-icon><Key /></el-icon>
            </el-button>
          </el-tooltip>
          <el-button class="logout-btn" @click="handleLogout" text>
            <el-icon><SwitchButton /></el-icon>
            退出
          </el-button>
        </div>
      </div>
    </el-header>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="pwdDialogVisible"
      title="修改密码"
      width="420px"
      :close-on-click-modal="false"
      @closed="resetPwdForm"
    >
      <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="90px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input v-model="pwdForm.oldPassword" type="password" show-password placeholder="请输入原密码" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="pwdForm.newPassword" type="password" show-password placeholder="至少6位" />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="pwdForm.confirmPassword" type="password" show-password placeholder="再次输入新密码" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="pwdDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="pwdLoading" @click="handleChangePassword">确认修改</el-button>
      </template>
    </el-dialog>

    <el-main :class="isAuthenticated ? 'app-main' : 'app-main-full'">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </el-main>
  </el-container>
</template>

<script setup>
import { computed, ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { School, User, Reading, Monitor, TrendCharts, UserFilled, SwitchButton, Key, UserFilled as TeacherIcon } from '@element-plus/icons-vue'
import { logout, changePassword } from './api/auth'

const router = useRouter()

const isAuthenticated = computed(() => !!localStorage.getItem('token'))
const currentUsername = computed(() => localStorage.getItem('username') || 'admin')

// 退出登录
const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await logout()
  } catch {
    // 无论接口成功与否都清除本地状态
  } finally {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    ElMessage.success('已退出登录')
    router.push('/login')
  }
}

// 修改密码
const pwdDialogVisible = ref(false)
const pwdLoading = ref(false)
const pwdFormRef = ref(null)
const pwdForm = reactive({ oldPassword: '', newPassword: '', confirmPassword: '' })

const validateConfirm = (rule, value, callback) => {
  if (value !== pwdForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const pwdRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '新密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    { validator: validateConfirm, trigger: 'blur' }
  ]
}

const resetPwdForm = () => {
  pwdForm.oldPassword = ''
  pwdForm.newPassword = ''
  pwdForm.confirmPassword = ''
  pwdFormRef.value?.clearValidate()
}

const handleChangePassword = async () => {
  if (!pwdFormRef.value) return
  await pwdFormRef.value.validate(async (valid) => {
    if (!valid) return
    pwdLoading.value = true
    try {
      await changePassword(pwdForm.oldPassword, pwdForm.newPassword)
      ElMessage.success('密码修改成功，请重新登录')
      pwdDialogVisible.value = false
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      router.push('/login')
    } catch {
      // 错误由 request.js 统一提示
    } finally {
      pwdLoading.value = false
    }
  })
}
</script>

<style scoped>
.app-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.app-header {
  background: rgba(255, 255, 255, 0.98);
  backdrop-filter: blur(10px);
  box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
  padding: 0;
  height: 70px;
  display: flex;
  align-items: center;
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #667eea;
}

.logo h1 {
  font-size: 24px;
  font-weight: 700;
  margin: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav-menu {
  display: flex;
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  border-radius: 10px;
  color: #4a5568;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
}

.nav-item:hover {
  background: rgba(102, 126, 234, 0.1);
  color: #667eea;
  transform: translateY(-2px);
}

.nav-item.router-link-active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.nav-item.router-link-active:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.username {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #4a5568;
  font-size: 14px;
  font-weight: 500;
}

.icon-btn {
  color: #909399;
  font-size: 16px;
  width: 32px;
  height: 32px;
  transition: all 0.3s ease;
}

.icon-btn:hover {
  color: #667eea;
  background: rgba(102, 126, 234, 0.08);
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #909399;
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  color: #f56c6c;
  background: rgba(245, 108, 108, 0.08);
}

.app-main {
  padding: 32px;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.app-main-full {
  padding: 0;
  width: 100%;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.fade-enter-from {
  opacity: 0;
  transform: translateY(10px);
}

.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}
</style>
