import storage from 'store'
import {
  SIDEBAR_TYPE
} from '@/store/const-value'

const app = {
  state: {
  },
  mutations: {
    [SIDEBAR_TYPE]: (state, type) => {
      state.sideCollapsed = type
      storage.set(SIDEBAR_TYPE, type)
    }
  },
  actions: {
  }
}

export default app
