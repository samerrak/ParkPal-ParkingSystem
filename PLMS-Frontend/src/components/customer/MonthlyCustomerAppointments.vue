<template>
  <div class="customerpage">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="src/assets/logo-transparent-png.png">
    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/product/">
    <link href="bootstrap-4.0.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="bootstrap-4.0.0/docs/4.0/examples/product/product.css" rel="stylesheet">

    <nav class="site-header sticky-top py-1">
      <div class="container d-flex flex-column flex-md-row justify-content-between">
        <a class="py-2" @click="RouteHome">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-box" viewBox="0 0 16 16">
            <path d="M8.186 1.113a.5.5 0 0 0-.372 0L1.846 3.5 8 5.961 14.154 3.5 8.186 1.113zM15 4.239l-6.5 2.6v7.922l6.5-2.6V4.24zM7.5 14.762V6.838L1 4.239v7.923l6.5 2.6zM7.443.184a1.5 1.5 0 0 1 1.114 0l7.129 2.852A.5.5 0 0 1 16 3.5v8.662a1 1 0 0 1-.629.928l-7.185 2.874a.5.5 0 0 1-.372 0L.63 13.09a1 1 0 0 1-.63-.928V3.5a.5.5 0 0 1 .314-.464L7.443.184z"/>
          </svg>      </a>
        <a class="py-2 d-none d-md-inline-block"  @click="RouteManage">Account</a>
        <a class="py-2 d-none d-md-inline-block"  @click="RoutePass">Passes</a>
        <a class="py-2 d-none d-md-inline-block" @click="Reload">Appointments</a>
        <a class="py-2 d-none d-md-inline-block" @click="RouteStart">Sign Out</a>
        <a class="py-2 d-none d-md-inline-block"  @click="Reload">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bootstrap-reboot" viewBox="0 0 16 16">
            <path d="M1.161 8a6.84 6.84 0 1 0 6.842-6.84.58.58 0 1 1 0-1.16 8 8 0 1 1-6.556 3.412l-.663-.577a.58.58 0 0 1 .227-.997l2.52-.69a.58.58 0 0 1 .728.633l-.332 2.592a.58.58 0 0 1-.956.364l-.643-.56A6.812 6.812 0 0 0 1.16 8z"/>
            <path d="M6.641 11.671V8.843h1.57l1.498 2.828h1.314L9.377 8.665c.897-.3 1.427-1.106 1.427-2.1 0-1.37-.943-2.246-2.456-2.246H5.5v7.352h1.141zm0-3.75V5.277h1.57c.881 0 1.416.499 1.416 1.32 0 .84-.504 1.324-1.386 1.324h-1.6z"/>
          </svg>
        </a>
      </div>
    </nav>

    <div class="position-relative overflow-hidden p-3 p-md-5 m-md-3 text-center bg-light">
      <label style="margin-right: 2%">Select an option</label>
      <div class="form-group" >
        <select id="select" class="custom-select col-md-8" v-model="selectedApp" >
          <option>Create an appointment</option>
          <option v-for="app in appointments" :value="app.id" :key="app.id">{{app.id}}</option>
        </select>
        <button style="margin-left: 2%" class="btn btn-primary col-md-1" v-bind:disabled="createUserButtonDisabled" @click="setValues()">Create</button>
        <button style="margin-left: 2%" class="btn btn-primary col-md-1" v-bind:disabled="selectUserButtonDisabled" @click="setValues()">Update</button>
        <button style="margin-left: 2%" class="btn btn-primary col-md-1" v-bind:disabled="selectUserButtonDisabled" @click="deleteApp()">Delete</button>


        <form id="form-update" style="display: none">
          <div class="form-row" style="margin-top: 3%">
            <div class="form-group col-md-6">
              <label>Date</label>
              <input v-model="date" class="form-control" type="date" placeholder="2024-05-05" >
            </div>
            <div class="form-group col-md-6">
              <label for="start">Start Time</label>
              <input type="time" v-model="startTime" class="form-control" id="start" placeholder="15:00:00">
            </div>
          </div>
          <div class="form-group">
            <label for="floor">Service</label>
            <select id="floor" class="custom-select" required v-model="serviceName">
              <option v-for="service in services" :key="service.serviceName">{{service.serviceName}}</option>
            </select>
          </div>
          <button id="create" style="display: none" type="submit" v-bind:disabled="allFields" @click = "createApp" class="btn btn-primary">Create</button>
          <button id="update" style="display: none" type="submit" v-bind:disabled="allFields" @click="updateApp" class="btn btn-primary">Update</button>

        </form>
      </div>
    </div>

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
  name: "MonthlyCustomerAppointments", // Component name
  props: {
    email: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      errorMsg: '',
      appointments: [],
      services: [],
      date: '',
      startTime: '',
      serviceName: '',
      selectedApp: ''
    }
  },
  created() {
    axiosClient.get("/serviceAppointment/customer/" + this.email) // Fetch the service appointments of the logged in customer to display them
      .then(response => {
        this.appointments = response.data
      })
      .catch(error => {
        let err = `Error: ${error.response.data}`
      })
    axiosClient.get("/service") // Fetch available service for the customer to chose from
      .then(response => {
        this.services= response.data
      })
      .catch(error => {
        let err = `Error: ${error.response.data}`
      })
  },
  methods: {
    //Redirection to other pages
    async RouteStart() {
      await this.$router.push({name: 'Home'})
    },
    async RouteHome() {
      await this.$router.push({name: 'MonthlyCustomerHome', params: {email: this.email}})
    },
    async Reload() {
      await this.$router.push({name: 'MonthlyCustomerAppointments', params: {email: this.email}})
    },
    async RoutePass() {
      await this.$router.push({name: 'MonthlyCustomerPasses', params: {email: this.email}})
    },
    async RouteManage() {
      await this.$router.push({name: 'MonthlyCustomerManageAccount', params: {email: this.email}})
    },

    async createApp() { // Method to create an appointment to the customer
      const formatedTime = this.startTime+":00"
      const request = {date: this.date, startTime: formatedTime, serviceName: this.serviceName, userEmail: this.email}
      axiosClient.post("/serviceAppointment", request)
        .then((response) => {
          alert("Your appointment has been created successfully")
        })
        .catch((err) => {
          console.log(err.response)
          this.errorMsg = `Failed to create: ${err.response.data}`
          alert(err.response.data)
        })
      await this.RouteHome()

    },
    async setValues() { // Method that maps the values in the input fields to be used to create the new appointment
      document.getElementById("form-update").style.display = ""
      if (document.getElementById("select").value !== "Create an appointment") {
        this.selectedApp = document.getElementById("select").value
        document.getElementById("update").style.display = ""
        axiosClient.get("/serviceAppointment/" + this.selectedApp)
          .then(response => {
            document.getElementById("date").value = response.data.date
            document.getElementById("service").value = response.data.serviceName
            document.getElementById("start").value = response.data.startTime
          })
      }
      else
        document.getElementById("create").style.display = ""
    },
    async deleteApp() {  // Method to delete an appointment
      axiosClient.delete("/serviceAppointment/"+ this.selectedApp)
        .catch((err) => {
          this.errorMsg = `Failed to create: ${err.response.data}`
          alert(this.errorMsg)
        })
      await this.RouteHome()
    },
    async updateApp() { // Method to update an existing appointment
      const formatedTime = this.startTime+":00"
      const request = {date: this.date, startTime: formatedTime, serviceName: this.serviceName, userEmail: this.email}
      axiosClient.put("/serviceAppointment/"+ this.selectedApp, request)
        .then((response) => {
          alert("Your appointment has been updated successfully")
        })
        .catch((err) => {
          this.errorMsg = `Failed to create: ${err.response.data}`
          alert(this.errorMsg)
        })
      await this.RouteHome()
    }
  },
  computed: {
    selectUserButtonDisabled() {
      return !(this.selectedApp !== "Create an appointment");
    },
    createUserButtonDisabled() {
      return (this.selectedApp !== "Create an appointment");
    },
    allFields() {
      return !this.date.trim() || !this.startTime.trim() || !this.serviceName.trim()
    }
  }

}
</script>

<style scoped>

</style>

