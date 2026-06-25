<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ticketApi } from '../../api/ticket'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const ticket = ref(null)
const summary = ref('')
const summaryLoading = ref(false)

const statusTagType = {
  PENDING: 'warning',
  IN_PROGRESS: 'primary',
  RESOLVED: 'success',
  CLOSED: 'info'
}

const statusLabel = {
  PENDING: '待处理',
  IN_PROGRESS: '处理中',
  RESOLVED: '已解决',
  CLOSED: '已关闭'
}

const priorityLabel = {
  LOW: '低',
  MEDIUM: '中',
  HIGH: '高',
  URGENT: '紧急'
}

const priorityTagType = {
  LOW: 'info',
  MEDIUM: '',
  HIGH: 'warning',
  URGENT: 'danger'
}

const categoryLabel = {
  ACCOUNT: '账户问题',
  PAYMENT: '支付问题',
  PRODUCT: '产品问题',
  TECHNICAL: '技术问题',
  OTHER: '其他'
}

onMounted(() => {
  fetchTicketDetail()
})

async function fetchTicketDetail() {
  const id = route.params.id
  loading.value = true
  try {
    ticket.value = await ticketApi.getDetail(id)
  } catch (error) {
    console.error('获取工单详情失败:', error)
    ElMessage.error('获取工单详情失败')
  } finally {
    loading.value = false
  }
}

async function handleUpdateStatus(status) {
  try {
    await ElMessageBox.confirm('确定要更新工单状态吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await ticketApi.updateStatus(ticket.value.id, status)
    ElMessage.success('状态更新成功')
    fetchTicketDetail()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('更新状态失败:', error)
    }
  }
}

async function handleGetSummary() {
  summaryLoading.value = true
  try {
    const data = await ticketApi.getSummary(ticket.value.id)
    summary.value = data.summary || '暂无摘要'
  } catch (error) {
    console.error('获取摘要失败:', error)
    ElMessage.error('获取摘要失败')
  } finally {
    summaryLoading.value = false
  }
}

function formatTime(time) {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

function handleBack() {
  router.back()
}
</script>

<template>
  <div class="ticket-detail" v-loading="loading">
    <div class="page-header">
      <el-button icon="ArrowLeft" @click="handleBack">返回</el-button>
      <h2>工单详情</h2>
    </div>

    <template v-if="ticket">
      <el-row :gutter="20">
        <!-- 工单信息 -->
        <el-col :span="16">
          <el-card shadow="never">
            <template #header>
              <div class="card-header">
                <span>工单信息</span>
                <div class="header-actions">
                  <el-button
                    v-if="ticket.status === 'PENDING'"
                    type="primary"
                    size="small"
                    @click="handleUpdateStatus('IN_PROGRESS')"
                  >
                    开始处理
                  </el-button>
                  <el-button
                    v-if="ticket.status === 'IN_PROGRESS'"
                    type="success"
                    size="small"
                    @click="handleUpdateStatus('RESOLVED')"
                  >
                    标记已解决
                  </el-button>
                  <el-button
                    v-if="ticket.status !== 'CLOSED'"
                    type="danger"
                    size="small"
                    @click="handleUpdateStatus('CLOSED')"
                  >
                    关闭工单
                  </el-button>
                </div>
              </div>
            </template>

            <el-descriptions :column="2" border>
              <el-descriptions-item label="工单ID">
                {{ ticket.id }}
              </el-descriptions-item>
              <el-descriptions-item label="状态">
                <el-tag :type="statusTagType[ticket.status]">
                  {{ statusLabel[ticket.status] }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="优先级">
                <el-tag :type="priorityTagType[ticket.priority]">
                  {{ priorityLabel[ticket.priority] }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="分类">
                {{ categoryLabel[ticket.category] || ticket.category }}
              </el-descriptions-item>
              <el-descriptions-item label="创建时间" :span="2">
                {{ formatTime(ticket.createdAt) }}
              </el-descriptions-item>
              <el-descriptions-item label="标题" :span="2">
                {{ ticket.title }}
              </el-descriptions-item>
              <el-descriptions-item label="内容" :span="2">
                <div class="content-text">{{ ticket.content }}</div>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>

          <!-- 对话记录 -->
          <el-card shadow="never" class="messages-card">
            <template #header>
              <span>对话记录</span>
            </template>

            <div v-if="ticket.messages && ticket.messages.length > 0" class="messages-list">
              <div
                v-for="msg in ticket.messages"
                :key="msg.id"
                class="message-item"
                :class="{ 'message-ai': msg.senderType === 'AI' }"
              >
                <div class="message-header">
                  <el-tag :type="msg.senderType === 'AI' ? 'primary' : 'success'" size="small">
                    {{ msg.senderType === 'AI' ? 'AI 助手' : '用户' }}
                  </el-tag>
                  <span class="message-time">{{ formatTime(msg.createdAt) }}</span>
                </div>
                <div class="message-content">{{ msg.content }}</div>
              </div>
            </div>
            <el-empty v-else description="暂无对话记录" />
          </el-card>
        </el-col>

        <!-- 侧边栏 -->
        <el-col :span="8">
          <!-- AI 摘要 -->
          <el-card shadow="never" class="summary-card">
            <template #header>
              <div class="card-header">
                <span>AI 摘要</span>
                <el-button
                  type="primary"
                  size="small"
                  :loading="summaryLoading"
                  @click="handleGetSummary"
                >
                  生成摘要
                </el-button>
              </div>
            </template>
            <div v-if="summary" class="summary-content">{{ summary }}</div>
            <el-empty v-else description="点击按钮生成 AI 摘要" :image-size="60" />
          </el-card>

          <!-- 工单状态流转 -->
          <el-card shadow="never" class="timeline-card">
            <template #header>
              <span>状态流转</span>
            </template>
            <el-timeline>
              <el-timeline-item
                timestamp="创建工单"
                placement="top"
                type="primary"
              >
                <p>{{ formatTime(ticket.createdAt) }}</p>
              </el-timeline-item>
              <el-timeline-item
                v-if="ticket.status !== 'PENDING'"
                timestamp="开始处理"
                placement="top"
                type="success"
              >
                <p>{{ formatTime(ticket.updatedAt) }}</p>
              </el-timeline-item>
              <el-timeline-item
                v-if="ticket.status === 'RESOLVED' || ticket.status === 'CLOSED'"
                timestamp="已解决"
                placement="top"
                type="success"
              >
                <p>{{ formatTime(ticket.updatedAt) }}</p>
              </el-timeline-item>
            </el-timeline>
          </el-card>
        </el-col>
      </el-row>
    </template>
  </div>
</template>

<style scoped>
.ticket-detail {
  padding: 0;
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

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.content-text {
  white-space: pre-wrap;
  line-height: 1.6;
}

.messages-card {
  margin-top: 20px;
}

.messages-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item {
  padding: 12px;
  background: #f5f7fa;
  border-radius: 8px;
}

.message-item.message-ai {
  background: #ecf5ff;
}

.message-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.message-time {
  font-size: 12px;
  color: #999;
}

.message-content {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
}

.summary-card {
  margin-bottom: 20px;
}

.summary-content {
  font-size: 14px;
  line-height: 1.6;
  color: #333;
  white-space: pre-wrap;
}

.timeline-card {
  margin-bottom: 20px;
}
</style>
