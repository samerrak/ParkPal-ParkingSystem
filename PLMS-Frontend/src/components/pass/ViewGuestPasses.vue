
<template>
<div>
   <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="src/assets/logo-transparent-png.png">
    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/product/">
    <link href="../../../bootstrap-4.0.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="../../../bootstrap-4.0.0/docs/4.0/examples/product/product.css" rel="stylesheet">
    <!-- <nav class="site-header sticky-top py-1">
        <div class="container d-flex flex-column flex-md-row justify-content-between">
          <a class="py-2" href="#product">
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
          <a class="py-2 d-none d-md-inline-block" href="http://localhost:8087/#/login-user">Sign Out</a>
        </div>
      </nav> -->
  <div class="container-fluid">
    <div class="row">
      <div class="col-sm-10">
        <!-- <h1>Passes</h1> -->
        <!-- <hr><br><br> -->
        <!-- <button type="button" class="btn btn-success btn-sm" v-b-modal.employee-modal>Create</button>
        <button type="button" class="btn btn-success btn-sm" v-b-modal.edit-employee-modal :disabled="selectedEmployee=== null" >Update</button>
        <button type="button" class="btn btn-success btn-sm" :disabled="selectedEmployee=== null" @click="onViewSchedule(selectedEmployee) "> View Schedule</button>
        <button type="button" class="btn btn-danger btn-sm"  :disabled="selectedEmployee=== null" @click="onDeleteEmployee(selectedEmployee) "> Delete </button> -->
        <button type="button" class="btn btn-success btn-sm" @click="fetchGuestPasses() "> Clear Filters</button>
        <b-button type="button" class="btn btn-success btn-sm" @click="openCreateModal"> Create New Pass</b-button>
        <b-modal v-if='showModal' v-model="showModal" title ="Create">
            <component :is="modalContent"></component>
        </b-modal>
        <br><br>

        <br><br>
        <table class="table table-hover">
          <thead>
            <tr>
              <th scope="col">ID</th>
              <th scope="col">Confirmation Code</th>
              <th scope="col">License Plate</th>
              <th scope="col">Spot Number</th>
              <th scope="col">Size</th>
              <th scope="col">Floor Number</th>
              <th scope="col">Date</th>
              <th scope="col">Start Time</th>
              <th scope="col">End Time</th>
              <th scope="col">Fee</th>

            </tr>
          </thead>
          <tbody>
            <tr>
                <td>
                    <input type="text" v-model="IDTextInput" @keyup.enter="handleIDInput" placeholder="Search by ID" @focus="clearIDTextField()"/>
                </td>
                <td></td>

                <td></td>
                <td></td>
                <td></td>
                <td><select id="floor" v-model="editFloorInput" @change="handleFloorSelect($event.target.value)">
                    <option value="" disabled selected>Search by floor</option>
                    <option value="all" >All</option>
                    <option v-for="(floor, index) in floorNumbers.sort((a, b) => a - b)" :key="index">{{ floor }} </option>
                  </select></td>
                <td>
                    <input type="date" id="date" name="date" v-model="selectedDate" @change="handleDateSelect($event.target.value)" placeholder="Search by Date"></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr  v-for="(guestPass, index) in guestPasses" :key="index"   @click="handleRowClick(guestPass)"
            :class="{}">
                <td>{{guestPass.id}}</td>
                <td>{{guestPass.confirmationCode}}</td>
                <td>{{guestPass.licensePlate}}</td>
                <td>{{guestPass.spotNumber}}</td>
                <td>{{guestPass.isLarge ? 'Large' : 'Small'}}</td>
                <td>{{guestPass.floorNumber}}</td>
                <td>{{guestPass.date}}</td>
                <td>{{guestPass.startTime}}</td>
                <td>{{guestPass.endTime}}</td>
                <td>{{guestPass.fee}}</td>
            </tr>
          </tbody>
        </table>


      </div>
    </div>
  </div>
  </div>
</template>

<script>
import axios from 'axios';
import GeneralCreateGuestPass from '@/components/pass/GeneralCreateGuestPass'

const config = require('../../../config');
const frontendUrl = config.dev.host + ':' + config.dev.port;
const axiosClient = axios.create({
  // Note the baseURL, not baseUrl
  baseURL: config.dev.backendBaseUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
});
export default {

   components: {
        GeneralCreateGuestPass
    },

    data() {
        return {
            guestPasses: [],
            guestPassIDs: [],
            floorNumbers: [],
            editIdInput: '',
            editFloorInput: '',
            IDTextInput: '',
            selectedDate: null,

            showModal: false,
            modalContent: null,
        };
    },
    created() {
        // Fetch all employees on component mount
        this.fetchGuestPasses();
        this.fetchFloors();

    },

    methods: {

        clearForm() {
        this.modalContent = null;
        },

        openCreateModal() {
            this.modalContent = GeneralCreateGuestPass;
            this.showModal = true;
        },

        clearInputs(){
            this.IDTextInput = ""
            this.selectedDate = null
            this.editFloorInput = ""
        },

        handleOptionSelected(option) {
            // handle option selected event
        },

        handleIDInput() {
            this.guestPasses = []
            if (this.IDTextInput == ""){
                this.fetchGuestPasses();
            }
            else{
                axiosClient.get("/guestPass/" + this.IDTextInput).then((response) => {

                    this.guestPasses.push(response.data)
                }).catch((err) => {
                alert(err.response.data)
                });
            }


        },

        handleDateSelect(date){
            axiosClient.get("/guestPass/date/" + date).then((response) => {
                this.guestPasses = response.data

           }).catch((err) => {
            alert(err.response.data)
           });
        },

        fetchGuestPasses() {
            this.clearInputs()
            axiosClient.get("/guestPass/").then((response) => {
            this.guestPasses = response.data
            this.guestPassIDs = response.data.map((guestPasses) => guestPasses.id);
           }).catch((err) => {
           });
        },

        fetchFloors(){
            axiosClient.get("/floor").then((response) => {
                this.floorNumbers = response.data.map((floor) => floor.floorNumber);
           }).catch((err) => {
            alert(err.response.data)
           });
        },

        clearIDTextField() {
            this.IDTextInput == ""
            this.fetchGuestPasses()
        },

        handleFloorSelect(floorNumber){
            if (floorNumber == 'all'){
                this.fetchGuestPasses()
            }
            else{
                axiosClient.get("/guestPass/floor/" + floorNumber).then((response) => {
                    this.guestPasses = response.data;
                    console.log('Selected floor:', floorNumber);
                }).catch((err) => {
                alert(err.response.data)
           });
            }

        },
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
      await this.$router.push({name: 'ViewGuestPasses'})
    }

    }
}

</script>

<style>
a {
  padding: 20px;
}
  .container-fluid{
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>

