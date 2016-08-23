package com.example.kaushalmandayam.eventmanager;

/**
 * Created by Kaushal.Mandayam on 8/22/2016.
 */
public class Friend {

    private String name;
    private String profilePicUrl;


    public Friend(String name, String profilePicUrl) {
        this.name = name;
        this.profilePicUrl = profilePicUrl;
    }

    public String getName() {
        return name;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }
}
