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

        <!-- Second column: Registration -->
        <div class="column">
            <b-container>
                <h2 class="tableTitle">Registration</h2>
                <div class="ScheduleTable">
                    <b-table hover :items="registrations" :fields="registrationFields" :sticky-header="true"
                        :outlined="true" :row-variant="rowVariant" select-mode="single" responsive="sm"
                        ref="registrationTable" selectable @row-selected="onRowSelected"></b-table>
                </div>
            </b-container>
        </div>

        <!-- Third column: Payment Method -->
        <div class="column">
            <b-container>
                <h2 class="tableTitle">Payment Method</h2>
                <div class="ScheduleTable">
                    <b-table hover :items="paymentMethods" :fields="paymentMethodFields" :sticky-header="true"
                        :outlined="true" :row-variant="rowVariant" select-mode="single" responsive="sm"
                        ref="paymentMethodTable" selectable @row-selected="onRowSelected"></b-table>
                </div>
                <div class="mb-3">
                    <b-button variant="success" @click="addPaymentMethod">Add Payment Method</b-button>
                </div>
            </b-container>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import globals from "@/globals.js"; // Import the globals.js file
import { BIcon } from 'bootstrap-vue';

const CLIENT = axios.create({
    baseURL: 'http://localhost:8080'
});

export default {
    name: 'AccountPage',
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
            paymentMethodFields: [],
            selectedItem: null
        };
    },
    mounted() {
        this.fetchAccountDetails(globals.accountId);
        this.fetchRegistrations();
        this.fetchPaymentMethods();
    },
    methods: {
        fetchAccountDetails(accountId) {
            CLIENT.get(`/clients/getById/${accountId}`)
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
        fetchRegistrations() {
            CLIENT.get('/registrations')
                .then(response => {
                    this.registrations = response.data;
                })
                .catch(error => {
                    console.error('Error fetching registrations:', error);
                });
        },
        fetchPaymentMethods() {
            CLIENT.get('/payment/methods')
                .then(response => {
                    this.paymentMethods = response.data;
                })
                .catch(error => {
                    console.error('Error fetching payment methods:', error);
                });
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
            CLIENT.put(`/clients/updateFirstName/${this.email}/${this.newFirstName}`)
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
            CLIENT.put(`/clients/updateLastName/${this.email}/${this.newLastName}`)
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
            CLIENT.put(`/clients/updatePassword/${this.email}/${this.oldPassword}/${this.newPassword}`)
                .then(response => {
                    this.isEditingPassword = false;
                    // Optionally, you may clear the input fields for security
                    this.oldPassword = '';
                    this.newPassword = '';
                })
                .catch(error => {
                    console.error('Error updating password:', error);
                });
        },
        addPaymentMethod() {
            // Functionality to add a new payment method
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
