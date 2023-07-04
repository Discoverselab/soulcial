<template>
  <div class="Nft_list">
    <div class="img_icon">
      <div class="match">
        <p
          :style="{ color: `hsla(${item.hsla + 120}, 60%, 60%, 1)` }"
          class="center"
        >
          {{ item.percen }}
        </p>
        <p
          :style="{ color: `hsla(${item.hsla + 120}, 60%, 60%, 1)` }"
          class="name"
        >
          match
        </p>
      </div>
      <img :src="item.url" alt="" />
    </div>
    <div class="bottom_infor">
      <svg-icon
        :style="{ color: `hsla(${item.hsla + 120}, 60%, 60%, 1)` }"
        className="svgName"
        :iconClass="urls()"
      ></svg-icon>
      <div class="grade_price">
        <div class="grade">
          <img src="../../../../assets/level1.png" alt="" />
          <p class="grade_name">{{ item.name }}</p>
        </div>
        <p class="price">{{ item.price }}BNB</p>
      </div>
      <div class="love">
        <img src="../../../../assets/love.png" alt="" />
        {{ item.number }}
      </div>
    </div>
  </div>
</template>
  <script>
export default {
  props: {},
  computed: {},
};
</script>
<style scoped lang="scss">
.Nft_list {
  margin-bottom: 38px;

  .img_icon {
    width: 164px;
    position: relative;

    img {
      width: 100%;
    }

    .match {
      position: absolute;
      right: 0;
      top: 0;
      text-align: right;

      .center {
        font-family: "DINCond-Bold";
        font-style: normal;
        font-weight: 700;
        font-size: 35px;
      }

      .name {
        font-family: "DINCond-Bold";
        font-style: normal;
        font-weight: 700;
        font-size: 16px;
        text-transform: uppercase;
      }
    }
  }

  .bottom_infor {
    position: relative;
    width: 165px;
    height: 80px;
    margin-top: -30px;

    .grade_price {
      position: absolute;
      top: 5px;
      left: 0;

      .grade {
        display: flex;
        align-items: center;

        img {
          display: block;
          min-width: 12.26px;
          min-height: 10px;
          margin-right: 3px;
        }

        .grade_name {
          font-family: "DINCond-Bold";
          font-style: normal;
          font-weight: 500;
          font-size: 14px;
          text-transform: uppercase;
          color: #000000;
        }
      }

      .price {
        font-family: "DINCond-Bold";
        font-style: normal;
        font-weight: 500;
        font-size: 32px;
      }
    }

    .love {
      display: flex;
      align-items: center;
      position: absolute;
      right: 5px;
      bottom: 10px;
      font-family: "DINCond-Bold";
      font-style: normal;
      font-weight: 500;
      font-size: 14px;
      text-transform: uppercase;
      img {
        display: block;
        margin-right: 3px;
        margin-top: 3px;
      }
    }
  }
}
</style>