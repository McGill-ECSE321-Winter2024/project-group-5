<template>
    <div>
        <p>You are not logged in.</p>
        <!-- Optionally, you can provide a button to navigate to the login page -->
        <router-link to="/">Go to Login Page</router-link>
    </div>
</template>

<script>
export default {
    name: 'AccountPageNotLoggedIn',
    beforeRouteEnter(to, from, next) {
        const userType = globalState.type;
        if (userType === 'Owner') {
            next(vm => {
                vm.$router.replace('/owner-account');
            });
        } else if (userType === 'Client') {
            next(vm => {
                vm.$router.replace('/client-account');
            });
        } else if (userType === 'Instructor') {
            next(vm => {
                vm.$router.replace('/instructor-account');
            });
        } else {
            next();
        }
    },
};
</script>
