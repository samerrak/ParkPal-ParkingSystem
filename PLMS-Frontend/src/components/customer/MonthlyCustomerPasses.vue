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
        <a class="py-2 d-none d-md-inline-block"  @click="Reload">Passes</a>
        <a class="py-2 d-none d-md-inline-block" @click="RouteApp">Appointments</a>
        <a class="py-2 d-none d-md-inline-block" @click="RouteStart">Sign Out</a>
        <a class="py-2 d-none d-md-inline-block"  @click="Reload">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bootstrap-reboot" viewBox="0 0 16 16">
            <path d="M1.161 8a6.84 6.84 0 1 0 6.842-6.84.58.58 0 1 1 0-1.16 8 8 0 1 1-6.556 3.412l-.663-.577a.58.58 0 0 1 .227-.997l2.52-.69a.58.58 0 0 1 .728.633l-.332 2.592a.58.58 0 0 1-.956.364l-.643-.56A6.812 6.812 0 0 0 1.16 8z"/>
            <path d="M6.641 11.671V8.843h1.57l1.498 2.828h1.314L9.377 8.665c.897-.3 1.427-1.106 1.427-2.1 0-1.37-.943-2.246-2.456-2.246H5.5v7.352h1.141zm0-3.75V5.277h1.57c.881 0 1.416.499 1.416 1.32 0 .84-.504 1.324-1.386 1.324h-1.6z"/>
          </svg>
        </a>
      </div>
    </nav>
    <!-- Used this button to debug, needs to be removed -->
    <!-- <button type="button" class="btn btn-success btn-sm" @click="generateConfirmationCode()">Create</button> -->

    <div class="position-relative overflow-hidden p-3 p-md-5 m-md-3 text-center bg-light">

        <form id="form-update" >
          <div class="form-row" style="margin-top: 3%">
            <div class="form-group col-md-6">
              <label>Months</label>
              <input class="form-control" type="number" placeholder="10" v-model="numberOfMonths">
            </div>
            <div class="form-group col-md-6">
              <label >Floor Number</label>
              <select class="custom-select" required v-model="floorNumber" @change="getSpotNumbers()">
                <!-- <option v-for="floor in floors" :key="floor.floorNumber" :value="floor.floorNumber">{{floor.floorNumber}}</option> -->
                <option v-for="(floorNumber, index) in floorNumbers.sort((a, b) => a - b)" :key="index" >{{ floorNumber }}</option>
              </select>
            </div>
          </div>
          <!-- <div class="form-group">
            <label >Confirmation Code</label>
            <input type="text" class="form-control"  placeholder="JK95HO95T3" v-model="confirmationCode">
          </div> -->
          <div class="form-group">
            <label >License Plate</label>
            <input type="text" class="form-control"   placeholder="T3ST41"  v-model="licensePlate">
          </div>
          <div class="form-row">

            <div class="form-group col-md-6">
              <label>Spot Number</label>
              <select v-model="spotNumber" class="form-control" :disabled="isSelectDisabled"  @click="getSpotNumbers">
                <option value="" disabled>Select a spot</option>
                <option v-for="(spot,index) in spotNumbers" :key="index">{{ spot }}</option>
              </select>
            </div>
            <div class="form-group col-md-6">
              <label>Start Date</label>
              <input type="date" class="form-control" v-model="startDate">
            </div>
          </div>
          <div class="form-group">
            <div class="form-check">
              <input class="form-check-input"  type="checkBox" id="isLarge" @click="onIsLargeChange()">
              <label class="form-check-label" >
                Large Spot
              </label>
            </div>
          </div>
          <button id="create" type="submit" v-bind:disabled="createUserButtonDisabled" @click="createPass()" class="btn btn-primary">Create Pass</button>

        </form>
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
  name: "MonthlyCustomerPasses", // Page to book monthly customer passes
  props: {
    email: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      errorMsg: '',
      floors: [],
      floorNumbers: [],
      spotNumbers: [],
      spotNumber: '',
      licensePlate: '',
      floorNumber: 0,
      isLarge: false,
      startDate: '',
      numberOfMonths:0,
      spotNumbersMap: {},
    };
  },
  created() {
    axiosClient.get("/floor") // Get floors to display the available spots
      .then(response => {
        this.floors = response.data
        this.floorNumbers = []
       for (const floor of this.floors) {
        console.log(floor.memberOnly)
        if (floor.isMemberOnly) {
          const floorNumber = floor.floorNumber;
          const largeSpotCapacity = floor.largeSpotCapacity;
          const smallSpotCapacity = floor.smallSpotCapacity;
          const largeSpots = [];
          for (let i = 1; i <= largeSpotCapacity; i++) {
            largeSpots.push(`${floorNumber}L${i}`);
          }

          const smallSpots = [];
          for (let i = 1; i <= smallSpotCapacity; i++) {
            smallSpots.push(`${floorNumber}S${i}`);
          }

          this.floorNumbers.push(floorNumber);
          this.spotNumbersMap[floorNumber] = {
            large: largeSpots,
            small: smallSpots
          };
        }
      }
      console.log('spotnumbermap', this.spotNumbersMap)
      })
      .catch(error => {

        alert(error.data)
      })
  },
  methods: {
    //Redirection to pages
    async RouteStart() {
      await this.$router.push({name: 'Home'})
    },
    async RouteHome() {
      await this.$router.push({name: 'MonthlyCustomerHome', params: {email: this.email}})
    },
    async Reload() {
      await this.$router.push({name: 'MonthlyCustomerPasses', params: {email: this.email}})
    },
    async RouteApp() {
      await this.$router.push({name: 'MonthlyCustomerAppointments', params: {email: this.email}})
    },
    async RouteManage() {
      await this.$router.push({name: 'MonthlyCustomerManageAccount', params: {email: this.email}})
    },
    async createPass() { // Method to create a new monthly pass
      this.confirmationCode = this.generateConfirmationCode()
      const request = {numberOfMonths: this.numberOfMonths, spotNumber: this.spotNumber, confirmationCode: this.confirmationCode, licensePlate: this.licensePlate,
        floorNumber: this.floorNumber, isLarge: this.isLarge, startDate: this.startDate, customerEmail: this.email};
      axiosClient.post("/monthlypass", request)
        .then((response) => {
          alert(`Your pass has been created successfully, \n Confirmation code: ${response.data.confirmationCode}`)
        })
        .catch((err) => {
          this.errorMsg = `Failed to create: ${err.response.data}`
          alert(this.errorMsg)
        })
      await this.RouteHome()
    },
    onIsLargeChange() { // Toggle monthly pass to large spot monthly passes
      this.isLarge = !this.isLarge;
      this.spotNumber = ''
    },

    getSpotNumbers(){ // Method to format the spot number of a particular spot
      const floorNumber = this.floorNumber.toString()
      let spotType = ''
      if(this.isLarge) spotType = "large"
      else spotType = "small"
      if (this.spotNumbersMap[floorNumber] && this.spotNumbersMap[floorNumber][spotType]) {
        this.spotNumbers = this.spotNumbersMap[floorNumber][spotType]
        console.log(this.spotNumbers)
      } else {
        console.log(`Spot numbers not found for floor ${floorNumber} and spot type ${spotType}.`)
      }
    },

    generateConfirmationCode() { // Create a random confirmation code
    console.log('test')
    const letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    let code = '';

    // Generate the first two letters
    for (let i = 0; i < 2; i++) {
      code += letters.charAt(Math.floor(Math.random() * letters.length));
    }

    // Add the underscore
    code += '_';

    // Generate the six numbers
    for (let i = 0; i < 6; i++) {
      code += Math.floor(Math.random() * 10);
    }
    console.log(code)
    return code;
  },

  },


  computed: {
    createUserButtonDisabled() {
      return !(this.numberOfMonths !== 0)|| !this.spotNumber.trim() || !this.licensePlate.trim()
        ||  !(this.floorNumber !== 0)  || !this.startDate.trim();
    },
    isSelectDisabled(){
    return (this.floorNumber == '')
    },
  }


}
</script>

<style scoped>

</style>

