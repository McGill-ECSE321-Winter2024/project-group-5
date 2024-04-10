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
            <b-container>
                <h2 class="tableTitle">Classes Taught By Instructor</h2>
                <div v-if="specificClasses.length > 0">
                    <div v-for="specificClass in specificClasses" :key="specificClass.id">
                        <p><strong>Date:</strong> {{ specificClass.date }}</p>
                        <p><strong>Start Time:</strong> {{ specificClass.startTime }}</p>
                        <p><strong>End Time:</strong> {{ specificClass.endTime }}</p>
                        <!-- Add more details as needed -->
                    </div>
                </div>
                <div v-else>
                    <p>No classes found for this instructor.</p>
                </div>
            </b-container>
        </div>

    </div>
</template>

<script>
import axios from "axios";
import { globalState } from "@/global.js"; // Import the globalState variable

const CLIENT = axios.create({
    baseURL: 'http://localhost:8080'
});

export default {
    name: 'AccountPageInstructor',
    data() {
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
            specificClasses: []
        };
    },
    computed: {
        unapprovedClassTypes() {
            return this.classTypes.filter(classType => !classType.approved);
        }
    },
    mounted() {
        this.fetchAccountDetails();
        this.fetchClassesByInstructor();
    },
    methods: {
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
