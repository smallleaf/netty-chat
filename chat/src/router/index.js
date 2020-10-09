import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Chat from '@/components/Chat'
import './'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/chat',
      name: 'chat',
      component: Chat
    }
  ]
})
