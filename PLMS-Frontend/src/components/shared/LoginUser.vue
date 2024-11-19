<template>
  <div class="login">
    <nav class="site-header sticky-top py-1">
    <div class="container d-flex flex-column flex-md-row justify-content-between">
      <a class="py-2">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-box" viewBox="0 0 16 16">
          <path d="M8.186 1.113a.5.5 0 0 0-.372 0L1.846 3.5 8 5.961 14.154 3.5 8.186 1.113zM15 4.239l-6.5 2.6v7.922l6.5-2.6V4.24zM7.5 14.762V6.838L1 4.239v7.923l6.5 2.6zM7.443.184a1.5 1.5 0 0 1 1.114 0l7.129 2.852A.5.5 0 0 1 16 3.5v8.662a1 1 0 0 1-.629.928l-7.185 2.874a.5.5 0 0 1-.372 0L.63 13.09a1 1 0 0 1-.63-.928V3.5a.5.5 0 0 1 .314-.464L7.443.184z"/>
        </svg>      </a>
      <a class="py-2 d-none d-md-inline-block" @click="Home">Return to Home Page</a>
    </div>
  </nav>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <img class="img-fluid mt-5 mb-3"  width="30%" src="@/assets/logo-png.png">

    <form style="margin-top: 2%">
      <!-- Email input -->
      <div class="form-outline mb-4">
        <label class="form-label" for="email">Email</label>
        <input v-model="email" type="email" id="email" class="form-control" style="width: 50%; margin-left: 25%" placeholder="john.doe@address.com"/>

      </div>


      <!-- Password input -->
      <div class="form-outline mb-4">
        <label class="form-label" for="password">Password</label>
        <input v-model="password" type="password" id="password" class="form-control"  style="width: 50%; margin-left: 25%" placeholder="*********"/>
      </div>

      <div class="form-check form-check-inline">
        <input v-model="user" class="form-check-input" type="radio" name="user" id="owner" value="Owner">
        <label class="form-check-label" for="owner">Owner</label>
      </div>

      <div class="form-check form-check-inline">
        <input v-model="user" class="form-check-input" type="radio" name="user" id="customer" value="Customer">
        <label class="form-check-label" for="customer">Customer</label>
      </div>
      <div class="form-check form-check-inline">
        <input v-model="user" class="form-check-input" type="radio" name="user" id="employee" value="Employee">
        <label class="form-check-label" for="employee">Employee</label>
      </div>
    </form>


    <!-- Submit button -->
    <button v-bind:disabled="createUserButtonDisabled" style="width: 50%; margin-top: 1%; margin-left: 25%" @click="getUser()" type="button" class="btn btn-primary btn-block mb-4">Sign in</button>

    <div class="text-center">
      <p>Don't have an account? </p>
    </div>
    <button style="background-color:lightskyblue; width: 20%; margin-top: 1%; margin-left: 40%;"  type="button" class="btn btn-primary btn-block mb-4" @click="SignUp">Sign up</button>
    <a @click="SignUp">
      <button style="background-color:lightskyblue; width: 20%; margin-top: 1%; margin-left: 40%;"  type="button" class="btn btn-primary btn-block mb-4" @click="Guest">Continue as Guest</button>
    </a>

  </div>
</template>


<script>
import axios from 'axios';
const config = require('../../../config');
const frontendUrl = config.dev.host + ':' + config.dev.port;
const axiosClient = axios.create({
  // Note the baseURL, not baseUrl
  baseURL: config.dev.backendBaseUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
});

export default {
  name: 'LoginUser',
  data() {
    return {
      email: '',
      password: '',
      user: '',
      errorMsg: '',
      logged_user: []
    };
  },
  methods: {
    getUser() {
      //const request =  {email: this.email, password: this.password, user: this.user};
      //axiosClient("/login/", request) previously
      axiosClient.get("/login/"+this.user+"?email="+this.email+"&password="+this.password)
        .then((response) => {
          alert("Success now logged in as " + this.user);
          this.logged_user = response;
          if (this.user === "Customer"){
            this.$router.push({name: 'MonthlyCustomerHome', params: {email: this.email}})
          }
          if (this.user === "Owner"){
            this.$router.push({name: 'OwnerHome'})
          }
          if (this.user === "Employee"){
            this.$router.push({name: 'EmployeeHome', params: {email: this.email}})
          }
        })
        .catch((err) => {
          this.errorMsg = `Error: ${err.response.data}`;
          alert(this.errorMsg)
        })
    },
    async SignUp() {
      await this.$router.push({name: 'SignUp'})
    },
    async Guest() {
      await this.$router.push({name: 'GeneralCreateGuestPass'})
    },
    async Home(){
      await this.$router.push({name: 'Home'})
    }
  },
  computed: {
    createUserButtonDisabled() {
      return !this.email.trim() || !this.password.trim() || !this.user.trim()
    }
  }
}
</script>

<style>


</style>
