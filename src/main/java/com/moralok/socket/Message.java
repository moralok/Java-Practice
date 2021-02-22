package com.moralok.socket;

import java.io.Serializable;

/**
 * @author moralok
 * @since 2021/2/22 3:30 下午
 */
public class Message implements Serializable {

    private String content;

    public Message(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                '}';
    }
}
