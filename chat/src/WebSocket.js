import Vue from 'vue'
import {EventBus} from './eventBus'
import {Message} from 'element-ui'
import router from './router'

if(this.$ws == undefined || this.$ws.readyState == WebSocket.CLOSED){
    var ws = new WebSocket("ws://localhost:8088")

    ws.onopen = function(){
        Message.info('websocket connected');
    }
    
    ws.onmessage = function(data){
        if(data.data == "ok"){
            EventBus.$emit("login",{})
        }else if(data.data =='login'){
            Message.warning('un login');
            router.push({name:"Login"})
        }else{
            EventBus.$emit("msg",{msg:data.data})
        }
    }
    
    ws.onclose = function(){
        Message.error('websocket close');
        router.push({name:"Login"})
    }
    Vue.prototype.$ws = ws
}

