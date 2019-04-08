package com.theeralabs.app.contentprovider.model;

/**
 * Created by Kuldeep on 16-Nov-17.
 */

public class Messages {
    private String from, body;

    public Messages(String from, String body) {
        this.from = from;
        this.body = body;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
