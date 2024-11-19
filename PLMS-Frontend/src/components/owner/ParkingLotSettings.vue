<template>
  <div id="settings">
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
    <div id="parkinglot_settings">
      <h2 class="parkinglot section title">Parking Lot Settings</h2>
      <form>
        <label for="opening">Opening Hours</label>
        <input type="time" id="opening" v-model="newOpeningTime"> <br>
        <label for="closing">Closing Hours</label>
        <input type="time" id="closing" v-model="newClosingTime"> <br>
        <label for="smallspot">Small spot Fee</label>
        <input type="number" id="smallspot" v-model="newSmallSpotFee"> <br>
        <label for="largespot">Large spot Fee</label>
        <input type="number" id="largespot" v-model="newLargeSpotFee"> <br>
        <label for="smallmonthly">Small spot monthly flat fee</label>
        <input type="number" id="smallmonthly" v-model="newSmallSpotMonthlyFlatFee"> <br>
        <label for="largemonthly">Large spot monthly flat fee</label>
        <input type="number" id="largemonthly" v-model="newLargeSpotMonthlyFlatFee"> <br>
      </form>
      <button class="btn btn-primary" v-bind:disabled="createParkingButtonDisabled"
        @click="createOrUpdateParkingLot()">Confirm</button><br>
    </div>
  </div>

  <div>
    <h2 class="floor section title">Floor Settings</h2>
    <div id="floor_settings" style="display: flex; justify-content: center; align-items: center;">
      <table>
        <tr>
          <th>Floor number</th>
          <th>Small spot capacity</th>
          <th>Large spot capacity</th>
          <th>Member only</th>
        </tr>
        <tr v-for="f in floors">
          <td :id="f.floorNumber">{{ f.floorNumber }}</td>
          <td :id="`${f.floorNumber} 2`">{{ f.smallSpotCapacity }}</td>
          <td :id="`${f.floorNumber} 3`">{{ f.largeSpotCapacity }}</td>
          <td :id="`${f.floorNumber} 4`">{{ f.isMemberOnly ? 'Yes' : 'No' }}</td>
          <td><button class="btn btn-danger" id="deletefloor" @click="deletefloor(f.floorNumber)">Delete</button></td>
          <td><button class="btn btn-success" :id="`editfloor ${f.floorNumber}`"
              @click="editfloor(f.floorNumber)">Edit</button></td>
        </tr>
        <tr>
          <td><input type="text" id="floornumber" v-model="newFloorNumber"></td>
          <td><input type="text" id="smallcapacity" v-model="newSmallSpotCapacity"></td>
          <td><input type="text" id="largecapacity" v-model="newLargeSpotCapacity"></td>
          <td><input type="checkbox" id="memberonly" v-model="newMemberOnly" v-bind:value="true"></td>
        </tr>
        <button class="btn btn-primary" id="addfloor" @click="addFloor()">Add Floor</button>
      </table>
    </div>
    <p class="error">{{ errorMsg }}</p>


  </div>
  </div>
</template>

