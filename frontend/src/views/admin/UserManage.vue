<script setup>
import { ref, reactive } from 'vue'
import { adminApi } from '../../api/admin'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const formRef = ref(null)
const showDialog = ref(false)

const form = reactive({
  username: '',
  password: '',
  nickname: '',
  role: 'USER'
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

const roleOptions = [
  { value: 'USER', label: '普通用户', description: '只能提交和查看自己的工单' },
  { value: 'AGENT', label: '客服', description: '可以处理工单、回复用户' },
  { value: 'ADMIN', label: '管理员', description: '拥有所有权限' }
]

function handleOpenDialog() {
  showDialog.value = true
  // 重置表单
  form.username = ''
  form.password = ''
  form.nickname = ''
  form.role = 'USER'
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
    loading.value = true

    await adminApi.createUser({
      username: form.username,
      password: form.password,
      nickname: form.nickname,
      role: form.role
    })

    ElMessage.success('用户创建成功')
    showDialog.value = false
  } catch (error) {
    if (error !== false) {
      console.error('创建用户失败:', error)
      ElMessage.error('创建失败，用户名可能已存在')
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="user-manage">
    <div class="page-header">
      <h2>用户管理</h2>
      <el-button type="primary" icon="Plus" @click="handleOpenDialog">
        创建用户
      </el-button>
    </div>

    <!-- 说明卡片 -->
    <el-card shadow="never" class="info-card">
      <template #header>
        <span>角色说明</span>
      </template>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="普通用户 (USER)">
          可以提交工单、查看自己的工单进度、与客服对话
        </el-descriptions-item>
        <el-descriptions-item label="客服 (AGENT)">
          可以查看所有工单、处理工单、回复用户消息
        </el-descriptions-item>
        <el-descriptions-item label="管理员 (ADMIN)">
          拥有所有权限，可管理用户、工单、知识库
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 创建用户弹窗 -->
    <el-dialog
      v-model="showDialog"
      title="创建用户"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入用户名（3-20位）"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码（6-20位）"
            show-password
          />
        </el-form-item>

        <el-form-item label="昵称" prop="nickname">
          <el-input
            v-model="form.nickname"
            placeholder="请输入昵称"
          />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-radio-group v-model="form.role" class="role-group">
            <el-radio-button
              v-for="item in roleOptions"
              :key="item.value"
              :value="item.value"
            >
              {{ item.label }}
            </el-radio-button>
          </el-radio-group>
          <div class="role-desc">
            {{ roleOptions.find(r => r.value === form.role)?.description }}
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSubmit">
          创建
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.user-manage {
  padding: 0;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #333;
}

.info-card {
  margin-bottom: 20px;
}

.role-group {
  display: flex;
  gap: 8px;
}

.role-desc {
  margin-top: 8px;
  font-size: 12px;
  color: #999;
}
</style>
