<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const loading = ref(false)
const formRef = ref(null)

const form = reactive({
  nickname: '',
  email: '',
  phone: ''
})

const rules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

onMounted(() => {
  loadUserInfo()
})

function loadUserInfo() {
  if (userStore.userInfo) {
    form.nickname = userStore.userInfo.nickname || ''
    form.email = userStore.userInfo.email || ''
    form.phone = userStore.userInfo.phone || ''
  }
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
    loading.value = true

    // TODO: 调用更新用户信息的 API
    // await userApi.updateProfile(form)

    ElMessage.success('保存成功')
  } catch (error) {
    if (error !== false) {
      console.error('保存失败:', error)
      ElMessage.error('保存失败')
    }
  } finally {
    loading.value = false
  }
}

function handleLogout() {
  userStore.logout()
}
</script>

<template>
  <div class="user-profile">
    <div class="page-header">
      <h2>个人中心</h2>
    </div>

    <el-row :gutter="20">
      <!-- 个人信息 -->
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <span>个人信息</span>
          </template>

          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="100px"
            label-position="top"
          >
            <el-form-item label="用户名">
              <el-input :value="userStore.userInfo?.username" disabled />
            </el-form-item>

            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="form.nickname" placeholder="请输入昵称" />
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>

            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :loading="loading" @click="handleSubmit">
                保存修改
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 侧边栏 -->
      <el-col :span="8">
        <!-- 头像卡片 -->
        <el-card shadow="never" class="avatar-card">
          <template #header>
            <span>头像</span>
          </template>
          <div class="avatar-content">
            <el-avatar :size="100" icon="UserFilled" />
            <el-button text type="primary" class="avatar-btn">修改头像</el-button>
          </div>
        </el-card>

        <!-- 账号信息 -->
        <el-card shadow="never" class="info-card">
          <template #header>
            <span>账号信息</span>
          </template>
          <div class="info-content">
            <div class="info-item">
              <span class="label">用户ID</span>
              <span class="value">{{ userStore.userInfo?.id }}</span>
            </div>
            <div class="info-item">
              <span class="label">用户名</span>
              <span class="value">{{ userStore.userInfo?.username }}</span>
            </div>
            <div class="info-item">
              <span class="label">角色</span>
              <span class="value">{{ userStore.userInfo?.role === 'ADMIN' ? '管理员' : '普通用户' }}</span>
            </div>
          </div>
        </el-card>

        <!-- 安全设置 -->
        <el-card shadow="never" class="security-card">
          <template #header>
            <span>安全设置</span>
          </template>
          <div class="security-content">
            <div class="security-item">
              <div>
                <div class="security-title">登录密码</div>
                <div class="security-desc">已设置</div>
              </div>
              <el-button text type="primary">修改</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.user-profile {
  padding: 0;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #333;
}

.avatar-card {
  margin-bottom: 20px;
}

.avatar-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16px;
}

.avatar-btn {
  margin-top: 8px;
}

.info-card {
  margin-bottom: 20px;
}

.info-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-item .label {
  color: #999;
  font-size: 14px;
}

.info-item .value {
  color: #333;
  font-size: 14px;
}

.security-card {
  margin-bottom: 20px;
}

.security-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.security-title {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}

.security-desc {
  font-size: 12px;
  color: #999;
}
</style>