<script>
import axios from 'axios';
import config from '../../../config';
const axiosClient = axios.create({
  // Note the baseURL, not baseUrl
  baseURL: config.dev.backendBaseUrl
});
export default {
  name: 'ParkingLotSettings',
  data() {
    return {
      floors: [],
      parkinglot: {},
      newOpeningTime: '',
      newClosingTime: '',
      newLargeSpotFee: '',
      newSmallSpotFee: '',
      newSmallSpotMonthlyFlatFee: '',
      newLargeSpotMonthlyFlatFee: '',
      newFloorNumber: '',
      newSmallSpotCapacity: '',
      newLargeSpotCapacity: '',
      newMemberOnly: false,
      errorMsg: '',
    };
  },
  created() {
    axiosClient.get('/floor')
      .then((response) => {
        this.floors = response.data;
        console.log(this.floors);
      })
      .catch((err) => {
        // this.errorMsg = err;
      })
    axiosClient.get('/parkingLot')
      .then((response) => {
        this.parkinglot = response.data;
        this.newOpeningTime = this.parkinglot.openingTime;
        this.newClosingTime = this.parkinglot.closingTime;
        this.newSmallSpotFee = this.parkinglot.smallSpotFee;
        this.newLargeSpotFee = this.parkinglot.largeSpotFee;
        this.newSmallSpotMonthlyFlatFee = this.parkinglot.smallSpotMonthlyFlatFee;
        this.newLargeSpotMonthlyFlatFee = this.parkinglot.largeSpotMonthlyFlatFee;
      })
      .catch((err) => {
        // this.errorMsg = err;
      })
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
    createOrUpdateParkingLot() {
      console.log(this.newOpeningTime)
      console.log(this.newClosingTime)

      let formattedOpeningTime = ""
      let formattedClosingTime = ""
      if (this.newOpeningTime.length == 5) {
        formattedOpeningTime = this.newOpeningTime + ":00"
      }
      if (this.newClosingTime == 5) {
        formattedClosingTime = this.newClosingTime + ":00"

        const request = { openingTime: formattedOpeningTime, closingTime: formattedClosingTime, largeSpotFee: this.newLargeSpotFee, smallSpotFee: this.newSmallSpotFee, smallSpotMonthlyFlatFee: this.newSmallSpotMonthlyFlatFee, largeSpotMonthlyFlatFee: this.newLargeSpotMonthlyFlatFee };
        axiosClient.put('/parkingLot/update', request)
          .then((response) => {
            const parkingLot = response.data;
            this.errorMsg = 'Parking lot updated sucessfully';
          })
          .catch((err) => {
            if (err.response.data === "Parking Lot not found.") {
              this.createParkingLot(request);
            } else if (typeof (err.response.data) === 'string') {
              this.errorMsg = `${err.response.data}`;
            } else {
              console.log(formattedOpeningTime)
              console.log(formattedClosingTime)
              this.errorMsg = `Please enter a new time.`;
            }
          })
      }
    },
    createParkingLot(request) {
      axiosClient.post('/parkingLot/creation', request)
        .then((response) => {
          const parkingLot = response.data;
          this.errorMsg = '';
        })
        .catch((err) => {
          this.errorMsg = `Failed to create: ${err.response.data}`;
        })
    },
    addFloor() {
      const request = { floorNumber: this.newFloorNumber, smallSpotCapacity: this.newSmallSpotCapacity, largeSpotCapacity: this.newLargeSpotCapacity, isMemberOnly: this.newMemberOnly };
      axiosClient.post('/floor', request)
        .then((response) => {
          const floor = response.data;
          this.floors.push(floor);
          this.newFloorNumber = '';
          this.newSmallSpotCapacity = '';
          this.newLargeSpotCapacity = '';
          this.newMemberOnly = false;
          this.errorMsg = '';
        })
        .catch((err) => {
          this.errorMsg = `Failed to create: ${err.response.data}`;
        })
    },
    deletefloor(floorNumber) {
      axiosClient.delete(`/floor/${floorNumber}`)
        .then((response) => {
          this.errorMsg = '';
          window.location.reload();
        })
        .catch((err) => {
          this.errorMsg = `Failed to delete: ${err.response.data}`;
        })
    },
    editfloor(floorNumber) {
      if (document.getElementById(`editfloor ${floorNumber}`).innerHTML === "Edit") {
        document.getElementById(`editfloor ${floorNumber}`).innerHTML = "Save";
        document.getElementById(`${floorNumber} 2`).innerHTML = `<input type="text" id="2 ${floorNumber}">`;
        document.getElementById(`${floorNumber} 3`).innerHTML = `<input type="text" id="3 ${floorNumber}">`;
        document.getElementById(`${floorNumber} 4`).innerHTML = `<input type="checkbox" id="4 ${floorNumber}">`;

      } else {

        const request = { floorNumber: `${floorNumber}`, smallSpotCapacity: document.getElementById(`2 ${floorNumber}`).value, largeSpotCapacity: document.getElementById(`3 ${floorNumber}`).value, isMemberOnly: document.getElementById(`4 ${floorNumber}`).checked };
        axiosClient.put('/floor', request)
          .then((response) => {
            console.log(response.data.isMemberOnly)
            window.location.reload();
            document.getElementById(`editfloor ${floorNumber}`).innerHTML = "Edit";

            this.errorMsg = '';
          })
          .catch((err) => {
            this.errorMsg = `${err.response.data}`;
          })
      }
    }
  },
  computed: {
    createParkingButtonDisabled() {
      return !this.newClosingTime.trim() || !this.newOpeningTime.trim();
    }
  }
}

</script>

<style>
td,
th {
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

a {
  padding: 20px;
}
</style>