import Vue from 'vue'
import Router from 'vue-router'
import SchedulePageOwner from '@/components/SchedulePageOwner'
import SchedulePage from '@/components/SchedulePage'
import SchedulePageClient from '@/components/SchedulePageClient'
import SchedulePageInstructor from '@/components/SchedulePageInstructor'
import LoginPage from '@/components/LoginPage'
import RegistrationPage from '@/components/RegistrationPage'
import AccountPageClient from '@/components/AccountPageClient'
import AccountPageInstructor from '@/components/AccountPageInstructor'
import AccountPageOwner from '@/components/AccountPageOwner'
import AccountPageNotLoggedIn from '@/components/AccountPageNotLoggedIn'
import { globalState } from '@/global.js'; // Import globalState if not already imported

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/Login',
      name: 'LoginPage',
      component: LoginPage
    },
    {
      path: '/',
      redirect: '/SchedulePage'
    },
    {
      path: '/SchedulePageOwner',
      name: 'SchedulePageOwner',
      component: SchedulePageOwner
    },
    {
      path: '/SchedulePage',
      name: 'SchedulePage',
      component: SchedulePage
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
      path: '/account',
      meta: { requiresAuth: true },
      component: AccountPageNotLoggedIn, // Default to not logged in page
      beforeEnter: (to, from, next) => {
        const userType = globalState.type;

        // navigates to proper account page based on user type for security
        if (userType === 'Client') {
          next('/client-account');
        } else if (userType === 'Instructor') {
          next('/instructor-account');
        } else if (userType === 'Owner') {
          next('/owner-account');
        } else {
          next('/not-logged-in-account');
        }
      }
    },
    {
      path: '/client-account',
      name: 'AccountPageClient',
      component: AccountPageClient
    },
    {
      path: '/instructor-account',
      name: 'AccountPageInstructor',
      component: AccountPageInstructor
    },
    {
      path: '/owner-account',
      name: 'AccountPageOwner',
      component: AccountPageOwner
    },
    {
      path: '/not-logged-in-account',
      name: 'AccountPageNotLoggedIn',
      component: AccountPageNotLoggedIn
    }
  ]
})
