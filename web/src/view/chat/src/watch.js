/* attribute value monitoring
1. It cannot be defined by () = > {} arrow function.
ps: https://cn.vuejs.org/v2/api/#watch
*/
export default {
    watch: {
        $route(to, from) {
            console.log(to,from)
        }
    },
};