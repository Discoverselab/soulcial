<template>
  <div class="mint_select">
    <div class="navigate">
      <img
        @click="$router.go(-1)"
        class="back"
        src="../../assets/back.png"
        alt=""
      />
      <div class="nav_name">
        <p class="name">Mint SoulCast</p>
      </div>
      <span></span>
    </div>
    <!-- mint slogin -->
    <div class="slogin">
      <p class="slogin_title">Choose your favorites to mint it.</p>
      <img class="sou_lin" src="../../assets/sou_lin.png" alt="" />
    </div>

    <div class="details_cont">
      <!-- NFTList -->
      <div class="nft_cont">
        <div
          @click="ticks(item, index)"
          class="nft_list"
          :class="{ nft_listac: active == index }"
          v-for="(item, index) in NftList"
          :key="index"
        >
          <img
            v-if="active != index"
            class="tick"
            src="../../assets/tick.png"
            alt=""
          />
          <img v-else class="tick" src="../../assets/ticks.png" alt="" />
          <img class="nftimg" :src="item.squarePictureUrl" alt="" />
        </div>
      </div>
      <!-- operation -->
      <div class="set_but">
        <button @click="nuxt">NEXT</button>
        <button @click="$router.go(-1)">Cancel</button>
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
      active: 99,
      pictureUrl: "",
      squarePictureUrl: "",
      colorAttribute: "",
      NftList: [],
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: {},
  created() {
    let me = this;
    this.getData();
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
