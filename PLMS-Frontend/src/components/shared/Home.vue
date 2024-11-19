<template>
  <div class="home">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="src/assets/logo-transparent-png.png">


    <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/product/">

    <!-- Bootstrap core CSS -->
    <link href="../../bootstrap-4.0.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/../bootstrap-4.0.0/docs/4.0/examples/product/product.css" rel="stylesheet">

  <nav class="site-header sticky-top py-1">
    <div class="container d-flex flex-column flex-md-row justify-content-between">
      <a class="py-2" href="#product">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-box" viewBox="0 0 16 16">
          <path d="M8.186 1.113a.5.5 0 0 0-.372 0L1.846 3.5 8 5.961 14.154 3.5 8.186 1.113zM15 4.239l-6.5 2.6v7.922l6.5-2.6V4.24zM7.5 14.762V6.838L1 4.239v7.923l6.5 2.6zM7.443.184a1.5 1.5 0 0 1 1.114 0l7.129 2.852A.5.5 0 0 1 16 3.5v8.662a1 1 0 0 1-.629.928l-7.185 2.874a.5.5 0 0 1-.372 0L.63 13.09a1 1 0 0 1-.63-.928V3.5a.5.5 0 0 1 .314-.464L7.443.184z"/>
        </svg>      </a>
      <a class="py-2 d-none d-md-inline-block"  href="#product">Product</a>
      <a class="py-2 d-none d-md-inline-block"  href="#pricing">Pricing</a>
      <a class="py-2 d-none d-md-inline-block"  @click="Appointments">Appointments</a>
      <a class="py-2 d-none d-md-inline-block" @click="Login">Login</a>
      <a class="py-2 d-none d-md-inline-block" @click="SignUp">Sign up</a>
    </div>
  </nav>

  <div id = "product" ></div>
  <div class="position-relative overflow-hidden p-3 p-md-5 m-md-3 text-center bg-light">
    <img width="30%" src="../../assets/img.png" style="margin-bottom: -5%">
    <div class="col-md-5 p-lg-5 mx-auto my-5">
      <h1 class="display-4 font-weight-light">Park your worries aside </h1>
      <p class="lead font-weight-normal">Do you ever find yourself worrying about finding a parking spot? Well set your worries aside and book a pass through ParkPal!</p>
      <a class="btn btn-outline-secondary" href="#pricing">Learn more ></a>
    </div>
  </div>

  <div id = "pricing" ></div>
  <div class="d-md-flex flex-md-equal w-100 my-md-3 pl-md-3">
    <div class="bg-dark mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center text-white overflow-hidden">
      <div class="my-3 py-3">
        <h2 class="display-5" style="margin-top: -10%; margin-bottom: 5%">Guest Pass</h2>
        <p class="lead">Introducing our new Guest Parking Pass - Where we service your temporary parking needs! The perfect solution for those who need a temporary parking solution for their guests! Whether it's for a dinner party, a weekend gathering, or a family reunion, our Guest Parking Pass ensures that your guests have a safe and convenient place to park without the worry of getting ticketed or towed</p>
      </div >
      <a style="margin-top: -10%" class="btn btn-outline-secondary" @click="Guest">Buy now ></a>
    </div>
    <div class="bg-light mr-md-3 pt-3 px-3 pt-md-5 px-md-5 text-center overflow-hidden">
      <div class="my-3 py-3">
        <h2 class="display-5" style="margin-top: -10%; margin-bottom: 5%">Monthly Pass</h2>
        <p class="lead">Introducing our Monthly Parking Pass - the ultimate parking solution for those who need a reliable and convenient parking spot on a regular basis. With our pass, you can enjoy unlimited access to our parking lot for a whole month, without the hassle of having to find and pay for parking every time.With our pass, you can park in any available spot within our parking lot, 24/7, for the entire month.</p>
      </div >
      <a style="margin-top: -10%" class="btn btn-outline-secondary" @click="SignUp">Sign up ></a>
    </div>
  </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data () {
    return {
      msg: 'Welcome to Your Home Page'
    }
  },
  methods : {
    async Login() {
      await this.$router.push({name: 'LoginUser'})
    },
    async SignUp() {
      await this.$router.push({name: 'SignUp'})
    },
    async Guest() {
      await this.$router.push({name: 'GeneralCreateGuestPass'})
    },
    async Appointments() {
      await this.$router.push({name: 'BookServiceAppointmentGuest'})
    }
  }
}
</script>

<style scoped>

</style>
