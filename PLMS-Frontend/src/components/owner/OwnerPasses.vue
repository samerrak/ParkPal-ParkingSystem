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
        <component :is="ViewPasses"></component>
    </div>
    
</div>
</template>

<script>
// import axios from 'axios';
import ViewPasses from '@/components/ViewPasses'

// const config = require('../../../config');
// const frontendUrl = config.dev.host + ':' + config.dev.port;
// const axiosClient = axios.create({
//   // Note the baseURL, not baseUrl
//   baseURL: config.dev.backendBaseUrl,
//   headers: { 'Access-Control-Allow-Origin': frontendUrl }
// });

export default{
    components: {
        ViewPasses,
    },
      data() {
        return {
            ViewPasses : ViewPasses,
        }
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
    }

}

}
   
</script>