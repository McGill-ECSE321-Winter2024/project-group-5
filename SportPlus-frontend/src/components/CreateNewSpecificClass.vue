<template>
  <div>
    <div id="newSpecificClasses">
      <div class="radio-container">
        <input type="radio" id="regular" value="regular" v-model="classMode" />
        <label for="regular">Regular Class</label>
        <input
          type="radio"
          id="recurring"
          value="recurring"
          v-model="classMode"
        />
        <label for="recurring">Recurring Class</label>
      </div>
      <!-- Input fields for regular class -->
      <div v-if="classMode === 'regular'">
        <label for="newSpecificClassDate">Date:</label>
        <input
          type="date"
          id="newSpecificClassDate"
          placeholder="Date"
          v-model="newSpecificClassDate"
        />
      </div>
      <!-- Input fields for recurring class -->
      <div v-else-if="classMode === 'recurring'">
        <label for="startDate">Start Date:</label>
        <input
          type="date"
          id="startDate"
          placeholder="Start Date"
          v-model="startDate"
        />
        <label for="endDate">End Date:</label>
        <input
          type="date"
          id="endDate"
          placeholder="End Date"
          v-model="endDate"
        />
      </div>
      <b-form-select
        v-model="selectedClassType"
        :options="classTypeOptions"
        placeholder="Select a class type"
      ></b-form-select>
      <b-form-select
        v-model="selectedInstructor"
        :options="instructorOptions"
        placeholder="Select an instructor"
      ></b-form-select>
      <b-form-select
        v-model="selectedTime"
        :options="timeOptions"
        placeholder="Select a time slot"
      ></b-form-select>
      <button
        @click="createSpecificClass()"
        v-bind:disabled="isCreateBtnDisabled"
      >
        Create Event
      </button>
      <button class="danger-btn" @click="clearInputs()">Clear</button>
    </div>
    <h2>New Specific Class</h2>
    <table>
      <tbody id="newSpecificClasses-tbody">
        <tr>
          <th>Name</th>
          <th>Instructor</th>
          <th>Start Time</th>
          <th>Duration</th>
          <th>Date</th>
        </tr>
        <tr v-for="s in newSpecificClasses">
          <td>{{ s.classType }}</td>
          <td>{{ s.supervisor }}</td>
          <td>{{ s.startTime }}</td>
          <td>{{ s.duration }}</td>
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

const frontendUrl = "http://" + config.dev.host + ":" + config.dev.port;
const backendUrl =
  "http://" + config.dev.backendHost + ":" + config.dev.backendPort;

const CLIENT = axios.create({
  // IMPORTANT: baseURL, not baseUrl
  baseURL: backendUrl,
  headers: { "Access-Control-Allow-Origin": frontendUrl },
});

//instead of push in newspecifc classes, get all specific classes like fetch
//check if overlapping specific classes

