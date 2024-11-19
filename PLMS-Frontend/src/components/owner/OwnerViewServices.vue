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
        <h2>Create a Service</h2>
        <div>
          <label for="serviceName">Service name:</label>
          <input type="text" id="serviceNameCreation" name="serviceNameCreation" v-model="createServiceNameInput">
        </div>
        <div>
          <label for="cost">Cost:</label>
          <input type="text" id="costCreation" name="costCreation" v-model="createCostInput">
        </div>
        <div>
          <label for="lengthInHoursCreation">Length in hours:</label>
          <input type="text" id="lengthInHoursCreation" name="lengthInHoursCreation" v-model="createLengthInput">
        </div>
        <button type="button" class="btn btn-primary" :disabled="!createServiceNameInput || !createCostInput || !createLengthInput"
          @click="createService()">Create Service</button>

        <p class="error">{{ errorMsgCreate }}</p>
      </div>

      <div>
        <h2>Edit a Service</h2>
        <div>
    <label for="serviceNameEdit">Service name:</label>
    <select id="serviceNameEdit" name="serviceNameEdit" v-model="editServiceNameInput">
      <option value="">Select a service</option>
      <option v-for="name in serviceNames" :value="name">{{ name }}</option>
    </select>
  </div>
        <div>
          <label for="costEdit">Cost:</label>
          <input type="text" id="costEdit" name="costEdit" v-model="editCostInput">
        </div>
        <div>
          <label for="lengthInHoursEdit">Length in hours:</label>
          <input type="text" id="lengthInHoursEdit" name="lengthInHoursEdit" v-model="editLengthInput">
        </div>

        <button type="button" class="btn btn-primary" :disabled="!editServiceNameInput || !editCostInput || !editLengthInput"
          @click="editService()">Edit Service</button>

        <p class="error">{{ errorMsgEdit }}</p>
      </div>
  
      <div>
        <h2>Delete a Service </h2>
        <div>
    <label for="serviceNameDelete">Service name:</label>
    <select id="serviceNameDelete" name="serviceNameDelete" v-model="deleteServiceInput">
      <option value="">Select a service</option>
      <option v-for="name in serviceNames" :value="name">{{ name }}</option>
    </select>
  </div>
        <button type="button" :disabled="!deleteServiceInput" class="btn btn-danger" @click="deleteService()">Delete Service</button>

        <p class="error">{{ errorMsgDelete }}</p>
      </div>
  
  
      <div style="padding: 0 550px;">
        <h2>All Services</h2>
        <table class="table table-striped">
          <thead>
            <tr>
              <th>Service Name</th>
              <th>Cost</th>
              <th>Length in Hours</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="service in services" :key="service.serviceName">
              <td>{{ service.serviceName ? service.serviceName : '' }}</td>
              <td>{{ service.cost ? service.cost : '' }}</td>
              <td>{{ service.lengthInHours ? service.lengthInHours : '' }}</td>
            </tr>
          </tbody>
        </table>
        <p class="error">{{ errorMsgAll }}</p>
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
    name: 'OwnerViewServices',
    data() {
      return {
        services: [],
        serviceNames: [],

        createServiceNameInput: '',
        createCostInput: '',
        createLengthInput: '',

        editServiceNameInput: '',
        editCostInput: '',
        editLengthInput: '',

        deleteServiceInput: '',

        errorMsgCreate: '',
        errorMsgEdit: '',
        errorMsgDelete: '',
        errorMsgAll: ''

      };
    },
    created() {
      this.getAllServices()
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

        createService() {
            const request = {
            serviceName: this.createServiceNameInput,
            cost: this.createCostInput,
            lengthInHours: this.createLengthInput
            };
            axiosClient.post("/service/create", request)
            .then((response) => {
                this.createServiceNameInput = '';
                this.createCostInput = '';
                this.createLengthInput = '';
                this.services.push(response.data)
                this.errorMsgCreate = "Service created successfully";
                this.getAllServices()
            })
            .catch(error => {
                console.log(error.response.data);
                this.errorMsgCreate = error.response.data;
            })
        },

        editService() {
            const request = {
            serviceName: this.editServiceNameInput,
            cost: this.editCostInput,
            lengthInHours: this.editLengthInput
            };
            axiosClient.put("/service/", request)
            .then((response) => {
                this.editServiceNameInput = '';
                this.editCostInput = '';
                this.editLengthInput = '';
                window.location.reload();
                this.errorMsgEdit = "Service updated successfully";
                this.getAllServices()
            })
            .catch((error) => {
                console.log(error.response.data);
                this.errorMsgEdit = error.response.data;
            })
        },
  
        deleteService() {
            axiosClient.delete(`/service/${this.deleteServiceInput}`)
            .then((response) => {
                window.location.reload();
                this.errorMsgDelete = "Service deleted successfully";
                this.getAllServices()
            })
            .catch((error) => {
                console.log(error.response.data);
                this.errorMsgDelete = error.response.data;
            })
        },
        
        getAllServices(){
          axiosClient.get('/service')
        .then((response) => {
          this.services = response.data;
          this.serviceNames = response.data.map((service) => service.serviceName)
          this.errorMsgAll = '';
        })
        .catch((error) => {
          console.log(error.response.data);
          this.errorMsgAll = error.response.data;
        });
        }
        
    },
    
  }
  
  </script>
  
  
  <style>
  .container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
  
  table.center,
  div.center {
    margin: 0 auto;
  }
  
  table {
    padding: 20px;
  }
  
  label {
    padding: 20px;
  }
  

  a {
  padding: 20px;
}
  </style>