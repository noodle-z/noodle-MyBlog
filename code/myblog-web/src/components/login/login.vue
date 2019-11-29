<template>
  <div id="backgroudImage">
    <div class="subject">
      <div class="title">
        <span>不吃£泡面的个人博客</span>
        <div class="description">自入冬来多是暖，无寻花处却闻香</div>
      </div>
      <transition name="el-zoom-in-center">
        <div v-show="show" class="transition-box form-login">

          <el-row :gutter="1">
            <el-col :span="8">
              <div class="grid-content bg-purple"></div>
            </el-col>
            <el-col :span="8">
              <div class="grid-content bg-purple">
                <div class="form-title"><span>欢迎登陆</span></div>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="grid-content bg-purple bg-more">
                <el-dropdown size="small" @command="handleCommand">
            <span class="more el-dropdown-link"><el-link type="primary" :underline="false">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-link>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="toHome">返回主页</el-dropdown-item>
              <el-dropdown-item>注册账号</el-dropdown-item>
              <el-dropdown-item>忘记密码</el-dropdown-item>
            </el-dropdown-menu>
            </span>
                </el-dropdown>
              </div>
            </el-col>
          </el-row>
          <div style="color: #f56c6c; font-size: 12px Extra Small;line-height: 35px" v-if="errorMark">{{errorMark}}
          </div>
          <el-form ref="form" :model="form" :rules="rules" label-width="80px" :hide-required-asterisk="hiddenRequire">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="form.username" placeholder="用户名/电话/邮箱"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="form.password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-row type="flex" class="row-bg" justify="space-between">
              <el-col :span="6">
                <div class="grid-content bg-purple-left">
                  <el-checkbox v-model="form.remember">记住登陆</el-checkbox>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-light"></div>
              </el-col>
              <el-col :span="6">
                <div class="grid-content bg-purple-right">
                  <el-button type="primary" @click="onSubmit('form')" style="width: 80px">登陆</el-button>
                </div>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </transition>
    </div>
  </div>
</template>

<script>
  import {putRequest} from "../../axios/index";

  export default {
    name: "login.vue",
    data() {
      return {
        form: {
          username: "",
          password: "",
          remember: false
        },
        rules: {
          username: [
            {required: true, message: '请输入用户名/电话/邮箱', trigger: 'blur'},
          ],
          password: [
            {required: true, message: '请输入密码', trigger: 'blur'},
          ]
        },
        show: false,
        hiddenRequire: true,
        errorMark: ""
      }
    },
    methods: {
      onSubmit(formName) {
        this.errorMark = "";
        this.$refs[formName].validate((valid) => {
          if (valid) {
            putRequest("/api/user/login", this.form).then(body => {
              if ("error" === body.data.status) {
                this.errorMark = body.data.description;
                return;
              }
              if (this.form.remember) {
                localStorage.setItem("loginTime", new Date().getTime());
                localStorage.setItem("loginInfo", this.form.username + "pmzzz" + this.form.password);
              }
              sessionStorage.setItem("token", body.data.data);
              this.$router.push("/home")
            });
          } else {
            return false;
          }
        });
      },
      handleCommand(order) {
        if ("toHome" === order)
          this.$router.push("/home")
      }
    },
    mounted: function () {
      this.show = true;
    }
  }
</script>

<style scoped>
  #backgroudImage {
    background-color: #F4F4F4;
    width: 100%;
    height: 100%;
    z-index: -10;
  }

  .form-login {
    background-color: aliceblue;
    width: 21%;
    min-width: 340px;
    margin: 0 auto;
    border: 1px solid #DCDFE6;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 0 5px #DCDFE6;
    display: inline-block;
  }

  .bg-purple-left {
    line-height: 100%;
    text-align: left;
  }

  .bg-purple-right {
    text-align: right;
  }

  .form-title {
    text-align: center;
    font: 20px Extra large;
    height: 40px;
  }

  .title {
    text-align: center;
    font: 30px Extra large;
    margin-bottom: 5%;
  }

  .title span {
    display: inline-block;
    margin-bottom: 2%;
  }

  .description {
    font: 12px Extra Small;
  }

  .grid-content {
    min-height: 36px;
  }

  .more {
    line-height: 26px;
  }

  .bg-more {
    text-align: right;
  }
  .subject{
    padding-top: 6%;
  }

</style>
