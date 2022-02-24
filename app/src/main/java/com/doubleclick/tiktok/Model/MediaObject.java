package com.doubleclick.tiktok.Model;

public class MediaObject {

    public MediaObject(String media_url) {
        this.media_url = media_url;
    }

    public MediaObject() {
    }


    public MediaObject(String title, String description, String data, String user_id, String post_categories, String post_id, String media_url, String thumbnail) {
        this.title = title;
        this.description = description;
        this.date = data;
        this.user_id = user_id;
        this.post_categories = post_categories;
        this.post_id = post_id;
//        this.user_name = user_name;
        this.media_url = media_url;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getData() {
        return date;
    }

    public void setData(String data) {
        this.date = data;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPost_categories() {
        return post_categories;
    }

    public void setPost_categories(String post_categories) {
        this.post_categories = post_categories;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

//    public String getView() {
//        return view;
//    }

//    public void setView(String view) {
//        this.view = view;
//    }

//    public String getUser_name() {
//        return user_name;
//    }

//    public void setUser_name(String user_name) {
//        this.user_name = user_name;
//    }

    public String getMedia_Url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }



    private String date;
    private String description;
    private String media_url;
    private String post_categories;
    private String post_id;
    private String thumbnail;
    private String title;
    private String user_id;
//    private String user_name;
//    private String view;

}
