import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/home.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('../views/about.vue')
  },
  {
    path: '/admin/ebook',
    name: 'admin-ebook',
    component: () => import('../views/admin/admin-ebook.vue')
  },
  {
    path: '/admin/category',
    name: 'admin-category',
    component: () => import('../views/admin/admin-category.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
