<template>
    <div>
        <div id="newSpecificClasses">
            <div class="radio-container">
                <input type="radio" id="regular" value="regular" v-model="classMode">
                <label for="regular">Regular Class</label>
                <input type="radio" id="recurring" value="recurring" v-model="classMode">
                <label for="recurring">Recurring Class</label>
            </div>
            <!-- Input fields for regular class -->
            <div v-if="classMode === 'regular'">
                <label for="newSpecificClassDate">Date:</label>
                <input type="date" id="newSpecificClassDate" placeholder="Date" v-model="newSpecificClassDate">
            </div>
            <!-- Input fields for recurring class -->
            <div v-else-if="classMode === 'recurring'">
                <label for="startDate">Start Date:</label>
                <input type="date" id="startDate" placeholder="Start Date" v-model="startDate">
                <label for="endDate">End Date:</label>
                <input type="date" id="endDate" placeholder="End Date" v-model="endDate">
            </div>
            <b-form-select v-model="selectedClassType" :options="classTypeOptions" placeholder="Select a class type"></b-form-select>
            <b-form-select v-model="selectedInstructor" :options="instructorOptions" placeholder="Select an instructor"></b-form-select>
            <b-form-select v-model="selectedTime" :options="timeOptions" placeholder="Select a time slot"></b-form-select>
            <button @click="createSpecificClass()" v-bind:disabled="isCreateBtnDisabled">Create Event</button>
            <button class="danger-btn" @click="clearInputs()">Clear</button>
        </div>
        <h2>New Specific Class</h2>
        <table>
            <tbody id="newSpecificClasses-tbody">
                <tr>
                    <th>Name</th>
                    <th>Date</th>
                </tr>
                <tr v-for="s in newSpecificClasses">
                    <td>{{ s.name }}</td>
                    <td>{{ s.date }}</td>
                </tr>
            </tbody>
        </table>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  import config from "../../config";
  import { globalState } from "@/global.js";
  
  const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port
  const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
  
  
  const CLIENT = axios.create({
    // IMPORTANT: baseURL, not baseUrl
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
  });

  //instead of push in newspecifc classes, get all specific classes like fetch 
  //check if overlapping specific classes
  
  export default {
    name: "CreateNewSpecificClass",
    data() {
        return {
            newSpecificClasses: [],
            classMode : 'regular',
            newSpecificClassDate: null,
            startDate: null,
            endDate: null,
            newSpecificClassStartTime: null,
            newSpecificClassEndTime: null,
            selectedClassType: null,
            selectedInstructor: null,
            selectedTime: null,
            classTypeOptions: [ { text: 'Select a class type', value: null }],
            instructorOptions: [{ text: 'Select an instructor', value: null }],
            timeOptions: [{ text: 'Select a time slot', value: null }]
            
        };
    },
   mounted() {
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
           // Make an HTTP GET request to fetch classTypes
           CLIENT.get('/classType/all')
                      .then(response => {
                          const resp = response.data.classTypes;
                          console.log("responseClassTypes", resp);
                          const approvedClassTypes = resp.filter(type => type.approved);
                          console.log("approvedTypes", approvedClassTypes);
                          const classTypesData = approvedClassTypes.map(classType =>({
                              text: `${classType.name}`,
                              value: classType.typeId,
                          }));
                          
                      // Assign the retrieved classTypes to types array
                      classTypesData.forEach(classType => {
                          this.classTypeOptions.push(classType);
                      });
  
                      })
                      .catch(error => {
                      console.error('Error fetching classTypes:', error);
                      });
      },
  
      async fetchInstructors(){
          // Make an HTTP GET request to fetch instructors
          CLIENT.get('/instructors/all')
                      .then(response => {
                          const resp = response.data.instructors;
                          console.log("responseInstructors", resp);
                          
                          const instructorsData = response.data.instructors.map(instructor => ({
                          text: `${instructor.lastName}, ${instructor.firstName}`, // Instructor name as the display text
                          value: instructor.accountId // Instructor ID as the value
                          }));
                      // Assign the retrieved instructors to the instructors array
                      instructorsData.forEach(instructor => {
                          this.instructorOptions.push(instructor);
                      });
  
                      })
                      .catch(error => {
                      console.error('Error fetching instructors:', error);
                      });
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
  
      getClassTypeName(selectedValue) {
          const selectedOption = this.classTypeOptions.find(option => option.value === selectedValue);
          return selectedOption ? selectedOption.text : null;
      },
  
      getStartTime(selectedValue) {
          const selectedOption = this.timeOptions.find(option => option.value === selectedValue);
          if (selectedOption) {
              const timeString = selectedOption.text.split(':')[0]; // Extract the hour part
              return parseInt(timeString); // Parse the hour as an integer
          }
          return null;
      },
      clearInputs() {
            this.selectedTime = null;
            this.newSpecificClassDate = null;
            this.selectedClassType = null;
            this.selectedInstructor = null;
            this.startDate = null;
            this.endDate = null;
    
        },
  
        async createSpecificClass() {
            //Format the time for java.sql.Time to read
          const startTime = this.getStartTime(this.selectedTime); 
          const endTime = startTime + 1;
          let javaStartTime = 0;
              let javaEndTime = 0;
  
              if(startTime < 10){
                  javaStartTime = '0'+startTime+':00:00';
              }else{
                  javaStartTime = startTime+':00:00';
              }
  
              if(endTime < 10){
                  javaEndTime = '0'+endTime+':00:00';
              }else{
                  javaEndTime = endTime+':00:00';
              }
              console.log("start time java : ", javaStartTime)
              console.log("end time java : ", javaEndTime)
          if(this.classMode == 'regular'){
            const date = new Date(this.newSpecificClassDate);
              const year = date.getFullYear();
              const month = date.getMonth()+1;
              const day = date.getDate()+1;
  
              let javaDate = null;
              if(month < 10 && day < 10){
                  javaDate = year + '-0' + month +'-0'+ day;
              }
              else if(month < 10 && day >= 10){
                  javaDate = year + '-0' + month +'-'+ day;
              }
              else if(month >= 10 && day < 10){
                  javaDate = year +'-'+ month +'-0'+day ;
              }else{
                  javaDate = year + '-'+ month+'-'+day;
              }
              console.log("date java", javaDate);
  
            const newSpecificClass = {
              date: javaDate,
              startTime: javaStartTime,
              endTime: javaEndTime,
              instructorId : this.selectedInstructor ,
              classTypeId :  this.selectedClassType
            };
            console.log("specific class", newSpecificClass)
            try {
                const response = await CLIENT.post('/specificClass/create', newSpecificClass);
                this.newSpecificClasses.push(response.data);
                this.clearInputs();
            }
            catch (e) {
                // TODO: show the user a warning
                console.log(e);
            }
          }else{
            const startDate = new Date(this.startDate);
            const startYear = startDate.getFullYear();
            const startMonth = startDate.getMonth()+1;
            const startDay = startDate.getDate()+1;
            let dayOfWeek = startDate.getDay();
            if(dayOfWeek === 0){
                dayOfWeek = 7;
            }
  
            let javaStartDate = null;
            if(startMonth < 10 && startDay < 10){
                javaStartDate = startYear + '-0' + startMonth +'-0'+ startDay;
            }
            else if(startMonth < 10 && startDay >= 10){
                javaStartDate = startYear + '-0' + startMonth +'-'+ startDay;
            }
            else if(startMonth >= 10 && startDay < 10){
                javaStartDate = startYear +'-'+ startMonth +'-0'+startDay ;
            }else{
                javaStartDate = startYear + '-'+ startMonth+'-'+startDay;
            }

            const endDate = new Date(this.endDate);
            const endYear = endDate.getFullYear();
            const endMonth = endDate.getMonth()+1;
            const endDay = endDate.getDate()+1;
  
            let javaEndDate = null;
            if(endMonth < 10 && endDay < 10){
                javaEndDate = endYear + '-0' + endMonth +'-0'+ endDay;
            }
            else if(endMonth < 10 && endDay >= 10){
                javaEndDate = endYear + '-0' + endMonth +'-'+ endDay;
            }
            else if(endMonth >= 10 && endDay < 10){
                javaEndDate = endYear +'-'+ endMonth +'-0'+endDay ;
            }else{
                javaEndDate = endYear + '-'+ endMonth+'-'+endDay;
            }

            console.log("date start java", javaStartDate);
            console.log("date end java", javaEndDate);

            const newRecurringSpecificClass = {
                date: javaStartDate,
                startTime: javaStartTime,
                endTime: javaEndTime,
                instructorId : this.selectedInstructor ,
                classTypeId :  this.selectedClassType,
                endDate: javaEndDate,
                dayOfWeek: dayOfWeek

            }
            console.log("reccuring specific class", newRecurringSpecificClass)
            try {
                const response = await CLIENT.post('/specificClass/recurring', newRecurringSpecificClass);
                console.log("response", response.data);
                console.log("response", response.data.specificClasses);



                const reccuring = response.data.specificClasses;
                reccuring.forEach(specificClass => {
                    this.newSpecificClasses.push(specificClass);
                });
                this.clearInputs();
            }
            catch (e) {
                // TODO: show the user a warning
                console.log(e);
            }
          }
              
        }
  
        
    },
    computed: {
        isCreateBtnDisabled() {
            if (this.classMode === 'regular') {
            return (
                !this.newSpecificClassDate ||
                !this.selectedTime ||
                !this.selectedClassType ||
                !this.selectedInstructor
            );
            } else {
            return (
                (!this.startDate || !this.endDate) || // Check for both start and end date
                !this.selectedTime ||
                !this.selectedClassType ||
                !this.selectedInstructor
            );
            }
        }
  
    }
  };
  </script>
  
  <style>
  #newSpecificClasses {
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
  