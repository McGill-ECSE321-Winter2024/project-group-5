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
                            <b-button variant="outline-primary" @click="handleShowSelected" class="mb-2">View Selected
                                || Register</b-button>
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
                            <b-tab id="all-available" title="Open for Registration" @click="tabIsAllAvailable"></b-tab>
                            <b-tab id="filter-by-dates" title="Filter By Date Range" @click="tabIsByDate">
                                <b-card style="width:100%; height: 180px">
                                    <b-row class="2">
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

                                    <div class="empty-divider-table"></div>
                                    <b-button variant="outline-primary" @click="fetchData" class="mb-2"
                                        v-if="endDate && startDate">Filter</b-button>
                                </b-card>
                            </b-tab>

                            <b-tab id="filter-by-instructors" title="Filter By Instructor" @click="tabIsInstructor">
                                <b-card style="width: 100%;
                                    height: 100%">
                                    <b-table hover small :items="instructors" :fields="filteredInstructors"
                                        :outlined="true" select-mode="single" responsive="sm" ref="selectableTable"
                                        selectable @row-selected="onInstructorSelected">
                                    </b-table>
                                    <b-button variant="outline-primary" @click="fetchData" class="mb-2"
                                        v-if="selectedInstructor">Filter</b-button>
                                    <div v-if="displayError_I" class="error-message"
                                        style="color: red; text-decoration: underline;">Please
                                        select an Instructor.
                                    </div>
                                </b-card>
                            </b-tab>
                            <b-tab id="filter-by-classType" title="Filter By ClassType" @click="tabIsClassType">
                                <b-card style="width: 100%;
                                        height: 100%">
                                    <b-table hover small :items="types" :fields="filteredClassTypes" :outlined="true"
                                        select-mode="single" responsive="sm" ref="selectableTable" selectable
                                        @row-selected="onTypeSelected">
                                    </b-table>
                                    <b-button variant="outline-primary" @click="fetchData" class="mb-2"
                                        v-if="selectedType">Filter</b-button>
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
            <b-modal v-model="showSelected" title="Class Details" @close="handleModalClose">
                <div v-if="selectedClass">
                    <p><strong>Instructor Name:</strong>
                        {{ JSON.parse(JSON.stringify(this.selectedClass))[0].supervisor }}</p>
                    <p><strong>Class Type:</strong> {{ JSON.parse(JSON.stringify(this.selectedClass))[0].classType }}
                    </p>
                    <p><strong>Description:</strong> {{ JSON.parse(JSON.stringify(this.selectedClass))[0].description }}
                    </p>
                    <b-col>

                    </b-col>
                    <b-col>
                        <b-button variant="outline-primary" @click="registerForClass" class="mb-2">Register for This
                            Class</b-button>
                        <div v-if="registrationOK" class="success-message"
                            style="color: green; text-decoration: underline;">
                            Your registration was processed successfully!
                        </div>
                        <div v-if="!registrationOK" class="Error-message"
                            style="color: red; text-decoration: underline;">
                            {{ this.registrationError }}
                        </div>

                    </b-col>
                </div>
                <template v-else>
                    <p>No item selected</p>
                </template>
            </b-modal>
        </div>
    </div>

</template>

