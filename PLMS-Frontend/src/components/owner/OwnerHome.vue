<template>
    <div class="ownerpage">
      <meta charset="utf-8">
      <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
      <meta name="description" content="">
      <meta name="author" content="">
      <link rel="icon" href="../../assets/logo-transparent-png.png">
      <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/product/">
      <link href="../../../bootstrap-4.0.0/dist/css/bootstrap.min.css" rel="stylesheet">
      <link href="../../../bootstrap-4.0.0/docs/4.0/examples/product/product.css" rel="stylesheet">

      <nav class="site-header sticky-top py-1">
        <div class="container d-flex flex-column flex-md-row justify-content-between">
          <a class="py-2">
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-box" viewBox="0 0 16 16">
              <path d="M8.186 1.113a.5.5 0 0 0-.372 0L1.846 3.5 8 5.961 14.154 3.5 8.186 1.113zM15 4.239l-6.5 2.6v7.922l6.5-2.6V4.24zM7.5 14.762V6.838L1 4.239v7.923l6.5 2.6zM7.443.184a1.5 1.5 0 0 1 1.114 0l7.129 2.852A.5.5 0 0 1 16 3.5v8.662a1 1 0 0 1-.629.928l-7.185 2.874a.5.5 0 0 1-.372 0L.63 13.09a1 1 0 0 1-.63-.928V3.5a.5.5 0 0 1 .314-.464L7.443.184z"/>
            </svg>      </a>
            <a class="py-2 d-none d-md-inline-block"  @click="Home">Home</a>
            <a class="py-2 d-none d-md-inline-block"  @click="Customers">Manage Customer Accounts</a>
          <a class="py-2 d-none d-md-inline-block"  @click="Employees">Manage Employee Accounts</a>
          <a class="py-2 d-none d-md-inline-block"  @click="Passes">Manage Passes</a>
          <a class="py-2 d-none d-md-inline-block" @click="Appointments">Manage Appointments</a>
          <a class="py-2 d-none d-md-inline-block" @click="ParkingLot">Manage Parking Lot</a>
          <a class="py-2 d-none d-md-inline-block" @click="Services">Manage Services</a>
          <a class="py-2 d-none d-md-inline-block" @click="SignOut">Sign Out</a>
        </div>
      </nav>

      <div class="position-relative overflow-hidden p-3 p-md-5 m-md-3 text-center bg-light">
        <img width="30%" src="../../assets/user.png.png" style="margin-bottom: -5%">
        <div class="col-md-5 p-lg-5 mx-auto my-5">
          <h1 class="display-4 font-weight-light"> Hello! Welcome {{name}} </h1>
          <p class="lead font-weight-normal">Here are the different options to help you run your parking lot!</p>
        </div>
        <div class="container my-7">
        <div class="row">
            <div class="col-md-6 mb-4">
            <a @click="Customers" class="btn btn-primary btn-lg btn-block big-high">Manage Customer Accounts</a>
            </div>
            <div class="col-md-6 mb-4">
            <a @click="Employees" class="btn btn-primary btn-lg btn-block big-high">Manage Employee Accounts</a>
            </div>
            <div class="col-md-6 mb-4">
            <a @click="Passes"  class="btn btn-primary btn-lg btn-block big-high">Manage Passes</a>
            </div>
            <div class="col-md-6 mb-4">
            <a @click="Appointments" class="btn btn-primary btn-lg btn-block big-high">Manage Appointments</a>
            </div>
            <div class="col-md-6 mb-4">
            <a @click="ParkingLot" class="btn btn-primary btn-lg btn-block big-high">Manage Parking Lot</a>
            </div>
            <div class="col-md-6 mb-4">
            <a @click="Services" class="btn btn-primary btn-lg btn-block big-high">Manage Services</a>
            </div>
        </div>
      </div>
      </div>


    </div>
  </template>

  <script>
import OwnerViewAppointments from '@/components/owner/OwnerViewAppointments.vue'
import ParkingLotSettings from '@/components/owner/ParkingLotSettings.vue'
import OwnerViewServices from '@/components/owner/OwnerViewServices.vue'
import ViewMonthlyCustomer from '@/components/employee/ViewMonthlyCustomer.vue'
  import axios from 'axios';
  const config = require('../../../config');
  const frontendUrl = config.dev.host + ':' + config.dev.port;
  const axiosClient = axios.create({
    // Note the baseURL, not baseUrl
    baseURL: config.dev.backendBaseUrl,
    headers: { 'Access-Control-Allow-Origin': frontendUrl }
  });
  export default {
    name: "OwnerHome",
      components: {
    OwnerViewAppointments,
    ParkingLotSettings,
    OwnerViewServices,
    ViewMonthlyCustomer
    },

    data() {
      return {
        errorMsg: '',
        name:'',
        password: '',
        passes: [],
        appointments: []
      }
    },
    created() {
      // axiosClient.get("/customer?email="+this.email)
      //   .then(response => {
      //     this.name = response.data.name
      //     this.password = response.data.password
      //   })
      //   .catch(error => {
      //     alert(error.data)
      //   })
      // axiosClient.get("/monthlypass/customer/"+this.email)
      //   .then(response => {
      //     this.passes = response.data
      //   })
      //   .catch(error => {
      //     alert(error.data)
      //   })
      // axiosClient.get("/serviceAppointment/customer/"+this.email)
      //   .then(response => {
      //     this.appointments = response.data
      //   })
    },
    methods : {
      async Home() {
      await this.$router.push({name: 'OwnerHome'})
    },
    async Appointments() {
      await this.$router.push({name: 'OwnerViewAppointments'})
    },
    async ParkingLot() {
      await this.$router.push({name: 'ParkingLotSettings'})
    },
    async Services(){
      await this.$router.push({name: 'OwnerViewServices'})
    },
    async Customers(){
      await this.$router.push({name: 'ViewMonthlyCustomer'})
    },
    async Employees(){
      await this.$router.push({name: 'ManageEmployees'})
    },
    async Passes(){
      await this.$router.push({name: 'OwnerPasses'})
    },
    async SignOut(){
      await this.$router.push({name: 'Home'})
    }
    }
  }
  </script>

  <style scoped>
  .big-high{
    line-height: 100px;
    font-size:xx-large;
  }

  a {
  padding: 20px;
}
  </style>
