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

export default {
    urls() {
        var urls = ['Label_01', 'Label_02', 'Label_03', 'Label_04', 'Label_05', 'Label_06', 'Label_07', 'Label_08', 'Label_09', 'Label_10'];
        return urls[Math.floor((Math.random() * urls.length))]
    },
    isUser() {
        return this.UserInfo.address.toLocaleUpperCase() === this.address.toLocaleUpperCase()
    },
    getUserInfo() {
        let url = this.$api.infor.getUserInfo
        get(url).then((res) => {
            if (res.code === 200) {
                this.UserInfo = res.data
            }
        }).catch((error) => {
            console.log(error)
        });
    },
    nuxt() {
        let me = this
        me.overlayshow = true
        let url = this.$api.infor.updateUserScore
        get(url).then((res) => {
            if (res.code === 200) {
                this.db3(res.data)
            } else {
                this.$toast(res.msg)
            }
        }).catch((error) => {
            this.$toast(error)
        });
    },
    async db3(data) {
        let me = this
        try {
            const account = await createFromExternal();
            const client = createClient(
                "https://rollup.cloud.db3.network",
                "https://index.cloud.db3.network",
                account
            );
            await syncAccountNonce(client);
            const collection = await getCollection(
                "0x1488e1c10d37961e377b29386756201e4bfede1f",
                "SoulScore",
                client
            );
            // 新增
            const res = await addDoc(collection, {
                address:this.$loginData.Auth_Token,
                level:data.level,
                charisma: data.charisma || 0,
                extroversion: data.extroversion || 0,
                energy: data.energy || 0,
                wisdom: data.wisdom || 0,
                art: data.art || 0,
                courage: data.courage || 0,
                soulscore: data.levelScore || 0,
                createdAt: new Date().toISOString(),
                updatedAt: new Date().toISOString(),
            });
            // console.log(res)
            // const resultSet = await queryDoc(collection, "/[author=Cixin-Liu]");
            // console.log(resultSet)
            me.overlayshow = false
            me.$router.push('/congr')
        } catch (e) {
            this.$toast(e)
            console.log(e);
        }
    },
}