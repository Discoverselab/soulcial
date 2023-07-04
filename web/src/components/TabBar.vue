<template>
  <!-- tabBar -->
  <div class="barCont">
    <div
      @click="BarClick(item)"
      class="bar_list"
      v-for="(item, index) in BarList"
      :key="index"
    >
      <p v-if="BarActive != item.path" class="barName">{{ item.name }}</p>
      <img :class="`imgs${index + 1}`" v-else :src="item.img" alt="" />
    </div>
    <Wallet @close="walletShow=false" :walletShow="walletShow"></Wallet>
  </div>
</template>
<script>
import { post } from "../http/http";
import Wallet from "../components/LinkWallet.vue";
export default {
  props: {},
  data: function () {
    let _clientH = document.documentElement.clientHeight;
    return {
      BarActive: this.$route.path,
      walletShow:false,
      BarList: [
        {
          name: "Explore",
          img: require("../assets/ExploreBar.png"),
          path: "/",
        },
        {
          name: "Earn",
          img: require("../assets/EarnBar.png"),
          path: "/earn",
        },
        {
          name: "Chat",
          img: require("../assets/ChatBar.png"),
          path: "/chat",
        },
        {
          name: "Home",
          img: require("../assets/HomeBar.png"),
          path: "/home",
        },
      ],
    };
  },
  computed: {},
  components: {
    Wallet
  },
  watch: {},
  methods: {
    BarClick(data) {
      if (this.BarActive != data.path) {
        if (data.name !== "Home") {
          this.BarActive = data.path;
          this.$router.push(data.path);
        } else {
          if(!this.$loginData.Auth_Token){
            this.walletShow = true
          }else{
            this.BarActive = data.path;
            this.$router.push(data.path);
          }
        }
      }
    },
  },
  mounted: async function () {
    this.BarActive = this.$route.path;
    console.log(this.$route.path)
  },
};
</script>
<style lang="scss">
.barCont {
  width: 100%;
  height: 84px;
  box-sizing: border-box;
  background-color: #000;
  position: fixed;
  z-index: 9999999;
  bottom: 0;
  left: 0;
  padding: 0 22px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  .bar_list {
    height: 100%;
    width: 25%;
    display: flex;
    align-items: center;
    justify-content: center;
    .barName {
      font-weight: 900;
      font-family: "Inter";
      font-style: normal;
      font-size: 20px;
      color: #fff;
    }
    img {
      display: block;
      &.imgs1 {
        width: 32px;
      }
      &.imgs2 {
        width: 32px;
      }
      &.imgs3 {
        width: 27px;
      }
      &.imgs4 {
        width: 27px;
      }
    }
  }
}
</style>
