<script setup lang="js">
import { ref } from 'vue';
const x = ref(0);

function onMouseMove() {
  x.value = Math.random() * (1000 - 1) + 1;
}
</script>

<template>
  <div
    @mousemove="onMouseMove"
    :style="{ backgroundColor: `hsl(${x}, 80%, 50%)` }"
    class="movearea"
  >
    <p>Move your mouse across this div...</p>
    <p>x: {{ x }}</p>
  </div>
</template>

<style>
.movearea {
  transition: 0.3s background-color ease;
}
</style>
