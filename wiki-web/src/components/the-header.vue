<script setup>

import {defineComponent, ref} from "vue";
import axios from "axios";

defineComponent({
  name: 'the-header'
})

//登录模态框是否显示
const loginModelShow = ref(false);
//登录是否成功
const loginResult = ref(false);
//登录表单数据
const loginFromData = ref({
  loginName: "",
  password: ""
});
//点击header登录按钮
const handleClickHeaderLogin = () => {
  loginModelShow.value = true;
}
//点击表单登录按钮，发送登录请求
const handleClickLoginButton = () => {
  axios.post("/user/login", {
    ...loginFromData.value
  }).then(res => {
    console.log(res);
  })
}
</script>

<template>
  <a-layout-header class="header">
    <!--    <div class="logo"/>-->
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook">
        <router-link to="/admin/ebook">电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>
    </a-menu>
    <div class="login-menu" @click="handleClickHeaderLogin">
      <span>登录</span>
    </div>
    <a-modal
        v-model:open="loginModelShow"
        :confirm-loading="loginResult"
        @ok="handleClickLoginButton"
        title="登录"
        ok-text="登录"
        cancel-text="取消">
      <a-form>
        <a-form-item
            label="用户名"
            name="loginName"
            :rules="[{ required: true, message: '请输入用户名' }]"
        >
          <a-input v-model:value="loginFromData.loginName"/>
        </a-form-item>
        <a-form-item
            label="密码"
            name="password"
            :rules="[{ required: true, message: '请输入密码' }]"
        >
          <a-input v-model:value="loginFromData.password"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<style lang="scss" scoped>

.header {
  display: flex;
  justify-content: space-between;

  .login-menu {
    color: #ffffff;

    &:hover {
      cursor: pointer;
    }
  }
}
</style>