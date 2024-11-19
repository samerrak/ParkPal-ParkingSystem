
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

  <div class="container-fluid">
    <div class="row">
      <div class="col-sm-10">
        <h1>Employees</h1>
        <hr><br><br>
        <button type="button" class="btn btn-success btn-sm" v-b-modal.employee-modal>Create</button>
        <button type="button" class="btn btn-success btn-sm" v-b-modal.edit-employee-modal :disabled="selectedEmployee=== null" >Update</button>
        <button type="button" class="btn btn-success btn-sm" :disabled="selectedEmployee=== null" @click="onViewSchedule(selectedEmployee) "> View Schedule</button>
        <button type="button" class="btn btn-danger btn-sm"  :disabled="selectedEmployee=== null" @click="onDeleteEmployee(selectedEmployee) "> Delete </button>

        <br><br>
        <table class="table">
          <thead>
            <tr>
              <th scope="col">Name</th>
              <th scope="col">Email</th>
              <th scope="col">JobTitle</th>
              <th scope="col">Hourly Wage</th>

            </tr>
          </thead>
          <tbody>
            <tr  v-for="(employee, index) in employees" :key="index"   @click="handleRowClick(employee)"
            @mouseover="handleRowHover(employee)"
            @mouseout="handleRowHover(null)"
            :class="{ 'row-highlighted': employee === hoveredEmployee,
            'row-selected': employee === selectedEmployee }">
                <td>{{employee.name}}</td>
                <td>{{employee.email}}</td>
                <td>{{employee.jobTitle}}</td>
                <td>{{employee.hourlyWage}}</td>
                <!-- <td>
                <div class="btn-group" role="group">
                  <button type="button" class="btn btn-warning btn-sm" :disabled="selectedEmployee=== null" v-b-modal.edit-employee-modal>Update</button>
                  <button type="button" class="btn btn-danger btn-sm">Delete</button>
                </div>
              </td> -->
            </tr>
          </tbody>
        </table>
      </div>
    </div>
<b-modal ref="addEmployeeModal"
         id="employee-modal"
         title="Add a new employee"
         hide-footer>
  <b-form @submit="onSubmit" @reset="onReset" class="w-100">
    <b-form-group id="form-name-group"
                  label="Name:"
                  label-for="form-name-input">
      <b-form-input id="form-name-input"
                    type="text"
                    v-model="addEmployeeForm.name"
                    required
                    placeholder="Enter name">
      </b-form-input>
    </b-form-group>
    <b-form-group id="form-email-group"
                  label="Email:"
                  label-for="form-email-input">
      <b-form-input id="form-email-input"
                    type="email"
                    v-model="addEmployeeForm.email"
                    required
                    placeholder="Enter email">
      </b-form-input>
    </b-form-group>
    <b-form-group id="form-password-group"
                  label="Password:"
                  label-for="form-password-input">
      <b-form-input id="form-password-input"
                    type="password"
                    v-model="addEmployeeForm.password"
                    required
                    placeholder="Enter password">
      </b-form-input>
    </b-form-group>
    <b-form-group id="form-job-title-group"
                  label="Job Title:"
                  label-for="form-job-title-input">
      <b-form-input id="form-job-title-input"
                    type="text"
                    v-model="addEmployeeForm.jobTitle"
                    required
                    placeholder="Enter job title">
      </b-form-input>
    </b-form-group>
    <b-form-group id="form-hourly-wage-group"
                  label="Hourly Wage:"
                  label-for="form-hourly-wage-input">
      <b-form-input id="form-hourly-wage-input"
                    type="number"
                    v-model="addEmployeeForm.hourlyWage"
                    required
                    placeholder="Enter hourly wage">
      </b-form-input>
    </b-form-group>
    <b-button type="submit" variant="primary">Submit</b-button>
    <b-button type="reset" variant="danger">Reset</b-button>
  </b-form>
</b-modal>

