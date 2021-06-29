import axios from 'axios'
import store from '@/store'
import storage from 'store'
import notification from 'ant-design-vue/es/notification'
import { ACCESS_TOKEN } from '@/store/const-value'

// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  baseURL: process.env.VUE_APP_API_BASE_URL,
  timeout: 6000 // 请求超时时间
})

// 异常拦截处理器
const errorHandler = (error) => {
  notification.error({
    message: error.response.data,
    description: '请求出错'
  })
  console.log(error)
  return Promise.reject(error)
}

// request interceptor
request.interceptors.request.use(config => {
  const token = storage.get(ACCESS_TOKEN)
  // 如果 token 存在
  // 让每个请求携带自定义 token 请根据实际情况自行修改
  if (token) {
    config.headers['Access-Token'] = token
  }
  return config
}, errorHandler)

// response interceptor
request.interceptors.response.use((response) => {
  if (response.data.code === 403 || response.data.code === 401) {
    notification.error({
      message: '请求失败',
      description: '请重新登陆'
    })
    store.dispatch('Logout').then(() => {
      setTimeout(() => {
        window.location.reload()
      }, 1500)
    })
  } else {
    if (response.data.code !== 200) {
      return Promise.reject(response.data.message)
    } else {
      return response.data.message
    }
  }
}, errorHandler)

export default request
