<template>
  <div class="followers">
    <div class="navigate">
      <img
        @click="$router.go(-1)"
        class="back"
        src="../../assets/back.png"
        alt=""
      />
      <div class="nav_name">
        <p class="name">followers</p>
      </div>
      <div></div>
    </div>
    <div class="followers_cont">
      <div class="followersList" v-for="(item,index) in Follower" :key="index">
        <div class="list_left">
            <img :src="item.portrait" alt="">
            <div class="name_address">
                <p class="name">Humphry</p>
                <p class="address">{{ item.address }}</p>
            </div>
        </div>
        <button class="Follows" :class="{noFollows:index==2}">{{ index!=2?'Follow':'Followed' }}</button>
      </div>
    </div>
  </div>
</template>
<script>
import watch from "./src/watch";
import methods from "./src/methods";
import computed from "./src/computed";
import AOS from "aos";
export default {
  name: "",
  data() {
    return {
      Follower: [
        {
          portrait: require("../../assets/portrait1.png"),
          address: "bc1pxane...yfgy06",
        },
        {
          portrait: require("../../assets/portrait2.png"),
          address: "bc1pxane...yfgy06",
        },
        {
          portrait: require("../../assets/portrait3.png"),
          address: "bc1pxane...yfgy06",
        },
        {
          portrait: require("../../assets/portrait4.png"),
          address: "bc1pxane...yfgy06",
        },
        {
          portrait: require("../../assets/portrait1.png"),
          address: "bc1pxane...yfgy06",
        },
        {
          portrait: require("../../assets/portrait2.png"),
          address: "bc1pxane...yfgy06",
        },
        {
          portrait: require("../../assets/portrait3.png"),
          address: "bc1pxane...yfgy06",
        },
        {
          portrait: require("../../assets/portrait4.png"),
          address: "bc1pxane...yfgy06",
        },
      ],
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: {},
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
