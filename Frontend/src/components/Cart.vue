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
    let body = {
      userId: 1,
      gameId: game.id,
    };

    await axios.post(url.value + '/cart/remove', body);
    cart.value = cart.value.filter((c) => c.gameId !== body.gameId);
    game.isInCart = false;
    if (cart.value.length <= 0) {
      emit('close');
    }
  } catch (err) {
    //TODO
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

async function getGames() {
  try {
    const res = await axios.get(url.value + '/games');
    games.value = res.data;
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
