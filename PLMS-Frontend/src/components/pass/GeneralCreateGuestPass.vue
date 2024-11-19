<template>
  <div id="guestpass_creation_search">
    <nav class="site-header sticky-top py-1">
    <div class="container d-flex flex-column flex-md-row justify-content-between">
      <a class="py-2">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-box" viewBox="0 0 16 16">
          <path d="M8.186 1.113a.5.5 0 0 0-.372 0L1.846 3.5 8 5.961 14.154 3.5 8.186 1.113zM15 4.239l-6.5 2.6v7.922l6.5-2.6V4.24zM7.5 14.762V6.838L1 4.239v7.923l6.5 2.6zM7.443.184a1.5 1.5 0 0 1 1.114 0l7.129 2.852A.5.5 0 0 1 16 3.5v8.662a1 1 0 0 1-.629.928l-7.185 2.874a.5.5 0 0 1-.372 0L.63 13.09a1 1 0 0 1-.63-.928V3.5a.5.5 0 0 1 .314-.464L7.443.184z"/>
        </svg>      </a>
      <a class="py-2 d-none d-md-inline-block" @click="Home">Return to Home Page</a>
    </div>
  </nav>
      <div id="guestpasscreation">
  <h2>Book a Guest Pass</h2>
  <table align="center" style="border-collapse: collapse;">
    <tr>
      <td style="border: none;"></td>
      <td style="border: none;">Floor number</td>
      <td style="border: none;">Large</td>
      <td style="border: none;">License plate</td>
      <td style="border: none;"></td>
    </tr>
    <tr>
      <td style="border: none;"></td>
      <td style="border: none;">
        <!-- <input type="number" placeholder="floor number" id="floor" v-model="floorNumber"> -->
        <select class="custom-select" required v-model="floorNumber">
                <!-- <option v-for="floor in floors" :key="floor.floorNumber" :value="floor.floorNumber">{{floor.floorNumber}}</option> -->
                <option v-for="(floorNumber, index) in floorNumbers.sort((a, b) => a - b)" :key="index" >{{ floorNumber }}</option>
        </select>
      </td>
      <td style="border: none;">
        <input type="checkBox" id="isLarge" @click="onIsLargeChange">
      </td>
      <td style="border: none;">
        <!-- <input type="text" placeholder="spot number" id="spot" v-model="spotNumber" v-bind:disabled="createGuestPassButtonDisabled"> -->
        <select v-model="spotNumber" class="form-control" :disabled="selectSpotDisabled"  @click="getSpotNumbers()">
                <option value="" disabled>Select a spot</option>
                <option v-for="(spot,index) in spotNumbers" :key="index">{{ spot }}</option>
        </select>
      </td>
      <td style="border: none;"></td>
    </tr>
    <tr>
      <td style="border: none;"></td>
      <td style="border: none;">License Plate</td>
      <td colspan="2" style="border: none;">15 minute increments</td>
      <td style="border: none;"></td>
    </tr>
    <tr>
      <td style="border: none;"></td>
      <td style="border: none;">
        <input type="text" placeholder="license plate" id="plate" v-model="licensePlate">
      </td>
      <td style="border: none;" colspan="2">
        <input type="number" placeholder="15 minute increments" id="increments" v-model="numberOfFifteenMinuteIncrements">
      </td>
      <td style="border: none;"></td>
    </tr>
    <tr>
      <td style="border: none;"></td>
      <td style="border: none;"></td>
      <td style="border: none;"></td>
      <td style="border: none;"></td>
      <td style="border: none;"></td>
    </tr>
  </table>
</div>



    <br>

    <p>
      <button class="btn btn-primary" v-bind:disabled="createGuestPassButtonDisabled" @click="createGuestPass()">Book a Guest Pass</button><br>
    </p>

    <p class="error">{{ errorMsgCreation }}</p>

    <div id="guestpass_search">
      <h2>Find Guest Pass</h2>
      <table align = center>
        <tr>
          <td>Guest Pass ID</td>
          <td>Confirmation code</td>
        </tr>
        <tr>
          <td>
            <input type="text" placeholder="guest pass id" v-model="id">
          </td>
          <td>
              <input type="text" placeholder="confirmation code" v-model="confirmationCodeIdentification">
          </td>
        </tr>
      </table>

    <br>

    <p>
      <button class="btn btn-primary" v-bind:disabled="findGuestPassButtonDisabled" @click="findGuestPass()">Find Pass</button>
    </p>

    <p class="error">{{ errorMsgIdentification }}</p>

    </div>


  </div>
</template>

<script>
import axios from 'axios'
var config = require('../../../config')
const frontendUrl = config.dev.host + ':' + config.dev.port;

const AXIOS = axios.create({
  baseURL: config.dev.backendBaseUrl,
  headers: { 'Access-Control-Allow-Origin': frontendUrl },
})

