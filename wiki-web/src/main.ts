import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import antd from 'ant-design-vue'
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue'

const app: any = createApp(App)
app.use(store).use(router).use(antd).mount('#app');

const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}
