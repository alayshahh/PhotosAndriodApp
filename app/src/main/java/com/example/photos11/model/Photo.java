package com.example.photos11.model;

import android.media.Image;
import android.widget.ImageView;

import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class Photo implements Serializable {

    static final long serialVersionUID = 1L;

    private String path;
    private ArrayList<String> people;
    private String location;

    public Photo(String path){
        this.path = path;
        people = new ArrayList<String>();
        location = "";
    }

    public boolean hasTag(String s){
        if(location.contains(s)){
            return true;
        }
        for(String p : people){
            if(p.contains(s)){
                return true;
            }
        }
        return true;
    }
    public void setLocation(String s){
        location = s;
    }
    public void addPerson(String s){
        people.add(s);
    }
    public void containsPerson(String s){
        people.contains(s);
    }
    public void deletePerson(String s){
        people.remove(s);
    }

    public boolean equals(Object o){
        if(o == null || !(o instanceof Photo)){
            return false;
        }
        Photo p = (Photo) o;
        return p.path.equals(path);
    }


}