export default {
  name: 'GuestCreateGuestPass',
  data(){
    return{
      floors: [],
      floorNumbers: [],
      spotNumber: '',
      confirmationCodeCreation: '',
      licensePlate: '',
      floorNumber: '',
      numberOfFifteenMinuteIncrements: '',
      errorMsgCreation: '',
      errorMsgIdentification: '',
      isLarge : false,
      confirmationCodeIdentification: '',
      id: '',

      spotNumbersMap:{},
      spotNumbers:[]

    };
  },

  created(){
    AXIOS.get('/guestPass')
    .then((response) => {
      console.log(response.data);
    })
    .catch((err) => {
      console.log(err);
    });

    AXIOS.get("/floor")
      .then(response => {
        this.floors = response.data
        // this.floorNumbers = response.data.map((floor) => floor.floorNumber)
        this.floorNumbers = []

       // Get all floors and subsequent floor numbers
       for (const floor of this.floors) {

        if (!floor.isMemberOnly) {
          const floorNumber = floor.floorNumber;
          const largeSpotCapacity = floor.largeSpotCapacity;
          const smallSpotCapacity = floor.smallSpotCapacity;

          // Generate spot numbers for large and for small spots
          const largeSpots = [];
          for (let i = 1; i <= largeSpotCapacity; i++) {
            largeSpots.push(`${floorNumber}L${i}`);
          }

          const smallSpots = [];
          for (let i = 1; i <= smallSpotCapacity; i++) {
            smallSpots.push(`${floorNumber}S${i}`);
          }

          this.floorNumbers.push(floorNumber);

          // Add the spot numbers to the hashmap
          this.spotNumbersMap[floorNumber] = {
            large: largeSpots,
            small: smallSpots
          };
        }
      }
      console.log('spotnumbermap', this.spotNumbersMap)
      })
      .catch(error => {
      })
  },

  methods: {
    async Home(){
      await this.$router.push({name: 'Home'})
    },
    createGuestPass(){
      this.confirmationCode = this.generateConfirmationCode()
      const request = {spotNumber: this.spotNumber, confirmationCode: this.confirmationCode, licensePlate: this.licensePlate,
      floorNumber: this.floorNumber, numberOfFifteenMinuteIncrements: this.numberOfFifteenMinuteIncrements,
      isLarge: document.getElementById(`isLarge`).checked};
      AXIOS.post('/guestPass', request)
      .then((response) => {
        this.errorMsgCreation = `Guest Pass Booked!  \t ID: ${response.data.id}  \n Confirmation Code: ${this.confirmationCode} `
      })
      .catch((err) => {
        this.errorMsgCreation = `Failed to book pass: ${err.response.data}`;
      })
    },

    findGuestPass(){
      AXIOS.get(`/guestPass/${this.id}`)
      .then((response) => {
        const pass = response.data
        if(pass.confirmationCode === this.confirmationCodeIdentification){
          this.errorMsgIdentification = `Guest Pass found!: \n fee: ${response.data.id}, Spot number: ${response.data.spotNumber}, License plate: ${response.data.licensePlate}, Is large: ${response.data.isLarge}, Date: ${response.data.date}, Start Time: ${response.data.startTime}, End Time: ${response.data.endTime}, Floor number: ${response.data.floorNumber}`
        }
        else{
          this.errorMsgIdentification = "The confirmation code entered is incorrect"
        }

      })
      .catch((err) => {
        this.errorMsgIdentification = `Failed to find pass: ${err.response.data}`;
      })

    },

    onIsLargeChange() {
      this.isLarge = !this.isLarge;
      this.spotNumber = ''
      // this.getSpotNumbers();
    },

    getSpotNumbers(){
      console.log(this.floorNumber);
      console.log(this.floorNumbers);
      const floorNumber = this.floorNumber.toString();
      let spotType = '';
      if(this.isLarge) {spotType = "large"}
      else {spotType = "small"}
      console.log(spotType);
      if (this.spotNumbersMap[floorNumber] && this.spotNumbersMap[floorNumber][spotType]) {
        this.spotNumbers = this.spotNumbersMap[floorNumber][spotType]
        console.log(this.spotNumbers)
      } else {
        console.log(`Spot numbers not found for floor ${floorNumber} and spot type ${spotType}.`)
      }
    },
    generateConfirmationCode() {
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
    createGuestPassButtonDisabled() {
      return !this.spotNumber.trim() || !this.licensePlate.trim() || !this.floorNumber.trim() || !this.numberOfFifteenMinuteIncrements.trim();
    },

    findGuestPassButtonDisabled(){
      return !this.id.trim() || !this.confirmationCodeIdentification.trim();
    },
    selectSpotDisabled(){
    return (this.floorNumber == '')
    },

  }

}


</script>



<style>
    td, th {
  border: 1px solid black;
  padding: 0.5em;
}
#home-page {
  display: flex;
  flex-direction: column;
  padding: 5%;
  align-items: stretch;
}
.error {
  color: red;
}
label {
        display: inline-block;
        width: 150px;
        text-align: right;
      }
</style>
