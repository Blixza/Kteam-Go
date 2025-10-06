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

const emit = defineEmits(['close']);

async function removeFromCart(game: Game) {
  try {
    await axios.post(url.value + `/users/${1}/cart/remove`, {
      userId: 1,
      gameId: game.id,
    });
    cart.value = cart.value.filter((c) => c.gameId !== game.id);
    game.isInCart = false;
    if (cart.value.length <= 0) {
      emit('close');
    }
  } catch (err) {
    console.log(err);
  }
}

async function addOrRemoveFromWishlsit(game: Game) {
  try {
    const isInWishlist = wishlist.value.includes(game.id);

    if (!isInWishlist) {
      await axios.post(url.value + `/users/${1}/wishlist/add`, {
        userId: 1,
        gameId: game.id,
      });
      wishlist.value.push(game.id);
      game.isWishlisted = true;
    } else {
      await axios.post(url.value + `/users/${1}/wishlist/remove`, {
        userId: 1,
        gameId: game.id,
      });
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

async function initCart() {
  try {
    const res = await axios.get(url.value + `/users/${1}/cart`);
    cart.value = res.data.cart;
    games.value.forEach((game) => {
      game.isInCart = cart.value.some((c) => c.gameId === game.id);
    });
  } catch (err) {
    console.error(err);
  }
}

async function initWishlisted() {
  try {
    const res = await axios.get(url.value + `/users/${1}/wishlist`);
    wishlist.value = res.data.wishlist;
    games.value.forEach((game) => {
      game.isWishlisted = wishlist.value.includes(game.id);
    });
  } catch (err) {
    console.error(err);
  }
}

async function getGames() {
  try {
    const res = await axios.get(url.value + '/games');
    games.value = res.data.games;
  } catch (err) {
    if (err instanceof Error) {
      // TODO
    }
  }
}

onMounted(async () => {
  await getGames();
  initWishlisted();
  initCart();
});
</script>

<template>
  <div class="store">
    <h1>Cart</h1>
    <div class="game-list">
      <div
        class="game-card"
        v-for="game in games.filter((g) => g.isInCart)"
        :key="game.id"
      >
        <img
          class="banner"
          :src="'/images/' + game.name.split(' ').join('_') + '.jpg'"
          :alt="game.name"
        />
        <h2>{{ game.name }}</h2>
        <p>Price: ${{ game.price }}</p>
        <p>By: {{ game.creator }}</p>
        <button @click="removeFromCart(game)">Remove from cart</button>
        <button @click="addOrRemoveFromWishlsit(game)">
          {{ game.isWishlisted ? 'Wishlisted' : 'Wishlist' }}
        </button>
      </div>
    </div>
  </div>
</template>
