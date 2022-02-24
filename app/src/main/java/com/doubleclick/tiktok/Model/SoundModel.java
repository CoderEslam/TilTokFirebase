package com.doubleclick.tiktok.Model;

public class SoundModel {


    public SoundModel(String sound_image, String sound_file, String sound_title) {
        this.sound_image = sound_image;
        this.sound_file = sound_file;
        this.sound_title = sound_title;
    }

    public SoundModel() {
    }

    public String getSound_image() {
        return sound_image;
    }

    public void setSound_image(String sound_image) {
        this.sound_image = sound_image;
    }

    public String getSound_file() {
        return sound_file;
    }

    public void setSound_file(String sound_file) {
        this.sound_file = sound_file;
    }

    public String getSound_title() {
        return sound_title;
    }

    public void setSound_title(String sound_title) {
        this.sound_title = sound_title;
    }

    private String sound_image;
    private String sound_file;
    private String sound_title;

    public SoundModel(String sound_image, String sound_file, String sound_title, String sound_id) {
        this.sound_image = sound_image;
        this.sound_file = sound_file;
        this.sound_title = sound_title;
        this.sound_id = sound_id;
    }

    private String sound_id;

}
