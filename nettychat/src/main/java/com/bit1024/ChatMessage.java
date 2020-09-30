package com.bit1024;

/**
 * \* @Author: yesheng
 * \* Date: 2020/9/30 10:10
 * \* Description:
 * \
 */
public class ChatMessage {

    /**
     * 1-tip 0-表示消息内容  2表示其他用户数据
     */
    int type;

    String content;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}