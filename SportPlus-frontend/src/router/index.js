import Vue from 'vue'
import Router from 'vue-router'
import SchedulePageOwner from '@/components/SchedulePageOwner'
import SchedulePageClient from '@/components/SchedulePageClient'
import SchedulePageInstructor from '@/components/SchedulePageInstructor'
import LoginPage from '@/components/LoginPage'
import RegistrationPage from '@/components/RegistrationPage'
import CreateNewSpecificClass from '@/components/CreateNewSpecificClass'
import AccountPage from '@/components/AccountPage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'LoginPage',
      component: LoginPage
    },
    {
      path: '/SchedulePageOwner',
      name: 'SchedulePageOwner',
      component: SchedulePageOwner
    },
    {
      path: '/SchedulePageClient',
      name: 'SchedulePageClient',
      component: SchedulePageClient
    },
    {
      path: '/SchedulePageInstructor',
      name: 'SchedulePageInstructor',
      component: SchedulePageInstructor
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
    },
    {
      path: '/AccountPage',
      name: 'AccountPage',
      component: AccountPage
    }
  ]
})
