import request from './request'

export const ticketApi = {
  // 创建工单
  create(data) {
    return request.post('/ticket', data)
  },

  // 获取工单列表
  getList(params) {
    return request.get('/ticket/list', { params })
  },

  // 获取工单详情
  getDetail(id) {
    return request.get(`/ticket/${id}`)
  },

  // 更新工单状态
  updateStatus(id, status) {
    return request.put(`/ticket/${id}/status`, { status })
  },

  // 获取工单摘要
  getSummary(id) {
    return request.get(`/ticket/${id}/summary`)
  }
}
