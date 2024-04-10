<template>
    <div class="container-wrapper">
        <b-container fluid class="vh-100">
            <b-row>
                <!-- big left column-->
                <b-col lg="2">
                    <div class="empty-divider"></div>

                    <b-card style="width: auto;">
                        <b-row class="h-100 align-items-center">
                            <!-- empty row for space-->
                        </b-row>
                        <!-- button group-->
                        <div class="btn-group-vertical">
                            <b-button variant="outline-primary" @click="showSelected = true" class="mb-2">View Selected</b-button>
                            <b-button variant="outline-primary" @click="registerForClass = true" class="mb-2">Register For Selected</b-button>
                            <b-button variant="outline-primary" @click="modifySelected = true" class="mb-2"> Modify Selected</b-button>
                            <b-button variant="outline-primary" @click="createNewSpecificClass = true" class="mb-2">Create New Specific Class</b-button>
                            <b-button variant="outline-primary" @click="createNewClassType = true" class="mb-2">Create New ClassType</b-button>
                        </div>
                    </b-card>
                </b-col>
                <!-- big right column-->
                <b-col lg="10"> 
                    <div class="empty-divider-table"></div>
                    <h2 class="tableTitle">Class Schedule</h2>
                    <div class="ScheduleTable">
                        <b-table hover
                            id="schedule-tb"
                            small
                            :items="classes"
                            :fields="filteredFields"
                            :sticky-header="true"
                            :outlined="true"
                            :small="true"
                            select-mode="single"
                            responsive="sm"
                            ref="selectableTable"
                            selectable
                            @row-selected="onClassSelected"
                            >
                            <template v-slot:cell(startTime)="data">
                                <b-table-simple :class="{ 'bold-row-separator': isDateSeparator(data.item) }">
                                    {{ isDateSeparator(data.item) ? data.item.dateSeparator : (data.value) }}
                                </b-table-simple>
                            </template>
                        </b-table>
                    </div>

                    <!-- radio buttons-->
                    <b-form-radio-group v-model="selectedOption">
                        <b-form-radio value="no-filter">No Filter</b-form-radio>
                        <b-form-radio value="all-available">All Available Classes</b-form-radio>
                        <b-form-radio value="filter-by-dates">Filter by Dates</b-form-radio>
                        <b-form-radio value="filter-by-instructors">Filter by Instructor</b-form-radio>
                        <b-form-radio value="filter-by-classType">Filter by Class Type</b-form-radio>
                    </b-form-radio-group>
                    <!-- sub-row-->
                    <b-row class="1">
                        <b-card style="width: auto;">
                            <b-col>
                                <b-row class="2">
                                    <b-col>
                                        <label for="datepicker-start">Start Date</label>
                                            <b-form-datepicker
                                                id="datepicker-start"
                                                today-button
                                                reset-button
                                                close-button
                                                :min="min" 
                                                v-model="startDate"
                                                :disabled="selectedOption !== 'filter-by-dates'"
                                                locale="en"
                                                placeholder="Pick start date" 
                                            ></b-form-datepicker>
                                            <div v-if="!startDate && selectedOption === 'filter-by-dates'" 
                                                class="error-message"
                                                style="color: red; text-decoration: underline;"
                                                >Please select a start date.
                                            </div>
                                    </b-col>
                                    <b-col>
                                        <label for="datepicker-end">End Date</label>
                                            <b-form-datepicker
                                                id="datepicker-end"
                                                today-button
                                                reset-button
                                                close-button
                                                :min="startDate ? startDate : min"
                                                v-model="endDate"
                                                :disabled="selectedOption !== 'filter-by-dates'"
                                                locale="en"
                                                placeholder="Pick end date" 
                                            ></b-form-datepicker>
                                            <div v-if="!endDate && selectedOption === 'filter-by-dates'" 
                                                class="error-message"
                                                style="color: red; text-decoration: underline;"
                                                >Please select an endDate.
                                            </div>
                                    </b-col>
                                </b-row>
                                <div class="empty-divider-table"></div>
                                <b-button variant="outline-primary" 
                                    @click="fetchData" 
                                    class="mb-2"
                                    v-if="endDate && startDate && selectedOption === 'filter-by-dates'"
                                    >Search</b-button>
                            </b-col>
                        </b-card>
                        <b-card style="width: auto;">
                            <b-col>
                                    <b-table hover
                                        small
                                        :items="instructors"
                                        :fields="filteredInstructors"
                                        :outlined="true"
                                        select-mode="single"
                                        responsive="sm"
                                        ref="selectableTable"
                                        selectable
                                        @row-selected="onInstructorSelected"
                                        :disabled="selectedOption !== 'filter-by-instructors'"
                                        >
                                    </b-table>
                                    <b-button variant="outline-primary" 
                                        @click="fetchData" 
                                        class="mb-2"
                                        v-if="selectedOption === 'filter-by-instructors' && selectedInstructor"
                                    >Search</b-button>
                                    <div v-if="displayError_I"  
                                                class="error-message"
                                                style="color: red; text-decoration: underline;"
                                                >Please select an Instructor.
                                            </div>
                            </b-col>
                        </b-card>
                        <b-card style="width: auto;">
                            <b-col>
                                <b-table hover
                                    small
                                    :items="types"
                                    :fields="filteredClassTypes"
                                    :outlined="true"
                                    select-mode="single"
                                    responsive="sm"
                                    ref="selectableTable"
                                    selectable
                                    @row-selected="onTypeSelected"
                                    :disabled="selectedOption !== 'filter-by-classType'"
                                    >
                                </b-table>
                                <b-button variant="outline-primary" 
                                        @click="fetchData" 
                                        class="mb-2"
                                        v-if="selectedOption === 'filter-by-classType' && selectedType"
                                    >Search</b-button>
                                    <div v-if="displayError_T" 
                                                class="error-message"
                                                style="color: red; text-decoration: underline;"
                                                >Please select a ClassType.
                                            </div>
                            </b-col>
                        </b-card>
                    </b-row>
                </b-col>
            </b-row>
            
        </b-container>
        <div>
            <b-modal v-model="createNewSpecificClass" title="Create New Class">
                <CreateNewSpecificClass />
            </b-modal>
            
            <b-modal v-model="showSelected" title="Class Details">
                
                <div v-if="selectedClass">
                    <p><strong>Instructor Name:</strong> {{JSON.parse(JSON.stringify(this.selectedClass))[0].supervisor}}</p>
                    <p><strong>Class Type:</strong> {{JSON.parse(JSON.stringify(this.selectedClass))[0].classType}}</p>
                    <p><strong>Description:</strong> {{JSON.parse(JSON.stringify(this.selectedClass))[0].description}}</p>
                    <b-col>

                    </b-col>
                    <b-col>
                        <b-button variant="outline-primary" @click="assignInstructor" class="mb-2">Assign Instructor</b-button>
                    </b-col>
                </div>
                <template v-else>
                    <p>No item selected</p>
                </template>
            </b-modal>
            <b-modal v-model="createNewClassType" title="Create New Class Type">
                <CreateNewClassType />
            </b-modal>
        </div>
    </div>

