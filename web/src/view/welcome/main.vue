<template>
  <div class="welcome">
    <p class="title">welcome!</p>
    <p class="title_infor">Let's create something fun!</p>
    <div class="userportrait">
      <img class="userportrait" :src="UserInfo.avatar" alt="" />
      <p class="humphry">{{ UserInfo.userName }}</p>
      <img class="con_lin" src="../../assets/con_lin.png" alt="" />
    </div>
    <img class="link_img link_img1" src="../../assets/link1.png" alt="" />
    <img class="link_img link_img2" src="../../assets/link2.png" alt="" />
    <img class="link_img link_img3" src="../../assets/link3.png" alt="" />
    <div class="cudset_but">
      <button @click="nuxt">reveal my Soul</button>
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
      overlayshow:false,
      UserInfo: {},
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: {Overlay},
  created() {
    let me = this;
    me.getUserInfo();
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
