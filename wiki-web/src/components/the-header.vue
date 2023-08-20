<script setup>

import {defineComponent, ref} from "vue";
import axios from "axios";
import {Md5} from "ts-md5";
import {notification} from "ant-design-vue";

defineComponent({
  name: 'the-header'
})

//登录成功后数据
const user = ref();

//登录模态框是否显示
const loginModelShow = ref(false);
//登录是否成功
const loginResult = ref(false);
const loginFormRef = ref();
//登录表单数据
const loginFormData = ref({
  loginName: "",
  password: ""
});
//登录表单提交的数据
const loginFormSubmitData = ref({
  loginName: "",
  password: ""
});
//点击header登录按钮
const handleClickHeaderLogin = () => {
  loginModelShow.value = true;
}
//点击表单登录按钮，发送登录请求
const handleClickLoginButton = async () => {

  //表单规则校验，不满足规则无法发起请求
  let ok = false;
  await loginFormRef.value.validate()
      .then(() => {
        ok = true;
      })
      .catch(error => {
        ok = false;
      })
  if (!ok) {
    return;
  }

  //开始登录流程
  loginResult.value = true;

  //密码加密
  loginFormSubmitData.value = {
    loginName: loginFormData.value.loginName,
    password: Md5.hashStr(loginFormData.value.password + process.env.VUE_APP_KEY)
  }

  //登录请求
  axios.post("/user/login", {
    ...loginFormSubmitData.value
  }).then(res => {
    if (!res.data.success) {
      return;
    }

    //写入登录成功后的用户数据
    user.value = res.data.content;
    console.log("用户数据：", user);

    //关闭模态窗
    loginModelShow.value = false;
    loginResult.value = false;

    //登录成功提示
    notification["success"]({
      message: '登录成功',
      description:
          `您好: ${user.value.name}, 欢迎回到云慕时空Wiki电子书`
    });
  })
}

const rules = {
  loginName: [
    {required: true, message: '请输入登录名', trigger: 'change'},
    {min: 6, max: 20, message: '长度需在6-20之间', trigger: 'blur'},
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'change'},
    {min: 6, max: 20, message: '长度需在6-20之间', trigger: 'blur'},
  ]
};
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
    <div v-if="!user?.id" class="login-menu" @click="handleClickHeaderLogin">
      <span>登录</span>
    </div>
    <div v-else class="login-menu" @click="handleClickHeaderLogin">
      <span>您好: {{ user.name }}</span>
    </div>
    <a-modal
        v-model:open="loginModelShow"
        :confirm-loading="loginResult"
        @ok="handleClickLoginButton"
        title="登录"
        ok-text="登录"
        cancel-text="取消">
      <a-form
          ref="loginFormRef"
          :model="loginFormData"
          :rules="rules"
          :label-col="{
            span: 4
          }"
      >
        <a-form-item
            label="用户名"
            name="loginName"
        >
          <a-input v-model:value="loginFormData.loginName">
            <template #prefix>
              <UserOutlined class="site-form-item-icon"/>
            </template>
          </a-input>
        </a-form-item>
        <a-form-item
            label="密码"
            name="password"
        >
          <a-input-password v-model:value="loginFormData.password">
            <template #prefix>
              <LockOutlined class="site-form-item-icon"/>
            </template>
          </a-input-password>
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