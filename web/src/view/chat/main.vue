<template>
  <div class="chat">
    <div class="chat_top">
      <span></span>
      <span>CHAT</span>
      <img src="../../assets/Union.png" alt="" />
    </div>
    <div class="chat_cont">
      <div class="chat_list" v-for="(item, index) in ChatList" :key="index">
        <div class="list_left">
          <div class="portrait">
            <img class="portrait_img" :src="item.portrait" alt="" />
            <img class="online" src="../../assets/online.png" alt="" />
          </div>
          <div class="name_news">
            <p class="name">{{ item.name }}</p>
            <p class="news">{{ item.news }}</p>
          </div>
        </div>
        <div class="list_right">
          <p class="times">{{ item.time }}</p>
          <span class="numbers" v-if="item.number>0">{{ item.number }}</span>
        </div>
      </div>
    </div>
    <TabBar></TabBar>
  </div>
</template>
<script>
import watch from "./src/watch";
import methods from "./src/methods";
import computed from "./src/computed";
import AOS from "aos";
import TabBar from "../../components/TabBar.vue";
export default {
  name: "home",
  data() {
    return {
      ChatList: [
        {
          portrait: require("../../assets/portrait1.png"),
          name: "Lili",
          news: "I’m watching Friends, what are I’m watching Friends, what are",
          time: "34 min",
          number: 3,
        },
        {
          portrait: require("../../assets/portrait2.png"),
          name: "Lana",
          news: "See you on the next meeting! 😂",
          time: "1 hour",
          number: 12,
        },
        {
          portrait: require("../../assets/portrait3.png"),
          name: "Lana",
          news: "Really find the subject very interesting 😍",
          time: "1 hour",
          number: 0,
        },
        {
          portrait: require("../../assets/portrait4.png"),
          name: "Lana",
          news: "So cool and I love it 😜",
          time: "4 hour",
          number: 0,
        },
      ],
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: {TabBar},
  created() {
    let me = this;
  },
  mounted: async function () {
    console.log("this：", this);
    console.log("$route：", this.$route);
    AOS.init({
      offset: 200,
      duration: 200, //duration
      easing: "ease-in-sine",
      delay: 100,
    });
    console.log(this.$loginData);
    window.addEventListener("scroll", this.scrollToTop);
  },
  beforeRouteLeave(to, form, next) {
    // Leave the route to remove the scrolling event
    window.removeEventListener("scroll", this.scrollToTop);
    next();
  },
  destroyed() {},
};
</script>

<style lang="scss">
@import "./sass/style.scss";
</style>
