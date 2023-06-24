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
    nuxt() {
        let me = this
        me.overlayshow = true
        setTimeout(()=>{
            me.overlayshow = false
            this.$router.push({
                path: "/mint_soulcast",
                query: {
                    personality: this.PersonalityID + 1,
                    mood: this.MoadID + 1,
                    color: this.ColorID + 1,
                    weather: this.WeatherID + 1,
                }
            })
        },3000)

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
            }
        }).catch((error) => {
            console.log(error)
        });
    },
}