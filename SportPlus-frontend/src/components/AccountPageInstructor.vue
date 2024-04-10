<template>
    <div class="columns-container">
        <!-- First column: Account Details -->
        <div class="column">
            <b-container>
                <h2 class="tableTitle">Account Details</h2>
                <div>
                    <p><strong>Email:</strong> {{ email }}</p>
                    <!-- First Name Editing -->
                    <p><strong>First Name:</strong>
                        <span v-if="!isEditingFirstName">{{ firstName }}</span>
                        <span v-else>
                            <b-form-input v-model="newFirstName" placeholder="Enter new first name"
                                @keyup.enter="saveFirstName"></b-form-input>
                            <b-button @click="saveFirstName" variant="success">Save</b-button>
                        </span>
                    </p>
                    <div class="mb-3">
                        <b-button v-if="!isEditingFirstName" @click="editFirstName" variant="warning">Modify First
                            Name</b-button>
                    </div>
                    <!-- Last Name Editing -->
                    <p><strong>Last Name:</strong>
                        <span v-if="!isEditingLastName">{{ lastName }}</span>
                        <span v-else>
                            <b-form-input v-model="newLastName" placeholder="Enter new last name"
                                @keyup.enter="saveLastName"></b-form-input>
                            <b-button @click="saveLastName" variant="success">Save</b-button>
                        </span>
                    </p>
                    <div class="mb-3">
                        <b-button v-if="!isEditingLastName" @click="editLastName" variant="warning">Modify Last
                            Name</b-button>
                    </div>
                    <!-- Password Editing -->
                    <p><strong>Password:</strong>
                        <span v-if="!isEditingPassword">
                            <span>********</span>
                        </span>
                        <span v-else>
                            <b-form-input v-model="oldPassword" :type="oldPasswordFieldType" placeholder="Old password">
                                <img src="@/assets/close-eye-icon.png" @mousedown="togglePasswordVisibility"
                                    style="cursor: pointer; width: 16px; height: 16px;" v-if="isEditingPassword" />
                            </b-form-input>
                            <b-form-input v-model="newPassword" :type="newPasswordFieldType" placeholder="New password"
                                @keyup.enter="savePassword">
                                <img src="@/assets/close-eye-icon.png" @mousedown="togglePasswordVisibility"
                                    style="cursor: pointer; width: 16px; height: 16px;" v-if="isEditingPassword" />
                            </b-form-input>
                            <img src="@/assets/open-eye-icon.png" @mousedown="togglePasswordVisibility"
                                style="cursor: pointer; width: 16px; height: 16px;"
                                v-if="isEditingPassword && !hidePassword" />
                            <img src="@/assets/close-eye-icon.png" @mousedown="togglePasswordVisibility"
                                style="cursor: pointer; width: 16px; height: 16px;"
                                v-if="isEditingPassword && hidePassword" />
                            <b-button @click="savePassword" variant="success">Save</b-button>
                        </span>
                    </p>
                    <div class="mb-3">
                        <b-button v-if="!isEditingPassword" @click="editPassword" variant="warning">Change
                            Password</b-button>
                    </div>
                </div>
            </b-container>
        </div>

        <!-- Second column: Specific Classes By Instructor -->
        <div class="column">
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
                            <b-card style="width:100%; height: 200px">
                                <b-row class="2">
                                    <b-col>
                                        <label for="datepicker-start">Start Date</label>
                                        <b-form-datepicker id="datepicker-start" today-button reset-button close-button
                                            :min="min" v-model="startDate" locale="en"
                                            placeholder="Pick start date"></b-form-datepicker>
                                        <div v-if="!startDate === 'filter-by-dates'" class="error-message"
                                            style="color: red; text-decoration: underline;">Please select a start
                                            date.
                                        </div>
                                    </b-col>
                                    <b-col>
                                        <label for="datepicker-end">End Date</label>
                                        <b-form-datepicker id="datepicker-end" today-button reset-button close-button
                                            :min="startDate ? startDate : min" v-model="endDate" locale="en"
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
                                    height: 200px">
                                <b-table hover small :items="instructors" :fields="filteredInstructors" :outlined="true"
                                    select-mode="single" responsive="sm" ref="selectableTable" selectable
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
                                        height: 200px">
                                <b-table hover small :items="types" :fields="filteredClassTypes" :outlined="true"
                                    select-mode="single" responsive="sm" ref="selectableTable" selectable
                                    @row-selected="onTypeSelected">
                                </b-table>
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
        </div>

    </div>
</template>

<script>
import axios from "axios";
import { globalState } from "@/global.js"; // Import the globalState variable
import config from "../../config";

