package com.example.abhinavchinta.gdg_app;

/**
 * Created by Abhinav Chinta on 12/17/2017.
 */

public class ListItem {
    private String title, id, body;

    public ListItem(String title, String id, String body) {
        this.title = title;
        this.id = id;
        this.body=body;
    }
    public ListItem(){

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
