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
                        <!-- button group, they trigger the modals showSelected, createNewSpecifciclass & createNewClassType -->
                        <div class="btn-group-vertical">
                            <b-button variant="outline-primary" @click="showSelected = true" class="mb-2">View Selected Class Menu</b-button>
                            <b-button variant="outline-primary" @click="createNewSpecificClass = true"
                                class="mb-2">Create New Specific Class</b-button>
                            <b-button variant="outline-primary" @click="createNewClassType = true" class="mb-2">Create
                                New ClassType</b-button>
                        </div>
                    </b-card>
                </b-col>
                <!-- big right column-->
                <b-col lg="10">
                    <div class="empty-divider-table"></div>
                    <h2 class="tableTitle">Class Schedule</h2>
                    <!-- Schedule Table -->
                    <div class="ScheduleTable">
                        <b-table hover id="schedule-tb" small :items="classes" :fields="filteredFields"
                            :sticky-header="true" :outlined="true" select-mode="single" responsive="sm"
                            ref="selectableTable" selectable @row-selected="onClassSelected">
                            <template v-slot:cell(startTime)="data">
                                <b-table-simple :class="{ 'bold-row-separator': isDateSeparator(data.item) }">
                                    {{ isDateSeparator(data.item) ? data.item.dateSeparator : (data.value) }}
                                </b-table-simple>
                            </template>
                        </b-table>
                    </div>
                    <!-- sub-row, beneath scheudle table, everyhting to handle sorting of the table-->
                    <b-row class="justify-content-center">
                        <!-- Tabs menu -->
                        <b-tabs v-model="selectedTab" pills card>
                            <b-tab id="no-filter" title="No Filter" @click="tabIsNoFilter"></b-tab>
                            <b-tab id="all-available" title="Open for Registration" @click="tabIsAllAvailable"></b-tab>
                            <!-- Search by date tab -->
                            <b-tab id="filter-by-dates" title="Filter By Date Range" @click="tabIsByDate">
                                <b-card style="width:100%; height: 200px">
                                    <b-row class="2">
                                        <!-- Date picker for start date-->
                                        <b-col>
                                            <label for="datepicker-start">Start Date</label>
                                            <b-form-datepicker id="datepicker-start" today-button reset-button
                                                close-button :min="min" v-model="startDate" locale="en"
                                                placeholder="Pick start date"></b-form-datepicker>
                                            <div v-if="!startDate === 'filter-by-dates'" class="error-message"
                                                style="color: red; text-decoration: underline;">Please select a start
                                                date.
                                            </div>
                                        </b-col>
                                        <!-- Date picker for enddate-->
                                        <b-col>
                                            <label for="datepicker-end">End Date</label>
                                            <b-form-datepicker id="datepicker-end" today-button reset-button
                                                close-button :min="startDate ? startDate : min" v-model="endDate"
                                                locale="en" placeholder="Pick end date"></b-form-datepicker>
                                            <div v-if="!endDate === 'filter-by-dates'" class="error-message"
                                                style="color: red; text-decoration: underline;">Please select an
                                                endDate.
                                            </div>
                                        </b-col>
                                    </b-row>
                                    <!-- Search button to trigger filtering -->
                                    <div class="empty-divider-table"></div>
                                    <b-button variant="outline-primary" @click="fetchData" class="mb-2"
                                        v-if="endDate && startDate">Search</b-button>
                                </b-card>
                            </b-tab>
                            <!-- search by instructor tab -->
                            <b-tab id="filter-by-instructors" title="Filter By Instructor" @click="tabIsInstructor">
                                <b-card style="width: 100%;
                                    height: 200px">
                                    <!-- selectable table of instructors -->
                                    <b-table hover small :items="instructors" :fields="filteredInstructors"
                                        :outlined="true" select-mode="single" responsive="sm" ref="selectableTable"
                                        selectable @row-selected="onInstructorSelected">
                                    </b-table>
                                    <!-- button to trigger filtering -->
                                    <b-button variant="outline-primary" @click="fetchData" class="mb-2"
                                        v-if="selectedInstructor">Search</b-button>
                                    <div v-if="displayError_I" class="error-message"
                                        style="color: red; text-decoration: underline;">Please
                                        select an Instructor.
                                    </div>
                                </b-card>
                            </b-tab>
                            <!-- filter by classType tab -->
                            <b-tab id="filter-by-classType" title="Filter By ClassType" @click="tabIsClassType">
                                <b-card style="width: 100%;
                                        height: 200px">
                                        <!-- Selectable table with classtypes -->
                                    <b-table hover small :items="types" :fields="filteredClassTypes" :outlined="true"
                                        select-mode="single" responsive="sm" ref="selectableTable" selectable
                                        @row-selected="onTypeSelected">
                                    </b-table>
                                    <!-- Button to trigger filtering -->
                                    <b-button variant="outline-primary" @click="fetchData" class="mb-2"
                                        v-if="selectedType">Search
                                    </b-button>
                                    <div v-if="displayError_T" class="error-message"
                                        style="color: red; text-decoration: underline;">Please
                                        select a ClassType.
                                    </div>
                                </b-card>
                            </b-tab>
                        </b-tabs>
                    </b-row>
                </b-col>
            </b-row>

        </b-container>
        <div>
            <!-- Brings createNewSpecificClass component into a "pop up" -->
            <b-modal v-model="createNewSpecificClass" title="Create New Class" @ok="handleOk">
                <CreateNewSpecificClass />
            </b-modal>

            <!-- Brings show selected menu into a "pop-up" -->
            <b-modal v-model="showSelected" title="Class Details" @ok="handleOk">

                <div v-if="selectedClass">
                    <!-- Description of selected class -->
                    <p><strong>Instructor Name:</strong>
                        {{ JSON.parse(JSON.stringify(this.selectedClass))[0].supervisor }}</p>
                    <p><strong>Class Type:</strong> {{ JSON.parse(JSON.stringify(this.selectedClass))[0].classType }}</p>
                    <p><strong>Description:</strong> {{ JSON.parse(JSON.stringify(this.selectedClass))[0].description }}
                    </p>
                    <b-col>

                    </b-col>
                    <!-- Assignment menu, buttons so Instructor can assign himself to selected class -->
                    <b-col>
                        <b-button v-if="!JSON.parse(JSON.stringify(this.selectedClass))[0].supervisor"
                            variant="outline-primary" @click="assignMyself" class="mb-2">Register to Teach this
                            Class</b-button>
                        <div v-if="teachOK && !JSON.parse(JSON.stringify(this.selectedClass))[0].supervisor"
                            class="success-message" style="color: green; text-decoration: underline;">Your request was
                            processed
                            successfully!
                        </div>
                        <div v-if="!teachOK && !JSON.parse(JSON.stringify(this.selectedClass))[0].supervisor"
                            class="Error-message" style="color: red; text-decoration: underline;">{{ this.teachError }}
                        </div>
                    </b-col>
                </div>
                <template v-else>
                    <p>No item selected</p>
                </template>
            </b-modal>
             <!-- Brings createNewClassType component into a "pop up" -->
            <b-modal v-model="createNewClassType" title="Create New Class Type" @ok="handleOk">
                <CreateNewClassType />
            </b-modal>
        </div>
    </div>

