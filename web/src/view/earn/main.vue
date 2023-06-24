<template>
  <div class="earn">
    <!-- Tab -->
    <div class="TabCont">
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
    <!-- earnings -->
    <div class="earnings" v-if="TabActive==1">
        <div class="earn_list earn_list_one">
          <p class="list_name">DST Balance:</p>
          <p class="balance">1,204,050</p>
        </div>
        <div class="earn_list_title">
          <p class="title">History</p>
        </div>
        <div class="earn_list" v-for="(item,index) in HistoryList" :key="index">
          <div class="list_left">
             <img v-if="item.number>0" src="../../assets/Pick.png" alt="">
             <img v-else src="../../assets/Promotion.png" alt="">
             <div class="title_time">
                <p class="list_name">{{ item.number>0?'Pick':'Promotion' }}</p>
                <p class="times">{{ item.time }}</p>
             </div>
          </div>
          <button class="list_right" :class="{list_right_fu:item.number<0}">{{ item.number>0?`+${item.number} DST`:`${item.number} DST` }}</button>
        </div>
    </div>
    <!-- wallet -->
    <div class="earnings" v-if="TabActive==2">
        <div class="earn_list earn_list_one">
          <p class="list_name">Available Fund:</p>
          <p class="balance">29.05 BNB</p>
        </div>
        <div class="earn_list">
          <p class="list_name" style="color:#62625F">Wallet Balance</p>
          <p class="er_balance">29.05 BNB</p>
        </div>
        <div class="earn_list">
          <p class="list_name" style="color:#62625F">Pool Balance</p>
          <p class="er_balance" style="color:#E03131">0 BNB</p>
        </div>
        <div class="earn_list">
          <div>
            <p class="list_name" style="color:#E03131">Your Pool Balance is empty</p>
            <p class="emptys">Add funds to Pick. You can withdraw anytime. No minimum required.</p>
          </div>
        </div>
    </div>
    <!-- set but -->
    <div class="set_but"  v-if="TabActive==2">
        <button>Add Funds To Pool</button>
        <button class="disabled">Withdraw From Pool</button>
    </div>
    <!-- collections -->
    <div class="collections" v-show="TabActive==3">
      <!-- Secondary menu list -->
      <div class="ercont_cont">
          <div class="ercont">
            <div @click="erID=item.id" class="ercont_list" :class="{ercont_list_active:item.id==erID}" v-for="(item,index) in erList" :key="index">
                {{ item.name }}
            </div>
          </div>
          <div class="Tab_right">
          <img src="../../assets/sift.png" alt="" />
        </div>
      </div>
      <!-- NFT List -->
      <div class="nft_cont">
         <div class="nft_list" v-for="(item,index) in NftList" :key="index">
            <div class="img_cont">
                <img class="nftUrl" :src="item.img" alt="">
                <div v-if="erID==1" class="point">
                  <span v-for="index in 3" :key="index"></span>
                </div>
                <img v-else class="subtract" src="../../assets/Subtract.png" alt="">
            </div>
            <div class="nft_infor">
              <p class="top_name_price">
                <span class="name">Top Pick</span>
                <span class="price">0.45 BNB</span>
              </p>
              <div class="flow_id">
                <img src="../../assets/level1.png" alt="">
                <span class="flow">Flow</span>
                <span class="flow_lin">#1527</span>
              </div>
              <div class="Listing">
                  <span>
                    Listing Price
                  </span>
                  <span>
                    Cost
                  </span>
              </div>
              <div class="price_botm">
                <span class="bot_price">0.50 BNB</span>
                <span class="bot_price">0.45 BNB</span>
              </div>
            </div>
         </div>
      </div>
    </div>
    <TabBar></TabBar>
  </div>
</template>
<script>
import watch from "./src/watch";
import methods from "./src/methods";
import computed from "./src/computed";
import AOS from "aos";
import TabBar from "../../components/TabBar.vue";
export default {
  name: "",
  data() {
    return {
      TabActive: 1,
      erID:1,
      TabList: [
        {
          name: "earnings",
          id: 1,
        },
        {
          name: "wallet",
          id: 2,
        },
        {
          name: "collections",
          id: 3,
        },
      ],
      erList:[
        {
          name:'Inventory',
          id:1
        },
        {
          name:'Picks',
          id:2
        }
      ],
      HistoryList:[
        {
          time:'2023-04-25 19:41',
          number:234
        },
        {
          time:'2023-04-25 19:41',
          number:220
        },
        {
          time:'2023-04-25 19:41',
          number:-400
        }
      ],
      NftList:[
        {
          img:require('../../assets/Frame1.png')
        },
        {
          img:require('../../assets/Frame2.png')
        },
        {
          img:require('../../assets/Frame1.png')
        },
        {
          img:require('../../assets/Frame2.png')
        },
        {
          img:require('../../assets/Frame1.png')
        },
        {
          img:require('../../assets/Frame2.png')
        },
      ]
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: {
    TabBar
  },
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
