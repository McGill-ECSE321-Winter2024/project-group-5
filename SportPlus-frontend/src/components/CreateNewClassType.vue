<template>
  <div class="container-wrapper">
    <!-- Flex Container for side-by-side layout -->
    <div class="flex-container">

      <!-- Create New Class Type Block -->
      <div class="form-block">
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
          <button type="submit">Create Class Type</button>
        </form>
      </div>

      <!-- Modify Existing Class Type Block -->
      <div class="form-block">
        <h2>Modify Class Type</h2>
        <form @submit.prevent="modifyClassType">
          <div>
            <label for="className">Select Class Type:</label>
            <select id="className" v-model="className" required>
              <option disabled value="">Please select one</option>
              <option v-for="type in classTypes" :value="type.name">{{ type.name }}</option>

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

      </div>
    </div>
  </div>
</template>



<script>
import axios from "axios";
import config from "../../config";
import { globalState } from "@/global.js";
// Setting up urls
const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort

const AXIOS = axios.create({
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
})
// Create the URL

// Determine the specific endpoint based on accountType
let endpointPath = '';
export default {
  name: "CreateNewClassType",
  data() {
    return {
      classTypes: [],
      description: null,
      imageURL: null,
      typeID: null,
      className: null,
      newDescription: null,
      newName: null
    };
  },
  mounted() {
    this.fetchAllClassTypes();

    // Test data: Adding sample class types
    const testClassType1 = {
      id: 1,
      name: 'Yoga Basics',
      description: 'An introductory class for yoga enthusiasts'
    };
    const testClassType2 = {
      id: 2,
      name: 'Advanced Pilates',
      description: 'A challenging class for experienced Pilates students'
    };

    // Sample initial content
    this.classTypes.push(testClassType1, testClassType2);
  },

  methods: {
    async fetchAllClassTypes() {
      AXIOS.get('/classType/all')
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
        console.log(globalState.type);
        const classtype = { name: this.className, description: this.description, approved: false, approver: null };
        const path = 'classType/create/';
        const response = await AXIOS.post(path, classtype);
        this.classTypes.push(response.data); // Add the newly created class type to the list
        const path1 = 'classType/get/' + this.className;
        const response1 = await AXIOS.get(path1);
        if (globalState.type == "Owner") { // if the owner iscreating approve
          console.log(globalState.type);
          console.log(response1.data.typeId);
          await AXIOS.post('classType/approve/' + response1.data.typeId);
        }
        this.resetForm(); // Reset form
        alert('Class type created successfully!');
        this.fetchAllClassTypes();
      } catch (error) {
        console.error('There was an error creating the class type:', error);
        alert('Failed to create class type.');
      }
    },
    async modifyClassType() {
      try {
        const path1 = 'classType/get/' + this.className;
        console.log(this.className);
        const response1 = await AXIOS.get(path1);
        const id = response1.data.typeId;
        console.log(id);
        // Check if there's a new name to update and send the request
        if (this.newName && id) {
          console.log("Name is", this.newName)
          const path = 'classType/updateName/' + id;
          await AXIOS.put(path, this.newName, {
            headers: {
              'Content-Type': 'text/plain'
            }
          });

        }

        // Check if there's a new description to update and send the request
        if (this.newDescription) {
          console.log("Description is", this.newDescription)
          const path = 'classType/updateDescription/' + id;
          await AXIOS.put(path, this.newDescription, {
            headers: {
              'Content-Type': 'text/plain'
            }
          });
        }
        console.log("test");

        this.fetchAllClassTypes();    // update display

        // Reset local component state
        this.resetForm();
        alert('Class type modified successfully!');

      } catch (error) {
        console.error('There was an error modifying the class type:', error);
        alert('Failed to modify class type.');
      }
    },
    resetForm() {
      this.className = null;
      this.description = null;
      this.typeID = null;
      this.newDescription = null;
      this.newName = null;

      // Clear other fields as necessary
    }
  }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');

/* Existing styles... */

.flex-container {
  display: flex;
  flex-direction: row;
  /* Arrange children in a row */
  justify-content: space-around;
  /* Space out the children evenly */
  flex-wrap: wrap;
  /* Allow items to wrap as needed */
  margin: 20px 0;
  /* Vertical spacing around the flex container */
}

.form-block {
  flex: 1;
  /* Flex children grow */
  min-width: 300px;
  /* Minimum width before wrapping */
  max-width: 600px;
  /* Maximum width for each form block */
  margin: 10px;
  /* Spacing between blocks */
  box-sizing: border-box;
  /* Include padding and border in the element's total width and height */
}

/* Adjustments for input and textarea widths may be necessary if you find the layout too cramped */
input[type="text"],
textarea {
  width: calc(100% - 16px);
  /* Adjust width to account for padding */
}

.container-wrapper {
  margin: 20px auto;
  /* Consistent centering with a bit of margin */
  max-width: 1600px;
}

/* Heading Styles */
h2 {
  text-align: left;
  font-size: 1.5rem;
  font-family: 'Roboto', sans-serif;
  color: #2c3e50;
  /* Dark blue color for consistency */
  margin-bottom: 20px;
  /* Space below the heading */
}

/* Form Styling */
form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  /* Soft shadow for depth */
  font-family: 'Roboto', sans-serif;
}

/* Input and Textarea Styles */
input[type="text"],
textarea {
  width: 100%;
  /* Full width */
  padding: 8px;
  margin: 10px 0;
  /* Spacing */
  box-sizing: border-box;
  /* Ensure padding doesn't increase size */
  border: 1px solid #ccc;
  /* Light grey border */
  border-radius: 4px;
  /* Slightly rounded corners */
}

/* Button Styling */
button {
  background-color: #2c3e50;
  /* Dark blue, consistent with the theme */
  color: white;
  padding: 10px 15px;
  margin-top: 10px;
  /* Space above the button */
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
}

button:disabled {
  background-color: #cccccc;
  /* Grey out when disabled */
  cursor: not-allowed;
}

/* Spacing between form sections */
.form-section {
  margin-bottom: 40px;
  /* Space between form sections */
}

/* Consistent styling for labels */
label {
  font-weight: bold;
  margin-bottom: 5px;
  display: block;
}

/* Additional stylings could be added here based on further requirements */
</style>
