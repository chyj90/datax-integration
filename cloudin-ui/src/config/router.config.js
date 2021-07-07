// eslint-disable-next-line
import { UserLayout, BasicLayout, RouteView } from '@/layouts'

export const asyncRouterMap = [
  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: '' },
    redirect: '/task',
    children: [
      {
        path: '/task',
        redirect: '/task/datax-task',
        component: RouteView,
        meta: { title: '任务管理', icon: 'form' },
        children: [
          {
            path: '/task/datax-task/:pageNo([1-9]\\d*)?',
            name: 'DataxList',
            component: () => import('@/views/task/DataxList'),
            meta: { title: 'datax任务模版', keepAlive: true }
          },
          {
            path: '/task/pipeline/:pageNo([1-9]\\d*)?',
            name: 'PipelineTask',
            component: () => import('@/views/task/PipelineList'),
            meta: { title: '任务流水线', keepAlive: true }
          },
          {
            path: '/task/logs/:pageNo([1-9]\\d*)?',
            name: 'TaskLog',
            component: () => import('@/views/task/TaskLog'),
            meta: { title: '任务日志', keepAlive: true }
          }
        ]
      }
    ]
  }
]
/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/views/user/Login')
      }
    ]
  },

  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }

]
