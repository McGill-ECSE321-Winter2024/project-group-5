import Vue from 'vue'
import Router from 'vue-router'
import SchedulePage from '@/components/SchedulePage'
import LoginPage from '@/components/LoginPage'
import RegistrationPage from '@/components/RegistrationPage'
import CreateNewSpecificClass from '@/components/CreateNewSpecificClass'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'LoginPage',
      component: LoginPage
    },
    {
      path: '/SchedulePage',
      name: 'SchedulePage',
      component: SchedulePage
    },
    {
      path: '/register',
      name: 'RegistrationPage',
      component: RegistrationPage
    },
    {
      path: '/tmp',//CreateNewSpecificClass
      name: 'CreateNewSpecificClass',
      component: CreateNewSpecificClass
    }
  ]
})
