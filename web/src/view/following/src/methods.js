/*General method definition
matters need attention
1. It cannot be defined by () = > {} arrow function.
2. A large number of calculation methods are suggested to be computed.

ps: https://cn.vuejs.org/v2/api/#methods
*/
import {
    get, post
} from '../../../http/http'
import {
    LensClient,
    production,
    development
} from "@lens-protocol/client";
import { ethers } from "ethers";
const client = new LensClient({
    environment: development,
});
const provider = new ethers.providers.Web3Provider(window.ethereum);
let version = false;
export default {
    getFollowers() {
        this.overlayshow = true
        let data = {
            userId: this.$route.query.id
        }
        let url = this.$api.infor.getFollowing
        get(url, data).then((res) => {
            if (res.code === 200) {
                this.Follower = res.data
            } else {
                this.$toast(res.msg)
            }
            this.overlayshow = false
        }).catch((error) => {
            this.overlayshow = false
        });
    },
    followUser(item) {
        this.UserInfo = item
        if (version) {
            this.follow()
        } else {
            this.unFollow()
        }
    },

    follow() {
        this.overlayshow = true
        let data = {
            followType: 0 ,
            subscribeUserId: this.UserInfo.id
        }
        let url = this.$api.infor.followUser
        post(url, data, true).then((res) => {
            if (res.code === 200) {
                this.$toast("Successful operation")
                this.getFollowers()
            } else {
                this.$toast(res.msg)
            }
            this.overlayshow = false
        }).catch((error) => {
            this.overlayshow = false
        });
    },

    async linkethers() {
        await window.ethereum.request({
            method: "wallet_switchEthereumChain",
            params: [{ chainId: "0x13881" }],
        });
        await window.ethereum.request({
            method: "eth_requestAccounts",
        });
        //签名
        const r = await client.authentication.generateChallenge(this.$loginData.Auth_Token);
        const signer = provider.getSigner();
        const signature = await signer.signMessage(r);
        const authData = await client.authentication.authenticate(this.$loginData.Auth_Token, signature)
        let res = await client.authentication.isAuthenticated();
        this.unFollow()
    },

    async unFollow() {
        await window.ethereum.request({
            method: "wallet_switchEthereumChain",
            params: [{ chainId: "0x13881" }],
        });
        await window.ethereum.request({
            method: "eth_requestAccounts",
        });
        this.overlayshow = true
        //取消关注
        try {
            const unfollowTypedDataResult =
                await client.profile.createUnfollowTypedData({
                    profile: this.UserInfo.lensProfile,
                });

            const data = unfollowTypedDataResult.unwrap();
            const signedTypedData = await provider
                .getSigner()
                ._signTypedData(
                    data.typedData.domain,
                    data.typedData.types,
                    data.typedData.value
                );

            const broadcastResult = await client.transaction.broadcast({
                id: data.id,
                signature: signedTypedData,
            });
            if (broadcastResult.value) {
                console.log('取关成功')
                setTimeout(async () => {
                    await this.follow()
                }, 1000)
            }
        } catch (err) {
            this.linkethers()
        }
    },


    substring(address) {
        return address.substring(0, 6) + '...' + address.substring(address.length - 5, address.length)
    },
}