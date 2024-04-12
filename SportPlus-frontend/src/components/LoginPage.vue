<template>
  <b-container fluid class="vh-100">
    <b-row class="h-100 align-items-center">
      <!-- Empty column to push the form to the right -->

      <b-col cols="12" md="7" lg="8" class="login-background d-none d-md-block"></b-col>

      <!-- Your login form column -->
      <b-col cols="12" md="5" lg="4" class="my-auto">
        <b-card class="mx-auto" style="max-width: 400px;">
          <h1 class="login-title">Welcome to SportPlus !</h1>
          <p class="login-subtitle">Please sign-in to your account and start working out !</p>
          <b-form @submit.prevent="onLoginSubmit" class="login-form">

            <b-button-group class="btn-group">
              <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Instructor'"
                @click="setUserType('Instructor')">Instructor </b-button>
              <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Owner'"
                @click="setUserType('Owner')">Owner</b-button>
              <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Client'"
                @click="setUserType('Client')">Client</b-button>
            </b-button-group>


            <b-form-group id="input-group-1" label="Email address:" label-for="input-1">
              <b-form-input id="input-1" type="email" v-model="loginForm.email" required placeholder="Enter email"
                :disabled="emailFieldDisabled" :state="emailState"></b-form-input>
            </b-form-group>

            <b-form-group id="input-group-2" label="Password:" label-for="input-2">
              <b-form-input id="input-2" type="password" v-model="loginForm.password" required
                placeholder="Enter password" :state="passwordState"></b-form-input>
            </b-form-group>

            <b-button type="submit" variant="primary" block class="login-button">Login</b-button>

          </b-form>

          <div class="mt-3">
            <b-link to="/register">New here? Register an account</b-link>
            <div class="empty-divider"></div>
            <router-link to="/SchedulePage">View the schedule as a visitor</router-link>
          </div>
        </b-card>
      </b-col>
    </b-row>
  </b-container>
</template>

<script>

import axios from "axios";
import config from "../../config";
import { globalState } from '@/global.js';

// Setting up urls
const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

const AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})

export default {
  name: 'LoginPage',
  data() {
    return {
      loginForm: {
        email: '',
        password: ''
      },
      userType: 'Client', // Default selection
      emailFieldDisabled: false,
      emailState: null,
      passwordState: null,
    };
  },
  methods: {
    setUserType(type) {
      this.userType = type;
      // Automatically set the email for 'Owner' and disable the field
      if (type === 'Owner') {
        this.loginForm.email = 'owner@sportplus.com';
        this.emailFieldDisabled = true;
      } else {
        this.loginForm.email = '';
        this.emailFieldDisabled = false; // Enable the field for other user types
      }
    },

    async onLoginSubmit() {

      // Create a new Date object
      const now = new Date();
      // Format the current time to match java.sql.Time format (HH:mm:ss)
      const currentTime = now.toTimeString().split(' ')[0];

      try {

        //Create the login url 
        const backendBaseUrl = `http://${config.dev.backendHost}:${config.dev.backendPort}`;
        const fullUrlLogin = backendBaseUrl + '/login';

        // Create a login
        const response = await AXIOS.post(fullUrlLogin, {
          email: this.loginForm.email,
          password: this.loginForm.password,
          type: this.userType.toUpperCase(), // or the type of the user (OWNER, INSTRUCTOR, CLIENT)
          currentTime: currentTime // or the format your backend expects
        });

        // LOGIN CREATED SUCCESFULLY

        // Set the global account variables
        //Retreive the account
        let endpointPathAccountId = '';
        if (this.userType === 'Client') {
          endpointPathAccountId = `/clients/getByEmail/${this.loginForm.email}`;
        } else if (this.userType === 'Instructor') {
          endpointPathAccountId = `/instructors/getByEmail/${this.loginForm.email}`;
        } else if (this.userType === 'Owner') {
          endpointPathAccountId = `/owner/get`;
        }
        else {
          console.log('Invalid user type');
          return false; // Invalid user type
        }

        // Get info of the account 
        const fullUrl = `http://${config.dev.backendHost}:${config.dev.backendPort}${endpointPathAccountId}`;
        const userResponseAccountID = await AXIOS.get(fullUrl);

        const accountId = userResponseAccountID.data.accountId;
        console.log(accountId);

        globalState.accountId = accountId;
        globalState.type = this.userType;
        globalState.accountEmail = this.loginForm.email;

        localStorage.setItem('accountId', accountId);
        localStorage.setItem('type', this.userType);
        localStorage.setItem('accountEmail', this.loginForm.email);

        console.log(globalState.accountId);
        console.log(globalState.accountEmail);
        console.log(globalState.type)

        // Go to schedule Page if succesfull
        this.emailState = null;
        this.passwordState = null;

        // Define your paths based on the isLoggedIn variable
        if (this.userType === "Owner") {
          this.$router.push('/SchedulePageOwner');
        } else if (this.userType === "Instructor") {
          this.$router.push('/SchedulePageInstructor');
        } else if (this.userType === "Client") {
          this.$router.push('/SchedulePageClient');
        } else {
          this.$router.push('/'); //if no one logged in, go back to loginPage
        }

      } catch (error) {

        // Any errors means the backend returned an error 
        // Meaning we couldn't login with the info provided 
        this.emailState = false;
        this.passwordState = false;
        console.error(error);
        alert('Login failed! Email or password incorrect !');
      }

      console.log('Login form submitted', this.loginForm, this.userType);

    }
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');

.login-background {
  position: relative;
  background: url('~@/assets/workoutjpg.jpg') no-repeat center center;
  background-size: cover;
  height: 100%;
}


.login-background::after {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  width: 50%;
  background: linear-gradient(to right, transparent, white 80%);
  pointer-events: none;
}

.login-title {
  text-align: left;
  font-size: 1.5rem;
  font-family: 'Roboto', sans-serif;
}

.login-subtitle {
  font-size: 0.95rem;
  font-family: 'Roboto', sans-serif;
  text-align: left;
  color: #6c757d;
  margin-top: -0.5rem;
}

.login-form {
  text-align: left;
  font-family: 'Roboto', sans-serif;
}

.login-button {
  margin-top: 2rem;
  font-family: 'Roboto', sans-serif;
}

.btn-group {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
}

.empty-divider {
  height: 9px;
}
</style>