<template>
  <div id="app">
    <keep-alive>
      <router-view v-if="$route.meta.keepAlive"></router-view>
    </keep-alive>
    <router-view v-if="!$route.meta.keepAlive"></router-view>
  </div>
</template>

<script>
// import TabBar from "./components/TabBar.vue";
export default {
  name: "app",
  data() {
    return {
      BarList: ["/", "/earn", "/chat", "/home"],
    };
  },
  components: {
    // TabBar,
  },
  methods: {
    IsBar() {
      if (this.BarList.indexOf(this.$route.path) === -1) {
        return false;
      } else {
        return true;
      }
    },
    _isMobile() {
      let flag = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      );
      return flag;
    },
  },
  mounted: async function () {},
};
</script>

<style lang="scss">
html,
body,
div,
p,
span,
h1,
h2,
h3,
h4,
h5,
ul,
li,
input,
textarea,
u,
select,
a:focus {
  padding: 0;
  margin: 0;
  list-style: none;
  font-size: 12px;
  outline: none;
}
img[lazy="loading"] {
  display: block;
  width: 50px !important;
  height: 50px !important;
  margin: 0 auto;
}
input {
  -webkit-user-select: auto; /*webkit浏览器*/
}
textarea {
  -webkit-user-select: auto; /*webkit浏览器*/
}
html {
  height: 100%;
  background-color: #f5f5ed;
}
#app {
  /* font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px; */
}
body {
  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }
  &::-webkit-scrollbar-thumb {
    border-radius: 3px;
    background-color: #e6e6e6;
    box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.2);
  }
  &::-webkit-scrollbar-track {
    background-color: #fff;
    box-shadow: none;
  }
  &::-webkit-scrollbar-track {
    border-radius: 3px;
    background: rgba(0, 0, 0, 0.06);
    box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1);
  }

  @media screen and (min-width: 750px) {
    #app {
      padding: 10px 430px;
    }
    #app>div{
      box-shadow:5px 5px 5px 5px #888888;
    }
  }
}
</style>
