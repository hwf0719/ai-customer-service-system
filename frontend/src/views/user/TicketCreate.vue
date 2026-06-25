<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ticketApi } from '../../api/ticket'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const formRef = ref(null)

const form = reactive({
  title: '',
  description: '',
  category: ''
})

const rules = {
  title: [
    { required: true, message: '请输入工单标题', trigger: 'blur' },
    { min: 5, max: 100, message: '标题长度在 5 到 100 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入问题描述', trigger: 'blur' },
    { min: 10, max: 2000, message: '描述长度在 10 到 2000 个字符', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择问题分类', trigger: 'change' }
  ]
}

const categoryOptions = [
  { value: 'ACCOUNT', label: '账户问题' },
  { value: 'PAYMENT', label: '支付问题' },
  { value: 'PRODUCT', label: '产品问题' },
  { value: 'TECHNICAL', label: '技术问题' },
  { value: 'OTHER', label: '其他' }
]

const categoryIcons = {
  ACCOUNT: 'User',
  PAYMENT: 'Money',
  PRODUCT: 'Goods',
  TECHNICAL: 'Monitor',
  OTHER: 'More'
}

const categoryDescs = {
  ACCOUNT: '账号登录、注册、密码找回等问题',
  PAYMENT: '订单支付、退款、发票等问题',
  PRODUCT: '产品功能、使用方法等问题',
  TECHNICAL: '系统故障、报错、兼容性等问题',
  OTHER: '其他类型的问题'
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
    loading.value = true

    await ticketApi.create({
      title: form.title,
      description: form.description,
      category: form.category
    })

    ElMessage.success('工单提交成功！我们会尽快处理')
    router.push('/user/tickets')
  } catch (error) {
    if (error !== false) {
      console.error('提交工单失败:', error)
      ElMessage.error('提交失败，请重试')
    }
  } finally {
    loading.value = false
  }
}

function handleCancel() {
  router.back()
}
</script>

<template>
  <div class="user-ticket-create">
    <div class="page-header">
      <el-button icon="ArrowLeft" @click="handleCancel">返回</el-button>
      <h2>提交工单</h2>
    </div>

    <el-card shadow="never" class="form-card">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        label-position="top"
      >
        <el-form-item label="问题分类" prop="category">
          <el-radio-group v-model="form.category" class="category-group">
            <el-radio-button
              v-for="item in categoryOptions"
              :key="item.value"
              :value="item.value"
            >
              <el-icon><component :is="categoryIcons[item.value]" /></el-icon>
              <span>{{ item.label }}</span>
            </el-radio-button>
          </el-radio-group>
          <div v-if="form.category" class="category-desc">
            {{ categoryDescs[form.category] }}
          </div>
        </el-form-item>

        <el-form-item label="工单标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="请简要描述您的问题（5-100字）"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="问题描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="6"
            placeholder="请详细描述您遇到的问题，包括：&#10;1. 问题的具体表现&#10;2. 问题出现的步骤&#10;3. 期望的结果&#10;&#10;（10-2000字）"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item>
          <div class="form-actions">
            <el-button @click="handleCancel">取消</el-button>
            <el-button type="primary" :loading="loading" @click="handleSubmit">
              提交工单
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 提示信息 -->
    <el-card shadow="never" class="tips-card">
      <template #header>
        <span>提交提示</span>
      </template>
      <div class="tips-content">
        <p><el-icon><InfoFilled /></el-icon> 请尽量详细描述问题，便于我们快速定位和解决</p>
        <p><el-icon><InfoFilled /></el-icon> 提交后可在"我的工单"中查看处理进度</p>
        <p><el-icon><InfoFilled /></el-icon> 紧急问题请选择"紧急"优先级</p>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.user-ticket-create {
  padding: 0;
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #333;
}

.form-card {
  margin-bottom: 20px;
}

.category-group {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.category-group .el-radio-button {
  margin-right: 0;
}

.category-group .el-radio-button :deep(.el-radio-button__inner) {
  display: flex;
  align-items: center;
  gap: 4px;
}

.category-desc {
  margin-top: 8px;
  font-size: 12px;
  color: #999;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.tips-card {
  margin-bottom: 20px;
}

.tips-content p {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 8px 0;
  font-size: 14px;
  color: #666;
}

.tips-content p .el-icon {
  color: #409eff;
}
</style>
