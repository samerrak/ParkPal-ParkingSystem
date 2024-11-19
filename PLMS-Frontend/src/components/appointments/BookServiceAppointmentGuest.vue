<template>
  <div>
    <nav class="site-header sticky-top py-1">
    <div class="container d-flex flex-column flex-md-row justify-content-between">
      <a class="py-2" @click="Home">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-box" viewBox="0 0 16 16">
          <path d="M8.186 1.113a.5.5 0 0 0-.372 0L1.846 3.5 8 5.961 14.154 3.5 8.186 1.113zM15 4.239l-6.5 2.6v7.922l6.5-2.6V4.24zM7.5 14.762V6.838L1 4.239v7.923l6.5 2.6zM7.443.184a1.5 1.5 0 0 1 1.114 0l7.129 2.852A.5.5 0 0 1 16 3.5v8.662a1 1 0 0 1-.629.928l-7.185 2.874a.5.5 0 0 1-.372 0L.63 13.09a1 1 0 0 1-.63-.928V3.5a.5.5 0 0 1 .314-.464L7.443.184z"/>
        </svg>      </a>
      <a class="py-2 d-none d-md-inline-block" @click="Home">Return to Home Page</a>
    </div>
  </nav>
  <div>
    <div>
        <h2 style="margin-top: 1%">All Services</h2>
        <table class="center bordered-table">
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
      </div>
    </div>
  <div style="margin-top: 2%">
    <h2>Book a Service Appointment as a Guest</h2>
    <div>
      <label for="serviceName">Service Name:</label>
      <select id="serviceName" v-model="serviceName">
        <option value="">-- Select an service name --</option>
        <option v-for="service in servicesDropDown" :value="service">{{ service }}</option>
      </select>
    </div>
    <div>
      <label for="date">Date:</label>
      <input type="date" id="date" name="date" v-model="date">
    </div>
    <div>
      <label for="startTime">Start Time:</label>
      <input type="time" id="startTime" name="startTime" v-model="startTime">
    </div>
    <button class="btn btn-primary" type="button" :disabled="!serviceName || !date || !startTime" @click="bookServiceAppointment">Book
      Appointment</button>
    <div>
      <p style="margin-top: 1%" class="parkingLotHours"> {{ parkingLotHours }}</p>
    </div>
    <p class="success">{{ successMsg }}</p>
    <p class="error">{{ errorMsg }}</p>
  </div>

  <div style="margin-top: 2%">
    <h2>Update your Service Appointment Details</h2>
    <div>
      <label for="id">Appointment ID:</label>
      <input type="text" id="id" name="id" v-model="idUpdate">
    </div>
    <div>
      <label for="serviceName">Service Name:</label>
      <select id="serviceName" v-model="serviceNameUpdate">
        <option value="">-- Select an service name --</option>
        <option v-for="service in servicesDropDown" :value="service">{{ service }}</option>
      </select>
    </div>
    <div>
      <label for="date">Date:</label>
      <input type="date" id="date" name="date" v-model="dateUpdate">
    </div>
    <div>
      <label for="startTime">Start Time:</label>
      <input type="time" id="startTime" name="startTime" v-model="startTimeUpdate">
    </div>
    <button class="btn btn-success" type="button" :disabled="!serviceNameUpdate || !dateUpdate || !startTimeUpdate" @click="updateServiceAppointment">Update Service Appointment Details</button>
    <p class="success">{{ successMsgUpdate }}</p>
    <p class="error">{{ errorMsgUpdate }}</p>
  </div>
  <div style="margin-top: 1%">
    <h2>Cancel Service Appointment</h2>
    <div>
      <label for="id">Appointment ID:</label>
      <input type="text" id="id" name="id" v-model="idDelete">
    </div>
    <button class="btn btn-danger" type="button" :disabled="!idDelete" @click="deleteAppointment">Cancel Service Appointment</button>
    <p class="success">{{ successMsgCancel }}</p>
    <p class="error">{{ errorMsgCancel }}</p>
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
      services: [],
      servicesDropDown: [],
      serviceName: '',
      date: '',
      startTime: '',
      successMsg: '',
      errorMsg: '',
      parkingLotHours: '',
      serviceNameUpdate: '',
      dateUpdate: '',
      startTimeUpdate: '',
      successMsgUpdate: '',
      errorMsgUpdate: '',
      idDelete: '',
      idUpdate: '',
      successMsgCancel: '',
      errorMsgCancel: ''
    };
  },

  created() {
    axiosClient.get('/service')
      .then((response) => {
        this.servicesDropDown = response.data.map(service => service.serviceName);
        this.services = response.data
      })
      .catch((error) => {
      });

      axiosClient.get('/parkingLot')
  .then((response) => {
    // format the parking lots hours of operations so we know when we can book appointments
    console.log(response.data)
    console.log(response.data.openingTime)
    const openingTime = response.data.openingTime;

    // get hours and minutes to know if it is AM or PM
    let openingHour = parseInt(openingTime.substring(0, 2));
    let openingMinute = openingTime.substring(3, 5);
    let ampmOpening = openingHour >= 12 ? 'PM' : 'AM';
    openingHour = openingHour % 12 || 12;
    let formattedOpeningTime = openingHour + ':' + openingMinute + ' ' + ampmOpening;

    console.log(response.data)
    console.log(response.data.closingTime)
    const closingTime = response.data.closingTime;

    let closingHour = parseInt(closingTime.substring(0, 2));
    let closingMinute = closingTime.substring(3, 5);
    let ampmClosing = closingHour >= 12 ? 'PM' : 'AM';
    closingHour = closingHour % 12 || 12;
    let formattedClosingTime = closingHour + ':' + closingMinute + ' ' + ampmClosing;

    console.log(formattedOpeningTime);
    this.parkingLotHours = "Parking lot hours of operation: " + formattedOpeningTime + "-" + formattedClosingTime;
  })
  .catch((error) => {
    this.parkingLotHours = "The parking lot has not been finished yet. Please book appointments at a later date."
  });
  },


  methods: {
    bookServiceAppointment() {
      const formattedTime = this.startTime + ":00"
      const requestBody = {
        date: this.date,
        startTime: formattedTime,
        customerEmail: null, // Replace with customer email if applicable
        serviceName: this.serviceName
      };
      console.log(this.date)
      console.log(this.serviceName)
      console.log(formattedTime)
      axiosClient.post('/serviceAppointment', requestBody)
        .then(response => {
          this.resetInputs()
          this.successMsg = `Appointment booked successfully. Your appointment details: ID: ${response.data.id}, Service: ${response.data.serviceName}, Date: ${response.data.date}, Start time: ${response.data.startTime}`;
        })
        .catch(error => {
          console.log(error.response.data);
          this.errorMsg = error.response.data;
        })
    },

    updateServiceAppointment(){
      const formattedTime = this.startTimeUpdate + ":00"
      if (!/^\d+$/.test(this.idUpdate)){
        this.errorMsgUpdate = "Please enter a valid integer ID"
        return
      }
      const requestBody = {
        date: this.dateUpdate,
        startTime: formattedTime,
        customerEmail: null, // Replace with customer email if applicable
        serviceName: this.serviceNameUpdate
      };
      console.log(this.date)
      console.log(this.serviceName)
      console.log(formattedTime)
      axiosClient.put(`/serviceAppointment/${this.idUpdate}`, requestBody)
        .then(response => {
          this.resetInputs()
          this.successMsgUpdate = `Appointment updated successfully. Your appointment details: ID: ${response.data.id}, Service: ${response.data.serviceName}, Date: ${response.data.date}, Start time: ${response.data.startTime}`;
        })
        .catch(error => {
          console.log(error.response.data);
          this.errorMsgUpdate = error.response.data;
        })
    },

    deleteAppointment(){
      if (!/^\d+$/.test(this.idDelete)){
        this.errorMsgCancel = "Please enter a valid integer ID"
        return
      }
      axiosClient.delete(`/serviceAppointment/${this.idDelete}`)
      .then(response =>{
        this.resetInputs()
        this.successMsgCancel = 'Appointment Cancelled Successfully'
      }).catch(error =>{
        this.resetInputs()
        console.log(error.response.data)
        this.errorMsgCancel = error.response.data
      })
    },

    resetInputs(){
      this.serviceName = ''
      this.date = ''
      this.startTime = ''
      this.successMsg = ''
      this.errorMsg = ''
      this.idUpdate = ''
      this.parkingLotHours = ''
      this.serviceNameUpdate = ''
      this.dateUpdate = ''
      this.startTimeUpdate = ''
      this.successMsgUpdate = ''
      this.errorMsgUpdate = ''
      this.idDelete = ''
      this.successMsgCancel = ''
      this.errorMsgCancel = ''
    },
    async Home(){
      await this.$router.push({name: 'Home'})
    }
  }
};
</script>

<style>
  .container {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
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

  .bordered-table th,
  .bordered-table td {
    border: 1px solid black;
    padding: 8px;
  }
</style>
