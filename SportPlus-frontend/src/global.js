import Vue from 'vue';

// Global variables of an account 
export const globalState = Vue.observable({
  accountId: null,
  type: null,
  accountEmail: null
});