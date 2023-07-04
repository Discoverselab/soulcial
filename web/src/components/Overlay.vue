<template>
  <div class="Overlay">
    <van-overlay
      class-name="ovrlay"
      :lock-scroll="true"
      z-index="9999"
      :show="overlayshow"
    >
      <img src="../assets/soulcial-loading.gif" alt="" />
    </van-overlay>
  </div>
</template>
    
  <script>
export default {
  props: {
    overlayshow: {
      type: Boolean,
    },
  },
  data() {
    return {};
  },
  computed: {},
  created() {},
  mounted() {},
  methods: {},
};
</script>
    
<style lang="scss" scoped>
.ovrlay {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(0,0,0,0.3);
  img{
    transform: scale(0.5); /* scaling */
  }
}
</style>
    