</template>

<script>
import CreateNewSpecificClass from '@/components/CreateNewSpecificClass.vue'
import CreateNewClassType from '@/components/CreateNewClassType.vue'
import { globalState } from '@/global';
import axios from "axios";
import config from "../../config";

    const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
    const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port

    const CLIENT = axios.create({
        baseURL: backendUrl,
        headers: { 'Access-Control-Allow-Origin': frontendUrl }
        
    });

    export default {
        name : 'SchedulePage',
        components: {
            CreateNewSpecificClass,
            CreateNewClassType
            },
        data() {
            const now = new Date();
            const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());

        return {
            min: today,
            displayError_I: false,
            displayError_T: false,
            startDate: null,
            endDate: null,
            selectedOption: 'no-filter',
            searchDate: false,
            createNewSpecificClass: false,
            createNewClassType: false,
            modifySelected: false,
            showSelected: false,
            selectedClass: null,
            selectedInstructor : null,
            selectedType : null,
            classes: [],
            instructors: [],
            types: [],
            fields_C: [
                { key: 'startTime', label: 'Start Time',show:true },
                { key: 'date', label: 'Date',show: false },
                { key: 'classType', label: 'Class Type',show:true },
                { key: 'supervisor', label: 'Instructor', show:true },
                { key: 'duration', label: 'Duration', show: true },
                { key: 'description', show: false}
                ],
            fields_I: [
                {key: 'firstName', label: 'Instructor', show: true},
                {key: 'accountId', label: 'Id', show: true}
            ],
            fields_T: [
                {key: 'name', label: 'Class Type', show: true},
                {key: 'typeId', label: 'Id', show: true}
            ]    
            };
        },
        watch: {
            selectedOption(newOption){
                if(newOption === 'no-filter' || newOption === 'all-available'){
                    this.fetchData();
                }else if(newOption === 'filter-by-classType'){
                    this.fetchData_ClassTypes();
                }else if(newOption ==='filter-by-instructors'){
                    this.fetchData_Instructors();
                }
            }
    
        },
        computed: {
            filteredFields() {
                return this.fields_C.filter(field => field.show);
            },
            filteredInstructors(){
                return this.fields_I.filter(field => field.show);
            },
            filteredClassTypes(){
                return this.fields_T.filter(field => field.show);
            }
        },
        mounted() {
            this.fetchData();
            this.fetchData_Instructors();
            this.fetchData_ClassTypes();
        },

        methods: {
            fetchEndpoint(option) {
                let endpoint;
                switch (option) {

                    case "all-available":
                        endpoint = `/specificClass/available`;
                        break;

                    case "filter-by-dates":
                        const newEnd = new Date(this.endDate);
                        newEnd.setDate(newEnd.getDate() + 1);
                        const endFormatted = this.endDate ? newEnd.toISOString().substring(0, 10) : '';
                        endpoint = `/specificClass/by-date-range?startDate=${this.startDate}&endDate=${endFormatted}`;
                        break;

                    case "filter-by-instructors":
                        const instructorId = JSON.parse(JSON.stringify(this.selectedInstructor));
                        endpoint = `/specificClass/instructor/${instructorId[0].accountId}`;
                        break;
                    
                    case "filter-by-classType":
                        const intId = JSON.parse(JSON.stringify(this.selectedType));
                        endpoint = `/specificClass/class-type/${intId[0].typeId}`;
                        break;

                    case "no-filter":
                        endpoint =`/specificClass/all`;
                        break;
                }
                console.log('Constructed endpoint :', endpoint); 
                return endpoint;
            },

            fetchData() {
                const endpoint = this.fetchEndpoint(this.selectedOption);
                CLIENT.get(endpoint)
                .then(response => {
                    console.log('Response:', response.data); 
                    const sortedClasses = response.data.sort((a, b) => {
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
                    sortedClasses.forEach(item => {
                    // Check if the date has changed
                    if (item.date !== currentDate) {
                        // Insert row with day, month, and year
                        const dateObj = new Date(item.date);
                        const formattedDate = dateObj.toLocaleDateString('en-US', { weekday: 'short', month: 'long', day: 'numeric', year: 'numeric' }).replace(',', '');;
                        formattedClasses.push({ dateSeparator: formattedDate });
                        currentDate = item.date;
                    }

                    // Insert the regular item
                        formattedClasses.push({
                        startTime: item.startTime,
                        date: item.date,
                        supervisor: item.instructor ? `${item.instructor.lastName}, ${item.instructor.firstName}` : '', 
                        classType: item.classType.name, 
                        duration: '60 min',
                        description: item.classType.description
                    });
                });

                    // Assign the formatted classes
                    this.classes = formattedClasses;
                })
                    .catch(error => {
                        console.error('Error fetching data:', error);
                        });
            },
            fetchData_Instructors(){
                // Make an HTTP GET request to fetch instructors
                CLIENT.get('/instructors/all')
                    .then(response => {
                        const resp = response.data.instructors;
                        console.log("responseInstructors", resp);
                        const instructorsData = resp.map(supervisor => ({
                            firstName: `${supervisor.lastName}, ${supervisor.firstName}`,
                            accountId: supervisor.accountId, 
                        }));
                    // Assign the retrieved instructors to the instructors array
                        this.instructors = instructorsData;
                    })
                    .catch(error => {
                    console.error('Error fetching instructors:', error);
                    });
            },
            fetchData_ClassTypes(){
                // Make an HTTP GET request to fetch classTypes
                CLIENT.get('/classType/all')
                    .then(response => {
                        const resp = response.data.classTypes;
                        console.log("responseClassTypes", resp);
                        const approvedClassTypes = resp.filter(type => type.approved);
                        console.log("approvedTypes", approvedClassTypes);
                        const classTypesData = approvedClassTypes.map(classType =>({
                            name: classType.name,
                            typeId: classType.typeId,
                        }));
                        
                    // Assign the retrieved classTypes to types array
                        this.types = classTypesData;
                    })
                    .catch(error => {
                    console.error('Error fetching classTypes:', error);
                    });
            },
            onClassSelected(item) {
                this.selectedClass = item;
                console.log('Selected class:', item);
            },
            onInstructorSelected(item){
                this.selectedInstructor = item;
                console.log('Selected instructor:', item);
            },
            onTypeSelected(item){
                this.selectedType = item;
                console.log('Selected type:', item);
            },
            rowVariant(index) {
                return this.selectedClass === index ? 'info' : null;
            },
            formatDate(dateString) {
            const date = new Date(dateString);
            const options = { weekday: 'short', month: 'long', day: 'numeric', year: 'numeric' };
            return date.toLocaleDateString('en-US', options);
            },
            isDateSeparator(item) {
                // Check if the item is a separator row
                return item.dateSeparator !== undefined;
            }
        }
    }
</script>

<style scoped>
.tableTitle th {
    text-align: left;
}

.bold-row-separator {
    font-weight: bold;
    font-size: larger;
    text-align: left;
}
.container-wrapper{
    margin: 0 auto; /* Center the container */
    max-width: 1600px; 
}

.custom-modal .modal-dialog {
    max-width: 1000px; /* Adjust as needed */
    width: 80%; /* Adjust as needed */
    max-height: 1000px; /* Adjust as needed */
    height: 60%; /* Adjust as needed */
}
.empty-divider {
  height: 65px; /* Adjust the height as needed */
}
.empty-divider-table {
  height: 20px; /* Adjust the height as needed */
}
#schedule-tb{
    height: 500px;
}
</style>
