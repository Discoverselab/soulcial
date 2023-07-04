/*General method definition
matters need attention
1. It cannot be defined by () = > {} arrow function.
2. A large number of calculation methods are suggested to be computed.

ps: https://cn.vuejs.org/v2/api/#methods
*/
import {
    get,
} from '../../../http/http'
export default {
    urls() {
        var urls = ['Label_01', 'Label_02', 'Label_03', 'Label_04', 'Label_05', 'Label_06', 'Label_07', 'Label_08', 'Label_09', 'Label_10'];
        return urls[Math.floor((Math.random() * urls.length))]
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
    nuxt(){
        let me = this
        me.overlayshow = true
        setTimeout(()=>{
            me.overlayshow = false
            me.$router.push('/congr')
        },3000)
    }
}