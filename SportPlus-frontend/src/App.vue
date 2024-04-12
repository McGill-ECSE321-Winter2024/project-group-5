<template>
  <div id="app">
    <!-- Navbar -->
    <b-navbar toggleable="lg" type="dark" variant="info" v-if="!isAuthRoute">
      <b-navbar-brand href="#">SportPlus</b-navbar-brand>
      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item :to="schedulePath">Schedule</b-nav-item>
          <b-nav-item to="/account">Account Page</b-nav-item>
          <!-- More links can be added here -->
        </b-navbar-nav>
        <!-- Right aligned items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-item @click="logout">Logout</b-nav-item>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>

    <!-- Page Content -->
    <router-view></router-view>
  </div>
</template>

<script>

import axios from "axios";
import config from "../config";
import { globalState } from '@/global.js';

const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

const schedulePath = null;

const AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'app',
  computed: {
    // Computed property to determine the schedule path based on the isLoggedIn variable
    schedulePath() {
      // Define your paths based on the isLoggedIn variable
      if (globalState.type === "Owner") {
        return '/SchedulePageOwner';
      } else if (globalState.type === "Instructor") {
        return '/SchedulePageInstructor';
      } else if (globalState.type === "Client") {
        return '/SchedulePageClient'
      } else {
        return '/SchedulePage'; //if no one logged in, go back to loginPage
      }
    },
    // Does not display the nav bar in register and login poge
    isAuthRoute() {
      return this.$route.path === '/Login' || this.$route.path === '/register' || this.$route.path === '/SchedulePage';
    },
    userType() {
      return globalState.type;
    },
    userEmail() {
      return globalState.accountEmail;
    },
    userAccountId() {
      return globalState.accountId;
    }
  },
  methods: {
    async logout() {
      try {

        // Get the current login Id
        const endpointPath = `/login/getByAccount/${this.userEmail}/${this.userType.toUpperCase()}`;

        const fullUrlloginId = `http://${config.dev.backendHost}:${config.dev.backendPort}${endpointPath}`;
        const login = await AXIOS.get(fullUrlloginId);

        // Logout from the session
        const endPointPathLogout = `/logout/${login.data.loginId}`;
        const fullUrlLogout = `http://${config.dev.backendHost}:${config.dev.backendPort}${endPointPathLogout}`;

        await AXIOS.delete(fullUrlLogout);

        // Go Back to login Page
        this.$router.push('/');

        globalState.accountEmail = null;
        globalState.accountEmail = null;
        globalState.type = null;

      } catch (error) {
        globalState.accountEmail = null;
        globalState.accountEmail = null;
        globalState.type = null;

        console.log("There was an error")
        console.log(error)
      }
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
