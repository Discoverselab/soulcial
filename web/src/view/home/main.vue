<template>
  <div class="home">
    <!--operate -->
    <div class="user_infor">
      <div class="set_top">
        <div></div>
        <div class="set_right">
          <img class="label" src="../../assets/label.png" alt="" />
          <img class="set" src="../../assets/set.png" alt="" />
        </div>
      </div>
      <!-- Avatar name and address -->
      <div class="userinfo">
        <img class="portrait" :src="UserInfo.avatar" alt="" />
        <!--  -->
        <div class="nameAddres">
          <p class="name">{{ UserInfo.userName }}</p>
          <p class="address">
            {{ substring($loginData.Auth_Token) }}
            <img
              v-copy="$loginData.Auth_Token"
              round
              src="../../assets//copy.png"
              alt=""
            />
          </p>
        </div>
      </div>
      <!-- User label -->
      <div class="label_cont">
        <div class="label_left">
          <div class="label label1">GameFi</div>
          <div class="label label2">Storage</div>
          <div class="label label3">DAO</div>
        </div>
        <img class="edit" src="../../assets/edit.png" alt="" />
      </div>
      <!-- Calculate points -->
      <div class="Calculate">
        <Hexagon
          v-if="this.values.length > 5"
          :type="true"
          :level="UserInfo.level"
          :levelScore="UserInfo.levelScore"
          :values="values"
        />
        <!-- Ranking data information -->
        <div class="rank_cont">
          <div @click="$router.push('/followers')" class="rank_list">
            <p class="value">26</p>
            <p class="key">Followers</p>
          </div>
          <div @click="$router.push('/following')" class="rank_list">
            <p class="value">159</p>
            <p class="key">Following</p>
          </div>
          <div @click="$router.push('/TopFans')" class="rank_list">
            <p class="value">RANK</p>
            <p class="key">Top Fans</p>
          </div>
        </div>
      </div>
    </div>
    <!-- Tab -->
    <div class="TabCont">
      <div
        class="Tab_list"
        :class="{
          Tab_list_Active: TabActive == item.id,
        }"
        @click="TabClick(item.id)"
        v-for="(item, index) in TabList"
        :key="index"
      >
        {{ item.name }}
      </div>
    </div>
    <div class="set_but" v-if="TabActive == 1">
      <button @click="link_mint">Mint SoulCast</button>
    </div>
    <!-- NFT list -->
    <div class="nft_cont">
      <div
        class="nft_list"
        v-for="(item, index) in NftList"
        @click="$router.push(`/explore_details?id=${item.id}`)"
        :key="index"
      >
        <div class="img_cont">
          <img class="nftUrl" :src="item.squarePictureUrl" alt="" />
          <div v-if="TabActive == 1 || TabActive == 2" class="point">
            <span v-for="index in 3" :key="index"></span>
          </div>
        </div>
        <div class="nft_infor">
          <p class="top_name_price">
            <span class="name">Top Pick</span>
            <span class="price">{{ item.topPick || 0 }} BNB</span>
          </p>
          <div class="flow_id">
            <img
              :class="`level${item.level}`"
              :src="levelImg[item.level]"
              alt=""
            />
            <span class="flow">{{ getNFTLevel[item.level] }}</span>
            <span class="flow_lin">#{{ item.id }}</span>
          </div>
          <div class="Listing">
            <span> Listing Price </span>
            <span> Cost </span>
          </div>
          <div class="price_botm">
            <span class="bot_price">{{ item.price || 0 }} BNB</span>
            <span class="bot_price">{{ item.price || 0 }} BNB</span>
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
import Wallet from "../../components/LinkWallet.vue";
import TabBar from "../../components/TabBar.vue";
import Hexagon from "../../components/Hexagon.vue";
import { getNFTLevel, levelImg } from "../../libs/target";
import Overlay from "../../components/Overlay.vue";
export default {
  name: "home",
  data() {
    return {
      overlayshow: false,
      levelImg: levelImg,
      getNFTLevel: getNFTLevel,
      UserInfo: {},
      values: [],
      TabActive: 1,
      TabList: [
        {
          name: "minted",
          id: 1,
        },
        {
          name: "collected",
          id: 2,
        },
        {
          name: "liked",
          id: 3,
        },
      ],
      NftList: [
        // {
        //   img: require("../../assets/Frame1.png"),
        // },
        // {
        //   img: require("../../assets/Frame2.png"),
        // },
        // {
        //   img: require("../../assets/Frame1.png"),
        // },
        // {
        //   img: require("../../assets/Frame2.png"),
        // },
        // {
        //   img: require("../../assets/Frame1.png"),
        // },
        // {
        //   img: require("../../assets/Frame2.png"),
        // },
      ],
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: { TabBar, Hexagon, Overlay },
  created() {
    let me = this;
    me.getUserInfo();
    me.getMintedNFTPage(1);
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
