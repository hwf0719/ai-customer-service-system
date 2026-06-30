<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import { ticketApi } from '../../api/ticket'

const router = useRouter()
const userStore = useUserStore()
const stats = ref({
  total: 0,
  pending: 0,
  processing: 0,
  resolved: 0
})
const recentTickets = ref([])
const loading = ref(false)

onMounted(() => {
  fetchDashboardData()
})

async function fetchDashboardData() {
  loading.value = true
  try {
    const data = await ticketApi.getList({ page: 1, pageSize: 100 })
    const tickets = data.records || []

    stats.value.total = tickets.length
    stats.value.pending = tickets.filter(t => t.status === 'PENDING').length
    stats.value.processing = tickets.filter(t => t.status === 'PROCESSING').length
    stats.value.resolved = tickets.filter(t => t.status === 'RESOLVED').length

    recentTickets.value = tickets.slice(0, 5)
  } catch (error) {
    console.error('获取数据失败:', error)
  } finally {
    loading.value = false
  }
}

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

function formatTime(time) {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

function handleViewTicket(id) {
  router.push(`/agent/tickets/${id}`)
}
</script>

<template>
  <div class="agent-dashboard" v-loading="loading">
    <!-- 欢迎信息 -->
    <el-card shadow="never" class="welcome-card">
      <div class="welcome-content">
        <div>
          <h2>欢迎回来，{{ userStore.userInfo?.nickname || userStore.userInfo?.username || '客服' }}</h2>
          <p>您有 {{ stats.processing }} 个工单正在处理中</p>
        </div>
      </div>
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon total">
              <el-icon><Tickets /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.total }}</div>
              <div class="stat-label">我的工单</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon pending">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.pending }}</div>
              <div class="stat-label">待处理</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon processing">
              <el-icon><Loading /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.processing }}</div>
              <div class="stat-label">处理中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon resolved">
              <el-icon><CircleCheck /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.resolved }}</div>
              <div class="stat-label">已解决</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近工单 -->
    <el-card shadow="never" class="recent-card">
      <template #header>
        <div class="card-header">
          <span>最近工单</span>
          <el-button text type="primary" @click="router.push('/agent/tickets')">查看全部</el-button>
        </div>
      </template>

      <el-table :data="recentTickets" stripe>
        <el-table-column prop="id" label="工单号" width="80" />
        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="{ row }">
            <el-link type="primary" @click="handleViewTicket(row.id)">{{ row.title }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType[row.status]">{{ statusLabel[row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>
      </el-table>

      <el-empty v-if="recentTickets.length === 0" description="暂无分配给您的工单" />
    </el-card>
  </div>
</template>

<style scoped>
.agent-dashboard {
  padding: 0;
}

.welcome-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #1e3a5f 0%, #2d5a8e 100%);
}

.welcome-card :deep(.el-card__body) {
  padding: 30px;
}

.welcome-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.welcome-content h2 {
  color: #fff;
  margin: 0 0 8px 0;
  font-size: 24px;
}

.welcome-content p {
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.stat-icon.total {
  background: #ecf5ff;
  color: #409eff;
}

.stat-icon.pending {
  background: #fdf6ec;
  color: #e6a23c;
}

.stat-icon.processing {
  background: #ecf5ff;
  color: #409eff;
}

.stat-icon.resolved {
  background: #f0f9eb;
  color: #67c23a;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-top: 4px;
}

.recent-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
