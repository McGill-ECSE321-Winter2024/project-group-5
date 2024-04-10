<template>
    <div>
      <!-- Create New Class Type Form -->
      <h2>Create New Class Type</h2>
      <form @submit.prevent="createClassType">
        <div>
          <label for="className">Class Name:</label>
          <input id="className" v-model="className" required placeholder="Enter class name">
        </div>
        <div>
          <label for="description">Description:</label>
          <textarea id="description" v-model="description" required placeholder="Enter description"></textarea>
        </div>
        <button type="submit" :disabled="isCreateBtnDisabled">Create Class Type</button>
      </form>
  
      <!-- Modify Existing Class Type -->
      <h2>Modify Class Type</h2>
      <form @submit.prevent="modifyClassType">
        <div>
          <label for="typeID">Select Class Type:</label>
          <select id="typeID" v-model="typeID" required>
            <option disabled value="">Please select one</option>
            <option v-for="type in classTypes" :value="type.id">{{ type.name }}</option>
          </select>
        </div>
        <div>
          <label for="newName">New Name:</label>
          <input id="newName" v-model="newName" placeholder="Enter new class name">
        </div>
        <div>
          <label for="newDescription">New Description:</label>
          <textarea id="newDescription" v-model="newDescription" placeholder="Enter new description"></textarea>
        </div>
        <button type="submit">Modify Class Type</button>
      </form>
  
      <!-- List of Class Types -->
      <h2>Class Types</h2>
      <ul>
        <li v-for="type in classTypes" :key="type.id">
          {{ type.name }} - {{ type.description }}
          <button @click="typeID = type.id; deleteClassType()">Delete</button>
        </li>
      </ul>
    </div>
  </template>
  
  
  <script>
  import axios from "axios";
  import config from "../../config";
  import { globalState } from "@/global.js";
  const client = axios.create({
    // IMPORTANT: baseURL, not baseUrl
    baseURL: config.dev.backendBaseUrl
  });
  
  export default {
    name: "CreateNewClassType",
    data() {
        return {
            classTypes: [],
            description: null,
            imageURL: null,
            typeID: null,
            className:null,
            newDescription:null,
            newName:null
        };
    },
    methods: {
    async fetchAllClassTypes(){
        CLIENT.get('/classType/all')
                .then(response => {
                    this.classTypes = response.data.classTypes;
                })
                .catch(error => {
                    console.error('Error fetching class types:', error);
                });
    },
    // Method to create a new class type
    async createClassType() {
      try {
        const classtype={ name: this.className, description: this.description ,approved : null,approver:null};
        const response = await axios.post('/create', this.classType);
        this.classTypes.push(response.data); // Add the newly created class type to the list
        const response1 = await axios.get('/get', this.typeID);
        if(globalState.user=="Owner"){ // if the owner iscreating approve
            await axios.post('/approve',response1.data.className);
        }
        this.classType = { name: '', description: '' }; // Reset form
        alert('Class type created successfully!');
      } catch (error) {
        console.error('There was an error creating the class type:', error);
        alert('Failed to create class type.');
      }
    },
    async modifyClassType() {
  try {
    // Check if there's a new name to update and send the request
    if (this.newName && this.typeID) {
      await axios.put(`/updateName/${this.typeID}`, this.newName, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
    }

    // Check if there's a new description to update and send the request
    if (this.newDescription && this.typeID) {
      await axios.put(`/updateDescription/${this.typeID}`, this.newDescription, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
    }

    // Optionally, fetch updated class types list here if the component displays it
    // this.fetchClassTypes();

    // Reset local component state
    this.resetForm();
    alert('Class type modified successfully!');
  } catch (error) {
    console.error('There was an error modifying the class type:', error);
    alert('Failed to modify class type.');
  }
},
    // Method to fetch all class types
    async fetchClassTypes() {
      try {
        const response = await axios.get('/all');
        this.classTypes = response.data.classTypes; // Assuming the response has a classTypes field
      } catch (error) {
        console.error('There was an error fetching the class types:', error);
      }
    },
    async deleteClassType (){
        try {
        await axios.delete('/delete',this.className);
      } catch (error) {
        console.error('There was an error deleting the class type:', error);
      }
    },
    resetForm() {
  this.className = null;
  this.description = null;
  this.typeID = null;
  this.newDescription=null;
  this.newName=null;

  // Clear other fields as necessary
},

    computed: {
        isCreateBtnDisabled() {
            return (
                !this.description
                || !this.name
            );
        }
    }
}
};
  </script>
  
  <style>
  #events {
    display: flex;
    flex-direction: column;
    align-items: stretch;
  }
  
  h2 {
    padding-top: 1em;
    text-decoration: underline;
  }
  
  td,
  th {
    padding: 0.5em;
    border: 1px solid black;
  }
  
  .danger-btn {
    border: 1px solid red;
    color: red;
  }
  </style>