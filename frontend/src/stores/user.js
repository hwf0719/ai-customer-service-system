import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userApi } from '../api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)

  // 登录
  async function login(loginData) {
    const data = await userApi.login(loginData)
    // 后端直接返回 token 字符串，不是对象
    token.value = data
    localStorage.setItem('token', data)
    return data
  }

  // 注册
  async function register(registerData) {
    return await userApi.register(registerData)
  }

  // 获取用户信息
  async function fetchUserInfo() {
    const data = await userApi.getUserInfo()
    userInfo.value = data
    return data
  }

  // 退出登录
  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  return {
    token,
    userInfo,
    login,
    register,
    fetchUserInfo,
    logout
  }
})
