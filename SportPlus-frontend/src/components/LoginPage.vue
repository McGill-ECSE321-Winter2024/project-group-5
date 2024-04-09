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
                <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Instructor'" @click="userType = 'Instructor'">Instructor </b-button>
                <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Owner'" @click="userType = 'Owner'">Owner</b-button>
                <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Client'" @click="userType = 'Client'">Client</b-button>
            </b-button-group>


              <b-form-group id="input-group-1" label="Email address:" label-for="input-1">
                <b-form-input
                  id="input-1"
                  type="email"
                  v-model="loginForm.email"
                  required
                  placeholder="Enter email"
                ></b-form-input>
              </b-form-group>
  
              <b-form-group id="input-group-2" label="Password:" label-for="input-2">
                <b-form-input
                  id="input-2"
                  type="password"
                  v-model="loginForm.password"
                  required
                  placeholder="Enter password"
                ></b-form-input>
              </b-form-group>

              
                <b-button type="submit" variant="primary" block class="login-button">Login</b-button>
            
            </b-form>
            
            <div class="mt-3">
              <b-link to="/register">New here? Register an account</b-link>
            </div>
          </b-card>
        </b-col>
      </b-row>
    </b-container>
</template>

  <script>
  
  import axios from "axios";
  import config from "../../config";
  import { globalState } from "@/global.js";

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
        userType: 'Client' // Default selection
      };
    },
    methods: {

      async onLoginSubmit() {

         // Create a new Date object
        const now = new Date();
        // Format the current time to match java.sql.Time format (HH:mm:ss)
        const currentTime = now.toTimeString().split(' ')[0];


        try {

          let userExists = false;
          // Determine the correct endpoint based on userType
          let endpointPath = '';
          if (this.userType === 'Client') {
            endpointPath = `/clients/getByEmail/${this.loginForm.email}`;
          } else if (this.userType === 'Inctructor') {
            // Update this path according to your actual instructor endpoint
            endpointPath = `/instructors/getByEmail/${this.loginForm.email}`;
          }

          const fullUrlGet = `http://${config.dev.backendHost}:${config.dev.backendPort}${endpointPath}`;

          // Attempt to fetch the user by email
          const userResponse = await AXIOS.get(fullUrlGet);

          console.log(userResponse)
          // Check if the user exists (based on how your backend responds)
          userExists = userResponse && userResponse.data;

          if (!userExists) {
            alert('No account found with this email.');
            return;
          }

        // If user exists but the password is wrong, throw an error or alert the user
        if (userResponse.data.password !== this.loginForm.password) {
          alert('Password is incorrect');
          return;
        }

        const backendBaseUrl = `http://${config.dev.backendHost}:${config.dev.backendPort}`;
        const fullUrlLogin = backendBaseUrl + '/login';
        console.log(fullUrlLogin);
        console.log(this.loginForm.email);
        console.log(this.loginForm.password);
        console.log(this.userType);

        // Create a login
        const response = await AXIOS.post(fullUrlLogin, {
          email: this.loginForm.email,
          password: this.loginForm.password,
          type: this.userType.toUpperCase(), // or the type of the user (OWNER, INSTRUCTOR, CLIENT)
          currentTime: currentTime // or the format your backend expects
        });
        

        // Set the global account ID 
        let endpointPathAccountId = '';
        if (this.userType === 'Client') {
          endpointPathAccountId = `/clients/getByEmail/${this.loginForm.email}`;
        } else if (this.userType === 'Instructor') {
          endpointPathAccountId = `/instructors/getByEmail/${this.loginForm.email}`;
        } else {
          console.log('Invalid user type');
          return false; // Invalid user type
        }

        const fullUrl = `http://${config.dev.backendHost}:${config.dev.backendPort}${endpointPathAccountId}`;
        const userResponseAccountID = await AXIOS.get(fullUrl);

        const accountId = userResponseAccountID.data.accountId;
        console.log(accountId);
        globalState.accountId = accountId;

        // Go to schedule Page
        this.$router.push('/SchedulePage'); 
      } catch (error) {
        console.error(error);
        alert('Login failed! No Account Found with this email !');
      }

        // TODO: Implement your login logic here
        console.log('Login form submitted', this.loginForm, this.userType);
      }
    }
  };
  </script>
  
  <style scoped>
    /* Add your font import here, for example from Google Fonts */
    @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');


    .login-background {
    position: relative; /* Needed for the absolute positioning of the pseudo-element */
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
    font-size: 1.5rem; /* Smaller size */
    font-family: 'Roboto', sans-serif; /* Change the font */
    }

    .login-subtitle {
    font-size: 0.95rem;
    font-family: 'Roboto', sans-serif;
    text-align: left;
    color: #6c757d; /* Subdued color for the subtitle */
    margin-top: -0.5rem; /* Adjust spacing as needed */
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

    /* Continue with the rest of your styles... */
  </style>

  