export default {
  name: "CreateNewSpecificClass",
  data() {
    return {
      newSpecificClasses: [],
      classMode: "regular",
      newSpecificClassDate: null,
      startDate: null,
      endDate: null,
      newSpecificClassStartTime: null,
      newSpecificClassEndTime: null,
      selectedClassType: null,
      selectedInstructor: null,
      selectedTime: null,
      classTypeOptions: [{ text: "Select a class type", value: null }],
      instructorOptions: [{ text: "Select an instructor", value: null }],
      timeOptions: [{ text: "Select a time slot", value: null }],
      fields_C: [
        { key: "startTime", label: "Start Time", show: true },
        { key: "date", label: "Date", show: false },
        { key: "classType", label: "Class Type", show: true },
        { key: "supervisor", label: "Instructor", show: true },
        { key: "duration", label: "Duration", show: true },
        { key: "description", show: false },
        { key: "id", show: false },
      ],
    };
  },
  mounted() {
    try {
      this.fetchClassTypes();
      this.fetchInstructors();
      this.generateTimeOptions();
      this.fetchData();
    } catch (e) {
      // TODO: show the user a warning
      console.log(e);
    }
  },
  methods: {
    async fetchClassTypes() {
      // Make an HTTP GET request to fetch classTypes
      CLIENT.get("/classType/all")
        .then((response) => {
          const resp = response.data.classTypes;
          console.log("responseClassTypes", resp);
          const approvedClassTypes = resp.filter((type) => type.approved);
          console.log("approvedTypes", approvedClassTypes);
          const classTypesData = approvedClassTypes.map((classType) => ({
            text: `${classType.name}`,
            value: classType.typeId,
          }));

          // Assign the retrieved classTypes to types array
          classTypesData.forEach((classType) => {
            this.classTypeOptions.push(classType);
          });
        })
        .catch((error) => {
          console.error("Error fetching classTypes:", error);
        });
    },

    async fetchInstructors() {
      // Make an HTTP GET request to fetch instructors
      CLIENT.get("/instructors/all")
        .then((response) => {
          const resp = response.data.instructors;
          console.log("responseInstructors", resp);

          const instructorsData = response.data.instructors.map(
            (instructor) => ({
              text: `${instructor.lastName}, ${instructor.firstName}`, // Instructor name as the display text
              value: instructor.accountId, // Instructor ID as the value
            })
          );
          // Assign the retrieved instructors to the instructors array
          instructorsData.forEach((instructor) => {
            this.instructorOptions.push(instructor);
          });
        })
        .catch((error) => {
          console.error("Error fetching instructors:", error);
        });
    },

    generateTimeOptions() {
      const startTime = 8; // Starting hour (8 am)
      const endTime = 18; // Ending hour (6 pm)

      for (let i = startTime; i < endTime; i++) {
        const startHour = i.toString().padStart(2, "0"); // Format start hour
        const endHour = (i + 1).toString().padStart(2, "0"); // Format end hour
        const slotLabel = `${startHour}:00 - ${endHour}:00`; // Combine start and end times
        this.timeOptions.push({ text: slotLabel, value: startHour });
      }
    },

    fetchData() {
      CLIENT.get("/specificClass/all")
        .then((response) => {
          const specificClasses = response.data;
          const sortedClasses = specificClasses.sort((a, b) => {
            // Compare dates
            if (a.date < b.date) return -1;
            if (a.date > b.date) return 1;

            // If dates are equal, compare start times
            if (a.startTime < b.startTime) return -1;
            if (a.startTime > b.startTime) return 1;

            // If both dates and start times are equal, maintain current order
            return 0;
          });
          const formattedClasses = [];
          let currentDate = null;
          sortedClasses.forEach((item) => {
            // Check if the date has changed
            if (item.date !== currentDate) {
              // Insert row with day, month, and year
              const dateObj = new Date(item.date);
              dateObj.setDate(dateObj.getDate());
              const formattedDate = dateObj
                .toLocaleDateString("en-US", {
                  weekday: "short",
                  month: "long",
                  day: "numeric",
                  year: "numeric",
                })
                .replace(",", "");
              formattedClasses.push({ dateSeparator: formattedDate });
              currentDate = item.date;
            }

            // Insert the regular item
            formattedClasses.push({
              startTime: item.startTime,
              date: item.date,
              supervisor: item.instructor
                ? `${item.instructor.lastName}, ${item.instructor.firstName}`
                : "",
              classType: item.classType.name,
              duration: "60 min",
              description: item.classType.description,
              id: item.id,
            });
          });
          // Assign the formatted classes
          this.newSpecificClasses = formattedClasses;
        })
        .catch((error) => {
          console.error("Error fetching data:", error);
        });
    },

    getStartTime(selectedValue) {
      const selectedOption = this.timeOptions.find(
        (option) => option.value === selectedValue
      );
      if (selectedOption) {
        const timeString = selectedOption.text.split(":")[0]; // Extract the hour part
        return parseInt(timeString); // Parse the hour as an integer
      }
      return null;
    },
    formatDate(year, month, day) {
      const formattedMonth = month < 10 ? "0" + month : month.toString();
      const formattedDay = day < 10 ? "0" + day : day.toString();
      return `${year}-${formattedMonth}-${formattedDay}`;
    },
    hasConflict() {
      // Get the start and end time of the new class

      const startTime = this.getStartTime(this.selectedTime);
      let newStartTime = 0;

      if (startTime < 10) {
        newStartTime = "0" + startTime + ":00:00";
      } else {
        newStartTime = startTime + ":00:00";
      }
      console.log("start time java : ", newStartTime);

      // Get the date of the new class
      const date = new Date(this.newSpecificClassDate);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate() + 1;

      const newDate = this.formatDate(year, month, day);
      console.log("date java", newDate);

      // Iterate over existing classes and check for conflicts
      for (const specificClass of this.newSpecificClasses) {
        // Check if the dates match
        if (specificClass.date === newDate) {
          console.log("go in same date");
          // Check if the time slots overlap
          if (newStartTime === specificClass.startTime) {
            console.log("go in same start time");

            // Conflict found
            console.log("entered has conflict state");
            return true;
          }
        }
      }

      // No conflicts found
      return false;
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
      if (this.hasConflict()) {
        // Show an error message to the user
        alert(
          "A class already exists during this time and date. Please select another time or date."
        );
        return;
      }
      //Format the time for java.sql.Time to read
      const startTime = this.getStartTime(this.selectedTime);
      const endTime = startTime + 1;
      let javaStartTime = 0;
      let javaEndTime = 0;

      if (startTime < 10) {
        javaStartTime = "0" + startTime + ":00:00";
      } else {
        javaStartTime = startTime + ":00:00";
      }

      if (endTime < 10) {
        javaEndTime = "0" + endTime + ":00:00";
      } else {
        javaEndTime = endTime + ":00:00";
      }

      if (this.classMode == "regular") {
        const date = new Date(this.newSpecificClassDate);
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const day = date.getDate() + 2;

        const javaDate = this.formatDate(year, month, day);

        const newSpecificClass = {
          date: javaDate,
          startTime: javaStartTime,
          endTime: javaEndTime,
          instructorId: this.selectedInstructor,
          classTypeId: this.selectedClassType,
        };
        console.log("specific class", newSpecificClass);
        try {
          const response = await CLIENT.post(
            "/specificClass/create",
            newSpecificClass
          );
          this.clearInputs();
        } catch (e) {
          // TODO: show the user a warning
          console.log(e);
        }
      } else {
        const startDate = new Date(this.startDate);
        const startYear = startDate.getFullYear();
        const startMonth = startDate.getMonth() + 1;
        const startDay = startDate.getDate() + 2;
        let dayOfWeek = startDate.getDay() +1;
        
        const javaStartDate = this.formatDate(startYear, startMonth, startDay);

        const endDate = new Date(this.endDate);
        const endYear = endDate.getFullYear();
        const endMonth = endDate.getMonth() + 1;
        const endDay = endDate.getDate() + 2;

        const javaEndDate = this.formatDate(endYear, endMonth, endDay);

        console.log("date start java", javaStartDate);
        console.log("date end java", javaEndDate);

        const newRecurringSpecificClass = {
          date: javaStartDate,
          startTime: javaStartTime,
          endTime: javaEndTime,
          instructorId: this.selectedInstructor,
          classTypeId: this.selectedClassType,
          endDate: javaEndDate,
          dayOfWeek: dayOfWeek,
        };
        console.log("reccuring specific class", newRecurringSpecificClass);
        try {
          const response = await CLIENT.post(
            "/specificClass/recurring",
            newRecurringSpecificClass
          );
          console.log("response", response.data);
          console.log("response", response.data.specificClasses);

          const reccuring = response.data.specificClasses;
          // reccuring.forEach(specificClass => {
          // });
          this.clearInputs();
        } catch (e) {
          // TODO: show the user a warning
          console.log(e);
        }
      }
      this.fetchData();
    },
  },
  computed: {
    isCreateBtnDisabled() {
      if (this.classMode === "regular") {
        return (
          !this.newSpecificClassDate ||
          !this.selectedTime ||
          !this.selectedClassType ||
          !this.selectedInstructor
        );
      } else {
        return (
          !this.startDate ||
          !this.endDate || // Check for both start and end date
          !this.selectedTime ||
          !this.selectedClassType ||
          !this.selectedInstructor
        );
      }
    },
  },
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
