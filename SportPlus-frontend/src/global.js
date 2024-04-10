// Import Vue and Axios
import Vue from 'vue';
import axios from 'axios';
import config from '../config';

// Global variables of an account
export const globalState = Vue.observable({
  accountId: localStorage.getItem('accountId') || null,
  type: localStorage.getItem('type') || null,
  accountEmail: localStorage.getItem('accountEmail') || null
});

// Setting up URLs
const frontendUrl = `http://${config.dev.host}:${config.dev.port}`;
const backendUrl = `http://${config.dev.backendHost}:${config.dev.backendPort}`;

const AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
});

// Export AXIOS instance for other components to use
export { AXIOS };
