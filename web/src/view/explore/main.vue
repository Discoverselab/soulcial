<template>
  <div class="explore">
    <!-- Tab -->
    <div class="TabCont">
      <div class="Tab_left">
        <div
          class="Tab_list"
          :class="{
            Tab_list_Active: TabActive == item.id,
          }"
          @click="TabActive = item.id"
          v-for="(item, index) in TabList"
          :key="index"
        >
          {{ item.name }}
        </div>
      </div>
      <div class="Tab_right">
        <img src="../../assets/sift.png" alt="" />
      </div>
    </div>
    <!-- NFT List -->
    <div class="nft_cont">
      <div class="cont_cnet cont_left">
        <div
          @click="$router.push(`/explore_details?id=${item.id}`)"
          class="Nft_list"
          v-if="index % 2 == 0"
          v-for="(item, index) in nftList"
          :key="index"
        >
          <div class="img_icon">
            <div class="match" v-if="item.pictureUrl&&$loginData.Auth_Token">
              <p
                :style="{ color: `hsla(${20 + 120}, 60%, 60%, 1)` }"
                class="center"
              >
                {{ item.match || "0" }}%
              </p>
              <p
                :style="{ color: `hsla(${20 + 120}, 60%, 60%, 1)` }"
                class="name"
              >
                match
              </p>
            </div>
            <img :src="item.pictureUrl" alt="" />
          </div>
          <div class="bottom_infor" v-if="item.pictureUrl">
            <svg-icon
              :style="{ color: `hsla(${20 + 120}, 60%, 60%, 1)` }"
              className="svgName"
              :iconClass="urls()"
            ></svg-icon>
            <div class="grade_price">
              <div class="grade">
                <img
                  :class="`level${item.level}`"
                  :src="levelImg[item.level]"
                  alt=""
                />
                <p class="grade_name">{{ getNFTLevel[item.level] }}</p>
              </div>
              <p class="price" v-if="item.price">{{ item.price }}BNB</p>
              <p class="price priceinfp" v-else>mediator infp</p>
            </div>
            <div class="love">
              <img src="../../assets/love.png" alt="" />
              {{ item.likes }}
            </div>
          </div>
        </div>
      </div>
      <div class="cont_cnet cont_right">
        <div
          @click="$router.push(`/explore_details?id=${item.id}`)"
          class="Nft_list"
          v-if="index % 2 == 1"
          v-for="(item, index) in nftList"
          :key="index"
        >
          <div class="img_icon">
            <div class="match" v-if="item.pictureUrl&&$loginData.Auth_Token">
              <p
                :style="{ color: `hsla(${20 + 120}, 60%, 60%, 1)` }"
                class="center"
              >
                {{ item.match || "0" }}%
              </p>
              <p
                :style="{ color: `hsla(${20 + 120}, 60%, 60%, 1)` }"
                class="name"
              >
                match
              </p>
            </div>
            <img :src="item.pictureUrl" alt="" />
          </div>
          <div class="bottom_infor" v-if="item.pictureUrl">
            <svg-icon
              :style="{ color: `hsla(${20 + 120}, 60%, 60%, 1)` }"
              className="svgName"
              :iconClass="urls()"
            ></svg-icon>
            <div class="grade_price">
              <div class="grade">
                <img
                  :class="`level${item.level}`"
                  :src="levelImg[item.level]"
                  alt=""
                />
                <p class="grade_name">{{ getNFTLevel[item.level] }}</p>
              </div>
              <p class="price" v-if="item.price">{{ item.price }}BNB</p>
              <p class="price priceinfp" v-else>mediator infp</p>
            </div>
            <div class="love">
              <img src="../../assets/love.png" alt="" />
              {{ item.likes }}
            </div>
          </div>
        </div>
      </div>
    </div>
    <Overlay :overlayshow="overlayshow"></Overlay>
    <TabBar></TabBar>
  </div>
</template>
<script>
import watch from "./src/watch";
import methods from "./src/methods";
import computed from "./src/computed";
import AOS from "aos";
import TabBar from "../../components/TabBar.vue";
import { getNFTLevel, levelImg } from "../../libs/target";
import Overlay from "../../components/Overlay.vue";
export default {
  name: "",
  data() {
    return {
      overlayshow:false,
      levelImg: levelImg,
      TabActive: 1,
      walletShow: true,
      getNFTLevel: getNFTLevel,
      nftList: [],
      TabList: [
        {
          name: "For You",
          id: 1,
        },
        {
          name: "Following",
          id: 2,
        },
        {
          name: "Liked",
          id: 3,
        },
      ],
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: {
    TabBar,
    Overlay
  },
  created() {
    let me = this;
  },
  mounted: async function () {
    console.log("this：", this);
    console.log("$route：", this.$route);
    this.getData();
    AOS.init({
      offset: 200,
      duration: 200, //duration
      easing: "ease-in-sine",
      delay: 100,
    });
    console.log(this.$loginData);
    window.addEventListener("scroll", this.scrollToTop);
    this.$nextTick(()=>{
      
    })
   
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
