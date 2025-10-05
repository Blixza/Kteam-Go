<script setup lang="ts">
import { onMounted, ref } from 'vue';
import axios from 'axios';

const url = ref('http://localhost:8080');
const username = ref('');
const body = ref({
  name: '',
  price: 0,
  creator: '',
  icon: '',
});
const error = ref('');
const status = ref<number>();

onMounted(() => {
  getUsername();
});

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

function handleIcon(event: Event) {
  const input = event.target as HTMLInputElement;
  const file = input.files?.[0];
  if (file) {
    body.value.icon = file.name;
  }
}

async function uploadGame() {
  try {
    const res = await axios.post(url.value + '/games/create', body.value);
    if (res.status != 200) {
      status.value = res.status;
      error.value = res.data;
    }
  } catch (err) {
    if (err instanceof Error) {
      // TODO
    }
  }
}
</script>

<template>
  <div class="upload-container">
    <div class="icon-container">
      <input
        type="file"
        accept="image/png, image/jpeg"
        @change="handleIcon"
        required
      />
      <img
        v-show="body.icon"
        :src="`src/assets/${body.icon}`"
        class="icon-preview"
        alt="Icon"
      />
    </div>
    <div class="name-container">
      <form @submit.prevent="uploadGame">
        <input
          type="text"
          placeholder="Game Name"
          v-model="body.name"
          class="textInput"
          required
        />
        <input
          type="text"
          placeholder="Creator"
          v-model="body.creator"
          class="textInput"
          required
        />
        <input
          type="number"
          placeholder="Price"
          v-model.number="body.price"
          class="textInput"
          required
        />
        <button type="submit" class="upload-button">Upload</button>
      </form>
    </div>
  </div>
</template>

<style>
.upload-container {
  display: flex;
  gap: 20px;
}

.upload-button {
  align-items: center;
  margin-top: 30px;
}

.icon-container {
  color: white;
  padding: 16px;
  width: 250px;
  height: 250px;
  border-radius: 8px;
  background-color: rgba(0, 0, 0, 0.5);
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}

.icon-container input[type='file'] {
  margin-top: 30px;
}

.icon-container img {
  margin-top: 20px;
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.right-column {
  display: flex;
  flex-direction: column;
  gap: 10px;
  flex: 1;
}

.name-container {
  color: white;
  padding: 16px;
  width: 250px;
  height: 250px;
  border-radius: 8px;
  background-color: rgba(0, 0, 0, 0.5);
  position: relative;
}

.name-container input {
  margin-top: 10px;
}

.name-container .textInput {
  border-radius: 5px 5px 0 0;
  box-shadow: 0px 2px 5px rgb(35 35 35 / 30%);
  max-height: 36px;
  background-color: #252525;
  transition: background-color 200ms cubic-bezier(0.25, 0.8, 0.25, 1);
  color: #e8e8e8;
  font-size: 14px;
  font-weight: 500;
  padding: 12px;
  width: 80%;
  border-left: none;
  border-bottom: none;
  border-right: none;
}

.name-container .textInput::placeholder {
  transition: opacity 250ms cubic-bezier(0, 0, 0.2, 1);
  opacity: 1;
  user-select: none;
  color: rgba(255, 255, 255, 0.582);
}

.name-container .textInput:focus,
.name-container .textInput:active {
  outline: none;
}

.name-container:focus-within .textInput,
.name-container .textInput:focus,
.name-container .textInput:active {
  background-color: #353535;
}

.name-container .textInput:focus {
  background-color: #353535;
}

.name-container .textInput:focus::placeholder {
  opacity: 0;
}
</style>
