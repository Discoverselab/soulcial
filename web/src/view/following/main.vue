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
        <p class="name">following</p>
      </div>
      <div></div>
    </div>
    <div class="followers_cont">
      <div class="followersList" v-for="(item, index) in Follower" :key="index">
        <div class="list_left">
          <img :src="item.avatar" alt="" />
          <div class="name_address">
            <p class="name">{{ item.userName.replace(".test","")  }}</p>
            <p class="address">{{ substring(item.address) }}</p>
          </div>
        </div>
        <button
          v-if="!$route.query.id"
          @click="followUser(item)"
          class="Follows noFollows"
        >
          Followed
        </button>
      </div>
    </div>
    <Overlay :overlayshow="overlayshow"></Overlay>
  </div>
</template>
<script>
import watch from "./src/watch";
import methods from "./src/methods";
import computed from "./src/computed";
import Overlay from "../../components/Overlay.vue";
import AOS from "aos";
export default {
  name: "",
  data() {
    return {
      UserInfo:{},
      overlayshow: false,
      Follower: [
        // {
        //   portrait: require("../../assets/portrait1.png"),
        //   address: "bc1pxane...yfgy06",
        // },
        // {
        //   portrait: require("../../assets/portrait2.png"),
        //   address: "bc1pxane...yfgy06",
        // },
        // {
        //   portrait: require("../../assets/portrait3.png"),
        //   address: "bc1pxane...yfgy06",
        // },
        // {
        //   portrait: require("../../assets/portrait4.png"),
        //   address: "bc1pxane...yfgy06",
        // },
        // {
        //   portrait: require("../../assets/portrait1.png"),
        //   address: "bc1pxane...yfgy06",
        // }
      ],
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: { Overlay },
  created() {
    let me = this;
  },
  mounted: async function () {
    console.log("this：", this);
    console.log("$route：", this.$route);
    this.getFollowers();
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
