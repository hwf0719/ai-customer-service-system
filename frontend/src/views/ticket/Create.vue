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
  content: '',
  category: '',
  priority: 'MEDIUM'
})

const rules = {
  title: [
    { required: true, message: '请输入工单标题', trigger: 'blur' },
    { max: 100, message: '标题长度不能超过 100 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入工单内容', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ]
}

const categoryOptions = [
  { value: 'ACCOUNT', label: '账户问题' },
  { value: 'PAYMENT', label: '支付问题' },
  { value: 'PRODUCT', label: '产品问题' },
  { value: 'TECHNICAL', label: '技术问题' },
  { value: 'OTHER', label: '其他' }
]

const priorityOptions = [
  { value: 'LOW', label: '低' },
  { value: 'MEDIUM', label: '中' },
  { value: 'HIGH', label: '高' },
  { value: 'URGENT', label: '紧急' }
]

async function handleSubmit() {
  try {
    await formRef.value.validate()
    loading.value = true
    await ticketApi.create(form)
    ElMessage.success('工单创建成功')
    router.push('/tickets')
  } catch (error) {
    if (error !== false) {
      console.error('创建工单失败:', error)
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
  <div class="create-ticket">
    <div class="page-header">
      <h2>创建工单</h2>
    </div>

    <el-card shadow="never">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        style="max-width: 600px"
      >
        <el-form-item label="标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="请输入工单标题"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="分类" prop="category">
          <el-select
            v-model="form.category"
            placeholder="请选择分类"
            style="width: 100%"
          >
            <el-option
              v-for="item in categoryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="优先级" prop="priority">
          <el-select
            v-model="form.priority"
            placeholder="请选择优先级"
            style="width: 100%"
          >
            <el-option
              v-for="item in priorityOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="6"
            placeholder="请详细描述您的问题..."
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleSubmit">
            提交工单
          </el-button>
          <el-button @click="handleCancel">
            取消
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped>
.create-ticket {
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
