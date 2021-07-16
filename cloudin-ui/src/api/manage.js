import request from '@/utils/request'

const api = {
  role: '/role',
  service: '/service',
  register: '/user/register',
  list_user: '/user/list',
  delete_user: '/user/delete',
  change_user_status: '/user/changeStatus',
  metric_list: '/monitor//metrics'
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

export function register (parameter) {
  return request({
    url: api.register,
    method: 'post',
    data: parameter
  })
}

export function deleteUser (parameter) {
  return request({
    url: api.delete_user,
    method: 'get',
    params: parameter
  })
}

export function listUser (parameter) {
  return request({
    url: api.list_user,
    method: 'get',
    params: parameter
  })
}

export function changeUserStatus (parameter) {
  return request({
    url: api.change_user_status,
    method: 'get',
    params: parameter
  })
}

export function metricList (parameter) {
  return request({
    url: api.metric_list,
    method: 'get',
    params: parameter
  })
}
