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
    mintFreeNft() {
        this.overlayshow = true
        let url = this.$api.nft.mintFreeNft
        let data = this.shaping_data
        post(url, data, true).then((res) => {
            if (res.code === 200) {
                this.$router.push(`/mint_success?id=${res.data}`)
            } else {
                Toast(res.msg)
                setTimeout(() => {
                    this.$router.push('/home')
                }, 1000)
            }
            this.overlayshow = false
        }).catch((error) => {
            console.log(error)
        });
    }
}