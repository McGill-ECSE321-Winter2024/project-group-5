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
                <h2 class="tableTitle">My Schedule</h2>
                <div class="ScheduleTable">
                    <b-table hover 
                        id="schedule-tb" 
                        small 
                        :items="clientClasses" 
                        :fields="filteredFields" 
                        :sticky-header="true"
                        :outlined="true" 
                        select-mode="single" 
                        responsive="sm" 
                        ref="selectableTable" 
                        selectable
                        @row-selected="onClassSelected">
                        <template v-slot:cell(startTime)="data">
                            <b-table-simple :class="{ 'bold-row-separator': isDateSeparator(data.item) }">
                                {{ isDateSeparator(data.item) ? data.item.dateSeparator : (data.value) }}
                            </b-table-simple>
                        </template>
                        <template v-slot:cell(unregister)="data">
                            <b-button class="unreg-button" v-if="!isDateSeparator(data.item)" @click="unregisterSpecificClass(data.item.regId)" variant="danger">Unregister</b-button>
                        </template>
                    </b-table>
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
                    <input type="text" id="cardNumber" v-model="newCardNumber"
                        @input="restrictNumericInput($event, 'cardNumber')"></br>
                </div>
                <div class="mb-3">
                    <label for="expDate">Expiry Date (YYYY-MM-DD):</label></br>
                    <input type="text" id="expDate" v-model="newExpDate"></br>
                </div>
                <div class="mb-3">
                    <label for="cvc">CVC:</label></br>
                    <input type="text" id="cvc" v-model="newCvc" @input="restrictNumericInput($event, 'cvc')"></br>
                </div>
                <div class="mb-3">
                    <label for="cardHolderName">Cardholder Name:</label></br>
                    <input type="text" id="cardHolderName" v-model="newCardHolderName"
                        @input="validateCardHolderName"></br>
                </div>
                <div class="mb-3">
                    <b-button variant="success" @click="addPaymentMethod">Add Payment Method</b-button></br>
                </div>
                <!-- End of input fields -->
                <div v-if="paymentMethods.length > 0">
                    <b-card v-for="method in paymentMethods" :key="method.cardNumber">
                        <p><strong>Card Holder Name:</strong> {{ method.cardHolderName }}</p>
                        <p><strong>Card Number:</strong> {{ maskedCardNumber(method.cardNumber) }}</p>
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
    </div>
</template>

<script>
import axios from "axios";
import { globalState } from "@/global.js"; // Import the globalState variable

const CLIENT = axios.create({
    baseURL: 'http://localhost:8080'
});

