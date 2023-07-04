<template>
  <div class="succesed">
    <p class="title">Succesed!</p>
    <p class="title_infor">
      You have successfully <br />
      Connect Wallet:
    </p>
    <div class="addresscud">0x23de...3399</div>
    <div class="logo_cont">
      <img class="logo2" src="../../assets/twitter.png" alt="" />
    </div>
    <!-- operation -->
    <div class="cudset_but">
      <button @click="$router.push('/welcome')">link with twitter</button>
      <button @click="$router.push('/welcome')">Skip</button>
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
    return {};
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
