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
let dataverse = false;
export default {
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
        this.$router.push('/welcome')
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
                this.values.push(item.charisma)
                this.values.push(item.courage)
                this.values.push(item.art)
                this.values.push(item.wisdom)
                this.values.push(item.energy)
                this.values.push(item.extroversion)
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