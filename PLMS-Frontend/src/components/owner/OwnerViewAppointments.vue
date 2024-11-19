<template>
  <div class="owner-view-appointments">
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
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-box"
            viewBox="0 0 16 16">
            <path
              d="M8.186 1.113a.5.5 0 0 0-.372 0L1.846 3.5 8 5.961 14.154 3.5 8.186 1.113zM15 4.239l-6.5 2.6v7.922l6.5-2.6V4.24zM7.5 14.762V6.838L1 4.239v7.923l6.5 2.6zM7.443.184a1.5 1.5 0 0 1 1.114 0l7.129 2.852A.5.5 0 0 1 16 3.5v8.662a1 1 0 0 1-.629.928l-7.185 2.874a.5.5 0 0 1-.372 0L.63 13.09a1 1 0 0 1-.63-.928V3.5a.5.5 0 0 1 .314-.464L7.443.184z" />
          </svg> </a>
        <a class="py-2 d-none d-md-inline-block" @click="Home">Home</a>
        <a class="py-2 d-none d-md-inline-block" @click="Customers">Manage Customer Accounts</a>
        <a class="py-2 d-none d-md-inline-block" @click="Employees">Manage Employee Accounts</a>
        <a class="py-2 d-none d-md-inline-block" @click="Passes">Manage Passes</a>
        <a class="py-2 d-none d-md-inline-block" @click="Appointments">Manage Appointments</a>
        <a class="py-2 d-none d-md-inline-block" @click="ParkingLot">Manage Parking Lot</a>
        <a class="py-2 d-none d-md-inline-block" @click="Services">Manage Services</a>
        <a class="py-2 d-none d-md-inline-block" @click="SignOut">Sign Out</a>
      </div>
    </nav>
    <div class="position-relative overflow-hidden p-3 p-md-5 m-md-3 text-center bg-light">
      <h2>Manage Your Service Appointments</h2>
      <div class="container my-6">
        <div class="row justify-content-center">
          <button data-inline="true" class="btn btn-primary btn-equal-size" @click="showByDateModal = true">Search for
            appointments by date</button>
          <div v-if="showByDateModal" class="modal">
            <div class="modal-content">
              <span class="close" @click="showByDateModal = false">&times;</span>
              <h5>Search for Service Appointments on a Date</h5>
              <div>
                <label for="date">Select Date:</label>
                <input type="date" id="date" name="date" v-model="selectedDate">
                <button type="button" class="btn btn-success" @click="getServiceAppointmentsByDate">Get
                  Appointments</button>
              </div>
            </div>
          </div>

          <button data-inline="true" class="btn btn-primary btn-equal-size" @click="showEmployeeModal = true">Search for
            appointments by employee</button>
          <div v-if="showEmployeeModal" class="modal">
            <div class="modal-content">
              <span class="close" @click="showEmployeeModal = false">&times;</span>
              <h5>Search for Service Appointments Assigned to an Employee</h5>
              <div>
                <label for="employee">Select Employee:</label>
                <select id="employee" name="employee" v-model="selectedEmployee">
                  <option value="">-- Select an employee --</option>
                  <option v-for="email in employeeEmails" :value="email">{{ email }}</option>
                </select>
                <button type="button" class="btn btn-success" @click="getServiceAppointmentsByEmployee">Get
                  Appointments</button>
              </div>
            </div>
          </div>

          <button data-inline="true" class="btn btn-primary btn-equal-size" @click="showCustomerModal = true">Search for
            appointments by customer</button>
          <div v-if="showCustomerModal" class="modal">
            <div class="modal-content">
              <span class="close" @click="showCustomerModal = false">&times;</span>
              <h5>Search for appointments by customer</h5>
              <div>
                <label for="email">Select Customer Email:</label>
                <select id="email" name="email" v-model="findByCustomerEmailInput">
                  <option disabled value="">Please select an email</option>
                  <option v-for="email in customerEmails" :key="email" :value="email">{{ email }}</option>
                </select>
                <button type="button" class="btn btn-success" @click="getServiceAppointmentsByCustomer">Find
                  Appointment</button>
              </div>
            </div>
          </div>

          <button data-inline="true" class="btn btn-primary btn-equal-size"
            @click="showSearchAppointmentModal = true">Search for Appointment by ID</button>
          <div v-if="showSearchAppointmentModal" class="modal">
            <div class="modal-content">
              <span class="close" @click="showSearchAppointmentModal = false">&times;</span>
              <h5>Search for Appointment by ID</h5>
              <div>
                <label for="appointment-id">Appointment ID:</label>
                <select id="appointment-id" name="appointment-id" v-model="searchAppointmentId">
                  <option disabled value="">Please select an ID</option>
                  <option v-for="id in appointmentIds" :key="id" :value="id">{{ id }}</option>
                </select>
              </div>
              <button type="button" class="btn btn-success" @click="getServiceAppointmentById">Search</button>
            </div>
          </div>

          <button data-inline="true" class="btn btn-primary btn-equal-size" @click="showUpdateEmployeeModal = true">Assign
            an
            Employee to an Appointment</button>
          <div v-if="showUpdateEmployeeModal" class="modal">
            <div class="modal-content">
              <span class="close" @click="showUpdateEmployeeModal = false">&times;</span>
              <h5>Assign an Employee to an Appointment</h5>
              <div>
                <label for="appointment-id">Appointment ID:</label>
                <select id="appointment-id" name="appointment-id" v-model="editByEmployeeEmailIdInput">
                  <option disabled value="">Please select an ID</option>
                  <option v-for="id in appointmentIds" :key="id" :value="id">{{ id }}</option>
                </select>
              </div>
              <div>
                <label for="employee-email">Employee Email:</label>
                <select id="employee-email" name="employee-email" v-model="editByEmployeeEmailInput">
                  <option disabled value="">Please select an email</option>
                  <option v-for="email in employeeEmails" :key="email" :value="email">{{ email }}</option>
                </select>
              </div>
              <button type="button" class="btn btn-success" @click="updateServiceAppointmentWithNewEmployee">Update
                Appointment's
                Employee</button>
            </div>
          </div>

          <button data-inline="true" class="btn btn-primary btn-equal-size" @click="showEditModal = true">Update an
            existing appointment</button>
          <div v-if="showEditModal" class="modal">
            <div class="modal-content">
              <span class="close" @click="showEditModal = false">&times;</span>
              <h5>Update an Existing Service Appointment's details</h5>
              <div>
                <div>
                  <label for="id">Appointment ID:</label>
                  <select id="id" v-model="editIdInput">
                    <option v-for="id in appointmentIds" :value="id">{{ id }}</option>
                  </select>
                </div>
              </div>
              <div>
                <label for="date">Date:</label>
                <input type="date" id="date" name="date" v-model="editDateInput">
              </div>
              <div>
                <label for="startTime">Start Time:</label>
                <input type="time" id="startTime" name="startTime" v-model="editStartTimeInput">
              </div>
              <div>
                <label for="serviceName">Service Name:</label>
                <select id="serviceName" name="serviceName" v-model="editServiceNameInput">
                  <option v-for="service in serviceNames" :key="service" :value="service">{{ service }}</option>
                </select>
              </div>
              <button type="button" class="btn btn-success"
                :disabled="!editIdInput || !editDateInput || !editStartTimeInput || !editServiceNameInput"
                @click="editServiceAppointment">Edit Appointment</button>
              <p class="success">{{ successMsgEdit }}</p>
              <p class="error">{{ errorMsgEdit }}</p>
            </div>
          </div>

          <button data-inline="true" class="btn btn-primary btn-equal-size" @click="showCancelModal = true">Cancel a
            Service
            Appointment</button>
          <div v-if="showCancelModal" class="modal">
            <div class="modal-content">
              <span class="close" @click="showCancelModal = false">&times;</span>
              <h5>Cancel a Service Appointment</h5>
              <div>
                <label for="cancelId">Appointment ID:</label>
                <select id="cancelId" v-model="cancelIdInput">
                  <option disabled value="">Please select an ID</option>
                  <option v-for="id in appointmentIds" :key="id" :value="id">{{ id }}</option>
                </select>
              </div>
              <button type="button" class="btn btn-danger" :disabled="!cancelIdInput"
                @click="cancelServiceAppointment">Cancel
                Appointment</button>
              <p class="success">{{ successMsgCancel }}</p>
              <p class="error">{{ errorMsgCancel }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>


    <div style="padding: 0 215px;">
      <button type="button" class="btn btn-secondary" @click="getAllAppointments">Get all
        appointments in the
        system</button>
      <p class="error">{{ errorMsgAll }}</p>
      <table class="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>Date</th>
            <th>Start Time</th>
            <th>End Time</th>
            <th>Customer Email</th>
            <th>Employee Email</th>
            <th>Service Name</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="appointment in serviceAppointments" :key="appointment.id">
            <td>{{ appointment.id ? appointment.id : '' }}</td>
            <td>{{ appointment.date ? appointment.date : '' }}</td>
            <td>{{ appointment.startTime ? appointment.startTime : '' }}</td>
            <td>{{ appointment.endTime ? appointment.endTime : '' }}</td>
            <td>{{ appointment.customerEmail ? appointment.customerEmail : '' }}</td>
            <td>{{ appointment.employeeEmail ? appointment.employeeEmail : '' }}</td>
            <td>{{ appointment.serviceName ? appointment.serviceName : '' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</div></template>


<script type="module">
import axios from 'axios';
const config = require('../../../config');
const frontendUrl = config.dev.host + ':' + config.dev.port;
const axiosClient = axios.create({
  // Note the baseURL, not baseUrl
  baseURL: config.dev.backendBaseUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl }
});

export default {
  name: 'OwnerViewAppointments',
  data() {
    return {
      serviceAppointments: [],
      appointmentIds: [],
      showByDateModal: false,
      showEmployeeModal: false,
      showCustomerModal: false,
      showUpdateEmployeeModal: false,
      showEditModal: false,
      showCancelModal: false,
      showSearchAppointmentModal: false,
      searchAppointmentId : '',
      selectedEmployee: '',
      employeeEmails: [],
      customerEmails: [],
      serviceNames: [],
      errorMsgAll: '',
      editIdInput: '',
      editDateInput: '',
      editStartTimeInput: '',
      editCustomerEmailInput: '',
      editServiceNameInput: '',
      selectedDate: '',
      appointmentId: '',
      errorMsgId: '',
      errorMsgEmployee: '',
      errorMsgCustomer: '',
      cancelIdInput: '',
      successMsgCancel: '',
      errorMsgCancel: '',
      successMsgEdit: '',
      errorMsgEdit: '',
      findByEmployeeEmailInput: '',
      findByCustomerEmailInput: '',
      successMsgEditWithEmployeeEmail: '',
      errorMsgEditWithEmployeeEmail: '',
      editByEmployeeEmailInput: '',
      editByEmployeeEmailIdInput: '',
      addEmployeeForm: {
        name: '',
        email: '',
        password: '',
        jobTitle: '',
        hourlyWage: '',
      }
    }
  },
  created() {
    this.getAllAppointments()

    axiosClient
      .get('/employees')
      .then((response) => {
        console.log(response.data); // log the response data
        this.employeeEmails = response.data.map((employee) => employee.email); // log each employee object
        this.errorMsgAll = '';
      })
      .catch((error) => {
        console.log(error.response.data);
      });


    axiosClient
      .get('/customers')
      .then((response) => {
        this.customerEmails = response.data.map((customer) => customer.email);
        this.errorMsgAll = ''
      })
      .catch((error) => {
        console.log(error.response.data)
      });

    axiosClient
      .get('/service')
      .then((response) => {
        this.serviceNames = response.data.map((service) => service.serviceName);
        this.errorMsgAll = ''
      })
      .catch((error) => {
        console.log(error.response.data)
      });
  },

  methods: {
    async Home() {
      await this.$router.push({ name: 'OwnerHome' })
    },
    async Appointments() {
      await this.$router.push({ name: 'OwnerViewAppointments' })
    },
    async ParkingLot() {
      await this.$router.push({ name: 'ParkingLotSettings' })
    },
    async Services() {
      await this.$router.push({ name: 'OwnerViewServices' })
    },
    async Customers() {
      await this.$router.push({ name: 'ViewMonthlyCustomer' })
    },
    async Employees() {
      await this.$router.push({ name: 'ManageEmployees' })
    },
    async Passes() {
      await this.$router.push({ name: 'OwnerPasses' })
    },
    async SignOut(){
      await this.$router.push({name: 'Home'})
    },

    getServiceAppointmentsByDate() {
      this.serviceAppointments = []
      axiosClient.get(`/serviceAppointment/date/${this.selectedDate}`)
        .then(response => {
          this.serviceAppointments = response.data
          this.errorMsgAll = ''
        })
        .catch(error => {
          console.log(error.response.data);
          this.errorMsgAll = error.response.data;
        });
      this.showByDateModal = false;
    },

    getServiceAppointmentById() {
      if (!/^\d+$/.test(this.searchAppointmentId)) {
        this.errorMsgEdit = "Please enter an integer id"
        return
      }
      this.serviceAppointments = []
      axiosClient.get(`/serviceAppointment/${this.searchAppointmentId}`)
        .then(response => {

          this.serviceAppointments.push(response.data)
          this.errorMsgAll = ''
        })
        .catch(error => {
          console.log(error.response.data);
          this.errorMsgAll = error.response.data;
        });
        this.showSearchAppointmentModal = false;
    },

    getServiceAppointmentsByCustomer() {
      this.serviceAppointments = []
      axiosClient.get(`/serviceAppointment/customer/${this.findByCustomerEmailInput}`)
        .then(response => {
          this.serviceAppointments = response.data
          this.errorMsgAll = ''
        })
        .catch(error => {
          console.log(error.response.data);
          this.errorMsgAll = error.response.data;
        })
      this.showCustomerModal = false
    },

    getServiceAppointmentsByEmployee() {
      this.serviceAppointments = []
      axiosClient.get(`/serviceAppointment/employee/${this.selectedEmployee}`)
        .then(response => {
          this.serviceAppointments = response.data
          this.errorMsgAll = ''
        })
        .catch(error => {
          console.log(error.response.data);
          this.errorMsgAll = error.response.data;
        })

      this.showEmployeeModal = false;
    },

    updateServiceAppointmentWithNewEmployee() {
      if (!/^\d+$/.test(this.editByEmployeeEmailIdInput)) {
        this.errorMsgAll = "Please enter an integer id"
        return
      }
      console.log(this.editByEmployeeEmailInput)
      axiosClient.put(`/serviceAppointment/employeeEmail/${this.editByEmployeeEmailIdInput}?employeeEmail=${this.editByEmployeeEmailInput}`)
        .then(response => {
          this.getAllAppointments()
          this.errorMsgAll = ''
        })
        .catch(error => {
          this.errorMsgAll = error.response.data
        })
      this.showUpdateEmployeeModal = false
    },

    editServiceAppointment() {
      // Format the time as HH:mm:ss
      const formattedTime = this.editStartTimeInput + ":00";
      console.log(formattedTime)
      if (!/^\d+$/.test(this.editIdInput)) {
        this.errorMsgEdit = "Please enter an integer id"
        return
      }
      const requestBody = {
        date: this.editDateInput,
        startTime: formattedTime,
        customerEmail: '',
        serviceName: this.editServiceNameInput
      };
      axiosClient.put(`/serviceAppointment/${this.editIdInput}`, requestBody)
        .then(response => {
          this.errorMsgAll = ''
          this.editIdInput = '';
          this.editDateInput = '';
          this.editStartTimeInput = '';
          this.editServiceNameInput = '';
          this.disableEditButton = false;
          this.getAllAppointments()
        })
        .catch(error => {
          console.log(error.response.data);
          this.errorMsgAll = error.response.data;
          this.disableEditButton = false;
        })
      this.showEditModal = false
    },

    cancelServiceAppointment() {
      if (!/^\d+$/.test(this.cancelIdInput)) {
        this.errorMsgCancel = "Please enter an integer id"
        return
      }

      axiosClient.delete(`/serviceAppointment/${this.cancelIdInput}`)
        .then(response => {
          this.errorMsgAll = "Appointment cancelled successfully";
          this.getAllAppointments()
        })
        .catch(error => {
          console.log(error.response.data);
          this.errorMsgAll = error.response.data;
        })
      this.showCancelModal = false
    },

    getAllAppointments() {
      axiosClient.get('/serviceAppointment')
        .then((response) => {
          this.serviceAppointments = response.data;
          this.appointmentIds = response.data.map((appointment) => appointment.id);
          this.errorMsgAll = ''
        })
        .catch((error) => {
          console.log(error.response.data);
          this.errorMsgAll = error.response.data;
        });
    },



  },

  computed: {
    createUserButtonDisabled() {
      return !this.email.trim() || !this.password.trim() || !this.name.trim()
    }
  }
}

</script>


<style scoped>
.button-container {
  margin-right: 10px;
}

.btn-equal-size {
  width: 150px;
  height: 100px;
}

table.center,
div.center {
  margin: 0 auto;
}

table {
  padding: 20px;
}

button {
  margin: 20px;
}

a {
  padding: 20px;
}

.bordered-table th,
.bordered-table td {
  border: 1px solid black;
  padding: 8px;
}

.modal {
  padding-top: 100px;
  display: block;
  position: fixed;
  z-index: 1;
  margin-top: 100px;
  width: 50%;
  height: 90%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.modal-content {
  background-color: #fefefe;
  margin: auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}</style>