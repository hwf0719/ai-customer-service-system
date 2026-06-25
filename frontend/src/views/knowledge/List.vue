<script setup>
import { ref, reactive, onMounted } from 'vue'
import { knowledgeApi } from '../../api/knowledge'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const knowledgeList = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('添加知识')
const formRef = ref(null)

const queryParams = reactive({
  page: 1,
  pageSize: 10,
  keyword: ''
})

const form = reactive({
  question: '',
  answer: '',
  category: ''
})

const rules = {
  question: [
    { required: true, message: '请输入问题', trigger: 'blur' }
  ],
  answer: [
    { required: true, message: '请输入答案', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ]
}

const categoryOptions = [
  { value: 'ACCOUNT', label: '账户问题' },
  { value: 'PAYMENT', label: '支付问题' },
  { value: 'PRODUCT', label: '产品问题' },
  { value: 'TECHNICAL', label: '技术问题' },
  { value: 'OTHER', label: '其他' }
]

onMounted(() => {
  fetchKnowledgeList()
})

async function fetchKnowledgeList() {
  loading.value = true
  try {
    const data = await knowledgeApi.getList(queryParams)
    knowledgeList.value = data.records || []
    total.value = data.total || 0
  } catch (error) {
    console.error('获取知识库列表失败:', error)
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  queryParams.page = 1
  fetchKnowledgeList()
}

function handleReset() {
  queryParams.keyword = ''
  queryParams.page = 1
  fetchKnowledgeList()
}

function handleAdd() {
  dialogTitle.value = '添加知识'
  form.question = ''
  form.answer = ''
  form.category = ''
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
    await knowledgeApi.add(form)
    ElMessage.success('添加成功')
    dialogVisible.value = false
    fetchKnowledgeList()
  } catch (error) {
    if (error !== false) {
      console.error('添加知识失败:', error)
    }
  }
}

async function handleDelete(id) {
  try {
    await ElMessageBox.confirm('确定要删除这条知识吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await knowledgeApi.remove(id)
    ElMessage.success('删除成功')
    fetchKnowledgeList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除知识失败:', error)
    }
  }
}

function handlePageChange(page) {
  queryParams.page = page
  fetchKnowledgeList()
}

function handleSizeChange(size) {
  queryParams.pageSize = size
  queryParams.page = 1
  fetchKnowledgeList()
}

function formatTime(time) {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}
</script>

<template>
  <div class="knowledge-list">
    <div class="page-header">
      <h2>知识库管理</h2>
      <el-button type="primary" icon="Plus" @click="handleAdd">
        添加知识
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="关键词">
          <el-input
            v-model="queryParams.keyword"
            placeholder="搜索问题/答案"
            clearable
            style="width: 300px"
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

    <!-- 知识列表 -->
    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="loading"
        :data="knowledgeList"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />

        <el-table-column prop="question" label="问题" min-width="200" show-overflow-tooltip />

        <el-table-column prop="answer" label="答案" min-width="300" show-overflow-tooltip />

        <el-table-column prop="category" label="分类" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.category }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.createdAt) }}
          </template>
        </el-table-column>

        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button
              size="small"
              type="danger"
              icon="Delete"
              @click="handleDelete(row.id)"
            >
              删除
            </el-button>
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

    <!-- 添加对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
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

        <el-form-item label="问题" prop="question">
          <el-input
            v-model="form.question"
            placeholder="请输入问题"
          />
        </el-form-item>

        <el-form-item label="答案" prop="answer">
          <el-input
            v-model="form.answer"
            type="textarea"
            :rows="4"
            placeholder="请输入答案"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.knowledge-list {
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
