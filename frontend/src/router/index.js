import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/user/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/user/Register.vue'),
    meta: { title: '注册', requiresAuth: false }
  },
  // 管理端路由
  {
    path: '/admin',
    component: () => import('../components/layout/MainLayout.vue'),
    redirect: '/admin/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('../views/dashboard/Index.vue'),
        meta: { title: '控制台', requiresAuth: true }
      },
      {
        path: 'tickets',
        name: 'AdminTickets',
        component: () => import('../views/ticket/List.vue'),
        meta: { title: '工单列表', requiresAuth: true }
      },
      {
        path: 'tickets/create',
        name: 'AdminCreateTicket',
        component: () => import('../views/ticket/Create.vue'),
        meta: { title: '创建工单', requiresAuth: true }
      },
      {
        path: 'tickets/:id',
        name: 'AdminTicketDetail',
        component: () => import('../views/ticket/Detail.vue'),
        meta: { title: '工单详情', requiresAuth: true }
      },
      {
        path: 'knowledge',
        name: 'AdminKnowledge',
        component: () => import('../views/knowledge/List.vue'),
        meta: { title: '知识库', requiresAuth: true }
      },
      {
        path: 'users',
        name: 'AdminUsers',
        component: () => import('../views/admin/UserManage.vue'),
        meta: { title: '用户管理', requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'AdminProfile',
        component: () => import('../views/admin/Profile.vue'),
        meta: { title: '个人中心', requiresAuth: true }
      }
    ]
  },
  // 用户端路由
  {
    path: '/user',
    component: () => import('../components/layout/UserLayout.vue'),
    redirect: '/user/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'UserDashboard',
        component: () => import('../views/user/Dashboard.vue'),
        meta: { title: '我的主页', requiresAuth: true }
      },
      {
        path: 'tickets',
        name: 'UserTickets',
        component: () => import('../views/user/TicketList.vue'),
        meta: { title: '我的工单', requiresAuth: true }
      },
      {
        path: 'tickets/create',
        name: 'UserCreateTicket',
        component: () => import('../views/user/TicketCreate.vue'),
        meta: { title: '提交工单', requiresAuth: true }
      },
      {
        path: 'tickets/:id',
        name: 'UserTicketDetail',
        component: () => import('../views/user/TicketDetail.vue'),
        meta: { title: '工单详情', requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('../views/user/Profile.vue'),
        meta: { title: '个人中心', requiresAuth: true }
      }
    ]
  },
  // 默认重定向
  {
    path: '/',
    redirect: '/user'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const userRole = localStorage.getItem('userRole')

  // 未登录跳转到登录页
  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
    return
  }

  // 已登录访问登录页，根据角色跳转
  if (to.path === '/login' && token) {
    if (userRole === 'ADMIN' || userRole === 'AGENT') {
      next('/admin')
    } else {
      next('/user')
    }
    return
  }

  // 角色权限检查
  if (to.path.startsWith('/admin') && userRole !== 'ADMIN' && userRole !== 'AGENT') {
    next('/user')
    return
  }

  next()
})

export default router
