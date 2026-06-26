<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { ticketApi } from '../../api/ticket'

const stats = ref({
  totalTickets: 0,
  pendingTickets: 0,
  resolvedTickets: 0,
  avgResponseTime: '0h'
})

const chartRef = ref(null)

onMounted(async () => {
  await fetchStats()
  initChart()
})

async function fetchStats() {
  try {
    const data = await ticketApi.getStats()
    stats.value = data
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

function initChart() {
  const chart = echarts.init(chartRef.value)

  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    legend: {
      data: ['新建', '处理中', '已解决']
    },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '新建',
        type: 'bar',
        data: [12, 8, 15, 10, 18, 6, 4]
      },
      {
        name: '处理中',
        type: 'bar',
        data: [8, 5, 10, 7, 12, 4, 3]
      },
      {
        name: '已解决',
        type: 'bar',
        data: [15, 12, 20, 16, 22, 10, 8]
      }
    ]
  }

  chart.setOption(option)

  window.addEventListener('resize', () => {
    chart.resize()
  })
}
</script>

<template>
  <div class="dashboard">
    <h2>控制台</h2>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">总工单数</div>
              <div class="stat-value">{{ stats.totalTickets }}</div>
            </div>
            <el-icon class="stat-icon" color="#409eff"><Ticket /></el-icon>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">待处理</div>
              <div class="stat-value warning">{{ stats.pendingTickets }}</div>
            </div>
            <el-icon class="stat-icon" color="#e6a23c"><Clock /></el-icon>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">已解决</div>
              <div class="stat-value success">{{ stats.resolvedTickets }}</div>
            </div>
            <el-icon class="stat-icon" color="#67c23a"><CircleCheck /></el-icon>
          </div>
        </el-card>
      </el-col>

      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">平均响应时间</div>
              <div class="stat-value">{{ stats.avgResponseTime }}</div>
            </div>
            <el-icon class="stat-icon" color="#909399"><Timer /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <span>本周工单趋势</span>
          </template>
          <div ref="chartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.dashboard h2 {
  margin-bottom: 20px;
  color: #333;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
}

.stat-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-value.warning {
  color: #e6a23c;
}

.stat-value.success {
  color: #67c23a;
}

.stat-icon {
  font-size: 48px;
  opacity: 0.8;
}

.chart-row {
  margin-top: 20px;
}

.chart-container {
  height: 400px;
}
</style>
