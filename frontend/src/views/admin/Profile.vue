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
    { required: true, message: '请输入昵称', trigger: 'blur' }
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
    // TODO: 调用更新接口
    ElMessage.success('保存成功')
  } catch (error) {
    if (error !== false) {
      console.error('保存失败:', error)
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="admin-profile">
    <div class="page-header">
      <h2>个人中心</h2>
    </div>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <span>个人信息</span>
          </template>

          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="80px"
            style="max-width: 500px"
          >
            <el-form-item label="用户名">
              <el-input :value="userStore.userInfo?.username" disabled />
            </el-form-item>

            <el-form-item label="角色">
              <el-tag :type="userStore.userInfo?.role === 'ADMIN' ? 'danger' : 'primary'">
                {{ userStore.userInfo?.role === 'ADMIN' ? '管理员' : userStore.userInfo?.role === 'AGENT' ? '客服' : '普通用户' }}
              </el-tag>
            </el-form-item>

            <el-form-item label="昵称" prop="nickname">
              <el-input v-model="form.nickname" placeholder="请输入昵称" />
            </el-form-item>

            <el-form-item label="邮箱">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>

            <el-form-item label="手机号">
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

      <el-col :span="8">
        <el-card shadow="never">
          <template #header>
            <span>头像</span>
          </template>
          <div style="text-align: center">
            <el-avatar :size="100" icon="UserFilled" />
            <div style="margin-top: 16px">
              <el-button text type="primary">修改头像</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.admin-profile {
  padding: 0;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #333;
}
</style>
