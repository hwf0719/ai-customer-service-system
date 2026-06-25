import request from './request'

export const knowledgeApi = {
  // 添加知识
  add(data) {
    return request.post('/knowledge', data)
  },

  // 删除知识
  remove(id) {
    return request.delete(`/knowledge/${id}`)
  },

  // 搜索知识
  search(params) {
    return request.get('/knowledge/search', { params })
  },

  // 获取知识列表
  getList(params) {
    return request.get('/knowledge/list', { params })
  }
}
