package com.example.appnew.enity;

public class BongDa {
    String title ;
    String thumbnail;
    String embed;
    String date;

    public BongDa(String title, String thumbnail, String embed, String date) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.embed = embed;
        this.date = date;
    }

    public String getEmbed() {
        return embed;
    }

    public void setEmbed(String embed) {
        this.embed = embed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
