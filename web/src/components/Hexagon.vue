<template>
  <div class="hex" :class="{ hextype: type }">
    <p class="tags tags1">
      charisma <span> {{ values[0] }}</span>
    </p>
    <p class="tags tags2">
      courage <span> {{ values[1] }}</span>
    </p>
    <p class="tags tags3">
      art <span> {{ values[2] }}</span>
    </p>
    <p class="tags tags4">
      wisdom <span> {{ values[3] }}</span>
    </p>
    <p class="tags tags5">
      energy <span> {{ values[4] }}</span>
    </p>
    <p class="tags tags6">
      extroversion <span> {{ values[5] }}</span>
    </p>
    <div class="calculate">
      <img
        :class="`calculateimg${level}`"
        class="calculateimg"
        :src="levelImg[level]"
        alt=""
      />
      <p class="leve">{{ getNFTLevel[level] }}</p>
      <p class="leve_number">{{ levelScore }}</p>
    </div>
    <div id="main"></div>
  </div>
</template>
  
<script>
import * as echarts from "echarts";
import { getNFTLevel, levelImg } from "../libs/target";
var chartDom;
var myChart;
export default {
  props: {
    levelScore: {
      type: Number,
    },
    level: {
      type: Number,
    },
    type: {
      type: Boolean,
    },
    values: {
      type: Array,
    },
  },
  data() {
    return {
      levelImg: levelImg,
      getNFTLevel: getNFTLevel,
    };
  },
  computed: {},
  created() {},
  mounted() {
    chartDom = document.getElementById("main");
    myChart = echarts.init(chartDom);
    let me = this;
    setTimeout(() => {
      me.init();
    }, 0);
    window.addEventListener("resize", () => {
      setTimeout(() => {
        myChart.resize();
      }, 100);
    });
  },
  methods: {
    init() {
      var option;
      option = {
        tooltip: {
          show: false,
        },
        legend: {
          enabled: false,
        },
        radar: [
          {
            indicator: [
              { max: 100 },
              { max: 100 },
              { max: 100 },
              { max: 100 },
              { max: 100 },
              { max: 100 },
            ],
            splitArea: {
              show: false,
              areaStyle: {
                color: "rgba(0,0,0,0)",
              },
            },
            splitLine: {
              show: true,
              lineStyle: {
                width: 1,
                color: "rgba(0,0,0,0)", 
              },
            },
            axisLine: {
              lineStyle: {
                color: "rgba(0,0,0,0)", 
              },
            },
            radius: ["0%", "100%"],
            center: ["50%", "50%"],
          },
        ],
        series: [
          {
            type: "radar",
            tooltip: {
              trigger: "item",
            },
            data: [
              {
                value: this.values,
                symbol: "none",
                areaStyle: {
                  color: "#F3B228",
                  opacity: 1,
                },
                lineStyle: {
                  color: "rgba(0,0,0,0)",
                },
                name: "",
              },
            ],
          },
        ],
      };

      option && myChart.setOption(option);
      myChart.resize();
    },
  },
};
</script>
  
<style lang="scss" scoped>
.hex {
  margin: 0 auto;
  width: 248px;
  height: 272px;
  background-image: url("../assets/hexBG.png");
  background-size: 100% 100%;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  &.hextype {
    background-image: url("../assets/hexBGs.png") !important;
  }
  .tags {
    position: absolute;
    font-family: "Inter";
    font-style: normal;
    font-weight: 700;
    font-size: 10px;
    text-transform: uppercase;
    color: #000;
    letter-spacing: -0.01em;
    display: flex;
    align-items: center;
    white-space: nowrap;
    &.tags1 {
      left: 128px;
      top: 8px;
      transform: rotate(29deg);
    }
    &.tags2 {
      left: -8px;
      top: 36px;
      transform: rotate(-30deg);
    }
    &.tags3 {
      left: -25px;
      top: 150px;
      transform: rotate(-90deg);
    }

    &.tags4 {
      left: 25px;
      bottom: 15px;
      transform: rotate(28deg);
    }
    &.tags5 {
      left: 180px;
      bottom:30px;
      transform: rotate(-30deg);
    }
    &.tags6 {
      left: 202px;
      top: 108px;
      transform: rotate(90deg);
    }
    span {
      font-family: "Inter";
      font-style: normal;
      font-weight: 700;
      font-size: 10px;
      text-transform: uppercase;
      color: #f3b228;
    }
  }
  .calculate {
    position: relative;
    z-index: 99;
    .calculateimg {
      display: block;
      margin: 0 auto;
      height: auto;
      object-fit: cover;
      &.calculateimg1 {
        width: 24px !important;
      }
      &.calculateimg2 {
        width: 22px !important;
      }
      &.calculateimg3 {
        width: 22px !important;
      }
      &.calculateimg4 {
        width: 22px !important;
      }
      &.calculateimg5 {
        width: 24px !important;
      }
    }
    .leve {
      font-family: "DINCond-Bold";
      font-style: normal;
      font-weight: 500;
      font-size: 32px;
      text-transform: uppercase;
      color: #000000;
      text-align: center;
      margin: 5px 0 0px 0;
    }
    .leve_number {
      font-family: "DINCond-Bold";
      font-style: normal;
      font-weight: 500;
      font-size: 32px;
      text-transform: uppercase;
      color: #000000;
      text-align: center;
    }
  }
  #main {
    position: absolute;
    left: 14px;
    top: 30px;
    width: 220px;
    height: 220px;
    max-width: 228px;
    max-height: 228px;
  }
}
</style>
  