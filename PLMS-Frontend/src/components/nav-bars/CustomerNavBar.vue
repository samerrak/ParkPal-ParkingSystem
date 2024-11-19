<template>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">
  <link rel="icon" href="../assets/logo-transparent-png.png">
  <link rel="canonical" href="https://getbootstrap.com/docs/4.0/examples/product/">
  <link href="../../bootstrap-4.0.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="../../bootstrap-4.0.0/docs/4.0/examples/product/product.css" rel="stylesheet">

  <nav class="site-header sticky-top py-1">
    <div class="container d-flex flex-column flex-md-row justify-content-between">
      <a class="py-2" @click="RouteHome">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" class="bi bi-box" viewBox="0 0 16 16">
          <path d="M8.186 1.113a.5.5 0 0 0-.372 0L1.846 3.5 8 5.961 14.154 3.5 8.186 1.113zM15 4.239l-6.5 2.6v7.922l6.5-2.6V4.24zM7.5 14.762V6.838L1 4.239v7.923l6.5 2.6zM7.443.184a1.5 1.5 0 0 1 1.114 0l7.129 2.852A.5.5 0 0 1 16 3.5v8.662a1 1 0 0 1-.629.928l-7.185 2.874a.5.5 0 0 1-.372 0L.63 13.09a1 1 0 0 1-.63-.928V3.5a.5.5 0 0 1 .314-.464L7.443.184z"/>
        </svg>      </a>
      <a class="py-2 d-none d-md-inline-block"  @click="RouteManage">Account</a>
      <a class="py-2 d-none d-md-inline-block"  @click="RoutePass">Passes</a>
      <a class="py-2 d-none d-md-inline-block" @click="Reload">Appointments</a>
      <a class="py-2 d-none d-md-inline-block" href="http://localhost:8087/#/login-user">Sign Out</a>
      <a class="py-2 d-none d-md-inline-block"  @click="Reload">
        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bootstrap-reboot" viewBox="0 0 16 16">
          <path d="M1.161 8a6.84 6.84 0 1 0 6.842-6.84.58.58 0 1 1 0-1.16 8 8 0 1 1-6.556 3.412l-.663-.577a.58.58 0 0 1 .227-.997l2.52-.69a.58.58 0 0 1 .728.633l-.332 2.592a.58.58 0 0 1-.956.364l-.643-.56A6.812 6.812 0 0 0 1.16 8z"/>
          <path d="M6.641 11.671V8.843h1.57l1.498 2.828h1.314L9.377 8.665c.897-.3 1.427-1.106 1.427-2.1 0-1.37-.943-2.246-2.456-2.246H5.5v7.352h1.141zm0-3.75V5.277h1.57c.881 0 1.416.499 1.416 1.32 0 .84-.504 1.324-1.386 1.324h-1.6z"/>
        </svg>
      </a>
    </div>
  </nav>
</template>

<script>
export default {
  name: "CustomerNavBar"
}
</script>

<style scoped>

</style>
