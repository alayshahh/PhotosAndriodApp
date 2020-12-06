package com.example.photos11.model;
/*
@author Alay Shah
 */

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    static final long serialVersionUID = 1L;
    private static String storeFile = "users.dat";
    static User cur = new User();

    private ArrayList<Album> albums = new ArrayList<>();
    private transient Album result;

    private User(){ }

    public static User getInstance(){
        return cur;
    }
    public ArrayList<Album> getAlbums(){
        return albums;
    }

    public void removeAlbum(Album a){
        albums.remove(a);
    }
    public void addAlbum(Album a){
        albums.add(a);
        try {
            cur.writeApp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean contains (Album a){
        return albums.contains(a);
    }
    public void setFilePath(String s){
        storeFile = s;
    }
    public Album getAlbum(Album a){
        return albums.get(albums.indexOf(a));
    }
    public Album getAlbum(String title){
        Album a = new Album(title);
        return getAlbum(a);
    }

    public void searchTag(String s){
        result = new Album(" ");
        for(Album a: albums){
            for(Photo p: a.getPhotos()){
                if(p.hasTag(s)) {
                    result.addPhoto(p);
                }
            }

        }
    }
    public Album getResult(){
        return result;
    }


    public void writeApp() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeFile));
        oos.writeObject(cur);
        oos.close();
    }

    public void readApp()throws IOException, ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream( new FileInputStream(storeFile));
        cur = (User) ois.readObject();
        ois.close();
    }

}
