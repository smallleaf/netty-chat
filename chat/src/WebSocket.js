import Vue from 'vue'
import {EventBus} from './eventBus'
import {Message} from 'element-ui'
import router from './router'

var ws;
var intervalInt;
function sendHeartBeat(){
    var timeStamp =  (new Date()).getTime();
    var data = {
        "type":2,
        "content":timeStamp
    }
    ws.send(JSON.stringify(data))
}


if(this.$ws == undefined || this.$ws.readyState == WebSocket.CLOSED){
    ws = new WebSocket("ws://localhost:8088")
    ws.onopen = function(){
        Message.info('websocket connected');
        intervalInt = self.setInterval(sendHeartBeat,3000)
    }

    
    ws.onmessage = function(data){
        if(data.data != null){
            var msg = JSON.parse(data.data);
            if(msg.type == 1){
                EventBus.$emit("msg",{msg:msg.content})
            }else if(msg.type == 2){
            }else if(msg.type == 3){
                EventBus.$emit("loginSuccess")
            }else if(msg.type == 4){
                EventBus.$emit("syncNum",{msg:msg.content})
            }
        }
    }
    
    ws.onclose = function(){
        Message.error('websocket close');
        router.push({name:"Login"})
        if(intervalInt > 0){
            window.clearInterval(intervalInt)
        }
    }
    Vue.prototype.$ws = ws
}

