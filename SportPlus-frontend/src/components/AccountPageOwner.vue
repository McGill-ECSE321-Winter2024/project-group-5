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

        <!-- Second column: Class Types -->
        <div class="column">
            <!-- Class Types -->
            <b-container>
                <h2 class="tableTitle">Need Approval</h2>
                <!-- Display class types and approve button -->
                <div v-if="unapprovedClassTypes.length > 0">
                    <b-card v-for="classType in unapprovedClassTypes" :key="classType.typeId">
                        <p><strong>Name:</strong> {{ classType.name }}</p>
                        <p><strong>Description:</strong> {{ classType.description }}</p>
                        <div>
                            <b-button @click="approveClassType(classType.typeId)" variant="success">Approve</b-button>
                            <b-button @click="deleteClassType(classType.name)" variant="success">Disapprove</b-button>
                        </div>
                    </b-card>
                </div>
                <div v-else>
                    <p>No unapproved class types found.</p>
                </div>
            </b-container>
        </div>
        <!-- Third column: Class Types -->
        <div class="column">
            <!-- Class Types -->
            <b-container>
                <h2 class="tableTitle">Approved</h2>
                <!-- Display class types and delete button -->
                <div v-if="classTypes.length > 0">
                    <b-card v-for="classType in approvedClassTypes" :key="classType.typeId">
                        <p><strong>Name:</strong> {{ classType.name }}</p>
                        <p><strong>Description:</strong> {{ classType.description }}</p>
                        <div>
                            <b-button @click="deleteClassType(classType.name)" variant="success">Delete</b-button>
                        </div>
                    </b-card>
                </div>
                <div v-else>
                    <p>No class types found</p>
                </div>
            </b-container>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import { globalState } from "@/global.js"; // Import the globalState variable
import config from "../../config";

// sets up urls for axios
const backendUrl = 'http://' + config.dev.backendHost + ':' + config.dev.backendPort
const frontendUrl = 'http://' + config.dev.host + ':' + config.dev.port

const CLIENT = axios.create({
    baseURL: backendUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }

});

export default {
    name: 'AccountPageOwner',

    // runs before loading the page
    beforeRouteEnter(to, from, next) {
        const userType = globalState.type;

        // if wrong page is loaded, loads and enforces correct account page
        if (userType === 'Owner') {
            next();
        } else if (userType === 'Client') {
            next(vm => {
                vm.$router.replace('/client-account');
            });
        } else if (userType === 'Instructor') {
            next(vm => {
                vm.$router.replace('/instructor-account');
            });
        } else {
            next(vm => {
                vm.$router.replace('/not-logged-in-account');
            });
        }
    },
    data() {
        return {
            // variables to store data for vue page
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
            password: '',
            hidePassword: true,
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
        };
    },
    computed: {
        // automatically change upon data changes
        unapprovedClassTypes() {
            return this.classTypes.filter(classType => !classType.approved);
        },
        approvedClassTypes() {
            return this.classTypes.filter(classType => classType.approved);
        },
    },
    mounted() {
        // load upon page mounting
        this.fetchAccountDetails();
        this.fetchClassTypes();
        this.fetchSpecificClasses();
    },
    methods: {
        // fetches all classes to load onto the schedule page
        fetchClassTypes() {
            CLIENT.get('/classType/all')
                .then(response => {
                    this.classTypes = response.data.classTypes;
                })
                .catch(error => {
                    console.error('Error fetching class types:', error);
                });
        },

        // Method to approve a class type
        approveClassType(typeId) {
            CLIENT.post(`/classType/approve/${typeId}`)
                .then(response => {
                    // Optionally, update the UI to reflect the approved class type
                    // For example, remove it from the list
                    this.classTypes = this.classTypes.filter(classType => classType.typeId !== typeId);
                    window.location.reload();
                })
                .catch(error => {
                    console.error('Error approving class type:', error);
                });
        },

        //Method to delete class type
        deleteClassType(className) {
            try {
                // Check if there are specific classes associated with this class type
                const hasSpecificClasses = this.specificClasses.some(specificClass => specificClass.classType.name === className);

                // If there are specific classes associated with this class type
                if (hasSpecificClasses) {
                    // Check if any of the specific classes have a date that has already passed
                    const hasPassedDate = this.specificClasses.some(specificClass => {
                        return specificClass.classType.name === className && new Date(specificClass.date) < new Date();
                    });

                    // If any specific class associated with this class type has a date that has passed
                    if (hasPassedDate) {
                        // Delete the class type
                        const path = 'classType/delete/' + className;
                        CLIENT.delete(path);
                        this.fetchClassTypes();
                        window.location.reload();
                    }

                } else {
                    const path = 'classType/delete/' + className;
                    CLIENT.delete(path)
                        .then(() => {
                            this.fetchClassTypes();
                            window.location.reload();
                        })
                        .catch(error => {
                            console.error('Error deleting class type:', error);
                            alert("Cannot delete class type. Some specific classes associated with this class type are currently in use.");
                        });
                }
            } catch (error) {
                console.error('There was an error deleting the class type:', error);
            }
        },

        fetchAccountDetails() {
            // gets details of owner's account using GET request
            CLIENT.get(`/owner/get`)
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
            CLIENT.put(`/owner/updateFirstName/${this.newFirstName}`)
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
            CLIENT.put(`/owner/updateLastName/${this.newLastName}`)
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
            CLIENT.put(`/owner/updatePassword/${this.oldPassword}/${this.newPassword}`)
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
