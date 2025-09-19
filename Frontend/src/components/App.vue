<script setup lang="ts">
import { ref } from 'vue';
const count = ref(0);
const showMsg = ref(true);
const msg = ref('hi1');
const newMsg = ref('');

function doThis(text: string, event: Event) {
  count.value += 5;
  msg.value = text;
  console.log(event.type);
}
</script>

<template>
  <div>
    <span v-on:click="doThis(newMsg, $event)">Message: {{ showMsg ? msg : 'meow' }}</span>
  </div>
  <input v-model="newMsg" placeholder="edit me"></input>
  <div id="app">
    <button @click="count++">Count is: {{ count }}</button>
  </div>
</template>

<style lang="css" scoped>
button {
  font-weight: bold;
}
</style>
