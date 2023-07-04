/*General method definition
matters need attention
1. It cannot be defined by () = > {} arrow function.
2. A large number of calculation methods are suggested to be computed.

ps: https://cn.vuejs.org/v2/api/#methods
*/
import {
    get, post,
} from '../../../http/http'
import { Toast } from 'vant';
export default {
    urls() {
        var urls = ['Label_01', 'Label_02', 'Label_03', 'Label_04', 'Label_05', 'Label_06', 'Label_07', 'Label_08', 'Label_09', 'Label_10'];
        //Randomly get a value from the array
        return urls[Math.floor((Math.random() * urls.length))]
    },
    set_click(type) {
        if (!this.$loginData.Auth_Token) {
            this.walletShow = true
        }
        if (type == 1) {
            this.collectNFT()
        }
    },
    async collectNFT() {
        let me = this
        this.overlayshow = true
        let url = this.$api.nft.collectNFT
        let data = {
            tokenId: this.NFTDetail.id,
            txn: this.NFTDetail.id + "0xxxxssssddaxfjkhjkdskjhnbxhghjgkljh" + Math.random() * (99999999999 - 100) + 10
        }
        post(url, data, true)
            .then((res) => {
                if (res.code === 200) {
                    setTimeout(() => {
                        me.overlayshow = false
                        me.$router.push(`/mint_success?id=${this.NFTDetail.id}`);
                    }, 2000)
                } else {
                    me.overlayshow = false
                    Toast(res.msg)
                }
            })
            .catch((error) => {
                me.overlayshow = false
                Toast(error.msg)
            });
    },
    getData() {
        this.overlayshow = true
        let url = this.$api.nft.getNFTDetail + `?id=${this.$route.query.id}`
        get(url)
            .then((res) => {
                if (res.code === 200) {
                    this.NFTDetail = res.data
                    let item = res.data
                    this.values.push(item.charisma)
                    this.values.push(item.courage)
                    this.values.push(item.art)
                    this.values.push(item.wisdom)
                    this.values.push(item.energy)
                    this.values.push(item.extroversion)
                    this.overlayshow = false
                }
            })
            .catch((error) => { });
    },
    substring(address) {
        if (!address) return
        return address.substring(0, 6) + '...' + address.substring(address.length - 5, address.length)
    },
}