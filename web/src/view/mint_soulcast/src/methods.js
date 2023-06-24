/*General method definition
matters need attention
1. It cannot be defined by () = > {} arrow function.
2. A large number of calculation methods are suggested to be computed.

ps: https://cn.vuejs.org/v2/api/#methods
*/
import {
    get,
} from '../../../http/http'
import { Toast } from 'vant';
export default {
    getData() {
        let data = this.$route.query
        let url = this.$api.nft.getMintPicture
        get(url, data)
            .then((res) => {
                if (res.code === 200) {
                    this.NftList = res.data
                }
            })
            .catch((error) => {
                console.log(error)
            });
    },
    ticks(item, index) {
        this.pictureUrl = item.pictureUrl
        this.squarePictureUrl = item.squarePictureUrl
        this.colorAttribute = item.colorAttribute
        this.active = index
    },
    nuxt() {
        if (this.active === 99) {
            Toast('Please choose what you like.')
            return
        }
        let data = this.$route.query
        data.pictureUrl = this.pictureUrl
        data.squarePictureUrl = this.squarePictureUrl
        data.colorAttribute = this.colorAttribute
        this.$router.push({
            path: "/mint_shaping",
            query: data
        })
    }
}