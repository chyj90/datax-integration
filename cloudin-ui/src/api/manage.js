import request from '@/utils/request'

const api = {
  role: '/role',
  service: '/service'
}

export default api

export function getRoleList (parameter) {
    return request({
      url: api.role,
      method: 'get',
      params: parameter
    })
  }

  export function getServiceList (parameter) {
    return request({
      url: api.service,
      method: 'get',
      params: parameter
    })
  }