const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port

const CLIENT = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }

});

export default {
    name: 'AccountPageInstructor',

    data() {
        const now = new Date();
        const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());

        return {
            firstName: '',
            lastName: '',
            email: '',
            isEditingFirstName: false,
            isEditingLastName: false,
            isEditingPassword: false,
            newFirstName: '',
            newLastName: '',
            oldPassword: '',
            newPassword: '',
            password: '', // Add password variable
            hidePassword: true, // Add hidePassword variable
            oldPasswordFieldType: 'password',
            newPasswordFieldType: 'password',
            registrations: [],
            registrationFields: [],
            paymentMethods: [],
            paymentMethodFields: [
                // Define fields for payment methods table
                { key: 'cardNumber', label: 'Card Number' }
            ],
            selectedItem: null,
            accountId: globalState.accountId,
            newCardNumber: '',
            newExpDate: '',
            newCvc: '',
            newCardHolderName: '',
            classTypes: [],
            specificClasses: [],
            min: today,
            displayError_I: false,
            displayError_T: false,
            teachError: null,
            teachOK: null,
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
        unapprovedClassTypes() {
            return this.classTypes.filter(classType => !classType.approved);
        },
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
        this.fetchAccountDetails();
        this.fetchClassesByInstructor();
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
                    endpoint = `/specificClass/all`;
                    break;
            }
            console.log('Constructed endpoint :', endpoint);
            return endpoint;
        },

        fetchData() {
            const endpoint = this.fetchEndpoint(this.option);
            CLIENT.get(endpoint)
                .then(response => {
                    console.log('Response:', response.data);
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
        },
        fetchClassesByInstructor() {
            // Make a HTTP GET request to fetch specific classes by instructor ID
            axios.get(`/specificClass/instructor/${this.accountId}`)
                .then(response => {
                    // Update your component's data with the fetched specific classes
                    this.specificClasses = response.data.specificClasses;
                })
                .catch(error => {
                    console.error('Error fetching specific classes by instructor:', error);
                });
        },
        fetchAccountDetails() {
            CLIENT.get(`/instructors/getById/${this.accountId}`)
                .then(response => {
                    const { firstName, lastName, email } = response.data;
                    this.firstName = firstName;
                    this.lastName = lastName;
                    this.email = email;
                })
                .catch(error => {
                    console.error('Error fetching account details:', error);
                });
        },
        onRowSelected(item) {
            // Handle row selection if needed
        },
        rowVariant(index) {
            // Define row variant if needed
        },
        editFirstName() {
            this.isEditingFirstName = true;
            this.newFirstName = this.firstName;
        },
        editLastName() {
            this.isEditingLastName = true;
            this.newLastName = this.lastName;
        },
        editPassword() {
            this.isEditingPassword = true;
        },
        togglePasswordVisibility() {
            this.hidePassword = !this.hidePassword;
            this.oldPasswordFieldType = this.hidePassword ? 'password' : 'text';
            this.newPasswordFieldType = this.hidePassword ? 'password' : 'text';
        },
        saveFirstName() {
            // Make API call to update first name
            CLIENT.put(`/instructors/updateFirstName/${this.email}/${this.newFirstName}`)
                .then(response => {
                    this.firstName = this.newFirstName;
                    this.isEditingFirstName = false;
                })
                .catch(error => {
                    console.error('Error updating first name:', error);
                });
        },
        saveLastName() {
            // Make API call to update last name
            CLIENT.put(`/instructors/updateLastName/${this.email}/${this.newLastName}`)
                .then(response => {
                    this.lastName = this.newLastName;
                    this.isEditingLastName = false;
                })
                .catch(error => {
                    console.error('Error updating last name:', error);
                });
        },
        savePassword() {
            // Make API call to update password
            CLIENT.put(`/instructors/updatePassword/${this.email}/${this.oldPassword}/${this.newPassword}`)
                .then(response => {
                    this.isEditingPassword = false;
                    // Optionally, you may clear the input fields for security
                    this.oldPassword = '';
                    this.newPassword = '';
                })
                .catch(error => {
                    console.error('Error updating password:', error);
                });
        }
    }
}
</script>

<style scoped>
.tableTitle {
    text-align: center;
}

.columns-container {
    display: flex;
    justify-content: space-between;
}

.column {
    flex-basis: 30%;
    padding: 10px;
}

/* Ensure responsiveness for small screens */
@media (max-width: 768px) {
    .columns-container {
        flex-wrap: wrap;
    }

    .column {
        flex-basis: 100%;
    }
}
</style>
