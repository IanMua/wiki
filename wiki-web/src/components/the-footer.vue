<script setup lang="ts">
import {computed, defineComponent} from "vue";
import store from "@/store";

defineComponent({
  name: "the-footer"
})

const user = computed(() => store.state.user)
</script>

<template>
  <a-layout-footer style="text-align: center">
    IanMu Ebook ©2023 Created by IanMu
  </a-layout-footer>
</template>

<style scoped>

</style>