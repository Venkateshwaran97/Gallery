package com.example.venkateshwaran.gallery;

public class image implements java.io.Serializable{

    public String title;
    private String artist;
    public image(String songTitle,String songArtist) {

        title=songTitle;
        artist=songArtist;
    }

    public String getTitle(){return title;}
    public String getdate(){return artist;}
}

