<template>
  <div class="internalCreatePage">

    <div class="position-relative overflow-hidden p-3 p-md-5 m-md-3 text-center bg-light">

        <form id="form-update" >
          <div class="form-group">
            <label >Customer Email (optional)</label>
            <input type="text" class="form-control"   placeholder="johnsmith@email.com"  v-model="customerEmail">
          </div>
          <div class="form-row" style="margin-top: 3%">
            <div class="form-group col-md-6">
              <label>Months</label>
              <input class="form-control" type="number" placeholder="10" v-model="numberOfMonths">
            </div>
            <div class="form-group col-md-6">
              <label >Floor Number</label>
              <select class="custom-select" required v-model="floorNumber"  @change="createSpotNumbers()">
                <!-- <option v-for="floor in floors" :key="floor.floorNumber" :value="floor.floorNumber">{{floor.floorNumber}}</option> -->
                <option v-for="(floorNumber, index) in floorNumbers.sort((a, b) => a - b)" :key="index">{{ floorNumber }}</option>
              </select>
            </div>
          </div>
          <div class="form-group">
            <label >Confirmation Code</label>
            <input type="text" class="form-control"  placeholder="JK95HO95T3" v-model="confirmationCode">
          </div>
          <div class="form-group">
            <label >License Plate</label>
            <input type="text" class="form-control"   placeholder="T3ST41"  v-model="licensePlate">
          </div>
          <div class="form-row">
            <div class="form-group col-md-6">
              <label>Spot Number</label>
              <!-- <input type="text" class="form-control" id="spot" placeholder="A24" v-model="spotNumber" > -->
              <select v-model="spotNumber" class="form-control" :disabled="isSelectDisabled" @click="createSpotNumbers()">
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
              <input class="form-check-input"  type="checkBox" id="isLarge" :disabled="isSelectDisabled" @change="onIsLargeChange">
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
  name: 'InternalCreateMonthlyPass',
  props: {
    // email: {
    //   type: String,
    //   required: true
    // }
  },
  data() {
    return {
      errorMsg: '',
      floors: [],
      floorNumbers: [],
      spotNumber: '',
      confirmationCode: '',
      licensePlate: '',
      floorNumber: '',
      isLarge: false,
      startDate: '',
      numberOfMonths:0,
      customerEmail: '',
      spotNumbers: [],
    };
  },
  created() {
    axiosClient.get("/floor")
      .then(response => {
        this.floors = response.data
        // this.floorNumbers = response.data.map((floor) => floor.floorNumber)
        this.floorNumbers = []
        for (const floor of this.floors) {
          if (floor.isMemberOnly){
            this.floorNumbers.push(floor.floorNumber)
          }
        }
      })
      .catch(error => {
        alert(error.data)
      })
  },
  methods: {
    // async RouteHome() {
    //   await this.$router.push({name: 'MonthlyCustomerHome', params: {email: this.email}})
    // },
    // async Reload() {
    //   await this.$router.push({name: 'MonthlyCustomerPasses', params: {email: this.email}})
    // },
    // async RouteApp() {
    //   await this.$router.push({name: 'MonthlyCustomerAppointments', params: {email: this.email}})
    // },
    // async RouteManage() {
    //   await this.$router.push({name: 'MonthlyCustomerManageAccount', params: {email: this.email}})
    // },
    async createPass() {
      if (this.customerEmail == ''){
        this.customerEmail = null
      }
      const request = {numberOfMonths: this.numberOfMonths, spotNumber: this.spotNumber, confirmationCode: this.confirmationCode, licensePlate: this.licensePlate,
        floorNumber: this.floorNumber, isLarge: document.getElementById(`isLarge`).checked, startDate: this.startDate, customerEmail: this.customerEmail};
      axiosClient.post("/monthlypass", request)
        .then((response) => {
          alert("Your pass has been created successfully")
        })
        .catch((err) => {
          this.errorMsg = `Failed to create: ${err.response.data}`
          alert(this.errorMsg)
        })
      // await this.RouteHome()
    },
    onIsLargeChange() {
      this.isLarge = !this.isLarge;
      this.spotNumber = ''
    },
    createSpotNumbers(){
      const isLarge = this.isLarge
      this.spotNumbers  = []
      const floor = null

      console.log(isLarge)

      axiosClient.get("/floor/" + this.floorNumber).then((response) => {
                    const numSpots = isLarge ? response.data.largeSpotCapacity : response.data.smallSpotCapacity;
                    const spotType = isLarge ? 'L' : 'S';
                    for (let i = 1; i <= numSpots; i++) {
                      this.spotNumbers.push(`${this.floorNumber}${spotType}${i}`);
                    }
                    console.log(this.spotNumbers)

                }).catch((err) => {

                alert(err.response.data)
                })
    },
  },



  computed: {
    createUserButtonDisabled() {
      return !(this.numberOfMonths !== 0)|| !this.spotNumber.trim() || !this.confirmationCode.trim() || !this.licensePlate.trim()
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
