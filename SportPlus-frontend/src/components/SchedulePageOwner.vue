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
                            <b-button variant="outline-primary" @click="toggleModal" class="mb-2">View Selected Class
                                Menu</b-button>
                            <b-button variant="outline-primary" @click="createNewSpecificClass = true"
                                class="mb-2">Create New Specific Class</b-button>
                            <b-button variant="outline-primary" @click="createNewClassType = true"
                                class="mb-2">ClassType Menu</b-button>
                        </div>
                    </b-card>
                </b-col>
                <!-- big right column-->
                <b-col lg="10">
                    <div class="empty-divider-table"></div>
                    <h2 class="tableTitle">Class Schedule</h2>
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
                    <!-- sub-row-->
                    <b-row class="justify-content-center">
                        <b-tabs v-model="selectedTab" pills card>
                            <b-tab id="no-filter" title="No Filter" @click="tabIsNoFilter"></b-tab>
                            <b-tab id="all-available" title="Open for Registration" @click="tabIsAllAvailable"></b-tab>
                            <b-tab id="filter-by-dates" title="Filter By Date Range" @click="tabIsByDate">
                                <b-card style="width:100%; height: 250px">
                                    <b-row class="2">
                                        <b-col>
                                            <label for="datepicker-start">Start Date</label>
                                            <b-form-datepicker id="datepicker-start" today-button reset-button
                                                close-button :min="status ? new Date('1995-01-01') : min"
                                                v-model="startDate" locale="en"
                                                placeholder="Pick start date"></b-form-datepicker>
                                            <div v-if="!startDate === 'filter-by-dates'" class="error-message"
                                                style="color: red; text-decoration: underline;">Please select a start
                                                date.
                                            </div>
                                        </b-col>
                                        <b-col>
                                            <label for="datepicker-end">End Date</label>
                                            <b-form-datepicker id="datepicker-end" today-button reset-button
                                                close-button :min="min" v-model="endDate" locale="en"
                                                placeholder="Pick end date"></b-form-datepicker>
                                            <div v-if="!endDate === 'filter-by-dates'" class="error-message"
                                                style="color: red; text-decoration: underline;">Please select an
                                                endDate.
                                            </div>
                                        </b-col>
                                    </b-row>

                                    <div class="empty-divider-table"></div>
                                    <b-button variant="outline-primary" @click="fetchData" class="mb-2"
                                        v-if="endDate && startDate">Search</b-button>
                                </b-card>
                            </b-tab>

                            <b-tab id="filter-by-instructors" title="Filter By Instructor" @click="tabIsInstructor">
                                <b-card style="width: 100%;
                                    height: 250px">
                                    <b-table hover id="filterInstructors" small :items="instructors"
                                        :fields="filteredInstructors" :outlined="true" select-mode="single"
                                        responsive="sm" ref="selectableTable" selectable
                                        @row-selected="onInstructorSelected">
                                    </b-table>
                                    <b-button variant="outline-primary" @click="fetchData" class="mb-2"
                                        v-if="selectedInstructor">Search</b-button>
                                    <div v-if="displayError_I" class="error-message"
                                        style="color: red; text-decoration: underline;">Please
                                        select an Instructor.
                                    </div>
                                </b-card>
                            </b-tab>
                            <b-tab id="filter-by-classType" title="Filter By ClassType" @click="tabIsClassType">
                                <b-card style="width: 100%;
                                        height: 250pxpx">
                                    <b-table hover small :items="types" :fields="filteredClassTypes" :outlined="true"
                                        select-mode="single" responsive="sm" ref="selectableTable" selectable
                                        @row-selected="onTypeSelected">
                                    </b-table>
                                    <b-button variant="outline-primary" @click="fetchData" class="mb-2"
                                        v-if="selectedType">Search</b-button>
                                    <div v-if="displayError_T" class="error-message"
                                        style="color: red; text-decoration: underline;">Please
                                        select a ClassType.
                                    </div>
                                </b-card>
                            </b-tab>
                        </b-tabs>
                    </b-row>
                    <b-form-checkbox id="checkbox" v-model="status" name="checkbox" value="accepted"
                        unchecked-value="not_accepted">
                        Include Past Classes in Search
                    </b-form-checkbox>
                </b-col>
            </b-row>

        </b-container>
        <div>
            <b-modal v-model="createNewSpecificClass" title="Create New Class" size="lg" @ok="handleOk">
                <CreateNewSpecificClass />
            </b-modal>

            <b-modal v-model="showSelected" title="Class Details" size="50%" @ok="handleOk">
                <b-container fluid>
                    <div v-if="selectedClass">
                        <b-row>
                            <b-col lg="15">
                                <p><strong>Instructor Name:</strong>
                                    {{ JSON.parse(JSON.stringify(this.selectedClass))[0].supervisor }}</p>
                                <p><strong>Class Type:</strong>
                                    {{ JSON.parse(JSON.stringify(this.selectedClass))[0].classType }}
                                </p>
                                <p><strong>Description:</strong>
                                    {{ JSON.parse(JSON.stringify(this.selectedClass))[0].description }}</p>

                                <b-button v-if="!displayForum" variant="outline-primary"
                                    @click="viewAssignInstructorForum" class="mb-2">
                                    {{ !JSON.parse(JSON.stringify(this.selectedClass))[0].supervisor ? 'Assign an Instructor to this Class' : 'Assign a Different Instructor' }}
                                </b-button>
                            </b-col>
                            <b-col lg="10">
                                <b-table hover id="forumTableInstructors" small :items="instructors"
                                    :fields="filteredInstructors" :outlined="true" select-mode="single" responsive="sm"
                                    v-if=displayForum ref="selectableTable" selectable
                                    @row-selected="onForumInstructorSelected">
                                </b-table>
                                <b-button v-if="assignmentSelectionForum" variant="outline-primary"
                                    @click="assignInstructor">Assign instructor to Selected Class</b-button>


                                <div v-if="teachOK" class="success-message"
                                    style="color: green; text-decoration: underline;">
                                    Your request was processed successfully!
                                </div>
                                <div v-if="!teachOK" class="Error-message"
                                    style="color: red; text-decoration: underline;">
                                    {{ this.teachError }}
                                </div>
                            </b-col>
                        </b-row>
                    </div>

                    <template v-else>
                        <p>No item selected</p>
                    </template>

                </b-container>
            </b-modal>
            <b-modal v-model="createNewClassType" title="Class Type Menu" size="lg" @ok="handleOk">
                <CreateNewClassType />
            </b-modal>
        </div>
    </div>
