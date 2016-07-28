package com.glowingsoft.dilchaspqissay.model;

/**
 * Created by mg on 6/18/2016.
 */
public class CustomModel {
    public int getTitle_id() {
        return title_id;
    }

    public void setTitle_id(int title_id) {
        this.title_id = title_id;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitleimage() {
        return titleimage;
    }

    public void setTitleimage(String titleimage) {
        this.titleimage = titleimage;
    }

    public String getContentimage() {
        return contentimage;
    }

    public void setContentimage(String contentimage) {
        this.contentimage = contentimage;
    }

    int title_id;
    int content_id;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    int category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
    String title,content,titleimage,contentimage;
}
