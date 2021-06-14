// with polyfills
import 'core-js/stable'
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store/'
import './mock'
import './core/use'
import './permission'
import ProLayout, { PageHeaderWrapper } from '@ant-design-vue/pro-layout'
Vue.config.productionTip = false

Vue.component('pro-layout', ProLayout)
Vue.component('page-header-wrapper', PageHeaderWrapper)
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
