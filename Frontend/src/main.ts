import { createApp } from 'vue';
import './style.css';
import Kteam from './components/Kteam.vue';
import VCodeBlock from '@wdns/vue-code-block';

const app = createApp(Kteam);

app.component('VCodeBlock', VCodeBlock);

app.mount('#app');
