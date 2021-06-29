import storage from 'store'
import { login, getLoginUser } from '@/api/login'
import { ACCESS_TOKEN } from '@/store/const-value'

const user = {
  state: {
    token: '',
    name: '',
    avatar: '',
    roles: [],
    info: {}
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { name }) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_INFO: (state, info) => {
      state.info = info
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(response => {
          const result = response
          storage.set(ACCESS_TOKEN, result[ACCESS_TOKEN], 7 * 24 * 60 * 60 * 1000)
          commit('SET_TOKEN', result[ACCESS_TOKEN])
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    // 登出
    Logout ({ commit, state }) {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      storage.remove(ACCESS_TOKEN)
    },
    // 获取用户信息
    GetInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        getLoginUser().then(response => {
          const username = response
          commit('SET_NAME', { name: username })
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }

  }
}

export default user
