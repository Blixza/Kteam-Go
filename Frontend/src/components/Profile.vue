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
    const res = await axios.get(url.value + '/users/me/nickname', {
      params: { id: 1 },
    });
    nickname.value = res.data;
  } catch (err) {
    if (err instanceof Error) {
      nickname.value = 'Failed to get nickname';
    }
  }
}

async function getUsername() {
  try {
    const res = await axios.get(url.value + '/users/me/username', {
      params: { id: 1 },
    });
    username.value = res.data;
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
