<template>
  <el-container style="padding:0">
    <el-header class="head">
        <el-row style="margin-top:20px">
            欢迎来到Netty群聊(<font id="num" style="color:blue;white-space:nowrap;">{{ person }}</font>)
        </el-row>
    </el-header>
    <el-main id="content" v-bind:style="{height:centerHeight + 'px'}" style="padding:0px">
            <div v-for="data in datas" class="infinite-list-item">
                <div v-if="data.type == 1">
                    <el-row>
                <el-col>
                    <p class="tip">{{data.content}}</p>
                </el-col>
            </el-row>
                </div>
                <div v-else-if="data.type == 0">
                   <el-row type="flex" justify="end">
                <p class="rightTip">{{data.content}}</p>
            </el-row>
                </div>
                <div v-else-if="data.type == 2">
                  <el-row type="flex" justify="start">
                <p class="leftTip">{{data.content}}</p>
            </el-row>
                </div>
            </div>
         
    </el-main>
    <el-footer class="footer">
      <el-row>
        <el-input type="textarea" :rows="3" class="input" placeholder="请输入内容" v-model="textarea" ref="inputContent"></el-input>
      </el-row>
      <el-row type="flex" justify="end">
        <el-button v-on:click="sendMessage">发送</el-button>
      </el-row>
    </el-footer>
  </el-container>
</template>

<script>
import {EventBus} from '../eventBus'
import Vue from 'vue'

var datas = []
EventBus.$on("msg",(msg)=>{
  var data = JSON.parse(msg.msg);
    datas.push(data)
})

EventBus.$on("syncNum",(msg)=>{
  var data = JSON.parse(msg.msg);
   document.getElementById("num").innerHTML  = data.content
})
var height = document.documentElement.clientHeight 
var center = height - 180;

export default {
  data() {
    return {
      textarea:"",
      person:0,
      centerHeight: center,
      datas:datas
    };
  },
  methods: {
    sendMessage: function () {
      var self = this;
      var chatMessage = {
          "type":1,
          "content":this.$refs.inputContent.value
      }
      this.$ws.send(JSON.stringify(chatMessage));
    }
  }
};
</script>
<style>
.login {
  margin-top: 200px;
}
.login-top {
  margin-top: 20px;
}
.input .el-textarea__inner {
  resize: none;
  border-bottom: 0;
  border-left: 0;
  border-right: 0;
}
.sendBtn {
  margin-left: 0px;
}
.footer{
    padding:0;
}
.head{
    background:lightgrey;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;
    font-size: 20px;
}
.tip{
    font-size: 12px;
    color:lightslategrey;
}
.rightTip{
    background: tan;
    padding: 12px;
    border-radius: 15px;
    max-width: 60%;
    margin-right: 8px;
}
.leftTip{
    background:rgb(180, 143, 74);
    padding: 12px;
    border-radius: 15px;
    max-width: 60%;
    margin-left: 8px;
}
</style>