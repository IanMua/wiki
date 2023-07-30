<template>
  <a-layout>
    <the-header/>
    <router-view/>
    <the-footer/>
  </a-layout>
</template>


<script setup lang="ts">

import {defineComponent} from "vue";
import TheHeader from "@/components/the-header.vue";
import TheFooter from "@/components/the-footer.vue";

defineComponent({
  components: {
    TheHeader,
    TheFooter
  }
})

</script>


<style scoped>
#components-layout-demo-top-side-2 .logo {
  float: left;
  width: 120px;
  height: 31px;
  margin: 16px 24px 16px 0;
  background: rgba(255, 255, 255, 0.3);
}

.ant-row-rtl #components-layout-demo-top-side-2 .logo {
  float: right;
  margin: 16px 0 16px 24px;
}

.site-layout-background {
  background: #fff;
}
</style>