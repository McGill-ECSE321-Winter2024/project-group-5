<template>
    <div>
        <b-container>
            <h2 class="tableTitle">Class Schedule</h2>
            <div class="ScheduleTable">
                <b-table hover
                :items="items"
                :fields="fields"
                :sticky-header="true"
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
                <b-button variant="success" @click="registerForClass = true">Register For Selected</b-button>
            </div>
            <div class="mb-3">
                <b-button variant="warning"> Modify Selected</b-button>
            </div>
            <div class="mb-3">
                <b-button variant="info" @click="createNewSpecificClass = true">Create New Specific Class</b-button>
                <b-button variant="primary" @click="createNewClassType = true">Create New ClassType</b-button>
            </div>
            <b-modal v-model="createNewSpecificClass" title="Create New Class">
                <CreateNewSpecificClass />
            </b-modal>
            
            <b-modal v-model="showSelected" title="Class Details">
                
                <div v-if="selectedItem">
                    <p><strong>Instructor Name:</strong> {{selectedItem.supervisor}}</p>
                    <p><strong>Class Type Description:</strong> {{selectedItem.description}}</p>
                </div>
                <template v-else>
                    <p>No item selected</p>
                </template>
            </b-modal>
            <b-modal v-model="createNewClassType" title="Create New Class Type">
                <CreateNewClassType />
            </b-modal>
        </b-container>
    </div>

</template>

<script>
import CreateNewSpecificClass from '@/components/CreateNewSpecificClass.vue'
import CreateNewClassType from '@/components/CreateNewClassType.vue'
import axios from "axios";
import config from "../../config";

    const CLIENT = axios.create({
        baseURL: 'http://localhost:8080'
    });

    export default {
        name : 'SchedulePage',
        components: {
            CreateNewSpecificClass,
            CreateNewClassType
            },
        data() {
            
        return {
            createNewSpecificClass: false,
            createNewClassType: false,
            showSelected: false,
            selectedItem: null,
            items: [],
            fields: [
                { key: 'startTime', label: 'Start Time',show:true },
                { key: 'date', label: 'Date',show:true },
                { key: 'classType', label: 'Class Type',show:true },
                { key: 'supervisor', label: 'Instructor', show:true },
                { key: 'duration', label: 'Duration', show: true },
                { key: 'description', show: false}
                ]
            };
        },
        mounted() {
            this.fetchData();
        },
        methods: {
            fetchData() {
            CLIENT.get('/specificClass/all')
            .then(response => {
                console.log('Response:', response.data); 
                this.items = response.data.map(item => ({
                    
                    startTime: item.startTime,
                    date: item.date,
                    supervisor: item.instructor ? `${item.instructor.lastName}, ${item.instructor.firstName}` : '', 
                    classType: item.classType.name, 
                    duration: '60 min',
                    description: item.classType.description
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

