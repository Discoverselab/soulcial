/*General method definition
matters need attention
1. It cannot be defined by () = > {} arrow function.
2. A large number of calculation methods are suggested to be computed.

ps: https://cn.vuejs.org/v2/api/#methods
*/
import {
    get,
} from '../../../http/http'
import {
    RuntimeConnector,
    Extension
} from "@dataverse/runtime-connector";
const runtimeConnector = new RuntimeConnector(Extension);
import Clipboard from 'clipboard';
import { Toast } from 'vant';
let dataverse = false;
import {
    createRandomAccount,
    createClient,
    getCollection,
    createFromExternal,
    createFromPrivateKey,
    addDoc,
    updateDoc,
    deleteDoc,
    queryDoc,
    syncAccountNonce
} from "db3.js";
// import { useNetwork } from 'wagmi'
//
//lens
import {
    LensClient,
    production,
    development,
    isRelayerResult
} from "@lens-protocol/client";
import { ethers } from "ethers";

import IStorageProvider from "../../../libs/LocalStorageProvider";
console.log(IStorageProvider)
const client = new LensClient({
    environment: development,
    storage: IStorageProvider
});


const provider = new ethers.providers.Web3Provider(window.ethereum);
//
export default {
    async db3() {
        try {
            const account = await createFromExternal();
            const client = createClient(
                "https://rollup.cloud.db3.network",
                "https://index.cloud.db3.network",
                account
            );
            await syncAccountNonce(client);
            const collection = await getCollection(
                "0x13fb1131fc3d3093ed8e02edd0bfa63a774df75d",
                "score",
                client
            );
            console.log(collection);
            // 新增
            // const res  = await addDoc(collection, {
            //     abc: "The Three-Body Problem",
            //     author: "Cixin-Liu",
            //     rate: "989898"
            // });
            // 修改
            // const res  = await updateDoc(collection,'15', {
            //     abc: "The Three-Body Problem",
            //     author: "Cixin-Liu",
            //     rate: "4.9"
            // });
            // 删除
            // const res = await deleteDoc(collection, ['15']);
            // console.log(res);
            const resultSet = await queryDoc(collection, "/[soulscore=140]");
            console.log(resultSet)

        } catch (e) {
            console.log(e);
        }
    },
    isUser() {
        return this.UserInfo.address.toLocaleUpperCase() === this.address.toLocaleUpperCase()
    },
    async linkethers() {
        //签名
        const r = await client.authentication.generateChallenge(this.address);
        console.log(r)
        const provider = new ethers.providers.Web3Provider(window.ethereum);
        const signer = provider.getSigner();
        const signature = await signer.signMessage(r);
        console.log(this.address, signature);

        const authData = await client.authentication.authenticate(this.address, signature).then((res) => {
            console.log(res)
        })
        console.log(authData)
        const { data: { authenticate: { accessToken } } } = authData
        console.log({ accessToken })
        let res = await client.authentication.isAuthenticated();
        console.log(res);
    },

    async contect() {
        //连接钱包
        const account = await window.ethereum.send("eth_requestAccounts");
        if (account.result.length) {
            this.address = account.result[0];
            this.myFollow();
        }
    },

    async checkLink() {
        //检查是否连接
        const accounts = await provider.listAccounts();
        console.log(accounts);
        if (accounts.length) {
            this.address = accounts[0];
            this.myFollow();
        } else {
            this.contect();
        }
    },

    async follow() {
        // 获取推荐列表
        const recommendedProfiles = await client.profile.allRecommended();
        console.log(recommendedProfiles);
        // Follow
        const followTypedDataResult = await client.profile.createFollowTypedData({
            follow: [
                {
                    profile: recommendedProfiles[0].id,
                },
            ],
        });
        // 签名信息
        const data = followTypedDataResult.unwrap();
        console.log(data);
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
        console.log(broadcastResult);
        if (broadcastResult.value) {
            console.log('关注成功')
            setTimeout(async () => {
                await this.myFollow()
            }, 5000)
        } else {
            console.log("error：", broadcastResult)
        }
    },

    async unFollow() {
        //取消关注
        const unfollowTypedDataResult =
            await client.profile.createUnfollowTypedData({
                profile: '0x01',
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
                await this.myFollow()
            }, 5000)
        } else {
            console.log("error：", broadcastResult)
        }
    },
    async myFollow() {
        //获取我的关注
        let myFollow = await client.profile.allFollowing({
            address: this.address,
        });
        console.log("我的关注：", myFollow.items);

        // 获取关注我的
        const allOwnedProfiles = await client.profile.fetchAll({
            ownedBy: [this.address],
            limit: 1,
        });
        if (allOwnedProfiles.items.length) {
            this.myId = allOwnedProfiles.items[0].id;
            let followMe = await client.profile.allFollowers({
                profileId: this.myId,
            });
            console.log("关注我的：", followMe.items);
        }
    },

    async creatMyInfo() {//创建个人资料
        const profileCreateResult = await client.profile.create({
            handle: "lens.hpy",
            profilePictureUri: null,
            followModule: {
                revertFollowModule: true,
            },
        });
        const profileCreateResultValue = profileCreateResult.unwrap();

        if (!isRelayerResult(profileCreateResultValue)) {
            console.log(`Something went wrong`, profileCreateResultValue);
            return;
        }

        console.log(
            `Transaction was successfuly broadcasted with txId ${profileCreateResultValue.txId}`
        );
    },








    FollList(type) {
        if (type == 1) {
            if (this.UserInfo.followers <= 0 || !this.UserInfo.followers) {
                this.$toast('No Followers at the moment')
                return
            } else {
                this.$router.push('/followers')
            }
        }
        if (type == 2) {
            if (this.UserInfo.following <= 0 || !this.UserInfo.following) {
                this.$toast('No following for the time being')
                return
            } else {
                this.$router.push('/following')
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
    async getStream(id) {//获取流
        this.overlayshow = true
        // let res = await runtimeConnector.loadStreamsBy({
        //     modelId:dataverse?'kjzl6hvfrbw6c7bz8dm3olx6u48rc7acu6d5khlbu3yeltho4k3oluq2bsbxvdo':'kjzl6hvfrbw6c5bt9cmvdi2e3v43oei5jst3messykjnkqo2r5n3hhliw7ecy53',
        //     // pkh: this.pkh
        // });
        let res = await runtimeConnector.loadStream(id);
        console.log(res)
        // let data = {}
        // data = res[id].streamContent.content
        // this.values.push(data.charisma)
        // this.values.push(data.courage)
        // this.values.push(data.art)
        // this.values.push(data.wisdom)
        // this.values.push(data.energy)
        // this.values.push(data.extroversion)
        // this.overlayshow = false
    },
    link_mint() {
        this.$router.push('/mint_select')
    },
    substring(address) {
        return address.substring(0, 6) + '...' + address.substring(address.length - 5, address.length)
    },
    close() {
        this.$router.push('/')
    },
    getUserInfo() {
        let url = this.$api.infor.getUserInfo
        get(url).then((res) => {
            if (res.code === 200) {
                this.UserInfo = res.data
                let item = res.data
                if (item.levelScore) {
                    this.values.push(item.charisma)
                    this.values.push(item.courage)
                    this.values.push(item.art)
                    this.values.push(item.wisdom)
                    this.values.push(item.energy)
                    this.values.push(item.extroversion)
                } else {
                    this.$router.push('/welcome')
                }
                // this.getStream(res.data.streamId)
            }
        }).catch((error) => {
            console.log(error)
        });
    },
    TabClick(id) {
        this.TabActive = id
        this.getMintedNFTPage(id)
    },
    getMintedNFTPage(type) {
        this.overlayshow = true
        let data = {
            current: 1,
            size: 99
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