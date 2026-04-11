import { createRouter, createWebHistory } from 'vue-router'

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/Login.vue'),
        meta: { requiresAuth: false }
    },
    {
        path: '/',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/classes',
        name: 'ClassList',
        component: () => import('../views/ClassList.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/students',
        name: 'StudentList',
        component: () => import('../views/StudentList.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/courses',
        name: 'CourseList',
        component: () => import('../views/CourseList.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/grades',
        name: 'GradeManage',
        component: () => import('../views/GradeManage.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/operation-logs',
        name: 'OperationLogList',
        component: () => import('../views/OperationLogList.vue'),
        meta: { requiresAuth: true }
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

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
