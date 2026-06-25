import request from './request'

export const userApi = {
  // 登录
  login(data) {
    return request.post('/user/login', data)
  },

  // 注册
  register(data) {
    return request.post('/user/register', data)
  },

  // 获取当前用户信息
  getUserInfo() {
    return request.get('/user/info')
  }
}
