import Vue from 'vue'
import App from './App'
import router from './router/index'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import 'element-ui/lib/theme-chalk/base.css';
import axios from 'axios';
import VueAxios from 'vue-axios';
import Vuex from 'vuex'
import {Message} from 'element-ui'
import {putRequest} from "./axios/index";


Vue.use(Vuex);

Vue.use(VueAxios, axios);

Vue.config.productionTip = false;
Vue.use(ElementUI);

// 在跳转前执行
router.beforeEach((to, form, next) => {
  // 注销
  if (to.path === '/logout') {
    // 清空
    localStorage.removeItem("loginInfo");
    localStorage.removeItem("loginTime");
    sessionStorage.removeItem("isLogin");
    // 跳转到登录
    return next({path: '/login'});
  }
  //判断是否登陆
  let isLogin = sessionStorage.getItem("isLogin");
  if (isLogin === "true") {
    next();
  }else{
    let time = localStorage.getItem("loginTime");
    if (!time) {
      return next();
    }
    else if (new Date().getTime() > (parseInt(time) + 1000 * 60 * 60 * 24 * 7)) {
      Message.warning("距离上次登陆已超过7天，需要重新登陆。");
      // 登陆失效，距离上次登陆超过7天
      return next();
    }
    let login = localStorage.getItem("loginInfo");
    if (login == null) {
      return next();
    } else {
      let strings = login.split("pmzzz");
      let loginInfo = {
        userName: strings[0],
        password: strings[1]
      };
      putRequest("/api/user/login", loginInfo).then(body => {
        if ("error" === body.data.status) {
          return next();
        } else {
          localStorage.setItem("loginTime", new Date().getTime().toString());
          sessionStorage.setItem("isLogin","true");
          sessionStorage.setItem("token",body.data.data)
        }
      })
    }
  }
  // 下一个路由
  next();
});


new Vue({
  el: '#app',
  router,
  render: h => h(App),
  components: {App},
  template: '<App/>'
});
