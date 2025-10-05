<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import { cart } from '../cart';
import { Trash2Icon } from 'lucide-vue-next';
import Fuse from 'fuse.js';

const url = ref('http://localhost:8080');

export interface Game {
  id: number;
  name: string;
  price: number;
  creator: string;
  icon?: string;
  isInCart?: boolean;
  isWishlisted?: boolean;
}

const games = ref<Game[]>([]);
// const filteredGames = ref([...games.value]);
const wishlist = ref<number[]>([]);
const isWishlisted = ref<boolean>();
const searchInput = ref<string>();

/**
 * CART
 */
async function addOrRemoveFromCart(game: Game) {
  try {
    let body = {
      userId: 1,
      gameId: game.id,
    };

    const isInCart = cart.value.some((c) => c.gameId === body.gameId);

    if (!isInCart) {
      await axios.post(url.value + '/cart/add', body);
      cart.value.push({
        userId: body.userId,
        gameId: body.gameId,
      });
      game.isInCart = true;
    } else {
      await axios.post(url.value + '/cart/remove', body);
      cart.value = cart.value.filter((c) => c.gameId !== body.gameId);
      game.isInCart = false;
    }
  } catch (err) {
    //TODO
  }
}

async function getGames() {
  try {
    const res = await axios.get(url.value + '/games');
    games.value = res.data;
    console.log(games.value);
  } catch (err) {
    if (err instanceof Error) {
      // TODO
    }
  }
}

async function initCart() {
  try {
    const res = await axios.get(url.value + '/cart');
    cart.value = res.data;
    games.value.forEach((game) => {
      game.isInCart = cart.value.some((c) => c.gameId === game.id);
    });
  } catch (err) {
    console.error(err);
  }
}

async function initWishlisted() {
  try {
    const res = await axios.get(url.value + '/users/me/wishlist');
    wishlist.value = res.data;
    games.value.forEach((game) => {
      game.isWishlisted = wishlist.value.includes(game.id);
    });
  } catch (err) {
    console.error(err);
  }
}

async function addOrRemoveFromWishlsit(game: Game) {
  try {
    let body = {
      userId: 1,
      gameId: game.id,
    };

    const isInWishlist = wishlist.value.includes(body.gameId);

    if (!isInWishlist) {
      await axios.post(url.value + '/users/me/wishlist/add', body);
      wishlist.value.push(game.id);
      game.isWishlisted = true;
    } else {
      await axios.post(url.value + '/users/me/wishlist/remove', body);
      wishlist.value = wishlist.value.filter((id) => id !== game.id);
      game.isWishlisted = false;
    }
  } catch (err) {
    if (err instanceof Error) {
      console.log(err.message);
    }
  }
  console.log(isWishlisted.value);
}

async function deleteGame(game: Game) {
  try {
    const res = await axios.delete(url.value + '/games/delete', {
      params: { gameId: game.id, userId: 1 },
    });
    games.value = games.value.filter((g) => g.id !== game.id);
  } catch (err) {
    if (err instanceof Error) {
      console.log(err.message);
    }
  }
}

const sortedGames = computed(() => {
  if (!searchInput.value) return games.value;

  const fuse = new Fuse(games.value, { keys: ['name'], threshold: 0.3 });
  const matches = fuse.search(searchInput.value).map((r) => r.item);

  const nonMatches = games.value.filter((g) => !matches.includes(g));

  return [...matches, ...nonMatches];
});

onMounted(async () => {
  await getGames();
  initWishlisted();
  initCart();
});
</script>

<template>
  <div class="store">
    <h1>Store</h1>
    <div class="search-container">
      <input class="textInput" placeholder="Search" v-model="searchInput" />
    </div>
    <div class="game-list">
      <div class="game-card" v-for="game in sortedGames" :key="game.id">
        <img class="banner" :src="`src/assets/${game.icon}`" :alt="game.name" />
        <h2>{{ game.name }}</h2>
        <p>Price: ${{ game.price }}</p>
        <p>By: {{ game.creator }}</p>
        <button @click="$emit('view-game', game)">Info</button>
        <button @click="addOrRemoveFromCart(game)">
          {{ game.isInCart ? 'In Cart' : 'Add to cart' }}
        </button>
        <button @click="addOrRemoveFromWishlsit(game)">
          {{ game.isWishlisted ? 'Wishlisted' : 'Wishlist' }}
        </button>
        <button class="delete-button" @click="deleteGame(game)">
          <Trash2Icon :size="18" />
        </button>
      </div>
    </div>
  </div>
</template>

<style>
/* CHROME, SAFARI, OPERA */
.store::-webkit-scrollbar {
  display: none;
}

.game-card .delete-button {
  position: absolute;
  right: 10px;
  bottom: 10px;
  width: 24px;
  height: 24px;
  display: flex;
}

.store {
  position: relative;
  background-color: #333;
  color: white;
  padding: 16px;
  width: 750px;
  height: 600px;
  border-radius: 8px;
  overflow-y: auto;
  background-color: rgba(0, 0, 0, 0.5);
  -ms-overflow-style: none; /* IE AND EDGE */
  scrollbar-width: none; /* FIREFOX */
}

.game-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-top: 16px;
  justify-content: center;
}

.game-list .game-card {
  margin-top: 10px;
}

.game-card {
  position: relative;
  background-color: #444;
  padding: 12px;
  border-radius: 6px;
  width: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.5);
}

.game-card button {
  margin-top: 10px;
}

.game-card .banner {
  width: 100%;
  height: 100px;
  object-fit: fill;
  border-radius: 4px;
  margin-bottom: 8px;
}

.store .search-container {
  position: absolute;
  top: 30px;
  right: 30px;
}

.search-container {
  color: white;
  padding: 16px;
  width: 150px;
  height: 65px;
  border-radius: 8px;
  background-color: rgba(0, 0, 0, 0.5);
  position: relative;
}

.search-container input {
  margin-top: 10px;
}

.search-container .textInput {
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

.search-container .textInput::placeholder {
  transition: opacity 250ms cubic-bezier(0, 0, 0.2, 1);
  opacity: 1;
  user-select: none;
  color: rgba(255, 255, 255, 0.582);
}

.search-container .textInput:focus,
.search-container .textInput:active {
  outline: none;
}

.search-container:focus-within .textInput,
.search-container .textInput:focus,
.search-container .textInput:active {
  background-color: #353535;
}

.search-container .textInput:focus {
  background-color: #353535;
}

.search-container .textInput:focus::placeholder {
  opacity: 0;
}
</style>
