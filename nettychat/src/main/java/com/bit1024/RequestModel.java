package com.bit1024;

/**
 * \* @Author: yesheng
 * \* Date: 2020/9/29 20:09
 * \* Description:
 * \
 */
public class RequestModel {
    int type;
    private Object content;
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}