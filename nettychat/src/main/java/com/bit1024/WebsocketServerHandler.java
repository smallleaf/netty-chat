package com.bit1024;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 * \* @Author: yesheng
 * \* Date: 2020/9/28 16:51
 * \* Description:
 * \
 */
public class WebsocketServerHandler extends ChannelInboundHandlerAdapter {

    private WebSocketServerHandshaker handshaker;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof FullHttpRequest){
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }else if(msg instanceof WebSocketFrame){
            handleWebsocketFrame(ctx, (WebSocketFrame) msg);
        }else if(msg instanceof CloseWebSocketFrame){
            System.out.println("================");
        }
        super.channelRead(ctx, msg);
    }

    private void handleHttpRequest(ChannelHandlerContext ctx,FullHttpRequest req){
        if(!req.decoderResult().isSuccess()){
            sendHttpResponse(ctx,req,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK));
            return;
        }
        if(!"websocket".equals(req.headers().get("Upgrade"))){
            sendHttpResponse(ctx,req,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket",null,false);
        handshaker = wsFactory.newHandshaker(req);
        if(handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else{
            handshaker.handshake(ctx.channel(),req);
        }
        //判断登录没有
        if(Room.getInstance().getUsername(ctx.channel()) == null){
            ctx.channel().writeAndFlush(new TextWebSocketFrame("login"));
        }
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse response){
        if(response.status() != HttpResponseStatus.OK){
            ByteBuf buf = Unpooled.copiedBuffer(response.status().toString().getBytes());
            response.content().writeBytes(buf);
            buf.release();
            HttpUtil.setContentLength(response,response.content().readableBytes());
        }
        ctx.channel().writeAndFlush(response);
    }

    private void handleWebsocketFrame(ChannelHandlerContext ctx,WebSocketFrame frame){
        if(frame instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),((CloseWebSocketFrame) frame).retain());
            return;
        }
        if(frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        String requestStr = ((TextWebSocketFrame)frame).text();
        RequestModel requestModel = JSON.parseObject(requestStr,RequestModel.class);
        if(requestModel.type == MessageType.LOGIN.code){
            String userName = String.valueOf(requestModel.getContent());
            Room.getInstance().addUser(userName,ctx.channel());
            ctx.channel().writeAndFlush(new TextWebSocketFrame("ok"));
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(1);
            chatMessage.setContent("欢迎"+userName+"进入房间");
            broadcastMessage(chatMessage);
            //推送在线人数
            broadcastMessageOnlineNum(Room.getInstance().channels().size());
            return;
        }


        String userName = Room.getInstance().getUsername(ctx.channel());
        if(userName == null){
            return;
        }
        if(requestModel.type == MessageType.HEARTBEAT.code){
            ctx.channel().writeAndFlush(new TextWebSocketFrame(String.valueOf(requestModel.getContent())));
            return;
        }
        if(requestModel.type == MessageType.TEXT.code){
            //转发所有的消息给在线的玩家

            List<Channel> channels = Room.getInstance().channels();
            for (Channel channel : channels) {
                ChatMessage chatMessage = new ChatMessage();
                if(!channel.equals(ctx.channel())){
                    String content = String.format("%s->%s",userName,String.valueOf(requestModel.getContent()));
                    chatMessage.setContent(content);
                    chatMessage.setType(2);
                }else{
                    String content = String.format("%s<-%s",String.valueOf(requestModel.getContent()),userName);
                    chatMessage.setContent(content);
                    chatMessage.setType(0);
                }
                channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMessage)));
            }
            return;
        }
    }

    public void broadcastMessageOnlineNum(int onlineNum){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setType(3);
        chatMessage.setContent(String.valueOf(onlineNum));
        List<Channel> channels = Room.getInstance().channels();
        for (Channel channel : channels) {
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(chatMessage)));
        }
    }
    public void broadcastMessage(Object msg){
        List<Channel> channels = Room.getInstance().channels();
        for (Channel channel : channels) {
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msg)));
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Room.getInstance().removeUser(ctx.channel());
        super.channelUnregistered(ctx);
    }
}