import { ref } from 'vue';

interface Cart {
  userId: number;
  gameId: number;
}

export const cart = ref<Cart[]>([]);