</template>

<script>
import CreateNewSpecificClass from '@/components/CreateNewSpecificClass.vue'
import CreateNewClassType from '@/components/CreateNewClassType.vue'
import { globalState } from '@/global';  // Import the globalState variable
import axios from "axios";
import config from "../../config";

const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port

const CLIENT = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }

});
// router that manages view based on user logged-in
export default {
    name: 'SchedulePageInstructor',
    beforeRouteEnter(to, from, next) {
        const userType = globalState.type;
        if (userType === 'Instructor') {
            next();
        } else if (userType === 'Client') {
            next(vm => {
                vm.$router.replace('/SchedulePageClient');
            });
        } else if (userType === 'Owner') {
            next(vm => {
                vm.$router.replace('/SchedulePageOwner');
            });
        } else {
            next(vm => {
                vm.$router.replace('/Login');
            });
        }
    },
    //Imported components for modals
    components: {
        CreateNewSpecificClass,
        CreateNewClassType
    },
    // when closing a modal, refresh page (information in table is put up to date)
    handleOk(){
            window.location.reload();
    },
    data() {
        const now = new Date();
        const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());

        return {
            //error messages for filtering
            displayError_I: false,
            displayError_T: false,
            //error vriables inside "show selected" modal
            teachError: null,
            teachOK: null,
            //date select variables
            min: today,
            startDate: null,
            endDate: null,
            //holds active tab, default is no filter
            option: 'no-filter', 
            // booleans handling modal operations
            createNewSpecificClass: false,
            createNewClassType: false,
            showSelected: false,
            //variables holding selected items from tables
            selectedClass: null,
            selectedInstructor: null,
            selectedType: null,
            // lists for data retrieval for tables
            classes: [],
            instructors: [],
            types: [],
            //fields for retrieved data for tables
            fields_C: [
                { key: 'startTime', label: 'Start Time', show: true },
                { key: 'date', label: 'Date', show: false },
                { key: 'classType', label: 'Class Type', show: true },
                { key: 'supervisor', label: 'Instructor', show: true },
                { key: 'duration', label: 'Duration', show: true },
                { key: 'description', show: false },
                { key: 'id', show: false }
            ],
            fields_I: [
                { key: 'firstName', label: 'Instructor', show: true },
                { key: 'accountId', label: 'Id', show: false }
            ],
            fields_T: [
                { key: 'name', label: 'Class Type', show: true },
                { key: 'typeId', label: 'Id', show: false }
            ]
        };
    },

    computed: {
        filteredFields() {
            return this.fields_C.filter(field => field.show);
        },
        filteredInstructors() {
            return this.fields_I.filter(field => field.show);
        },
        filteredClassTypes() {
            return this.fields_T.filter(field => field.show);
        }
    },
    mounted() {
        this.fetchData();
        this.fetchData_Instructors();
        this.fetchData_ClassTypes();
    },

    methods: {
        //will create proper endpoint for fetchData depending on tab selected (option)
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
                    endpoint = `/specificClass/all`;
                    break;
            }
            console.log('Constructed endpoint :', endpoint);
            return endpoint;
        },
        //populates ScheduleTable 
        fetchData() {
            const endpoint = this.fetchEndpoint(this.option);
            CLIENT.get(endpoint)
                .then(response => {
                    console.log('Response:', response.data);
                    //filters classes to removed passed classes
                    const filteredClasses = response.data.filter(item => {
                        const classDate = new Date(item.date);
                        const today = new Date();
                        return classDate >= today;
                    });
                    //sort received classes by --> date, then --> start time
                    const sortedClasses = filteredClasses.sort((a, b) => {
                        // Compare dates
                        if (a.date < b.date) return -1;
                        if (a.date > b.date) return 1;

                        // If dates are equal, compare start times
                        if (a.startTime < b.startTime) return -1;
                        if (a.startTime > b.startTime) return 1;

                        // If both dates and start times are equal, maintain current order
                        return 0;
                    });
                    //format to include date seperator option 
                    const formattedClasses = [];
                    let currentDate = null;
                    sortedClasses.forEach(item => {
                        // Check if the date has changed
                        if (item.date !== currentDate) {
                            // Insert row with day, month, and year
                            const dateObj = new Date(item.date);
                            dateObj.setDate(dateObj.getDate() + 1);
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
                            description: item.classType.description,
                            id: item.id
                        });
                    });

                    // Assign the formatted classes
                    this.classes = formattedClasses;
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        },
        //populates intructors table for filter by instructor
        fetchData_Instructors() {
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
        },//populates classtypes table for filter by classtype
        fetchData_ClassTypes() {
            CLIENT.get('/classType/all')
                .then(response => {
                    const resp = response.data.classTypes;
                    console.log("responseClassTypes", resp);
                    const approvedClassTypes = resp.filter(type => type.approved);
                    console.log("approvedTypes", approvedClassTypes);
                    const classTypesData = approvedClassTypes.map(classType => ({
                        name: classType.name,
                        typeId: classType.typeId,
                    }));

                    // Assign the retrieved classTypes to types array
                    this.types = classTypesData;
                })
                .catch(error => {
                    console.error('Error fetching classTypes:', error);
                });
        },//assigns logged in instructor to selected classType. triggered by button in modal
        assignMyself() {
            console.log("yeehaw", JSON.parse(JSON.stringify((globalState.accountId))));
            const specificClassRequestBody = {
                instructorId: globalState.accountId
            }
            const id = JSON.parse(JSON.stringify(this.selectedClass))[0].id;
            console.log("this.selectedClass", this.selectedClass);
            console.log("id", id);
            console.log("url", `/${id}/assign-instructor`);
            CLIENT.put(`specificClass/${id}/assign-instructor`, specificClassRequestBody).then(response => {
                this.teachOK = true;

            }).catch(error => {
                this.teachError = "Could not Assign you to this class";
                this.teachOK = false;
                console.log("error", error);
            });

        }, 
        //updates selectedClass upon selection of new class
        onClassSelected(item) {
            this.selectedClass = item;
            console.log('Selected class:', item);
        },
        //updates instructor upon selection of new instructor
        onInstructorSelected(item) {
            this.selectedInstructor = item;
            console.log('Selected instructor:', item);
        },
        //updates classType upon selection of new type
        onTypeSelected(item) {
            this.selectedType = item;
            console.log('Selected type:', item);
        },
        //the following tab___ handle updating the "option" variable to the selected tab
        tabIsAllAvailable(tab) {
            this.option = 'all-available';
            this.fetchData();
        },
        tabIsNoFilter(tab) {
            this.option = 'no-filter';
            this.fetchData();
        },
        tabIsInstructor(tab) {
            this.option = 'filter-by-instructors';
            this.fetchData_Instructors();
        },
        tabIsClassType(tab) {
            this.option = 'filter-by-classType';
            this.fetchData_ClassTypes();
        },
        tabIsByDate() {
            this.option = 'filter-by-dates';
            console.log('tab', option);
        },
        //handle dates and date sepearot for Schedule Table 
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

.ScheduleTable {
    height: 300px;
}

.bold-row-separator {
    font-weight: bold;
    font-size: larger;
    text-align: left;
}

.container-wrapper {
    margin: 0 auto;
    /* Center the container */
    max-width: 1600px;
}

.custom-modal .modal-dialog {
    max-width: 1000px;
    width: 80%;
    max-height: 1000px;
    height: 60%;
}

.empty-divider {
    height: 65px;
}

.empty-divider-table {
    height: 20px;
}

#schedule-tb {
    height: 500px;
}
</style>
