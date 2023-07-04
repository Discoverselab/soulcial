/*General method definition
matters need attention
1. It cannot be defined by () = > {} arrow function.
2. A large number of calculation methods are suggested to be computed.

ps: https://cn.vuejs.org/v2/api/#methods
*/
import {
    get, post
} from '../../../http/http'
import WalletConnectProvider from "@walletconnect/web3-provider";
import {
    RuntimeConnector,
    Extension,
    RESOURCE
} from "@dataverse/runtime-connector";
const runtimeConnector = new RuntimeConnector(Extension);
const app = 'soulcial'
export default {
    async updataStream(){//Update stream
        const encrypted = JSON.stringify({
            level: false,
            charisma: false,
            extroversion: false,
            energy: false,
            wisdom: false,
            art: false,
            courage: false,
            soulscore: false
        });
        await runtimeConnector.updateStream({
            streamId: 'kjzl6kcym7w8y9exy11mvicci8yoc4uaiaefslcoew664o3qrypfhh641nwnl2p',
            streamContent: {
                level: '88',
                charisma: 88,
                extroversion: 88,
                energy: 88,
                wisdom: 88,
                art: 88,
                courage: 88,
                soulscore: 88,
                createdAt: new Date().toISOString(),
                updatedAt: new Date().toISOString(),
                encrypted,

            },
        });
    },



    urls() {
        var urls = ['Label_01', 'Label_02', 'Label_03', 'Label_04', 'Label_05', 'Label_06', 'Label_07', 'Label_08', 'Label_09', 'Label_10'];
        //Randomly get a value from the array
        return urls[Math.floor((Math.random() * urls.length))]
    },
    getNFTLevels() {
        let url = this.$api.nft.getNFTLevel
        get(url)
            .then((res) => {
                if (res.code === 200) {
                    this.getNFTLevelList = res.data.records
                }
            })
            .catch((error) => { });
    },
    getData() {
        this.overlayshow = true
        let url = this.$api.nft.getNFTPage
        let data = {
            current: 1,
            orderColumn: 1,
            orderType: 0,
            size: 99
        }
        get(url, data)
            .then((res) => {
                if (res.code === 200) {
                    this.nftList = res.data.records
                    this.overlayshow = false
                }
            })
            .catch((error) => { });
    }
}