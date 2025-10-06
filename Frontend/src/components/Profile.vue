<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';

const url = ref('http://localhost:8080');

const nickname = ref('');
const username = ref('');

onMounted(() => {
  getNickname();
  getUsername();
});

async function getNickname() {
  try {
    const res = await axios.get(url.value + `/users/${1}/info`);
    nickname.value = res.data.userInfo.nickname;
  } catch (err) {
    if (err instanceof Error) {
      nickname.value = 'Failed to get nickname';
    }
  }
}

async function getUsername() {
  try {
    const res = await axios.get(url.value + `/users/${1}/info`);
    username.value = res.data.userInfo.username;
  } catch (err) {
    if (err instanceof Error) {
      username.value = 'Failed to get username';
    }
  }
}
</script>

<template>
  <div class="profile">
    <h1>Nickname: {{ nickname }}</h1>
    <h2>Username: @{{ username }}</h2>
  </div>
</template>

<style>
.profile {
  color: white;
  padding: 16px;
  width: 750px;
  height: 600px;
  border-radius: 8px;
  background-color: rgba(0, 0, 0, 0.5);
}
</style>
