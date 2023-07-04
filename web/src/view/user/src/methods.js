/*General method definition
matters need attention
1. It cannot be defined by () = > {} arrow function.
2. A large number of calculation methods are suggested to be computed.

ps: https://cn.vuejs.org/v2/api/#methods
*/
import {
    get, post
} from '../../../http/http'
import Clipboard from 'clipboard';
import { Toast } from 'vant';
import {
    LensClient,
    production,
    development
} from "@lens-protocol/client";
let version = false;
import { ethers } from "ethers";
const client = new LensClient({
    environment: development,
});
const provider = new ethers.providers.Web3Provider(window.ethereum);
export default {
    FollList(type) {
        if (type == 1) {
            if (this.UserInfo.followers <= 0 || !this.UserInfo.followers) {
                this.$toast('No Followers at the moment')
                return
            } else {
                this.$router.push(`/followers?id=${this.UserInfo.id}`)
            }
        }
        if (type == 2) {
            if (this.UserInfo.following <= 0 || !this.UserInfo.following) {
                this.$toast('No following for the time being')
                return
            } else {
                this.$router.push(`/following?id=${this.UserInfo.id}`)
            }
        }
    },
    copy() {
        const clipboard = new Clipboard('.copy-button', {
            text: () => this.$loginData.Auth_Token
        });
        clipboard.on('success', e => {
            Toast('Copy succeeded')
        });
        clipboard.on('error', e => {
            Toast('No content');
        })
    },
    substring(address) {
        return address.substring(0, 6) + '...' + address.substring(address.length - 5, address.length)
    },
    close() {
        this.$router.push('/')
    },
    getUserInfo() {
        this.overlayshow = true
        let url = this.$api.infor.getUserInfo
        let data = {
            userId: this.$route.query.id
        }
        get(url, data).then((res) => {
            if (res.code === 200) {
                this.UserInfo = res.data
                let item = res.data
                this.values.push(item.charisma)
                this.values.push(item.courage)
                this.values.push(item.art)
                this.values.push(item.wisdom)
                this.values.push(item.energy)
                this.values.push(item.extroversion)
            }
            this.overlayshow = false
        }).catch((error) => {
            this.overlayshow = false
            console.log(error)
        });
    },
    TabClick(id) {
        this.TabActive = id
        this.getMintedNFTPage(id)
    },
    followUser() {
        if (version) {
            this.follow()
        } else {
            this.UserInfo.isFollow ? this.unFollow() : this.follow_lens()
        }
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
        this.UserInfo.isFollow ? this.unFollow() : this.follow_lens()
    },
    async follow_lens() {
        await window.ethereum.request({
            method: "wallet_switchEthereumChain",
            params: [{ chainId: "0x13881" }],
        });
        await window.ethereum.request({
            method: "eth_requestAccounts",
        });
        this.overlayshow = true
        // 关注
        try {
            const followTypedDataResult = await client.profile.createFollowTypedData({
                follow: [
                    {
                        profile: this.UserInfo.lensProfile,
                    },
                ],
            });
            // 签名信息
            const data = followTypedDataResult.unwrap();
            // sign with the wallet
            const signedTypedData = await provider
                .getSigner()
                ._signTypedData(
                    data.typedData.domain,
                    data.typedData.types,
                    data.typedData.value
                );
            console.log(signedTypedData);
            const broadcastResult = await client.transaction.broadcast({
                id: data.id,
                signature: signedTypedData,
            });
            if (broadcastResult.value) {
                console.log('关注成功')
                setTimeout(async () => {
                    await this.follow()
                }, 1000)
            }
        } catch (err) {
            console.log(err)
            this.linkethers()
        }
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
    follow() {
        this.overlayshow = true
        let data = {
            followType: this.UserInfo.isFollow ? 0 : 1,
            subscribeUserId: this.$route.query.id
        }
        let url = this.$api.infor.followUser
        post(url, data, true).then((res) => {
            if (res.code === 200) {
                this.$toast("Successful operation")
                this.getUserInfo()
            } else {
                this.$toast(res.msg)
            }
            this.overlayshow = false
        }).catch((error) => {
            this.overlayshow = false
        });
    },
    getMintedNFTPage(type) {
        this.overlayshow = true
        let data = {
            current: 1,
            size: 99,
            userId: this.$route.query.id
        }
        let url = type == 1 ? this.$api.infor.getMintedNFTPage : this.$api.infor.getCollectNFTPage
        this.NftList = []
        get(url, data).then((res) => {
            if (res.code === 200) {
                this.NftList = res.data.records
            }
            this.overlayshow = false
        }).catch((error) => {
            this.overlayshow = false
        });
    }
}