<b-modal ref="editEmployeeModal"
         id="edit-employee-modal"
         title="Edit employee"
         hide-footer>
  <b-form @submit="onSubmitUpdate" @reset="onReset" class="w-100">
    <b-form-group id="form-name-group"
                  label="Name:"
                  label-for="form-name-input">
      <b-form-input id="form-name-input"
                    type="text"
                    v-model="editEmployeeForm.name"
                    :placeholder="selectedEmployee ? selectedEmployee.name : 'Enter Name: '">
      </b-form-input>
    </b-form-group>

    <b-form-group id="form-password-group"
                  label="Password:"
                  label-for="form-password-input">
      <b-form-input id="form-password-input"
                    type="password"
                    v-model="editEmployeeForm.password"
                    :placeholder="selectedEmployee ? selectedEmployee.password : 'Enter Password: '">
      </b-form-input>
    </b-form-group>
    <b-form-group id="form-job-title-group"
                  label="Job Title:"
                  label-for="form-job-title-input">
      <b-form-input id="form-job-title-input"
                    type="text"
                    v-model="editEmployeeForm.jobTitle"
                    :placeholder="selectedEmployee ? selectedEmployee.jobTitle: 'Enter Job Title:' ">

      </b-form-input>
    </b-form-group>
    <b-form-group id="form-hourly-wage-group"
                  label="Hourly Wage:"
                  label-for="form-hourly-wage-input">
      <b-form-input id="form-hourly-wage-input"
                    type="number"
                    v-model="editEmployeeForm.hourlyWage"
                    :placeholder="selectedEmployee ? selectedEmployee.hourlyWage.toString() : 'Enter Hourly Wage:' ">
      </b-form-input>
    </b-form-group>
    <b-button type="submit" variant="primary">Save Changes</b-button>
    <b-button type="reset" variant="danger">Reset</b-button>
  </b-form>