export default {
    name: 'AccountPageClient',
    beforeRouteEnter(to, from, next) {
        const userType = globalState.type;
        if (userType === 'Client') {
            next();
        } else if (userType === 'Instructor') {
            next(vm => {
                vm.$router.replace('/instructor-account');
            });
        } else if (userType === 'Owner') {
            next(vm => {
                vm.$router.replace('/owner-account');
            });
        } else {
            next(vm => {
                vm.$router.replace('/not-logged-in-account');
            });
        }
    },
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
            hidePassword: true, // Add hidePassword variable
            oldPasswordFieldType: 'password',
            newPasswordFieldType: 'password',
            registrations: [],
            clientClasses:[],
            classes: [],
            fields_C: [
                { key: 'date', label: 'Date', show: false },
                { key: 'startTime', label: 'Start Time', show: true },
                { key: 'classType', label: 'Class Type', show: true },
                { key: 'supervisor', label: 'Instructor', show: false },
                { key: 'duration', label: 'Duration', show: true },
                { key: 'description', show: false },
                { key: 'id', show: false },
                { key: 'unregister', label: '', class: 'text-center', show: true },
                { key: 'regId', show: false}
            ],
            paymentMethods: [],
            paymentMethodFields: [
                // Define fields for payment methods table
                { key: 'cardNumber', label: 'Card Number' }
            ],
            selectedItem: null,
            accountId: globalState.accountId,
            accountEmail: globalState.accountEmail,
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
        },
        filteredFields(){
            return this.fields_C.filter(field => field.show);
        }
    },
    mounted() {
        this.fetchAccountDetails();
        this.fetchRegistrations();
        this.fetchPaymentMethods();
    },
    methods: {
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
            CLIENT.get(`/registrations/getByClient/${this.accountEmail}`)
                .then(response => {
                    console.log("raw response",response);
                    this.registrations = response.data.registrations;
                    this.registrations.forEach(reg =>{
    
                    const spe_class = reg.specificClass;
                    this.classes.push({
                        startTime: spe_class.startTime,
                        date: spe_class.date,
                        supervisor: spe_class.instructor ? `${spe_class.instructor.lastName}, ${spe_class.instructor.firstName}` : '',
                        classType: spe_class.classType.name,
                        duration: '60 min',
                        description: spe_class.classType.description,
                        id: spe_class.id,
                        regId: reg.regId
                    });
                    });
                    console.log("classes in fetchReg", this.classes);
                    console.log("clientClasses in fetchReg", this.clientClasses);
                    const sortedClasses = this.classes.sort((a, b) => {
                    // Compare dates
                    if (a.date < b.date) return -1;
                    if (a.date > b.date) return 1;

                    // If dates are equal, compare start times
                    if (a.startTime < b.startTime) return -1;
                    if (a.startTime > b.startTime) return 1;

                    // If both dates and start times are equal, maintain current order
                    return 0;
                    });
                    console.log("sorted classes", sortedClasses);
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
                    name: item.name
                });
                
                });
                    console.log("formattedClasses", formattedClasses);
                    // Assign the formatted classes
                    this.clientClasses = formattedClasses;
                    console.log("clientClasses", this.clientClasses)
                })
                .catch(error => {
                    console.error('Error fetching registrations:', error);
                });
        },
        fetchMyClasses(){
            this.fetchRegistrations().then(()=>{
            const sortedClasses = this.classes.sort((a, b) => {
                // Compare dates
                if (a.date < b.date) return -1;
                if (a.date > b.date) return 1;

                // If dates are equal, compare start times
                if (a.startTime < b.startTime) return -1;
                if (a.startTime > b.startTime) return 1;

                // If both dates and start times are equal, maintain current order
                return 0;
            });
            console.log("sorted classes", sortedClasses);
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
                    name: item.name
                });
                
            });
                console.log("formattedClasses", formattedClasses);
                // Assign the formatted classes
                this.clientClasses = formattedClasses;
                console.log("clientClasses", this.clientClasses)
            })
        },
        unregisterSpecificClass(regId){
            CLIENT.delete(`/registrations/deleteByRegistrationId/${regId}`).then(response =>{
                console.log("success", response);
                this.fetchRegistrations();
            }).catch(error => {
                // Handle error: Log and/or display an error message
                console.error('Error deleting class type:', error);
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

        restrictNumericInput(event, fieldName) {
            // Prevent non-numeric characters from being entered
            event.target.value = event.target.value.replace(/\D/g, '');
            // Update the data model with the sanitized value
            this[fieldName] = event.target.value;
        },

        validateCardHolderName() {
            // Get the input value
            const name = this.newCardHolderName;
            // Validate if the name contains any numbers
            if (/\d/.test(name)) {
                alert("Cardholder name should not contain numbers.");
                // Remove the numbers from the input value
                this.newCardHolderName = name.replace(/\d/g, "");
            }
        },

        maskedCardNumber(cardNumber) {
            const visibleDigits = cardNumber.slice(-4); // Get the last 4 digits of the card number
            const maskedDigits = 'X'.repeat(cardNumber.length - 4); // Replace the remaining digits with 'X'
            return maskedDigits + visibleDigits; // Concatenate the masked digits with the visible ones
        },

        addPaymentMethod() {
            // Collect payment method details from input fields
            const cardNumber = this.newCardNumber;
            const expDate = this.newExpDate;
            const cvc = this.newCvc;
            const cardHolderName = this.newCardHolderName;

            const expDateRegex = /^\d{4}-\d{2}-\d{2}$/;
            if (!expDate.match(expDateRegex)) {
                alert("Expiry date must be in YYYY-MM-DD format.");
                return; // Stop execution if the format is incorrect
            }

            const currentDate = new Date();
            const expiryDate = new Date(expDate);
            if (expiryDate <= currentDate) {
                alert("The payment method you are inserting has already expired.");
                return; // Stop execution if the expiry date is not in the future
            }

            // Validate the card number
            if (!/^\d+$/.test(cardNumber)) {
                alert("Card number should contain only numbers.");
                return; // Stop execution if the card number contains non-numeric characters
            }

            // Validate the cvc
            if (!/^\d+$/.test(cvc)) {
                alert("CVC should contain only numbers.");
                return; // Stop execution if the cvc contains non-numeric characters
            }

            if (/\d/.test(cardHolderName)) {
                alert("Cardholder name should not contain numbers.");
                return; // Stop execution if the cardholder name contains numbers
            }


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
        onClassSelected(item) {
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
        },
        isDateSeparator(item) {
            // Check if the item is a separator row
            return item.dateSeparator !== undefined;
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
.bold-row-separator {
    font-weight: bold;
    font-size: 15px;
    text-align: left;
}

/* Ensure responsiveness for small screens */
@media (max-width: 800px) {
    .columns-container {
        flex-wrap: wrap;
    }

    .column {
        flex-basis: 100%;
    }
}
</style>
