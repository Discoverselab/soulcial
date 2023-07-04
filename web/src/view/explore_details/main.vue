<template>
  <div class="explore_details">
    <div class="navigate">
      <img
        @click="$router.go(-1)"
        class="back"
        src="../../assets/back.png"
        alt=""
      />
      <div class="nav_name" v-if="NFTDetail.pictureUrl">
        <img class="userportrait" :src="NFTDetail.ownerUserAvatar" alt="" />
        <p class="name">{{ NFTDetail.ownerUserName }}</p>
      </div>
      <img class="label" src="../../assets/label.png" alt="" />
    </div>
    <div class="details_cont" v-if="NFTDetail.pictureUrl">
      <div class="Nft_details">
        <!-- NFT -->
        <div class="box" :class="{ flipped: turnShow }">
          <div class="img_icon img_icon1">
            <div
              class="match"
              :class="{ matchs: turnShow }"
              v-if="NFTDetail.pictureUrl && $loginData.Auth_Token"
            >
              <p
                :style="{ color: `hsla(${20 + 120}, 60%, 60%, 1)` }"
                class="center"
              >
                {{ NFTDetail.match || "0" }}%
              </p>
              <p
                :style="{ color: `hsla(${20 + 120}, 60%, 60%, 1)` }"
                class="name"
              >
                match
              </p>
            </div>
            <img :src="NFTDetail.pictureUrl" alt="" />
          </div>
          <!-- hexagonCalculate points -->
          <div class="img_icon img_icon2">
            <Hexagon
              v-if="this.values.length > 5"
              :type="false"
              :level="NFTDetail.level"
              :levelScore="NFTDetail.levelScore"
              :values="values"
            />
          </div>
        </div>
        <div class="bottom_infor" v-if="NFTDetail.pictureUrl">
          <svg-icon
            :style="{ color: `hsla(${20 + 120}, 60%, 60%, 1)` }"
            className="svgName"
            iconClass="Vector1"
          ></svg-icon>
          <div class="grade_price">
            <div class="grade">
              <img
                :class="`level${NFTDetail.level}`"
                :src="levelImg[NFTDetail.level]"
                alt=""
              />
              <p class="grade_name">{{ getNFTLevel[NFTDetail.level] }}</p>
              <p class="Personality_name">
                {{ getNFTPersonality[NFTDetail.personality] }}
              </p>
            </div>
            <p class="price" v-if="NFTDetail.price">
              {{ NFTDetail.price || 0 }}BNB
            </p>
            <p class="price priceinfp" v-else>mediator infp</p>
          </div>
          <div class="turn" @click="turnShow = !turnShow">
            <img src="../../assets/turn.png" alt="" />
          </div>
          <div class="love">
            <img src="../../assets/love.png" alt="" />
            {{ NFTDetail.likes }}
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
      <!-- more -->
      <div class="more">
        <van-collapse v-model="activeNames">
          <van-collapse-item name="1">
            <template #title>
              <div class="chid">#{{ NFTDetail.id }}</div>
            </template>
            <template #value>
              <div class="MoreDetail">More Detail</div>
            </template>
            <div class="more_list_cont">
              <div class="more_list">
                <span class="key">Contract Address</span>
                <span class="value">{{
                  substring(NFTDetail.contractAddress)
                }}</span>
              </div>
              <div class="more_list">
                <span class="key">Token ID</span>
                <span class="value">#{{ NFTDetail.id }}</span>
              </div>
              <div class="more_list">
                <span class="key">Blockchain</span>
                <span class="value">{{ NFTDetail.network }}</span>
              </div>
              <div class="more_list">
                <span class="key">Creator Earnings</span>
                <span class="value">2.5%</span>
              </div>
            </div>
          </van-collapse-item>
        </van-collapse>
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
          <p class="name">{{ NFTDetail.ownerUserName }}</p>
        </div>
      </div>
       <!-- operation -->
      <div class="set_but" v-if="NFTDetail.ownerAddress">
        <button
          v-if="
            NFTDetail.ownerAddress.toLocaleUpperCase() !=
            $loginData.Auth_Token.toLocaleUpperCase()&&NFTDetail.price
          "
          @click="set_click(1)"
        >
          Collect Now
        </button>
        <button @click="set_click(2)">Pick & Earn</button>
      </div>
      <!-- picks -->
      <div class="more">
        <van-collapse v-model="activeNamesPicks">
          <van-collapse-item name="1">
            <template #title>
              <div class="chid">Picks</div>
            </template>
            <div class="more_list_cont">
              <div class="more_list more_list_picks">
                <span class="key">Price</span>
                <span class="key">From</span>
              </div>
              <div
                class="more_list"
                v-for="(item, index) in PicksList"
                :key="index"
              >
                <span class="value">{{ item.price }}</span>
                <span class="value">{{ item.from }}</span>
              </div>
            </div>
          </van-collapse-item>
        </van-collapse>
      </div>
      <!-- history -->
      <div class="more history">
        <van-collapse v-model="activeNamesHistory">
          <van-collapse-item name="1">
            <template #title>
              <div class="chid">History</div>
            </template>
            <div class="more_list_cont">
              <div class="more_list more_list_history">
                <div class="type_cont">
                  <img class="left_icon" src="../../assets/start.png" alt="" />
                  <div>
                    <span class="value">Collect</span>
                    <p class="hisname">+ More</p>
                  </div>
                </div>
                <div>
                  <span class="value">0.50 BNB</span>
                  <p class="hisname hisname_right">1 hour ago</p>
                </div>
              </div>
              <div class="more_list more_list_history">
                <div class="type_cont">
                  <img
                    class="left_icon"
                    src="../../assets/Transfer.png"
                    alt=""
                  />
                  <div>
                    <span class="value">Transfer</span>
                    <p class="hisname">+ More</p>
                  </div>
                </div>
                <div>
                  <span class="value">0.52 BNB</span>
                  <p class="hisname hisname_right">2 hour ago</p>
                </div>
              </div>
              <div
                style="padding: 0; margin: 0; border: none"
                class="more_list more_list_history"
              >
                <div class="type_cont">
                  <img class="left_icon" src="../../assets/mint.png" alt="" />
                  <div>
                    <span class="value">Mint</span>
                    <p class="hisname">+ More</p>
                  </div>
                </div>
                <div>
                  <p class="hisname hisname_right">0 hour ago</p>
                </div>
              </div>
            </div>
          </van-collapse-item>
        </van-collapse>
      </div>
    </div>
    <Overlay :overlayshow="overlayshow"></Overlay>
    <Wallet @close="walletShow = false" :walletShow="walletShow"></Wallet>
  </div>
