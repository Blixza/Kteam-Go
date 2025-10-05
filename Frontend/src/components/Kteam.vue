<script setup lang="ts">
import { ref, defineAsyncComponent, computed, watch } from 'vue';
import Prism from 'prismjs';
import 'prismjs/components/prism-json';
import { cart } from '../cart';
import type { Game } from './Store.vue';
import GameView from './Game.vue';

const currentComponent = ref<string>('Kteam');
const lastComponent = ref<string>('Kteam');
const selectedGame = ref<Game | null>(null);
const theme = ref('dark-theme');

function openGame(game: Game) {
  lastComponent.value = currentComponent.value;
  selectedGame.value = game;
  currentComponent.value = 'Game';
}

function backFromGame() {
  currentComponent.value = lastComponent.value;
  selectedGame.value = null;
}

function setTheme(name: string) {
  theme.value = name;
  document.documentElement.className = name;
}

function loadScene(name: string) {
  currentComponent.value = currentComponent.value === name ? 'Kteam' : name;
}

const showCart = computed(() => cart.value.length > 0);

watch(cart, (newVal) => {
  if (newVal.length === 0 && currentComponent.value === 'Cart') {
    currentComponent.value = 'Kteam';
  }
});

const AsyncProfile = defineAsyncComponent(() => import('./Profile.vue'));
const AsyncStore = defineAsyncComponent(() => import('./Store.vue'));
const AsyncCart = defineAsyncComponent(() => import('./Cart.vue'));
const AsyncUpload = defineAsyncComponent(() => import('./Upload.vue'));

(window as any).Prism = Prism; // doesn't works without this
</script>

<template>
  <div class="top-buttons">
    <button id="kteam-button" @click="loadScene('Kteam')">Kteam</button>
    <button id="store-button" @click="loadScene('Store')">Store</button>
    <button id="profile-button" @click="loadScene('Profile')">Profile</button>
    <button id="cart-button" v-show="showCart" @click="loadScene('Cart')">
      Cart ({{ cart.length }})
    </button>
    <button id="upload-button" @click="loadScene('Upload')">Upload game</button>
  </div>
  <div class="theme-button">
    <button
      @click="setTheme(theme == 'pink-theme' ? 'dark-theme' : 'pink-theme')"
    >
      Theme: {{ theme }}
    </button>
  </div>
  <div class="debug-button">
    <button id="debug-button" @click="console.log('DEBUG')">DEBUG</button>
  </div>
  <component
    :is="
      currentComponent === 'Profile'
        ? AsyncProfile
        : currentComponent === 'Store'
        ? AsyncStore
        : currentComponent === 'Cart'
        ? AsyncCart
        : currentComponent === 'Upload'
        ? AsyncUpload
        : currentComponent === 'Game' && selectedGame
        ? GameView
        : null
    "
    @view-game="openGame"
    v-bind="currentComponent === 'Game' ? { game: selectedGame } : {}"
    @back="backFromGame"
  />
</template>

<style>
.top-buttons {
  position: absolute;
  position: fixed;

  top: 0;
  left: 0;

  margin-top: 10px;
  margin-left: 10px;

  padding: 10px;
  z-index: 1000;
}

.top-buttons button {
  margin-right: 10px;
}

.debug-button {
  position: absolute;
  position: fixed;

  bottom: 10px;
  right: 10px;

  padding: 10px;
  z-index: 1000;
}

.theme-button {
  position: absolute;
  position: fixed;
  bottom: 10px;
  left: 10px;

  padding: 10px;
  z-index: 1000;
}

/* .top-buttons button:last-child {
  padding: 5px 15px;
  margin: 0 5px;
} */
</style>
