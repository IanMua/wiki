import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import HomeView from '../views/home.vue'
import store from "@/store";
import {Tool} from "@/util/tool";

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
        path: '/doc',
        name: 'doc',
        component: () => import('../views/doc.vue')
    },
    {
        path: '/admin/ebook',
        name: 'admin-ebook',
        component: () => import('../views/admin/admin-ebook.vue'),
        meta: {
            loginRequire: true
        }
    },
    {
        path: '/admin/category',
        name: 'admin-category',
        component: () => import('../views/admin/admin-category.vue'),
        meta: {
            loginRequire: true
        }
    },
    {
        path: '/admin/doc',
        name: 'admin-doc',
        component: () => import('../views/admin/admin-doc.vue'),
        meta: {
            loginRequire: true
        }
    },
    {
        path: '/admin/user',
        name: 'admin-user',
        component: () => import('../views/admin/admin-user.vue'),
        meta: {
            loginRequire: true
        }
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

//路由登录拦截
router.beforeEach((to, from, next) => {
    if (to.matched.some(function (item) {
        return item.meta.loginRequire;
    })) {
        const loginUser = store.state.user;
        if (Tool.isEmpty(loginUser)) {
            next("/");
        } else {
            next();
        }
    } else {
        next();
    }
})

export default router