<script>
import CreateNewSpecificClass from '@/components/CreateNewSpecificClass.vue'
import CreateNewClassType from '@/components/CreateNewClassType.vue'
import { globalState } from "@/global.js";
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
    name: 'SchedulePageClient',

    // runs before the page is loaded
    beforeRouteEnter(to, from, next) {
        const userType = globalState.type;

        // checks if correct user is accessing page
        // if permission not allowed, page will load to the proper page based on user type
        if (userType === 'Client') {
            next();
        } else if (userType === 'Instructor') {
            next(vm => {
                vm.$router.replace('/SchedulePageInstructor');
            });
        } else if (userType === 'Owner') {
            next(vm => {
                vm.$router.replace('/SchedulePageOwner');
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
            registrationOK: true,
            registrationError: null,
            closeRegPanel: null,
            DISABLE: false,
            min: today,
            displayError_I: false,
            displayError_T: false,
            startDate: null,
            endDate: null,
            option: 'all-available',
            searchDate: false,
            showSelected: false,
            selectedClass: null,
            selectedInstructor: null,
            selectedType: null,
            isAlreadyReg: null,
            classIsFull: null,
            hasPaymentMethod: null,
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
                { key: 'id', show: false },
                { key: 'numOfRegistrations', show: false }
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
                    const filteredClasses = response.data.filter(item => {
                        const classDate = new Date(item.date);
                        const today = new Date();
                        return classDate >= today;
                    });
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
                            id: item.id,
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
        registerForClass() {
            // attemps to register for selected class
            this.DISABLE = true;
            this.isAlreadyRegisteredForClass()
                .then(() => this.classIsComplete())
                .then(() => this.checkForPaymentMethod())
                .then(() => {
                    console.log("this.hasPaymentMethod", this.hasPaymentMethod);

                    // if already registered to class, show error message and don't register
                    if (this.isAlreadyReg) {
                        this.registrationError = "You are already registered for this class!";
                        this.registrationOK = false;

                    // if class is full, don't register and show error message
                    } else if (this.classIsFull) {
                        this.registrationError = "This class is full!";
                        this.registrationOK = false;

                    // if client has not payment method, ask to add payment method
                    } else if (!this.hasPaymentMethod) {
                        this.registrationError = "You must have a payment method to register for a class";
                        this.registrationOK = false;

                    } else {
                        
                        // if no errors, POST request to create registration to class
                        CLIENT.post(`/registrations/create/${JSON.parse(JSON.stringify(this.selectedClass))[0].id}/${globalState.accountEmail}`)
                            .then(response => {
                                this.registrationOK = true;
                            })
                            .catch(error => {
                                // error if registration does not work from backend
                                console.error('Error Create:', error);
                                this.registrationError = "Process could not be completed";
                                this.registrationOK = false;
                            });
                    }
                })
                .catch(error => {
                    console.error("Error occurred during registration:", error);
                    // Handle error if needed
                });
        },

        isAlreadyRegisteredForClass() {
            // checks if client is already registered for class
            return new Promise((resolve, reject) => {
                console.log("email input to isAlre...", globalState.accountEmail);
                CLIENT.get(`/registrations/getByClient/${globalState.accountEmail}`)
                    .then(response => {
                        const resp = response.data.registrations;
                        if (resp.length === 0) {
                            // if no registrations, return false
                            this.isAlreadyReg = false;
                        } else {
                            resp.forEach(registration => {
                                if (registration.specificClass.id === JSON.parse(JSON.stringify(this.selectedClass))[0].id) {
                                    // if registration exists for this class, return true
                                    this.isAlreadyReg = true;
                                    return;
                                }
                                console.log(registration);
                            });
                        }
                        resolve(); // Resolve the promise once the logic is complete
                    })
                    .catch(error => {
                        console.error('Error getByClient:', error);
                        this.isAlreadyReg = true;
                        reject(error); // Reject the promise if an error occurs
                    });
            });
        },
        classIsComplete() {
            // checks if class is already full
            return new Promise((resolve, reject) => {
                CLIENT.get(`/registrations/getBySpecificClass/${JSON.parse(JSON.stringify(this.selectedClass))[0].id}`)
                    .then(response => {
                        if (response.data.length >= 30) {
                            this.classIsFull = true;
                        } else {
                            this.classIsFull = false;
                        }
                        resolve(); // Resolve the promise once the logic is complete
                    })
                    .catch(error => {
                        console.error('Error classIsComplete:', error);
                        this.classIsFull = true;
                        reject(error); // Reject the promise if an error occurs
                    });
            });
        },

        checkForPaymentMethod() {
            // checks if client has a payment method
            return new Promise((resolve, reject) => {
                // sends GET request to get payment methods
                CLIENT.get(`clients/hasPaymentMethod/${globalState.accountEmail}`)
                    .then(response => {
                        console.log("paymentresponse", response.data.hasPaymentMethod);
                        this.hasPaymentMethod = response.data.hasPaymentMethod;
                        console.log("i am here_else", this.hasPaymentMethod);
                        resolve(); // Resolve the promise once the logic is complete
                    })
                    .catch(error => {
                        console.error("checkForPaymentMethod", error);
                        this.hasPaymentMethod = false;
                        reject(error); // Reject the promise if an error occurs
                    });
            });
        },
        handleOk() {
            window.location.reload();
        },
        handleShowSelected() {
            this.showSelected = true;
            this.registrationError = null;
            this.registrationOK = null;
        },
        handleModalClose() {
            this.registrationOK = null;
            this.registrationError = null;
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
        tabIsAllAvailable(tab) {
            this.option = 'all-available';
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
    max-width: 1000px;
    /* Adjust as needed */
    width: 80%;
    /* Adjust as needed */
    max-height: 1000px;
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
