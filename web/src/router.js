import Vue from 'vue'
import Router from 'vue-router'
import explore from '@/view/explore/main'
import earn from '@/view/earn/main'
import chat from '@/view/chat/main'
import home from '@/view/home/main'
import explore_details from '@/view/explore_details/main'
import followers from '@/view/followers/main'
import following from '@/view/following/main'
import TopFans from '@/view/TopFans/main'
import Succesed from '@/view/Succesed/main'
import welcome from '@/view/welcome/main'
import congr from '@/view/congr/main'
import mint_select from '@/view/mint_select/main'
import mint_soulcast from '@/view/mint_soulcast/main'
import mint_shaping from '@/view/mint_shaping/main'
import mint_success from '@/view/mint_success/main'
Vue.use(Router)
export default new Router({
  mode: 'hash',
  linkActiveClass: 'active',
  routes: [
    {
      path: '/',
      name: 'explore',
      component: explore,
    },
    {
      path: '/earn',
      name: 'earn',
      component: earn,
    },
    {
      path: '/chat',
      name: 'chat',
      component: chat
    },
    {
      path: '/home',
      name: 'home',
      component: home,
    },
    {
      path: '/explore_details',
      name: 'explore_details',
      component: explore_details,
    },
    {
      path: '/followers',
      name: 'followers',
      component: followers,
    },
    {
      path: '/following',
      name: 'following',
      component: following,
    },
    {
      path: '/TopFans',
      name: 'TopFans',
      component: TopFans,
    },
    {
      path: '/Succesed',
      name: 'Succesed',
      component: Succesed,
    },
    {
      path: '/welcome',
      name: 'welcome',
      component: welcome,
    },
    {
      path: '/congr',
      name: 'congr',
      component: congr,
    },
    {
      path: '/mint_select',
      name: 'mint_select',
      component: mint_select,
    },
    {
      path: '/mint_soulcast',
      name: 'mint_soulcast',
      component: mint_soulcast,
    },
    {
      path: '/mint_shaping',
      name: 'mint_shaping',
      component: mint_shaping,
    },
    {
      path: '/mint_success',
      name: 'mint_success',
      component: mint_success,
    }
  ],
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return {
        x: 0,
        y: 0
      }
    }
  }
})
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}