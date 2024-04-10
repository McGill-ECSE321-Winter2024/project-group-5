import Vue from 'vue';

export const globalState = Vue.observable({
  accountId: null,
  type: null,
  accountEmail: null
});