</template>
<script>
import watch from "./src/watch";
import methods from "./src/methods";
import computed from "./src/computed";
import AOS from "aos";
import Wallet from "../../components/LinkWallet.vue";
import Hexagon from "../../components/Hexagon.vue";
import Overlay from "../../components/Overlay.vue";
import {
  getNFTLevel,
  getNFTPersonality,
  NFTColor,
  getNFTMood,
  Weather,
  levelImg,
} from "../../libs/target";
export default {
  name: "",
  data() {
    return {
      overlayshow: false,
      turnShow: false,
      levelImg: levelImg,
      values: [],
      getNFTPersonality: getNFTPersonality,
      getNFTLevel: getNFTLevel,
      NFTColor: NFTColor,
      getNFTMood: getNFTMood,
      Weather: Weather,
      walletShow: false,
      activeNames: [],
      activeNamesPicks: [],
      activeNamesHistory: [],
      NFTDetail: {},
      PicksList: [
        {
          price: "0.56 BNB",
          from: "8FE87",
        },
        {
          price: "0.42 BNB",
          from: "FC36CE",
        },
        {
          price: "0.37 BNB",
          from: "29F37",
        },
        {
          price: "0.29 BNB",
          from: "HKZ21G",
        },
      ],
      TabActive: 1,
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: {
    Wallet,
    Hexagon,
    Overlay,
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
