<template>
  <div class="congr">
    <p class="title">Congratulations!</p>
    <p class="title_infor">Your soul in web3 has been revealed</p>
    <Hexagon
      v-if="this.values.length > 5"
      :type="false"
      :level="UserInfo.level"
      :levelScore="UserInfo.levelScore"
      :values="values"
    />
    <!-- operation -->
    <div class="cudset_but">
      <button @click="$router.push('/mint_select')">
        Mint SoulCast NFT
        <span class="FREE">FREE</span>
      </button>
      <button class="Cancel">SHare</button>
      <p class="skip" @click="$router.push('/')">
        <span>Skip</span> and <span>Explore</span> other souls
      </p>
    </div>
  </div>
</template>
<script>
import watch from "./src/watch";
import methods from "./src/methods";
import computed from "./src/computed";
import AOS from "aos";
import Hexagon from "../../components/Hexagon.vue";
export default {
  name: "",
  data() {
    return {
      UserInfo: {},
      values: [],
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: {
    Hexagon,
  },
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
