<template>
  <!-- tabBar -->
  <div class="wallet">
    <van-action-sheet @close="close" v-model="walletShow">
      <div class="content">
        <p class="headLin"></p>
        <P class="title">Connect Wallet</P>
        <img class="wallet_img" src="../assets/wellat_img.png" alt="" />
        <div class="wallet_list" @click="metamask">
          <div class="list_left">
            <img src="../assets/metamask.png" alt="" />
            <p>MetaMask</p>
          </div>
          <svg-icon className="Polygon" iconClass="Polygon"></svg-icon>
        </div>
        <div class="wallet_list" @click="WalletConnect">
          <div class="list_left">
            <img src="../assets/wallet_connect.png" alt="" />
            <p>WalletConnect</p>
          </div>
          <svg-icon className="Polygon" iconClass="Polygon"></svg-icon>
        </div>
        <div class="attention">
          <van-icon name="info" size="20" />
          <div class="attention_text">
            <p class="attention_title">What is a Wallet?</p>
            <p class="title_one">A Home for your Digital Assets</p>
            <p class="text">
              Wallets are used to send, receivestore, and display digital assets
              likeEthereum and NFTs.
            </p>
            <p class="title_one">A New Way to Log In</p>
            <p class="text">
              Instead of creating new accounts andpasswords on every website,
              justconnect your wallet.
            </p>
          </div>
        </div>
        <div class="lins">
          <p class="lin"></p>
          <p class="lin_text">Or connect with Particle Wallet</p>
          <p class="lin"></p>
        </div>
        <div class="img_list">
          <img
            @click="particle('phone', 1)"
            src="../assets/Subtract1.png"
            alt=""
          />
          <img
            @click="particle('email', 2)"
            src="../assets/Exclude.png"
            alt=""
          />
          <img
            @click="particle('apple', 3)"
            src="../assets/phones.png"
            alt=""
          />
          <img @click="particle('google', 4)" src="../assets/gogo.png" alt="" />
          <img
            @click="particle('google', 5)"
            src="../assets/Twitters.png"
            alt=""
          />
        </div>
      </div>
    </van-action-sheet>
    <Overlay :overlayshow="overlayshow"></Overlay>
  </div>
</template>
  <script>
