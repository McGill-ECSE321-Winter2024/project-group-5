<template>
    <div>
        <b-container>
            <h2 class="tableTitle">Class Schedule</h2>
            <div class="ScheduleTable">
                <b-table hover
                :items="items"
                :fields="fields"
                :bordered="bordered"
                :sticky-header="true"
                :head-variant="dark"
                :outlined="true"
                :row-variant="rowVariant"
                select-mode="single"
                responsive="sm"
                ref="selectableTable"
                selectable
                @row-selected="onRowSelected"
                >
                </b-table>
            </div>
        </b-container>
        <b-container>
            <div class="mb-3">
                <b-button variant="secondary" @click="showSelected = true">View Selected</b-button>
                <b-button variant="success">Register For Selected</b-button>
                
            </div>
            <div class="mb-3">
                <b-button variant="info" @click="createNewSpecificClass = true">Create New Specific Class</b-button>
                <b-button variant="primary">Create New ClassType</b-button>
            </div>
            <div class="mb-3">
                <b-button variant="warning"> Modify Selected</b-button>
            </div>
            <b-modal v-model="createNewSpecificClass" title="Create New Class">
                <CreateNewSpecificClass />
            </b-modal>
            <b-modal v-model="showSelected" title="Class Details">
                
                <div v-if="selectedItem">
                    <p><strong>Instructor:</strong> {{ selectedItem.Instructor }}</p>
                    <p><strong>Description:</strong> {{ selectedItem.description }}</p>

                </div>
                <template v-else>
                    <p>No item selected</p>
                </template>
            </b-modal>
        </b-container>
    </div>

</template>

<script>
import CreateNewSpecificClass from '@/components/CreateNewSpecificClass.vue'
import axios from "axios";
import config from "../../config";

const AXIOS = axios.create({
    baseURL: config.dev.backendBaseUrl
});

  export default {
    name : 'SchedulePage',
    components: {
        CreateNewSpecificClass
        },
    data() {
        
      return {
        createNewSpecificClass: false,
        showSelected: false,
        selectedItem: null,
        items: [],
        fields: [
            { key: 'startTime', label: 'Start Time' },
            { key: 'date', label: 'Date' },
            { key: 'supervisor', label: 'Instructor' },
            { key: 'classType', label: 'Class Type' },
            { key: 'duration', label: 'Duration' }
            ]
        };
    },
    mounted() {
        this.fetchData();
    },
    methods: {
        fetchData() {
        AXIOS.get('https//SportPlus-backend/specificClass/all')
        .then(response => {
        this.items = response.data.map(item => ({
            startTime: item.startTime,
            date: item.date,
            supervisor: item.supervisor.name, 
            classType: item.classType.name, 
            duration: calculateDuration(item.startTime, item.endTime) 
        }));
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
    },
        onRowSelected(item) {
            this.selectedItem = item;
        },
        rowVariant(index) {
            return this.selectedItem === index ? 'info' : null;
        }
  }
  }
</script>

<style scoped>
.tableTitle th {
  text-align: left;
}

.custom-modal .modal-dialog {
  max-width: 1000px; /* Adjust as needed */
  width: 80%; /* Adjust as needed */
  max-height: 800px; /* Adjust as needed */
  height: 60%; /* Adjust as needed */
}
</style>

