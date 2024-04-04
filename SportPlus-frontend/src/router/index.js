import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import SchedulePage from '@/components/SchedulePage'
import CreateNewSpecificClass from '@/components/CreateNewSpecificClass'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    },
    {
      path: '/SchedulePage',
      name: 'SchedulePage',
      component: SchedulePage
    },
    {
      path: '/tmp',//CreateNewSpecificClass
      name: 'CreateNewSpecificClass',
      component: CreateNewSpecificClass
    }
  ]
})
