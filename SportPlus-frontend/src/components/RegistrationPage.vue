<template>
    <b-container fluid class="vh-100">
      <b-row class="h-100 align-items-center">


        <b-col cols="12" md="7" lg="8" class="register-background d-none d-md-block"></b-col>

        <b-col cols="12" md="5" lg="4" class="my-auto">
          <b-card class="mx-auto" style="max-width: 400px;">
            <h1 class="register-title">Create Your Account </h1>
            <p class="register-subtitle">Make your workouts fun and easy !</p>
            <b-form @submit.prevent="onRegisterSubmit"  class="register-form">

              
            <b-button-group class="btn-group">
                <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Instructor'" @click="userType = 'Instructor'">Instructor </b-button>
                <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Owner'" @click="userType = 'Owner'">Owner</b-button>
                <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Client'" @click="userType = 'Client'">Client</b-button>
            </b-button-group>
              
              <b-form-group label="Full Name:" label-for="input-name">
                <b-form-input
                  id="input-name"
                  v-model="registerForm.name"
                  required
                  placeholder="Enter full name"
                ></b-form-input>
              </b-form-group>
  
              <b-form-group label="Email address:" label-for="input-email">
                <b-form-input
                  id="input-email"
                  type="email"
                  v-model="registerForm.email"
                  required
                  placeholder="Enter email"
                ></b-form-input>
              </b-form-group>
  
              <b-form-group label="Password:" label-for="input-password">
                <b-form-input
                  id="input-password"
                  type="password"
                  v-model="registerForm.password"
                  required
                  placeholder="Enter password"
                ></b-form-input>
              </b-form-group>
  
              <b-form-group label="Confirm Password:" label-for="input-password-confirm">
                <b-form-input
                  id="input-password-confirm"
                  type="password"
                  v-model="registerForm.confirmPassword"
                  required
                  placeholder="Confirm password"
                ></b-form-input>
              </b-form-group>
  
              <b-button type="submit" variant="primary" block class="register-button">Sign Up</b-button>
            
            </b-form>
            
            <div class="mt-3">
              <b-link to="/">Already have an account? Log in instead</b-link>
            </div>
          </b-card>
        </b-col>
      </b-row>
    </b-container>
  </template>
  
  <script>
  export default {
    name: 'RegisterPage',
    data() {
      return {
        registerForm: {
          name: '',
          email: '',
          password: '',
          confirmPassword: ''
        },
        userType: 'Client' // Default selection
      };
    },
    methods: {
      onRegisterSubmit() {
        if (this.registerForm.password !== this.registerForm.confirmPassword) {
          // Handle password mismatch
          alert("Passwords do not match.");
          return;
        }
        // TODO: Implement your registration logic here
        console.log('Registration form submitted', this.registerForm);
      }
    }
  };
  </script>
  
  <!-- Add any additional styles if required -->
  <style scoped>

      /* Add your font import here, for example from Google Fonts */
      @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');


      .register-background {
      position: relative; /* Needed for the absolute positioning of the pseudo-element */
      background: url('~@/assets/crossfitjpg.jpg') no-repeat center center;
      background-size: cover;
      height: 100%;
      }

      .register-background::after {
      content: '';
      position: absolute;
      right: 0;
      top: 0;
      bottom: 0;
      width: 50%;
      background: linear-gradient(to right, transparent, white 80%);
      pointer-events: none; 
      }
    
      .register-title {
      text-align: left;
      font-size: 1.5rem; /* Smaller size */
      font-family: 'Roboto', sans-serif; /* Change the font */
      }

      .register-subtitle {
      font-size: 0.95rem;
      font-family: 'Roboto', sans-serif;
      text-align: left;
      color: #6c757d; /* Subdued color for the subtitle */
      margin-top: -0.5rem; /* Adjust spacing as needed */
      }

      .register-form {
      text-align: left;
      font-family: 'Roboto', sans-serif;
      }

    .register-button {
    margin-top: 2rem;
    font-family: 'Roboto', sans-serif;
    }

    .btn-group {
    display: flex;
    justify-content: center;
    margin-bottom: 2rem;
    }


  </style>