import { post, get } from "../http/http";
import WalletConnectProvider from "@walletconnect/web3-provider";
import Overlay from "../components/Overlay.vue";
import {
  RuntimeConnector,
  Extension,
  RESOURCE,
} from "@dataverse/runtime-connector";
const runtimeConnector = new RuntimeConnector(Extension);
let dataverse = false;
const app = dataverse ? "soulcial" : "soulcial3";
import { ParticleNetwork, WalletEntryPosition } from "@particle-network/auth";
import { ParticleProvider } from "@particle-network/provider";
import Web3 from "web3";
import { WALLET } from "@dataverse/runtime-connector";
import { Toast } from "vant";
export default {
  props: {
    walletShow: Boolean,
  },
  data: function () {
    let _clientH = document.documentElement.clientHeight;
    return {
      wallet: "",
      address: "",
      loginType: 0,
      overlayshow: false,
      preferredAuthType: "",
    };
  },
  computed: {},
  components: { Overlay },
  watch: {},
  methods: {
    async testLink(data) {
      //Connect wallet
      try {
        this.overlayshow = true;
        const res = await runtimeConnector.connectWallet();
        this.wallet = res.wallet;
        await this.linkCapability();
        await this.creatData(data);
      } catch (error) {
        this.overlayshow = false;
      }
    },
    async linkCapability() {
      //Establish capability model createCapability
      let pkh = await runtimeConnector.createCapability({
        app,
        resource: RESOURCE.CERAMIC,
        wallet: this.wallet,
      });
      this.pkh = pkh;
    },
    async creatData(data) {
      //Create streamcreateStream
      const encrypted = JSON.stringify({
        level: false,
        charisma: false,
        extroversion: false,
        energy: false,
        wisdom: false,
        art: false,
        courage: false,
        soulscore: false,
      });
      let Stream = await runtimeConnector.createStream({
        modelId: dataverse
          ? "kjzl6hvfrbw6c7bz8dm3olx6u48rc7acu6d5khlbu3yeltho4k3oluq2bsbxvdo"
          : "kjzl6hvfrbw6c5bt9cmvdi2e3v43oei5jst3messykjnkqo2r5n3hhliw7ecy53",
        streamContent: {
          level: String(data.level),
          charisma: data.charisma,
          extroversion: data.extroversion,
          energy: data.energy,
          wisdom: data.wisdom,
          art: data.art,
          courage: data.courage,
          soulscore: data.levelScore,
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
          encrypted,
        },
      });
      this.overlayshow = false;
      this.setUserStreamId(Stream.streamId);
    },
    setUserStreamId(id) {
      let data = {
        streamId: id,
      };
      let url = this.$api.infor.setUserStreamId;
      post(url, data, true)
        .then((res) => {
          if (res.code === 200) {
            this.close();
            this.$router.push("/welcome");
          }
        })
        .catch((error) => {});
    },
    close() {
      this.$emit("close", true);
    },
    getUserInfo(type) {
      let url = this.$api.infor.getUserInfo;
      get(url)
        .then((res) => {
          if (res.code === 200) {
            if (res.data.streamId) {
              this.close();
              this.$router.push("/welcome");
            } else {
              let me = this;
              setTimeout(() => {
                me.testLink(res.data);
              }, 100);
            }
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    login(address) {
      let url =
        this.$api.login.login +
        `?address=${address}&&loginType=${this.loginType}&&particleType=${this.preferredAuthType}`;
      post(url)
        .then((res) => {
          if (res.code === 200) {
            this.$loginData.in({
              authToken: address,
              id: res.data.tokenValue,
            });
            this.getUserInfo();
          }
        })
        .catch((error) => {});
    },
    // Link metamask wallet
    async metamask() {
      if (typeof window.ethereum !== "undefined") {
        let provider = window.ethereum;
        const accounts = await provider.request({
          method: "eth_requestAccounts",
        });
        this.loginType = 0;
        this.preferredAuthType = "";
        this.login(accounts);
      } else {
        window.location.href("https://metamask.io/");
      }
    },
    // interlinkageWalletConnect
    async WalletConnect() {
      const provider = new WalletConnectProvider({
        infuraId:
          process.env.VUE_APP_MODE === "production"
            ? "5491a1035f5d42cfb17022aa25a97ea9"
            : "5491a1035f5d42cfb17022aa25a97ea9",
      });
      try {
        let accounts = await provider.enable();
        this.loginType = 0;
        this.preferredAuthType = "";
        this.login(accounts[0]);
        this.disconnect(provider);
      } catch (err) {
        console.log(err);
      }
    },
    disconnect(provider) {
      // Monitor wallet exit status
      provider.on("disconnect", (code, reason) => {
        this.$loginData.out();
        location.href = "/";
      });
    },
    async particle(preferredAuthType, type) {
      this.preferredAuthType = type;
      const particle = new ParticleNetwork({
        projectId: "07962ac6-fde2-4363-82af-9eacc9b6ada5",
        clientKey: "cT8A9u0bPUBmP7mKLPhy3lkhbWlv2hcbyFmsau4Z",
        appId: "47102ab8-d9f6-41b4-87f8-7b5f07072963",
        chainName: "Ethereum", //optional: current chain name, default Ethereum.
        chainId: 1, //optional: current chain id, default 1.
        wallet: {
          //optional: by default, the wallet entry is displayed in the bottom right corner of the webpage.
          displayWalletEntry: true, //show wallet entry when connect particle.
          defaultWalletEntryPosition: WalletEntryPosition.BR, //wallet entry position
          uiMode: "dark", //optional: light or dark, if not set, the default is the same as web auth.
          supportChains: [
            { id: 1, name: "Ethereum" },
            { id: 5, name: "Ethereum" },
            { id: 56, name: "bsc" },
            { id: 97, name: "bsc" },
          ], // optional: web wallet support chains.
          customStyle: {}, //optional: custom wallet style
        },
      });
      const userInfo = await particle.auth.login({
        preferredAuthType: preferredAuthType, //support facebook,google,twitter,apple,discord,github,twitch,microsoft,linkedin etc.
      });
      const particleProvider = new ParticleProvider(particle.auth);
      //if you use web3.js
      window.web3 = new Web3(particleProvider);
      window.web3.currentProvider.isParticleNetwork; // => true
      const accounts = await web3.eth.getAccounts();
      this.loginType = 1;
      this.login(accounts[0]);
    },
  },
  created() {},
  mounted: async function () {},
};
</script>
<style lang="scss">
.wallet {
  .van-action-sheet {
    max-height: 100% !important;
  }
  .content {
    padding: 8px 25px 42px 25px;
    background-color: #f5f5ed;
    .headLin {
      width: 88px;
      height: 4px;
      background: #dfdfce;
      border-radius: 100px;
      margin: 0 auto 30px auto;
    }
    .title {
      font-family: "Inter";
      font-style: normal;
      font-weight: 900;
      font-size: 20px;
      text-transform: uppercase;
      text-align: center;
      color: #000000;
    }
    .wallet_img {
      display: block;
      width: 136px;
      height: auto;
      margin: 35px auto 50px auto;
    }
    .wallet_list {
      display: flex;
      align-items: center;
      justify-content: space-between;
      border-bottom: 2px solid #dfdfce;
      padding-bottom: 10px;
      margin-bottom: 20px;
      .list_left {
        display: flex;
        align-items: center;
        img {
          display: block;
          width: 22.5px;
          height: auto;
          margin-right: 18px;
        }
        p {
          font-family: "Inter";
          font-style: normal;
          font-weight: 600;
          font-size: 16px;
          color: #000000;
        }
      }
      .Polygon {
        width: 13px;
        height: 8.5px;
      }
    }
    .attention {
      display: flex;
      .attention_text {
        margin-left: 15px;
        .attention_title {
          font-family: "Inter";
          font-style: normal;
          font-weight: 600;
          font-size: 14px;
          color: #000000;
          margin-bottom: 15px;
        }
        .title_one {
          font-family: "Inter";
          font-style: normal;
          font-weight: 600;
          font-size: 12px;
          color: #000000;
          margin-bottom: 5px;
        }
        .text {
          font-family: "Inter";
          font-style: normal;
          font-weight: 500;
          font-size: 12px;
          line-height: 150%;
          color: #62625f;
          margin-bottom: 15px;
        }
      }
    }
    .lins {
      display: flex;
      align-content: center;
      align-items: center;
      margin-top: 26px;
      .lin {
        width: 100%;
        border-bottom: 2px solid #dfdfce;
      }
      .lin_text {
        font-family: "Inter";
        font-style: normal;
        font-weight: 600;
        font-size: 10px;
        text-transform: uppercase;
        color: #000000;
        margin: 0 10px;
        white-space: nowrap;
      }
    }
    .img_list {
      margin-top: 30px;
      display: flex;
      align-items: center;
      justify-content: space-around;
      img {
        transform: scale(0.5); /* scaling */
      }
    }
  }

  .ovrlay {
    display: flex;
    align-items: center;
    justify-content: center;
  }
}
</style>
  