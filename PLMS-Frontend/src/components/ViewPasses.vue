<template>
<div>
  <b-card no-body>
    <b-tabs pills card vertical nav-wrapper-class="w-20">
      <b-tab title="Guest Pass" active><b-card-text>
          <component :is="ViewGuestPasses"></component>
        </b-card-text></b-tab>
      <b-tab title="Monthly Pass"><b-card-text>
          <component :is="ViewMonthlyPasses"></component>
        </b-card-text></b-tab>
    </b-tabs>
  </b-card>
</div>
</template>

<script>
import axios from 'axios';
import ViewGuestPasses from '@/components/pass/ViewGuestPasses'
import ViewMonthlyPasses from '@/components/pass/ViewMonthlyPasses';

// const config = require('../../../config');
// const frontendUrl = config.dev.host + ':' + config.dev.port;
// const axiosClient = axios.create({
//   // Note the baseURL, not baseUrl
//   baseURL: config.dev.backendBaseUrl,
//   headers: { 'Access-Control-Allow-Origin': frontendUrl }
// });

export default{
     components: {
        ViewGuestPasses,
        ViewMonthlyPasses,
    },
      data() {
        return {
            ViewGuestPasses : ViewGuestPasses,
            ViewMonthlyPasses : ViewMonthlyPasses,
        }
      }


}

</script>