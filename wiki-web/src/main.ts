import {createApp, h} from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import antd, {notification, NotificationPlacement} from "ant-design-vue";
import "ant-design-vue/dist/reset.css";
import * as Icons from "@ant-design/icons-vue";
import axios from "axios";
import {CloseCircleFilled} from "@ant-design/icons-vue";

axios.defaults.baseURL = process.env.VUE_APP_SERVER;

/**
 * axios 拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log("请求参数 : ", config);
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response: any) {
    console.log("返回结果 : ", response);
    if (!response.data.success) {
        errorNotification("topRight", "请求错误", response.data.message);
    }
    return response;
}, error => {
    console.log("返回错误 : ", error);
    return Promise.reject(error);
});

/**
 * 错误提示
 */
const errorNotification = (placement: NotificationPlacement, title: string, msg: string) => {
    notification.open({
        message: title,
        description: msg,
        duration: 5,
        icon: () => h(CloseCircleFilled, {style: 'color: #FF5733'}),
        placement
    });
};

const app = createApp(App)
app.use(store).use(router).use(antd).mount('#app');

//全局图标
const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}
