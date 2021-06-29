import request from '@/utils/request'

const userApi = {
  Login: '/login',
  login_user_info: '/user/info'
}

/**
 * login func
 * parameter: {
 *     username: '',
 *     password: '',
 *     remember_me: true,
 *     captcha: '12345'
 * }
 * @param parameter
 * @returns {*}
 */
export function login (parameter) {
  return request({
    url: userApi.Login,
    method: 'post',
    data: parameter
  })
}
export function getLoginUser () {
  return request({
    url: userApi.login_user_info,
    method: 'get'
  })
}
