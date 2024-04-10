<template>
  <div>
      <div id="events">
          <b-form-select v-model="selectedClassType" :options="classTypeOptions" placeholder="Select a class type"></b-form-select>
          <b-form-select v-model="selectedInstructor" :options="instructorOptions" placeholder="Select an instructor"></b-form-select>
          <input type="date" placeholder="Date" v-model="newEventDate" />
          <b-form-select v-model="selectedTime" :options="timeOptions" placeholder="Select a time slot"></b-form-select>
          <button @click="createEvent()" v-bind:disabled="isCreateBtnDisabled">Create Event</button>
          <button class="danger-btn" @click="clearInputs()">Clear</button>
      </div>
      <h2>New Specific Class</h2>
      <table>
          <tbody id="events-tbody">
              <tr>
                  <th>Name</th>
                  <th>Date</th>
                  <th>Limit</th>
              </tr>
              <tr v-for="e in events">
                  <td>{{ e.name }}</td>
                  <td>{{ e.date }}</td>
                  <td>{{ e.registrationLimit }}</td>
              </tr>
          </tbody>
      </table>
  </div>
</template>

<script>
import axios from "axios";
import config from "../../config";

const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort


const client = axios.create({
  // IMPORTANT: baseURL, not baseUrl
  baseURL: backendUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
});

export default {
  name: "CreateNewSpecificClass",
  data() {
      return {
          events: [],
          newEventName: null,
          newEventDate: null,
          newEventStartTime: null,
          newEventEndTime: null,
          newEventRegLimit: null,
          newEventLocation: null,
          selectedClassType: null,
          selectedInstructor: null,
          classTypeOptions: [ { text: 'Select a class type', value: null },{ text: 'Option 1', value: 'option1' },{ text: 'Option 2', value: 'option2' }],
          instructorOptions: [{ text: 'Select an instructor', value: null }],
          selectedTime: null,
          timeOptions: [{ text: 'Select a time slot', value: null }]

      };
  },
  async created() {
      try {
          this.fetchClassTypes();
          this.fetchInstructors();
          this.generateTimeOptions();
      }
      catch (e) {
          // TODO: show the user a warning
          console.log(e);
      }
  },
  methods: {
    
    async fetchClassTypes(){
        try{
            let endpointPath = '';
            endpointPath = `/classType/all`;
            const fullUrl = `http://${config.dev.backendHost}:${config.dev.backendPort}${endpointPath}`;
            const userResponse = await axios.get(fullUrl);

            this.classTypeOptions = userResponse.data;

        }catch(e){
            // TODO: show the user a warning
            console.log(e);
        }
    },

    async fetchInstructors(){
        try{
            let endpointPath = '';
            endpointPath = `/instructors/all`;
            const fullUrl = `http://${config.dev.backendHost}:${config.dev.backendPort}${endpointPath}`;
            const userResponse = await axios.get(fullUrl);

            this.instructorOptions = userResponse.data;

        }catch(e){
            // TODO: show the user a warning
            console.log(e);
        }
    },

    generateTimeOptions() {
      const startTime = 8; // Starting hour (8 am)
      const endTime = 18; // Ending hour (6 pm)

      for (let i = startTime; i < endTime; i ++) {
        const startHour = i.toString().padStart(2, '0'); // Format start hour
        const endHour = (i + 1).toString().padStart(2, '0'); // Format end hour
        const slotLabel = `${startHour}:00 - ${endHour}:00`; // Combine start and end times
        this.timeOptions.push({ text: slotLabel, value: startHour });
      }
    },

    formatTime(time) {
      return `${time.toString().padStart(2, '0')}:00`; // Format time as HH:00
    },

      async createEvent() {
          const newEvent = {
              type: "IN_PERSON",
              name: this.newEventName,
              date: this.newEventDate,
              startTime: this.newEventStartTime,
              endTime: this.newEventEndTime,
              registrationLimit: this.newEventRegLimit,
              location: this.newEventLocation
          };
          try {
              const response = await client.post("/events", newEvent);
              this.events.push(response.data);
              this.clearInputs();
          }
          catch (e) {
              // TODO: show the user a warning
              console.log(e);
          }
      },
      clearInputs() {
          this.newEventName = null;
          this.newEventDate = null;
          this.newEventStartTime = null;
          this.newEventEndTime = null;
  
      }
  },
  computed: {
      isCreateBtnDisabled() {
          return (
              !this.newEventName
              || !this.newEventDate
              || !this.newEventStartTime
              || !this.newEventEndTime
              || !this.newEventLocation
          );
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