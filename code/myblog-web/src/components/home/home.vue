<template>
  <div>我是主页
    <el-button type="primary" @click="click()" style="width: 80px">点我点我点我！！</el-button>
  </div>

</template>

<script>
  import {getRequest} from "../../axios/index";

  export default {
    name: "home",
    methods: {
      click() {
        getRequest("/api/user/1").then();
      }
    }
  }
</script>

<style scoped>

</style>
