import request from './request'

export const adminApi = {
  // 管理员创建用户
  createUser(data) {
    return request.post('/user/admin/create', data)
  }
}
