package com.doubleclick.tiktok.Model.Responses;

import com.doubleclick.tiktok.Model.MediaObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Users {

    @SerializedName("ALL_POSTS")
    private List<MediaObject> All_Postes;

    public ArrayList<MediaObject> getAll_Postes() {
        return (ArrayList<MediaObject>) All_Postes;
    }

    public void setAll_Postes(List<MediaObject> all_Postes) {
        All_Postes = all_Postes;
    }



}
