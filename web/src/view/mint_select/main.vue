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
   <div class="details_cont">
    <!-- Personality -->
    <div class="Personality">
      <van-collapse v-model="activeNames">
        <van-collapse-item name="1">
          <template #title>
            <div class="chid">Personality</div>
          </template>
          <template #value>
            <div class="MoreDetail">{{Personality[PersonalityID] }}</div>
          </template>
          <div class="more_list_cont">
            <button :class="{butac:PersonalityID==index}" @click="PersonalityID=index" v-for="(item,index) in Personality" :key="index">{{item}}</button>
          </div>
        </van-collapse-item>
      </van-collapse>
    </div>
    <!-- Mood -->
    <div class="Personality">
      <van-collapse v-model="activeNames1">
        <van-collapse-item name="1">
          <template #title>
            <div class="chid">Moad</div>
          </template>
          <template #value>
            <div class="MoreDetail">{{Moad[MoadID]}}</div>
          </template>
          <div class="more_list_cont">
            <button :class="{butac:MoadID==index}" @click="MoadID=index" v-for="(item,index) in Moad" :key="index">{{item}}</button>
          </div>
        </van-collapse-item>
      </van-collapse>
    </div>
    <!-- Weather -->
    <div class="Personality">
      <van-collapse v-model="activeNames2">
        <van-collapse-item name="1">
          <template #title>
            <div class="chid">Weather</div>
          </template>
          <template #value>
            <div class="MoreDetail">{{Weather[WeatherID]}}</div>
          </template>
          <div class="more_list_cont">
            <button :class="{butac:WeatherID==index}" @click="WeatherID=index" v-for="(item,index) in Weather" :key="index">{{item}}</button>
          </div>
        </van-collapse-item>
      </van-collapse>
    </div>
    <!-- Color -->
    <div class="Personality">
      <van-collapse v-model="activeNames3">
        <van-collapse-item name="1">
          <template #title>
            <div class="chid">Color</div>
          </template>
          <template #value>
            <div class="MoreDetail">{{ Color[ColorID] }}</div>
          </template>
          <div class="more_list_cont">
            <button :class="{butac:ColorID==index}" @click="ColorID=index" v-for="(item,index) in Color" :key="index">{{item}}</button>
          </div>
        </van-collapse-item>
      </van-collapse>
    </div>
    <div class="mark" v-if="this.values.length>5">
      <Hexagon :type="false" :level="UserInfo.level" :levelScore="UserInfo.levelScore" :values="values" />
    </div>

    <!-- <img class="mark" src="../../assets/mark.png" alt="" /> -->
     <!-- operation -->
    <div class="set_but">
        <button @click="nuxt">NEXT</button>
        <button @click="$router.go(-1)">Cancel</button>
    </div>
    </div> 
    <Overlay :overlayshow="overlayshow"></Overlay>
  </div>
</template>
<script>
import watch from "./src/watch";
import methods from "./src/methods";
import computed from "./src/computed";
import AOS from "aos";
import Hexagon from "../../components/Hexagon.vue";
import Overlay from "../../components/Overlay.vue";
export default {
  name: "",
  data() {
    return {
      overlayshow:false,
      activeNames: [],
      activeNames1: [],
      activeNames2: [],
      activeNames3: [],
      Personality:['Architect INTJ','Logician INTP','Commander ENTJ','Debater ENTP','Advocate INFJ','Mediator INFP','Protagonist ENFJ','Campaigner ENFP',' Logistician ISTJ','Defender ISFJ','Executive ESTJ','Consul ESFJ','Virtuoso ISTP','Entrepreneur ESTP','Entertainer ESFP'],
      Moad:['Excited','calm','Angry','Shocked','cheerful','Confused','heartbroken','Fearful'],
      Weather:['Sunny','cloudy','Overcast','drizzling','stormy','windy','Misty','Snowy'],
      Color:['White','red','orange','Yellow','Green','Blue','Indigo','Violet','Black'],
      PersonalityID:0,
      MoadID:0,
      WeatherID:0,
      ColorID:0,
      TabActive: 1,
      UserInfo:{},
      values:[]
    };
  },
  watch: watch,
  methods: methods,
  computed: computed,
  components: {
    Hexagon,
    Overlay
  },
  created() {
    let me = this;
    me.getUserInfo()
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
