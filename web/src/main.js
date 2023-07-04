import Vue from 'vue'
import App from './App.vue'
import router from './router'
import rem from './rem'
// import './libs/wickedcss.css'
import api from './http/api';
import http from './http/http';
import axios from 'axios'
import qs from 'qs';
import loginData from './libs/loginData';
import copy from './libs/copy';
import VueDragscroll from 'vue-dragscroll'
import AOS from "aos";
import "../node_modules/aos/dist/aos.css";
import "./assets/css/public.scss"
import "./assets/font/font.css"
import Vant from 'vant';
import 'vant/lib/index.css';
Vue.use(Vant);
Vue.use(AOS)
Vue.use(VueDragscroll)
// import vConsole from 'vconsole'
// Vue.prototype.$vConsole = new vConsole()
import MetaInfo from 'vue-meta-info'
import VueI18n from 'vue-i18n'
import "./icons/index.js"
Vue.use(MetaInfo)
Vue.use(VueI18n)
import Video from 'video.js'
import 'video.js/dist/video-js.css'

Vue.prototype.$video = Video
const i18n = new VueI18n({
  locale: localStorage.getItem('lang') ? localStorage.getItem('lang') : 'en-US', 
  messages: {
    'en-US': require('./lang/en'),
    'ko-RE': require('./lang/ko'), 
    'jpn': require('./lang/jpn'),
  }
})
var flag
if (/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
  flag = false;
} else {
  flag = true;
}
Vue.prototype.$flag = flag;
Vue.prototype.$api = api;
Vue.prototype.$qs = qs
Vue.prototype.$loginData = loginData
// Vue.prototype.$loginData = loginData;
Vue.prototype.$axios = axios;
Vue.config.productionTip = false
// new WOW().init();
new Vue({
  render: h => h(App),
  router,
  rem,
  i18n,
  mounted() {
    document.dispatchEvent(new Event('render-event'))
  },
}).$mount('#app')