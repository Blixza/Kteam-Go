<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { cart } from '../cart';

const url = ref('http://localhost:8080');

interface Game {
  id: number;
  name: string;
  price: number;
  creator: string;
  isInCart?: boolean;
  isWishlisted?: boolean;
}

const games = ref<Game[]>([]);
const wishlist = ref<number[]>([]);
const isWishlisted = ref<boolean>();
const isInCart = ref<boolean>();

/**
 * CART
 */
async function addOrRemoveFromCart(game: Game) {
  try {
    var body = {
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
    const res = await axios.get(url.value + '/games/');
    games.value = res.data;
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
    var body = {
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

onMounted(async () => {
  await getGames();
  initWishlisted();
  initCart();
});
</script>

<template>
  <div class="store">
    <h1>Store</h1>
    <div class="game-list">
      <div class="game-card" v-for="game in games" :key="game.id">
        <img
          class="banner"
          :src="'src/assets/' + game.name.split(' ').join('_') + '.jpg'"
          :alt="game.name"
        />
        <h2>{{ game.name }}</h2>
        <p>Price: ${{ game.price }}</p>
        <p>By: {{ game.creator }}</p>
        <button @click="addOrRemoveFromCart(game)">
          {{ game.isInCart ? 'In Cart' : 'Add to cart' }}
        </button>
        <button @click="addOrRemoveFromWishlsit(game)">
          {{ game.isWishlisted ? 'Wishlisted' : 'Wishlist' }}
        </button>
      </div>
    </div>
  </div>
</template>

<style>
.store {
  background-color: #333;
  color: white;
  padding: 16px;
  width: 750px;
  height: 600px;
  border-radius: 8px;
  overflow-y: auto;
}

.game-list {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  margin-top: 16px;
  justify-content: left;
}

.game-list .game-card {
  margin-top: 10px;
}

.game-card {
  background-color: #444;
  padding: 12px;
  border-radius: 6px;
  width: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
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
</style>
