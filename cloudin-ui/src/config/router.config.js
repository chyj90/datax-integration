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
            meta: { title: '任务流水线(Alpha)', keepAlive: true }
          },
          {
            path: '/task/logs/:pageNo([1-9]\\d*)?',
            name: 'TaskLog',
            component: () => import('@/views/task/TaskLog'),
            meta: { title: '任务日志', keepAlive: true }
          }
        ]
      },
      {
        path: '/meta',
        redirect: '/meta/datasource',
        component: RouteView,
        meta: { title: '元数据管理', icon: 'form' },
        children: [
          {
            path: '/meta/datasource',
            name: 'DSList',
            component: () => import('@/views/meta/DataSource'),
            meta: { title: '数据源管理', keepAlive: true }
          },
          {
            path: '/meta/resolver/:pageNo([1-9]\\d*)?',
            name: 'RSList',
            component: () => import('@/views/meta/Resolver'),
            meta: { title: '占位符管理', keepAlive: true }
          }
        ]
      },
      {
        path: '/sys',
        redirect: '/sys/users',
        component: RouteView,
        meta: { title: '系统管理', icon: 'form' },
        children: [
          {
            path: '/sys/users/:pageNo([1-9]\\d*)?',
            name: 'UserList',
            component: () => import('@/views/user/UserList'),
            meta: { title: '用户列表', keepAlive: true }
          },
          {
            path: '/sys/monitor',
            name: 'Monitor',
            component: () => import('@/views/system/Monitor'),
            meta: { title: '监控列表', keepAlive: true }
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
