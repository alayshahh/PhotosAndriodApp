package com.example.photos11.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    static final long serialVersionUID = 1L;

    /**
     * Album Name
     */
    String name;

    /**
     * Photos the Album Contains
     */
    ArrayList<Photo> photos= new ArrayList<>();
    public Album(String name) {
        this.name = name;
    }
    public Album(String name, ArrayList<Photo>photos) {
        this.name = name;
        this.photos = photos;
    }
    public ArrayList<Photo> getPhotos(){
        return photos;
    }
    public void addPhoto(Photo me) {
        photos.add(me);
    }

    public void rename(String name) {
        this.name = name;
    }

    public void removePhoto(Photo me) {
        photos.remove(me);
    }
    public boolean contains(Photo me) {
        return photos.contains(me);
    }
    public Photo getPrev(Photo p) {
        int i = photos.indexOf(p);
        if(i==0) {
            i=photos.size()-1;
        }else i--;
        return photos.get(i);

    }
    public Photo getNext(Photo p) {
        int i = photos.indexOf(p);
        if(i==photos.size()-1) {
            i=0;
        }else i++;
        return photos.get(i);

    }
    public boolean equals(Object o) {
        if(!(o instanceof Album)||o==null){
            return false;
        }else {
            Album a = (Album) o;
            return this.name.equals(a.name);
        }
    }
    public String getName() {
        return name;
    }

    public String toString(){
        return getName();
    }

    public Photo getPhoto(Photo p){
        return photos.get(photos.indexOf(p));
    }

    public Photo getPhoto(String path){
        Photo p = new Photo(path);
        return getPhoto(p);
    }

}
