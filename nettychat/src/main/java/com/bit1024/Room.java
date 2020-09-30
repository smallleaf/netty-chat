package com.bit1024;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * \* @Author: yesheng
 * \* Date: 2020/9/29 19:48
 * \* Description:
 * \
 */
public class Room {

    private static final Room INSTANCE = new Room();

    public static Room getInstance(){
        return INSTANCE;
    }

    private Room(){}

    private Map<Channel,String> channelUser = new ConcurrentHashMap<>();

    public void addUser(String username,Channel channel){
        channelUser.put(channel,username);
    }

    public void removeUser(Channel channel){
        String userName = channelUser.get(channel);
        if(userName != null){
            channelUser.remove(channel);
        }
    }

    public String getUsername(Channel channel){
        return channelUser.get(channel);
    }

    public List<Channel> channels(){
        return new ArrayList<>(channelUser.keySet());
    }

}