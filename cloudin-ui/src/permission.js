import router from './router'
import store from './store'
import storage from 'store'
import { ACCESS_TOKEN } from '@/store/const-value'
import NProgress from 'nprogress' // progress bar
import '@/components/NProgress/nprogress.less' // progress bar custom style

NProgress.configure({ showSpinner: false }) // NProgress Configuration
const loginRoutePath = '/user/login'
const whiteList = ['login', 'logout']
router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar
  if (!whiteList.includes(to.name) && !store.getters.name) {
    store.dispatch('GetInfo').then(res => {
      processRoute(to, from, next)
    })
  } else {
    processRoute(to, from, next)
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})

function processRoute (to, from, next) {
  if (storage.get(ACCESS_TOKEN)) {
    if (store.getters.addRouters && store.getters.addRouters.length === 0) {
      store.dispatch('GenerateRoutes', { roles: {} }).then(() => {
        // 根据roles权限生成可访问的路由表
        // 动态添加可访问路由表
        router.addRoutes(store.getters.addRouters)
        // 请求带有 redirect 重定向时，登录自动重定向到该地址
        const redirect = decodeURIComponent(from.query.redirect || to.path)
        if (to.path === redirect) {
          // set the replace: true so the navigation will not leave a history record
          next({ ...to, replace: true })
        } else {
          // 跳转到目的路由
          next({ path: redirect })
        }
      })
    } else {
      next()
    }
  } else {
    if (whiteList.includes(to.name)) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next({ path: loginRoutePath, query: { redirect: to.fullPath } })
      NProgress.done()
    }
  }
}
