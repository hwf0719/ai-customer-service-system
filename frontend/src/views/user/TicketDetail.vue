<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ticketApi } from '../../api/ticket'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const ticket = ref(null)
const messages = ref([])
const messageContent = ref('')
const messageLoading = ref(false)
const messagesContainer = ref(null)

const statusLabel = {
  PENDING: '待处理',
  PROCESSING: '处理中',
  RESOLVED: '已解决',
  CLOSED: '已关闭'
}

const statusType = {
  PENDING: 'warning',
  PROCESSING: 'primary',
  RESOLVED: 'success',
  CLOSED: 'info'
}

const priorityLabel = {
  LOW: '低',
  MEDIUM: '中',
  HIGH: '高',
  URGENT: '紧急'
}

const priorityType = {
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

const senderTypeLabel = {
  USER: '我',
  AGENT: '客服',
  AI: 'AI 助手'
}

const senderTypeTag = {
  USER: 'success',
  AGENT: 'warning',
  AI: 'primary'
}

onMounted(() => {
  fetchTicketDetail()
  fetchMessages()
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

async function fetchMessages() {
  try {
    const id = route.params.id
    const data = await ticketApi.getMessages(id)
    messages.value = data || []
    await nextTick()
    scrollToBottom()
  } catch (error) {
    console.error('获取消息列表失败:', error)
  }
}

async function handleSendMessage() {
  if (!messageContent.value.trim()) {
    ElMessage.warning('请输入消息内容')
    return
  }

  messageLoading.value = true
  try {
    const id = route.params.id
    await ticketApi.sendMessage(id, messageContent.value.trim(), 'USER')
    messageContent.value = ''
    ElMessage.success('发送成功')
    await fetchMessages()
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送失败')
  } finally {
    messageLoading.value = false
  }
}

function scrollToBottom() {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

function handleKeydown(e) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    handleSendMessage()
  }
}

function formatTime(time) {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

function handleBack() {
  router.push('/user/tickets')
}
</script>

<template>
  <div class="user-ticket-detail" v-loading="loading">
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
                <el-tag :type="statusType[ticket.status]" size="large">
                  {{ statusLabel[ticket.status] }}
                </el-tag>
              </div>
            </template>

            <el-descriptions :column="2" border>
              <el-descriptions-item label="工单号">
                #{{ ticket.id }}
              </el-descriptions-item>
              <el-descriptions-item label="优先级">
                <el-tag :type="priorityType[ticket.priority]">
                  {{ priorityLabel[ticket.priority] }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="问题分类">
                {{ categoryLabel[ticket.category] || ticket.category }}
              </el-descriptions-item>
              <el-descriptions-item label="提交时间">
                {{ formatTime(ticket.createTime) }}
              </el-descriptions-item>
              <el-descriptions-item label="标题" :span="2">
                {{ ticket.title }}
              </el-descriptions-item>
              <el-descriptions-item label="问题描述" :span="2">
                <div class="content-text">{{ ticket.description }}</div>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>

          <!-- 对话记录 -->
          <el-card shadow="never" class="messages-card">
            <template #header>
              <div class="card-header">
                <span>对话记录</span>
                <el-button size="small" icon="Refresh" @click="fetchMessages">刷新</el-button>
              </div>
            </template>

            <div
              v-if="messages.length > 0"
              ref="messagesContainer"
              class="messages-list"
            >
              <div
                v-for="msg in messages"
                :key="msg.id"
                class="message-item"
                :class="{
                  'message-mine': msg.senderType === 'USER',
                  'message-other': msg.senderType !== 'USER'
                }"
              >
                <div class="message-header">
                  <el-tag :type="senderTypeTag[msg.senderType]" size="small">
                    {{ senderTypeLabel[msg.senderType] }}
                  </el-tag>
                  <span class="message-time">{{ formatTime(msg.createTime) }}</span>
                </div>
                <div class="message-content">{{ msg.content }}</div>
              </div>
            </div>
            <el-empty v-else description="暂无对话记录" />

            <!-- 发送消息 -->
            <div class="message-input" v-if="ticket.status !== 'CLOSED'">
              <el-input
                v-model="messageContent"
                type="textarea"
                :rows="3"
                placeholder="输入消息内容，按 Enter 发送（Shift+Enter 换行）"
                @keydown="handleKeydown"
                :disabled="messageLoading"
              />
              <el-button
                type="primary"
                :loading="messageLoading"
                @click="handleSendMessage"
                class="send-btn"
              >
                发送
              </el-button>
            </div>
            <div v-else class="closed-tip">
              <el-alert title="该工单已关闭，无法发送消息" type="info" show-icon :closable="false" />
            </div>
          </el-card>
        </el-col>

        <!-- 侧边栏 -->
        <el-col :span="8">
          <!-- 状态说明 -->
          <el-card shadow="never" class="status-card">
            <template #header>
              <span>状态说明</span>
            </template>
            <div class="status-desc">
              <div class="status-item">
                <el-tag type="warning">待处理</el-tag>
                <span>工单已提交，等待处理</span>
              </div>
              <div class="status-item">
                <el-tag type="primary">处理中</el-tag>
                <span>客服正在处理您的问题</span>
              </div>
              <div class="status-item">
                <el-tag type="success">已解决</el-tag>
                <span>问题已解决</span>
              </div>
              <div class="status-item">
                <el-tag type="info">已关闭</el-tag>
                <span>工单已关闭</span>
              </div>
            </div>
          </el-card>

          <!-- 帮助提示 -->
          <el-card shadow="never" class="help-card">
            <template #header>
              <span>需要帮助？</span>
            </template>
            <div class="help-content">
              <p>如果您遇到紧急问题，可以通过以下方式联系我们：</p>
              <p><el-icon><Phone /></el-icon> 客服电话：400-xxx-xxxx</p>
              <p><el-icon><Message /></el-icon> 邮箱：support@example.com</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </template>
  </div>
</template>

<style scoped>
.user-ticket-detail {
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
  max-height: 400px;
  overflow-y: auto;
}

.message-item {
  padding: 12px;
  margin-bottom: 12px;
  border-radius: 8px;
}

.message-mine {
  background: #ecf5ff;
  margin-left: 40px;
}

.message-other {
  background: #f5f7fa;
  margin-right: 40px;
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
  word-break: break-word;
}

.message-input {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #eee;
}

.send-btn {
  margin-top: 8px;
  float: right;
}

.closed-tip {
  margin-top: 16px;
}

.status-card {
  margin-bottom: 20px;
}

.status-desc {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.status-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.status-item span {
  font-size: 13px;
  color: #666;
}

.help-card {
  margin-bottom: 20px;
}

.help-content p {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 8px 0;
  font-size: 13px;
  color: #666;
}
</style>
