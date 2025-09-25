<script setup lang="ts">
import { ref, defineAsyncComponent, computed, watch } from 'vue';
import Prism from 'prismjs';
import 'prismjs/components/prism-json';
import { Settings } from 'lucide-vue-next';
import { cart } from '../cart';

const currentComponent = ref<string | null>(null);

function loadProfile() {
  currentComponent.value =
    currentComponent.value === 'Profile' ? null : 'Profile';
}

function loadStore() {
  currentComponent.value = currentComponent.value === 'Store' ? null : 'Store';
}

function loadKteam() {
  currentComponent.value = currentComponent.value === 'Kteam' ? null : 'Kteam';
}

function loadCart() {
  currentComponent.value = currentComponent.value === 'Cart' ? null : 'Cart';
}

const showCart = computed(() => cart.value.length > 0);

watch(cart, (newVal) => {
  if (newVal.length === 0 && currentComponent.value === 'Cart') {
    currentComponent.value = null;
  }
});

const AsyncProfile = defineAsyncComponent(() => import('./Profile.vue'));
const AsyncStore = defineAsyncComponent(() => import('./Store.vue'));
const AsyncCart = defineAsyncComponent(() => import('./Cart.vue'));

(window as any).Prism = Prism; // doesn't works without this
</script>

<template>
  <div class="top-buttons">
    <button class="kteam-button" @click="loadKteam">Kteam</button>
    <button class="store-button" @click="loadStore">Store</button>
    <button class="profile-button" @click="loadProfile">Profile</button>
    <button class="cart-button" v-show="showCart" @click="loadCart">
      Cart ({{ cart.length }})
    </button>
  </div>

  <!-- <button class="options-button">
    <Settings />
  </button> -->
  <component
    :is="
      currentComponent === 'Profile'
        ? AsyncProfile
        : currentComponent === 'Store'
        ? AsyncStore
        : currentComponent === 'Cart'
        ? AsyncCart
        : null
    "
  />
</template>

<style>
.top-buttons {
  position: absolute;
  top: 0;
  left: 0;

  margin-top: 10px;
  margin-left: 10px;

  padding: 10px;
}

.top-buttons button {
  margin-right: 10px;
}

.options-button {
  background: none;
  border: none;
  cursor: pointer;

  position: absolute;
  top: 10px;
  right: 10px;

  padding: 10px;
}

/* .top-buttons button:last-child {
  padding: 5px 15px;
  margin: 0 5px;
} */
</style>
