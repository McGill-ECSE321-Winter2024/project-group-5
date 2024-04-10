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
              <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Instructor'" @click="setUserType('Instructor')">Instructor</b-button>
              <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Owner'" @click="setUserType('Owner')">Owner</b-button>
              <b-button variant="outline-primary" class="user-type-btn" :pressed="userType === 'Client'" @click="setUserType('Client')">Client</b-button>
            </b-button-group>
              
              <b-form-group label="First Name:" label-for="input-first-name">
                <b-form-input
                  id="input-first-name"
                  v-model="registerForm.firstName"
                  required
                  placeholder="Enter your first name"
                ></b-form-input>
              </b-form-group>

              <b-form-group label="Last name:" label-for="input-last-name">
                <b-form-input
                  id="input-last-name"
                  v-model="registerForm.lastName"
                  required
                  placeholder="Enter your last name"
                ></b-form-input>
              </b-form-group>
  
              <b-form-group label="Email address:" label-for="input-email">
                <b-form-input
                  id="input-email"
                  type="email"
                  v-model="registerForm.email"
                  :disabled="emailDisabled"
                  required
                  placeholder="Enter email"
                  @input="validateEmail"
                  :state="emailState"
                  aria-describedby="input-email-live-feedback"
                ></b-form-input>
                <b-form-invalid-feedback id="input-email-live-feedback">
                  {{ emailFeedback }}
                </b-form-invalid-feedback>
              </b-form-group>
  
              <b-form-group label="Password:" label-for="input-password">
                <b-form-input
                  id="input-password"
                  type="password"
                  v-model="registerForm.password"
                  required
                  placeholder="Enter password"
                  @input="validatePassword"
                  :state="passwordState"
                  aria-describedby="input-password-live-feedback"
                ></b-form-input>
                <b-form-invalid-feedback id="input-password-live-feedback">
                  {{ passwordFeedback }}
                </b-form-invalid-feedback>
              </b-form-group>
  
              <b-form-group label="Confirm Password:" label-for="input-password-confirm" :state="confirmPasswordState">
              <b-form-input
                id="input-password-confirm"
                type="password"
                v-model="registerForm.confirmPassword"
                required
                placeholder="Confirm password"
                @input="validateConfirmPassword"
                :state="confirmPasswordState"
                aria-describedby="input-confirm-password-live-feedback"
              ></b-form-input>
              <b-form-invalid-feedback id="input-confirm-password-live-feedback">
                {{ confirmPasswordFeedback }}
              </b-form-invalid-feedback>
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

  import axios from "axios";
  import config from "../../config";
  import { globalState } from "@/global.js";

  // Create URLs
  const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
  const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

  const AXIOS = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
  })


  export default {
    name: 'RegisterPage',
    data() {
      return {
        emailDisabled: false,
        confirmPasswordState: null,
        confirmPasswordFeedback: '',
        registerForm: {
          firstName: '',
          lastName: '',
          email: '',
          password: '',
          confirmPassword: ''
        },
        userType: 'Client' // Default selection
      };
    },
    methods: {
      setUserType(type) {
        this.userType = type;
        if (type === 'Owner') {
            this.registerForm.email = 'owner@sportplus.com';
            this.emailDisabled = true; // Disable the email input
        } else {
            this.registerForm.email = ''; // Clear the email input
            this.emailDisabled = false; // Enable the email input
        }
        },
      // Validate the password 
      validatePassword() {
      const password = this.registerForm.password;
      if (!password) {
        this.passwordState = false;
        this.passwordFeedback = "Password cannot be empty.";
       } else if (password.length < 5) {
        this.passwordState = false;
        this.passwordFeedback = "Password must contain at least five characters.";
       } else if (!password.match(/[A-Z]/)) {
        this.passwordState = false;
        this.passwordFeedback = "Password must contain an uppercase letter.";
       } else {
        this.passwordState = true;
        this.passwordFeedback = "";
       }
     },
     //Make sure the passowrd matches
     validateConfirmPassword() {
      if (this.registerForm.confirmPassword !== this.registerForm.password) {
        this.confirmPasswordState = false;
        this.confirmPasswordFeedback = "Passwords must match.";
      } else {
        this.confirmPasswordState = true;
        this.confirmPasswordFeedback = "";
      }
    },
    // Check if email meets requirements
      validateEmail() {
      const email = this.registerForm.email;
      const allowedCharacters = 'abcdefghijklmnopqrstuvwxyz._-@1234567890';
      const hasSpace = /\s/.test(email);
      const atSymbolCount = (email.match(/@/g) || []).length;
      const startsWithAt = email.startsWith('@');
      const hasDotAt = email.includes('.@') || email.includes('@.');
      const endsWithValidDomain = email.endsWith('.com') || email.endsWith('.ca');
      const hasInvalidChars = !email.split('').every(char => allowedCharacters.includes(char.toLowerCase()));
      const isClientWithSportPlusEmail = this.userType === 'Client' && email.endsWith('@sportplus.com');
      const isInstructorNotWithSportPlusEmail = this.userType === 'Instructor' && !email.endsWith('@sportplus.com');

      if (!email || hasSpace || atSymbolCount !== 1 || startsWithAt || hasDotAt || !endsWithValidDomain || hasInvalidChars || isClientWithSportPlusEmail || isInstructorNotWithSportPlusEmail) {
        this.emailFeedback = 'Please enter a valid email address.';
        if (!email) this.emailFeedback = 'Email cannot be empty.';
        else if (hasSpace) this.emailFeedback = 'Email cannot contain spaces.';
        else if (atSymbolCount !== 1) this.emailFeedback = 'Email can only have 1 "@" symbol.';
        else if (!endsWithValidDomain) this.emailFeedback = 'Email can only end with .com or .ca.';
        else if (startsWithAt) this.emailFeedback = 'Email cannot start with "@".';
        else if (hasDotAt) this.emailFeedback = 'Email cannot contain ".@" or "@.".';
        else if (hasInvalidChars) this.emailFeedback = 'Email contains invalid characters.';
        else if (isClientWithSportPlusEmail) this.emailFeedback = 'Clients cannot have an email ending with "@sportplus.com".';
        else if (isInstructorNotWithSportPlusEmail) this.emailFeedback = 'Instrcutors can only have an email ending with "@sportplus.com".';
        this.emailState = false;
      } else {
        this.emailFeedback = '';
        this.emailState = true;
      }
    },

    // Check if account already created 
    async checkEmailExists(email) {
    try {
      let endpointPath = '';
      if (this.userType === 'Client') {
        endpointPath = `/clients/getByEmail/${email}`;
      } else if (this.userType === 'Instructor') {
        endpointPath = `/instructors/getByEmail/${email}`;
      } else if (this.userType === 'Owner') {
        endpointPath = `/owner/get`;
      }
       else {
        console.log('Invalid user type');
        return false; // Invalid user type
      }

      // Try to get the account with the email
      const fullUrl = `http://${config.dev.backendHost}:${config.dev.backendPort}${endpointPath}`;
      const userResponse = await axios.get(fullUrl);

      return true; // If the request succeeds, the email exists
    } catch (error) {
       return false; // An error occurred, means an account with that email does not exist
    }
  },

      async onRegisterSubmit() {
        // Create a new Date object
        const now = new Date();
        // Format the current time to match java.sql.Time format (HH:mm:ss)
        const currentTime = now.toTimeString().split(' ')[0];

        this.validateEmail();
        this.validatePassword();
        this.validateConfirmPassword();

        const emailExists = await this.checkEmailExists(this.registerForm.email);

        // First check if emaail is preprely written,
        // Password meets requirements
        // Email is not used
        if (!this.emailState || !this.passwordState || !this.confirmPasswordState) {
          alert(this.emailFeedback || this.passwordFeedback || this.confirmPasswordFeedback);
          return;
        }
        if (this.registerForm.password !== this.registerForm.confirmPassword) {
          // Handle password mismatch
          alert("Passwords do not match.");
          return;
        }

        if (emailExists){
          alert("Email already in use") 
          return;
        }

        // Create the request
        const userData = {
          email: this.registerForm.email,
          firstName: this.registerForm.firstName,
          lastName: this.registerForm.lastName,
          password: this.registerForm.password,
        };

        // Create the URL
        const backendBaseUrl = `http://${config.dev.backendHost}:${config.dev.backendPort}`;

        // Determine the specific endpoint based on accountType
        let endpointPath = '';
        switch (this.userType) {
          case 'Client':
            endpointPath = '/clients/create';
            break;
          case 'Instructor':
            endpointPath = '/instructors/create';
            break;
          case 'Owner':
            endpointPath = '/owner/create';
            break;
          default:
            this.errorMessage = 'Invalid account type selected.';
            return;
        }
        // Construct the full URL with the base and endpoint path
        const fullUrlCreate = backendBaseUrl + endpointPath;
        const fullUrlLogin = backendBaseUrl + '/login'

        try {
          //Creates an account with 
          const responseCreate = await axios.post(fullUrlCreate, userData);

          // Creates a login
          const responseLogin = await AXIOS.post(fullUrlLogin, {
            email: this.registerForm.email,
            password: this.registerForm.password,
            type: this.userType.toUpperCase(), // or the type of the user (OWNER, INSTRUCTOR, CLIENT)
            currentTime: currentTime // or the format your backend expects
            }
          );

          // Determine the specific endpoint based on accountType
          let endpointPathAccountId = '';
          if (this.userType === 'Client') {
            endpointPathAccountId = `/clients/getByEmail/${this.registerForm.email}`;
          } else if (this.userType === 'Instructor') {
            endpointPathAccountId = `/instructors/getByEmail/${this.registerForm.email}`;
          } else if (this.userType === 'Owner') {
            endpointPathAccountId = '/owner/get/';
          }
          else {
            console.log('Invalid user type');
            return false; // Invalid user type
          }

          // Get the added user 
          const fullUrl = `http://${config.dev.backendHost}:${config.dev.backendPort}${endpointPathAccountId}`;
          const userResponseAccountID = await AXIOS.get(fullUrl);

          // Retreive its information
          const accountId = userResponseAccountID.data.accountId;
          console.log(accountId);
          
          //Setting up global variables
          globalState.accountId = accountId;
          globalState.type = this.userType;
          globalState.accountEmail = this.registerForm.email;
          console.log(globalState.type)

          // Handle the response, such as redirecting the user to the SchedulePage page  
          this.$router.push('/SchedulePage');
          console.log('Registration form submitted', this.registerForm);
        } catch (error) {
          // Handle errors, such as displaying a message to the user
          this.errorMessage = "Registration failed: " + (error.response.data.message || error.message);
        }
        
      }
    }
  };
  </script>
  

  <style scoped>

      @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');

      .register-background {
      position: relative; 
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
      font-size: 1.5rem; 
      font-family: 'Roboto', sans-serif;
      }

      .register-subtitle {
      font-size: 0.95rem;
      font-family: 'Roboto', sans-serif;
      text-align: left;
      color: #6c757d;
      margin-top: -0.5rem;
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