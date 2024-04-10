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
                <!-- Input fields for adding a new payment method -->
                <div class="mb-3">
                    <label for="cardNumber">Card Number:</label></br>
                    <input type="text" id="cardNumber" v-model="newCardNumber"></br>
                </div>
                <div class="mb-3">
                    <label for="expDate">Expiry Date (YYYY-MM-DD):</label></br>
                    <input type="text" id="expDate" v-model="newExpDate"></br>
                </div>
                <div class="mb-3">
                    <label for="cvc">CVC:</label></br>
                    <input type="text" id="cvc" v-model="newCvc"></br>
                </div>
                <div class="mb-3">
                    <label for="cardHolderName">Cardholder Name:</label></br>
                    <input type="text" id="cardHolderName" v-model="newCardHolderName"></br>
                </div>
                <div class="mb-3">
                    <b-button variant="success" @click="addPaymentMethod">Add Payment Method</b-button></br>
                </div>
                <!-- End of input fields -->
                <div v-if="paymentMethods.length > 0">
                    <b-card v-for="method in paymentMethods" :key="method.cardNumber">
                        <p><strong>Card Number:</strong> {{ method.cardNumber }}</p>
                        <!-- Add more fields of payment method as required -->
                        <div>
                            <b-button @click="deletePaymentMethod(method.cardNumber)" variant="danger">Delete</b-button>
                        </div>
                    </b-card>
                </div>
                <div v-else>
                    <p>No payment methods found.</p>
                </div>
            </b-container>
        </div>


        <!-- Fourth column: Class Types -->
        <div class="column">
            <!-- Class Types -->
            <b-container>
                <h2 class="tableTitle">Class Types</h2>
                <!-- Display class types and approve button -->
                <div v-if="unapprovedClassTypes.length > 0">
                    <b-card v-for="classType in unapprovedClassTypes" :key="classType.typeId">
                        <p><strong>Name:</strong> {{ classType.name }}</p>
                        <p><strong>Description:</strong> {{ classType.description }}</p>
                        <div>
                            <b-button @click="approveClassType(classType.typeId)" variant="success">Approve</b-button>
                        </div>
                    </b-card>
                </div>
                <div v-else>
                    <p>No unapproved class types found.</p>
                </div>
            </b-container>
        </div>

        <!-- Fourth column: Specific Classes By Instructor -->
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
        this.fetchRegistrations();
        this.fetchPaymentMethods();
        this.fetchClassTypes();
        this.fetchClassesByInstructor();
    },
    methods: {
        fetchClassesByInstructor() {
            // Make a HTTP GET request to fetch specific classes by instructor ID
            axios.get(`/specificClass/client/${this.accountId}`)
                .then(response => {
                    // Update your component's data with the fetched specific classes
                    this.specificClasses = response.data.specificClasses;
                })
                .catch(error => {
                    console.error('Error fetching specific classes by instructor:', error);
                });
        },
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
                })
                .catch(error => {
                    console.error('Error approving class type:', error);
                });
        },
        fetchAccountDetails() {
            CLIENT.get(`/clients/getById/${this.accountId}`)
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
            CLIENT.get(`/paymentMethod/getByClient/${this.accountId}`)

                .then(response => {
                    this.paymentMethods = response.data.paymentMethods;
                })
                .catch(error => {
                    console.error('Error fetching payment methods:', error);
                });
        },
        deletePaymentMethod(cardNumber) {
            CLIENT.delete(`/paymentMethod/deleteByCardNumber/${cardNumber}`)
                .then(() => {
                    // Remove the deleted payment method from the list
                    this.paymentMethods = this.paymentMethods.filter(method => method.cardNumber !== cardNumber);
                })
                .catch(error => {
                    console.error('Error deleting payment method:', error);
                });
        },
        addPaymentMethod() {
            // Collect payment method details from input fields
            const cardNumber = this.newCardNumber;
            const expDate = this.newExpDate;
            const cvc = this.newCvc;
            const cardHolderName = this.newCardHolderName;

            // You might need to fetch the client ID from somewhere in your application
            const clientId = this.accountId; // Replace this with the actual client ID

            // Prepare the payment method object
            const paymentMethod = {
                cardNumber: cardNumber,
                expDate: expDate,
                cvc: cvc,
                cardHolderName: cardHolderName,
                clientId: clientId // Assuming client ID is required and you have it available
            };

            // Send the payment method object to the backend
            CLIENT.post('/paymentMethod/create', paymentMethod)
                .then(response => {
                    // Handle success response if needed
                    // For example, you might want to refresh the payment methods list
                    this.fetchPaymentMethods();
                })
                .catch(error => {
                    console.error('Error adding payment method:', error);
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
