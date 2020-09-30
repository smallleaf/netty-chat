package com.bit1024;

public enum MessageType {
    /**
     * 文本消息
     */
    TEXT(1),
    /**
     * 心跳消息
     */
    HEARTBEAT(2),

    /**
     * 登录
     */
    LOGIN(3);

    int code;
    MessageType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
