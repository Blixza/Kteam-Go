<script setup lang="ts">
const props = defineProps<{ game: any }>();
const emit = defineEmits(['back']);
</script>

<template>
  <div class="game-page">
    <img
      class="game-icon"
      :src="'http://localhost:8080' + '/uploads/' + game.icon"
      :alt="props.game.name"
    />
    <div class="game-name">{{ props.game.name }}</div>
    <p>Price: ${{ props.game.price }}</p>
    <p>Creator: {{ props.game.creator }}</p>
    <button @click="emit('back')">Back</button>
  </div>
</template>

<style>
.game-page {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
  position: relative;
}

.game-page .game-icon {
  height: 200px;
  width: 200px;
  object-fit: cover;
  border-radius: 8px;
}

.game-page .game-name {
  font-size: 24px;
  font-weight: bold;
  color: white;
}
</style>
