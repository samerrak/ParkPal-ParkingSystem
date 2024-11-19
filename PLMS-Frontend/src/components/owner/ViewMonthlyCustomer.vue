<template>
  <div>
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

      <div>
      <h1>Customer Information</h1>

      <form @submit.prevent="searchCustomer">
        <label for="email">Email:</label>
          <input type="email" id="email" v-model="searchEmail">
        <button class="btn btn-primary" type="submit">Search</button>
      </form>
      <p class="error">{{ gettingCustomerErrorMsg }}</p>


      <div class="container-fluid">
        <div class="row">
          <div class="col-md-6">
            <h2>All Customers</h2>
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Email</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(customer, index) in customers" :key="index"
                @click="handleRowClick(customer)" @mouseover="handleRowHover(customer)"
                @mouseout="handleRowHover(null)" :class="{ 'row-highlighted': customer === hoveredCustomer, 'row-selected': customer === selectedCustomer }">
                  <td>{{ customer.name }}</td>
                  <td>{{ customer.email }}</td>
                </tr>
              </tbody>
            </table>
            <p class="error">{{ gettingAllCustomerErrorMsg }}</p>
          </div>
          <div class="col-md-6">
            <div class="card" v-if="selectedCustomer">
              <div class="card-header">Selected Customer</div>
              <div class="card-body">
                <div>
                <p>Name: {{ selectedCustomer.name }}</p>
                <p>Email: {{ selectedCustomer.email }}</p>
              </div>


              <button class="btn btn-secondary dropdown-toggle" @click="togglePasses()">
                Show Passe(s)
              </button>
              <button  class="btn btn-secondary dropdown-toggle" @click="toggleAppointment()">
                Show Service Appointment(s)
              </button>
              <div v-if="showPasses">
                <p>Passes:</p>
                <table class="table table-striped" v-if="!gettingPassErrorMsg">
                  <thead>
                    <tr>
                      <th>Start Date</th>
                      <th>End Date</th>
                      <th>Floor Number</th>
                      <th>Spot Number</th>
                      <th>Large Spot</th>
                      <th>License Plate</th>
                    </tr>
                  </thead>
                  <tbody class="row-selectable">
                    <tr v-for="(pass, index) in selectedCustomer.passes" :key="index">
                      <td>{{ pass.startDate }}</td>
                      <td>{{ pass.endDate }}</td>
                      <td>{{ pass.floorNumber }}</td>
                      <td>{{ pass.spotNumber }}</td>
                      <td>{{ pass.large }}</td>
                      <td>{{ pass.licensePlate }}</td>
                    </tr>
                  </tbody>
                </table>
                <p class="error" v-if="gettingPassErrorMsg">{{ gettingPassErrorMsg }}</p>
              </div>


              <div v-if="showAppointments">
                <p>Appointments:</p>
                <table class="table table-striped" v-if="!gettingAppointmentsErrorMsg">
                  <thead>
                    <tr>
                      <th>Date</th>
                      <th>Start Time</th>
                      <th>End Time</th>
                      <th>Customer Email</th>
                      <th>Employee Email</th>
                      <th>Service Name</th>
                    </tr>
                  </thead>
                  <tbody class="row-selectable">
                    <tr v-for="(appointment, index) in selectedCustomer.appointments" :key="index">
                      <td>{{ appointment.date }}</td>
                      <td>{{ appointment.startTime }}</td>
                      <td>{{ appointment.endTime }}</td>
                      <td>{{ appointment.customerEmail }}</td>
                      <td>{{ appointment.employeeEmail }}</td>
                      <td>{{ appointment.serviceName }}</td>
                    </tr>
                  </tbody>
                </table>
                <p class="error" v-if="gettingAppointmentsErrorMsg">{{ gettingAppointmentsErrorMsg }}</p>
              </div>

            </div>
          </div>
        </div>
      </div>
      <div v-if="selectedCustomer">



      </div>

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
    data() {
        return {
            customers: [],
            searchEmail: '',
            gettingCustomerErrorMsg: '',
            gettingAllCustomerErrorMsg: '',
            gettingPassErrorMsg: '',
            gettingAppointmentsErrorMsg: '',
            hoveredCustomer: null,
            selectedCustomer: null,
            showPasses: false,
            showAppointments: false
        };
    },
    created() {
        // Fetch all customers on component mount
        this.fetchCustomers();
    },
    methods: {
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
    },
        async fetchCustomers() {
            try {
                const response = await axiosClient.get('/customers');
                this.customers = response.data;
                this.gettingAllCustomerErrorMsg = '';
            } catch (error) {
                this.gettingAllCustomerErrorMsg = error.response.data;
            }
        },
        async fetchMonthlyCustomer(email) {
          try {
            const response = await axiosClient.get(`/customer?email=${email}`);
            this.updateSelectedMonthlyCustomer(response.data);
            this.gettingCustomerErrorMsg = '';
          }catch (error) {
            this.gettingCustomerErrorMsg = error.response.data;
          }
        },
        async updateSelectedMonthlyCustomer(monthlyCustomer){
          try {
            this.selectedCustomer = monthlyCustomer;
            const responsePass = await axiosClient.get(`/monthlypass/customer/${monthlyCustomer.email}`);
            this.selectedCustomer.passes = responsePass.data;
            this.gettingPassErrorMsg = '';
          }catch (error) {
            console.log(error);
            this.gettingPassErrorMsg = error.response.data;
          }
          try {
            const responseAppointment = await axiosClient.get(`/serviceAppointment/customer/${monthlyCustomer.email}`);
            this.selectedCustomer.appointments = responseAppointment.data;
            this.gettingAppointmentsErrorMsg = '';
          }catch (error) {
            this.gettingAppointmentsErrorMsg = error.response.data;
          }
        },
        searchCustomer() {
          this.fetchMonthlyCustomer(this.searchEmail);
        },
        handleRowClick(customer) {
          this.updateSelectedMonthlyCustomer(customer);
        },
        handleRowHover(customer) {
          this.hoveredCustomer = customer;
        },
        togglePasses() {
          this.showPasses = !this.showPasses;
          this.showAppointments = false;
        },
        toggleAppointment(){
          this.showAppointments = !this.showAppointments;
          this.showPasses = false;
        }
    }
};
</script>
<style>
.row-highlighted {
  background-color: #cbcbcb;
  cursor: pointer;
}
.row-selected{
  background-color: #8e8e8e;
}
.error{
  color: red;
}

.row-selectable{
  cursor: pointer;
}

a {
  padding: 20px;
}
</style>
