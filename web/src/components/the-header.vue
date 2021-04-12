<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu
      theme="dark"
      mode="horizontal"
      v-model:selectedKeys="selectedKeys1"
      :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="home">
        <router-link to="/">Home</router-link>
      </a-menu-item>
      <a-menu-item key="user" :style="user.id ? {} : {display: 'none'}">
        <router-link to="/admin/user">User</router-link>
      </a-menu-item>
      <a-menu-item key="ebook" :style="user.id ? {} : {display: 'none'}">
        <router-link to="/admin/ebook">Ebook</router-link>
      </a-menu-item>
      <a-menu-item key="category" :style="user.id ? {} : {display: 'none'}">
        <router-link to="/admin/category">Category</router-link>
      </a-menu-item>
      <a-menu-item key="about">
        <router-link to="/about">About Us</router-link>
      </a-menu-item>
      <a class="login-menu" @click="showLoginModal" v-show="!user.id">
        <span>登录</span>
      </a>
      <a-popconfirm
        title="Do you confirm to logout"
        ok-text="Yes"
        cancel-text="No"
        @confirm="logout()"
      >
        <a class="login-menu" v-show="user.id">
          <span>Logout</span>
        </a>
      </a-popconfirm>
      <a class="login-menu" v-show="user.id">
        <span>Hello! {{ user.name }}</span>
      </a>
    </a-menu>
    <a-modal
      title="登录"
      v-model:visible="loginModalVisible"
      :confirm-loading="loginModalLoading"
      @ok="login"
    >
      <a-form
        :model="loginUser"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from "vue";
import axios from "axios";
import { message } from "ant-design-vue";
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: "the-header",
  setup() {
    const user = computed(() => store.state.user);
    const loginUser = ref({
      loginName: "test",
      password: "test123",
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    // 登录
    const login = () => {
      console.log("开始登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post("/user/login", loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");
          store.commit("setUser", data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    const logout = () => {
      console.log("Logout");
      axios.get("/user/logout/" + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("You are logged out!");
          store.commit("setUser", {});
        } else {
          message.error(data.message);
        }
      });
    };

    return {
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,
      logout,
      user,
    };
  },
});
</script>

<style>
.login-menu {
  float: right;
  color: white;
  padding-left: 10px;
}
</style>