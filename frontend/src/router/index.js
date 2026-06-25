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
  {
    path: '/',
    component: () => import('../components/layout/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/dashboard/Index.vue'),
        meta: { title: '控制台', requiresAuth: true }
      },
      {
        path: 'tickets',
        name: 'Tickets',
        component: () => import('../views/ticket/List.vue'),
        meta: { title: '工单列表', requiresAuth: true }
      },
      {
        path: 'tickets/create',
        name: 'CreateTicket',
        component: () => import('../views/ticket/Create.vue'),
        meta: { title: '创建工单', requiresAuth: true }
      },
      {
        path: 'tickets/:id',
        name: 'TicketDetail',
        component: () => import('../views/ticket/Detail.vue'),
        meta: { title: '工单详情', requiresAuth: true }
      },
      {
        path: 'knowledge',
        name: 'Knowledge',
        component: () => import('../views/knowledge/List.vue'),
        meta: { title: '知识库', requiresAuth: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth !== false && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
