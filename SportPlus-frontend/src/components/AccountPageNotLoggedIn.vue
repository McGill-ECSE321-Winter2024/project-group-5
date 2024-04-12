<template>
    <div>
        <p>You are not logged in.</p>
        <router-link to="/Login">Go to Login Page</router-link>
        <router-link to="/SchedulePage">View the schedule as a visitor</router-link>
    </div>
</template>

<script>
export default {
    name: 'AccountPageNotLoggedIn',

    // before loading page, if user is already logged-in, route to correct page
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
