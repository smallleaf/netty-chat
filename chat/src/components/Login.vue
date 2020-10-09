<template>
 <div class="login">
   <h1>请登录netty聊天室</h1>
   <el-input v-model='username' placeholder="请求输入昵称" ref="username"></el-input>
   <el-row class="login-top">
        <el-button type="primary" style="width:70%;" v-on:click="loginNetty">登录</el-button>
   </el-row>

 </div>
</template>

<script>
import {EventBus} from '../eventBus'
export default {
  data () {
    return {
      username: ''
    }
  },
  methods:{
    loginNetty: function(){
        var name = this.$refs.username.value
        var self = this;
        var data = {
          "type":3,
          "content":name
        }
        this.$ws.send(JSON.stringify(data))
        EventBus.$on("loginSuccess",()=>{
          console.log("登录成功")
          self.$router.push({name:"chat"})
        })
    }
  }
}
</script>

<style>
.login{
  margin-top: 200px;
}
.login-top{
  margin-top: 20px;
}
</style>