</template>

<script>
import CreateNewSpecificClass from '@/components/CreateNewSpecificClass.vue'
import CreateNewClassType from '@/components/CreateNewClassType.vue'
import { globalState } from '@/global.js';
import axios from "axios";
import config from "../../config";

// sets up urls for axios
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port

const CLIENT = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }

});

export default {
    name: 'SchedulePageOwner',

    // runs before the page is loaded
    beforeRouteEnter(to, from, next) {
        const userType = globalState.type;

        // checks if correct user is accessing page
        // if permission not allowed, page will load to the proper page based on user type
        if (userType === 'Owner') {
            next();
        } else if (userType === 'Client') {
            next(vm => {
                vm.$router.replace('/SchedulePageClient');
            });
        } else if (userType === 'Instructor') {
            next(vm => {
                vm.$router.replace('/SchedulePageInstructor');
            });
        } else {
            // if user type not selected, routes back to login page
            next(vm => {
                vm.$router.replace('/Login');
            });
        }
    },
    components: {
        // imported components to be used on page
        CreateNewSpecificClass,
        CreateNewClassType
    },
    data() {
        const now = new Date();
        const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());

        return {
            // variables to hold page info for vue
            displayForum: false,
            assignmentSelectionForum: null,
            listInstructAssignment: [
                { value: 'id', text: "name" }
            ],
            min: today,
            status: 'not_accepted',
            teachError: null,
            teachOK: null,
            displayError_I: false,
            displayError_T: false,
            startDate: null,
            endDate: null,
            option: 'no-filter',
            searchDate: false,
            createNewSpecificClass: false,
            createNewClassType: false,
            modifySelected: false,
            showSelected: false,
            selectedClass: null,
            selectedInstructor: null,
            selectedType: null,
            classes: [],
            instructors: [],
            types: [],
            fields_C: [
                // field for schedule table
                { key: 'startTime', label: 'Start Time', show: true },
                { key: 'date', label: 'Date', show: false },
                { key: 'classType', label: 'Class Type', show: true },
                { key: 'supervisor', label: 'Instructor', show: true },
                { key: 'duration', label: 'Duration', show: true },
                { key: 'description', show: false },
                { key: 'id', show: false }
            ],
            fields_I: [
                // field for instructors for classes
                { key: 'firstName', label: 'Instructor', show: true },
                { key: 'accountId', label: 'Id', show: false }
            ],
            fields_T: [
                // fields for classtypes
                { key: 'name', label: 'Class Type', show: true },
                { key: 'typeId', label: 'Id', show: false }
            ]
        };
    },

    computed: {
        // properties update upon change
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
        // called upon init/mounting of the page
        this.fetchData();
        this.fetchData_Instructors();
        this.fetchData_ClassTypes();
    },

    methods: {
        fetchEndpoint(option) {
            // sets up endpoint for axios to fetch classes
            let endpoint;
            switch (option) {

                case "all-available":
                    // fetches all available classes
                    endpoint = `/specificClass/available`;
                    break;

                case "filter-by-dates":
                    // filters classes by date ranges specified by frontend
                    const newEnd = new Date(this.endDate);
                    newEnd.setDate(newEnd.getDate() + 1);
                    const endFormatted = this.endDate ? newEnd.toISOString().substring(0, 10) : '';
                    endpoint = `/specificClass/by-date-range?startDate=${this.startDate}&endDate=${endFormatted}`;
                    break;

                case "filter-by-instructors":
                    // filters classes by selected instructor
                    const instructorId = JSON.parse(JSON.stringify(this.selectedInstructor));
                    endpoint = `/specificClass/instructor/${instructorId[0].accountId}`;
                    break;

                case "filter-by-classType":
                    // filters classes by selected class type
                    const intId = JSON.parse(JSON.stringify(this.selectedType));
                    endpoint = `/specificClass/class-type/${intId[0].typeId}`;
                    break;

                case "no-filter":
                    // requests all classes with no filter
                    endpoint = `/specificClass/all`;
                    break;
            }

            // logs the endpoint and returns it to requested function to be called
            console.log('Constructed endpoint :', endpoint);
            return endpoint;
        },

        fetchData() {
            // fetches data based on specified endpoint option
            const endpoint = this.fetchEndpoint(this.option);
            CLIENT.get(endpoint)
                .then(response => {
                    console.log('Response:', response.data);
                    // Filter out classes with dates earlier than today
                    let filteredClasses = null;
                    if (this.status === 'not_accepted') {
                        filteredClasses = response.data.filter(item => {
                            const classDate = new Date(item.date);
                            const today = new Date();
                            return classDate >= today;
                        });
                    } else {
                        filteredClasses = response.data;
                    }
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
        fetchData_Instructors() {
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
        fetchData_ClassTypes() {
            // Make an HTTP GET request to fetch classTypes
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
        },
        viewAssignInstructorForum() {
            // opens the instructor assigning form
            this.displayForum = true;
            this.fetchData_Instructors();
            console.log("instrucot forum info", this.instructors);
        },
        assignInstructor() {
            // assigns an instructor to a specific class
            console.log("this.assignmentSelectionForum", this.assignmentSelectionForum);
            const specificClassRequestBody = {
                instructorId: JSON.parse(JSON.stringify(this.assignmentSelectionForum))[0].accountId
            }
            const id = JSON.parse(JSON.stringify(this.selectedClass))[0].id;
            console.log("url", `/${id}/assign-instructor`);
            // sends a PUT request to assign an isntructor to specific class
            CLIENT.put(`/specificClass/${id}/assign-instructor`, specificClassRequestBody).then(response => {
                this.teachOK = true;

            }).catch(error => {
                // if error raised that instructor is not assigned in backend
                this.teachError = "Could not Assign Instructor to class";
                this.teachOK = false;
                console.log("error", error);
            });
        },
        deleteSelected() {
            // deletes the specific class
            CLIENT.delete(`/specificClass/deleteBySessionId/${JSON.parse(JSON.stringify(this.selectedClass))[0].id}`).then(response => {

            }).catch(error => {
                console.log("delete error", error);
            });
        },
        onForumInstructorSelected(item) {
            this.assignmentSelectionForum = item;
        },
        onClassSelected(item) {
            this.selectedClass = item;
            console.log('Selected class:', item);
        },
        onInstructorSelected(item) {
            this.selectedInstructor = item;
            console.log('Selected instructor:', item);
        },
        onTypeSelected(item) {
            this.selectedType = item;
            console.log('Selected type:', item);
        },
        handleOk() {
            window.location.reload();
        },
        toggleModal() {
            this.showSelected = true;
            this.displayForum = false;
            this.assignmentSelectionForum = null;
            this.teachOK = null;
        },
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
    max-width: 80%;
    /* Adjust as needed */
    width: 80%;
    /* Adjust as needed */
    max-height: 80%;
    /* Adjust as needed */
    height: 60%;
    /* Adjust as needed */
}

.empty-divider {
    height: 65px;
    /* Adjust the height as needed */
}

.empty-divider-table {
    height: 20px;
    /* Adjust the height as needed */
}

#schedule-tb {
    height: 500px;
}
</style>