</b-modal>
<b-modal ref="ServiceAppointmentModal"
         id="serviceAppointments-modal"
         title="Employee Scheduled Appointments"
         hide-footer>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Service Name</th>
            <th>Date</th>
            <th>Start Time</th>
            <th>End Time</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(serviceAppointment, index) in serviceAppointments" :key="index">
            <td>{{ serviceAppointment.id }}</td>
            <td>{{ serviceAppointment.serviceName }}</td>
            <td>{{ serviceAppointment.date }}</td>
            <td>{{ serviceAppointment.startTime }}</td>
            <td>{{ serviceAppointment.endTime }}</td>
          </tr>
        </tbody>
      </table>
    </b-modal>

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
            employees: [],
            selectedEmployee: null,
            selectedEmployeePassword: '',
            selectedEmployeeJobTitle: '',
            selectedEmployeeHourlyWage: 0,
            selectedEmployeeName: '',
            selectedEmployeeEmail: '',
            hoveredEmployee: null,
            addEmployeeForm: {
                name: '',
                email: '',
                password: '',
                jobTitle: '',
                hourlyWage: '',
            },
            editEmployeeForm: {
                name: '',
                password: '',
                jobTitle: '',
                hourlyWage: ''
            },
            serviceAppointments: [],

        };
    },
    created() {
        // Fetch all employees on component mount
        this.fetchEmployees();
    },
    methods: {


        fetchEmployees() {
           axiosClient.get("employees").then((response) => {
            this.employees = response.data
           }).catch((err) => {
           });
        },

        createEmployee(request) {
            // Promise is used to prevent the form from hiding if request was wrong
            return new Promise((resolve, reject) => {
                axiosClient.post("/employee/create", request)
                .then((response) => {
                    alert("Employee account with email " + response.data.email + " has been created successfully")
                    this.fetchEmployees();
                    resolve();
                })
                .catch((err) => {
                    this.errorMsg = `Failed to create: ${err.response.data}`
                    reject(err);
                    alert(this.errorMsg)
                    });
            });
        },

        editEmployee(request) {
            // Promise is used to prevent the form from hiding if request was wrong
            return new Promise((resolve, reject) => {
                axiosClient.put("/employee/update", request)
                .then((response) => {
                    alert("Employee account with email " + response.data.email + " has been edited successfully")
                    this.fetchEmployees();
                    this.selectedEmployee = null;
                    resolve();
                })
                .catch((err) => {
                    this.errorMsg = `Failed to edit Employee: ${err.response.data}`
                    reject(err);
                    alert(this.errorMsg)
                    });
            });
        },
        onDeleteEmployee(employee) {
            // Promise is used to prevent the form from hiding if request was wrong
            return new Promise((resolve, reject) => {
                this.selectedEmployeeEmail = employee != null ? employee.email : '';
                axiosClient.delete('/employee/delete/' + this.selectedEmployeeEmail)
                .then((response) => {
                    this.fetchEmployees();
                    this.selectedEmployee = null;
                    this.selectedEmployeeEmail = '';
                    resolve();
                    window.location.reload();
                })
                .catch((err) => {
                    this.errorMsg = `Failed to delete Employee: ${err.response.data}`
                    reject(err);
                    alert(this.errorMsg)
                    });
            });
        },

        onViewSchedule(selectedEmployee){
            axiosClient.get("/serviceAppointment/employee/" + selectedEmployee.email)
            .then((response) => {
                this.serviceAppointments = response.data
                this.$bvModal.show('serviceAppointments-modal')
            }).catch((err) => {
                alert(err.response.data)
           });
        },

        initForm() {
        this.addEmployeeForm.name = '';
        this.addEmployeeForm.email = '';
        this.addEmployeeForm.password = '';
        this.addEmployeeForm.jobTitle = '';
        this.addEmployeeForm.hourlyWage = '';

        this.editEmployeeForm.name = '';
        this.editEmployeeForm.email = '';
        this.editEmployeeForm.password = '';
        this.editEmployeeForm.jobTitle = '';
        this.editEmployeeForm.hourlyWage = '';


        },

        onSubmit(evt) {
        evt.preventDefault();
            const request = {
                name: this.addEmployeeForm.name,
                email: this.addEmployeeForm.email,
                password: this.addEmployeeForm.password,
                jobTitle: this.addEmployeeForm.jobTitle,
                hourlyWage: this.addEmployeeForm.hourlyWage,
            };
            this.createEmployee(request)
            // Only hide modal if succesfull
            .then(() => {
                this.$refs.addEmployeeModal.hide();
                this.initForm();
             })
            .catch(() => {
            // handle error if needed
            });

        },
        onSubmitUpdate(evt){
                const request = {
                    name: this.editEmployeeForm.name || this.selectedEmployee.name,
                    email: this.selectedEmployee.email ,
                    password: this.editEmployeeForm.password || this.selectedEmployee.password,
                    jobTitle: this.editEmployeeForm.jobTitle || this.selectedEmployee.jobTitle ,
                    hourlyWage: this.editEmployeeForm.hourlyWage || this.selectedEmployee.hourlyWage,
                };
            this.editEmployee(request)
            // Only hide modal if succesfull
            .then(() => {
                this.$refs.editEmployeeModal.hide();
                this.initForm();
             })
            .catch(() => {
            // handle error if needed
            });

        },
        handleRowClick(employee) {
          this.selectedEmployee = employee;
          console.log('selected new employee');
        },
        handleRowHover(employee) {
          this.hoveredEmployee = employee;
        },
        onReset(evt) {
        evt.preventDefault();
        // this.$refs.addEmployeeModal.hide();
        this.initForm();
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
      await this.$router.push({name: 'OwnerPasses'})
    },
    async SignOut(){
      await this.$router.push({name: 'Home'})
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
    .row-highlighted {
    background-color: #cbcbcb;
    cursor: pointer;
    }
    .row-selected{
    background-color: #8e8e8e;
    }

</style>

