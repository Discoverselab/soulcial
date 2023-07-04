<template>
  <div class="purchase_success">
    <div class="navigate">
      <img
        @click="$router.go(-1)"
        class="back"
        src="../../assets/back.png"
        alt=""
      />
      <div class="nav_name">
        <p class="name"></p>
      </div>
      <span></span>
    </div>
    <div class="success">
      <p class="success_title">Congratulations!</p>
      <p class="success_infor">You have succcessfully <br /> collected the SoulCast.</p>
    </div>
    <div class="details_cont">
      <div class="Nft_details">
        <!-- NFT -->
        <div class="box">
          <div class="img_icon">
            <img :src="NFTDetail.pictureUrl" alt="" />
          </div>
        </div>
        <div class="bottom_infor" v-if="NFTDetail.pictureUrl">
          <svg-icon
            :style="{
              color: `hsla(${NFTDetail.colorAttribute + 120}, 60%, 60%, 1)`,
            }"
            className="svgName"
            iconClass="Vector1"
          ></svg-icon>
          <div class="grade_price">
            <div class="grade">
              <img src="../../assets/level1.png" alt="" />
              <p class="grade_name">{{ getNFTLevel[NFTDetail.level] }}</p>
              <p class="Personality_name">
                {{ getNFTPersonality[NFTDetail.personality] }}
              </p>
            </div>
            <p class="price" v-if="NFTDetail.price">
              {{ NFTDetail.price || 0 }}ETH
            </p>
            <p class="price priceinfp" v-else>mediator infp</p>
          </div>
          <div class="love">
            #{{ NFTDetail.id }}
          </div>
        </div>
        <!-- User label -->
        <div class="label_cont">
          <div class="label_left">
            <div class="label label1">{{ getNFTMood[NFTDetail.mood] }}</div>
            <div class="label label2">{{ Weather[NFTDetail.weather] }}</div>
            <div class="label label3">{{ NFTColor[NFTDetail.color] }}</div>
          </div>
        </div>
      </div>
            <!-- The author has something. -->
      <div class="author">
        <div class="author_list">
          <div class="portrait">
            <img class="portrait1" :src="NFTDetail.mintUserAvatar" alt="" />
            <img class="chat_link" src="../../assets/chat.png" alt="" />
          </div>
          <p class="Created">Created By</p>
          <p class="name">{{ NFTDetail.mintUserName }}</p>
        </div>
        <div class="author_list">
          <div class="portrait">
            <img class="portrait1" :src="NFTDetail.ownerUserAvatar" alt="" />
            <img class="chat_link" src="../../assets/chat.png" alt="" />
          </div>
          <p class="Created">Owned By</p>
          <p class="name">You</p>
        </div>
      </div>
      <!-- Selected picture -->
      <!-- operation -->
      <div class="shapset_but">
        <button @click="$router.push('/')">EXPLORE MORE</button>
        <button class="prohibit">SHARE</button>
      </div>
    </div>
  </div>
</template>
<script>
import watch from "./src/watch";
import methods from "./src/methods";
import computed from "./src/computed";
import AOS from "aos";
import {
  getNFTLevel,
  getNFTPersonality,
  NFTColor,
  getNFTMood,
  Weather,
} from "../../libs/target";
export default {
  name: "",
  data() {
    return {
      NFTDetail: {},
      getNFTLevel: getNFTLevel,
      getNFTPersonality: getNFTPersonality,
      NFTColor: NFTColor,
      getNFTMood: getNFTMood,
      Weather: Weather,
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: {},
  created() {
    let me = this;
    me.getData();
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
