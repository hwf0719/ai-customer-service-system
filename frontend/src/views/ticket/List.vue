<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ticketApi } from '../../api/ticket'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const tickets = ref([])
const total = ref(0)

const queryParams = reactive({
  page: 1,
  pageSize: 10,
  status: '',
  keyword: ''
})

const statusOptions = [
  { value: '', label: '全部' },
  { value: 'PENDING', label: '待处理' },
  { value: 'PROCESSING', label: '处理中' },
  { value: 'RESOLVED', label: '已解决' },
  { value: 'CLOSED', label: '已关闭' }
]

const statusTagType = {
  PENDING: 'warning',
  PROCESSING: 'primary',
  RESOLVED: 'success',
  CLOSED: 'info'
}

const statusLabel = {
  PENDING: '待处理',
  PROCESSING: '处理中',
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
  TECHNICAL: '技术问题',
  BILLING: '账单问题',
  GENERAL: '一般咨询',
  OTHER: '其他'
}

const categoryType = {
  TECHNICAL: 'danger',
  BILLING: 'warning',
  GENERAL: 'info',
  OTHER: ''
}

const sentimentLabel = {
  POSITIVE: '😊',
  NEUTRAL: '😐',
  NEGATIVE: '😠'
}

const sentimentType = {
  POSITIVE: 'success',
  NEUTRAL: 'info',
  NEGATIVE: 'danger'
}

onMounted(() => {
  fetchTickets()
})

async function fetchTickets() {
  loading.value = true
  try {
    const data = await ticketApi.getList(queryParams)
    tickets.value = data.records || []
    total.value = data.total || 0
  } catch (error) {
    console.error('获取工单列表失败:', error)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryParams.page = 1
  fetchTickets()
}

function handleReset() {
  queryParams.status = ''
  queryParams.keyword = ''
  queryParams.page = 1
  fetchTickets()
}

function handlePageChange(page) {
  queryParams.page = page
  fetchTickets()
}

function handleSizeChange(size) {
  queryParams.pageSize = size
  queryParams.page = 1
  fetchTickets()
}

function handleCreate() {
  router.push('/admin/tickets/create')
}

function handleView(id) {
  router.push(`/admin/tickets/${id}`)
}

async function handleUpdateStatus(id, status) {
  try {
    await ElMessageBox.confirm('确定要更新工单状态吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await ticketApi.updateStatus(id, status)
    ElMessage.success('状态更新成功')
    fetchTickets()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('更新状态失败:', error)
    }
  }
}

function formatTime(time) {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}
</script>

<template>
  <div class="ticket-list">
    <div class="page-header">
      <h2>工单管理</h2>
      <el-button type="primary" icon="Plus" @click="handleCreate">
        创建工单
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="状态">
          <el-select
            v-model="queryParams.status"
            placeholder="请选择状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="关键词">
          <el-input
            v-model="queryParams.keyword"
            placeholder="搜索标题/内容"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="Refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 工单列表 -->
    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="tickets"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />

        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row.id)">
              {{ row.title }}
            </el-link>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusTagType[row.status]">
              {{ statusLabel[row.status] }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="{ row }">
            <el-tag :type="priorityTagType[row.priority]">
              {{ priorityLabel[row.priority] }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="category" label="分类" width="120">
          <template #default="{ row }">
            <el-tag :type="row.category ? categoryType[row.category] || 'info' : 'info'" size="small">
              {{ row.category ? categoryLabel[row.category] || row.category : '未分类' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="sentiment" label="情绪" width="80">
          <template #default="{ row }">
            <el-tag :type="row.sentiment ? sentimentType[row.sentiment] || 'info' : 'info'" size="small">
              {{ row.sentiment ? sentimentLabel[row.sentiment] || '?' : '?' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createTime) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="handleView(row.id)">
              查看
            </el-button>
            <el-dropdown
              v-if="row.status !== 'CLOSED'"
              @command="(cmd) => handleUpdateStatus(row.id, cmd)"
            >
              <el-button size="small" type="primary">
                更新状态
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-if="row.status === 'PENDING'"
                    command="PROCESSING"
                  >
                    开始处理
                  </el-dropdown-item>
                  <el-dropdown-item
                    v-if="row.status === 'PROCESSING'"
                    command="RESOLVED"
                  >
                    标记已解决
                  </el-dropdown-item>
                  <el-dropdown-item command="CLOSED">
                    关闭工单
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        class="pagination"
        :current-page="queryParams.page"
        :page-size="queryParams.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </el-card>
  </div>
</template>

<style scoped>
.ticket-list {
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

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
