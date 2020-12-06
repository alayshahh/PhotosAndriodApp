package com.example.photos11;
/*
@author Alay Shah
 */
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.health.SystemHealthManager;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.photos11.model.Album;
import com.example.photos11.model.Photo;
import com.example.photos11.model.User;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Context context = this;
    EditText query;

    int selected = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File path = context.getFilesDir();
        File f = new File(path + "/users.dat");
        User.getInstance().setFilePath(f.getAbsolutePath());
        try {
            f.createNewFile();
        } catch (IOException e) {
            Log.i("FILEF", "Already made users.dat");
        }
        try {
            User.getInstance().readApp();
        } catch (EOFException e) {
            Log.i("FILEF", "NO ALBUMS");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        listView = findViewById(R.id.album_list);
        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album_in_list, User.getInstance().getAlbums()));
        listView.setOnItemClickListener((parent, view, position, id) -> selected = position);
        query = findViewById(R.id.search_tag);
    }
    public void addAlbum(View view){
        showAddItemDialog();
        //listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album_in_list, User.getInstance().getAlbums()));
    }

    private void showAddItemDialog() {
        Context c = this;
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a new album")
                .setMessage("What is the album title?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        task=task.trim();
                        if(task.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Invalid album name",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Album a = new Album(task);
                        if(User.getInstance().contains(a)){
                            Toast.makeText(getApplicationContext(),"Duplicate album name",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        User.getInstance().addAlbum(a);
                        listView.invalidateViews();
                        Toast.makeText(getApplicationContext(),"Successfully Created",Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void showRenameItemDialog() {
        Context c = this;
        Log.i("Position", String.valueOf(selected));
        Album album = User.getInstance().getAlbums().get(selected);
        String name = album.toString();
        Log.i("Album", String.valueOf(name));
        final EditText taskEditText = new EditText(c);
        taskEditText.setText(name);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Rename album")
                .setMessage("What is the album title")
                .setView(taskEditText)
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText()).trim();
                        if(task.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Invalid album name",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(album.getName().equals(task)){
                            return;
                        }
                        Album a = new Album(task);
                        if(User.getInstance().contains(a)){
                            Toast.makeText(getApplicationContext(),"Duplicate album name",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        album.rename(task);
                        try {
                            User.getInstance().writeApp();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listView.invalidateViews();
                        Toast.makeText(getApplicationContext(),"Successfully Renamed",Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void renameAlbum(View view){
        Log.i("Selected Pos", String.valueOf(selected));
        if(selected==-1||selected>=User.getInstance().getAlbums().size()){
            return;
        }

        Album album = User.getInstance().getAlbums().get(selected);
        String name = album.toString();
        Log.i("Album", String.valueOf(name));
        showRenameItemDialog();

    }


    public void deleteAlbum(View view){
        if(selected==-1||selected>=User.getInstance().getAlbums().size()){
            return;
        }
        showDeleteItemDialog();
        Log.i("DeleteDone", String.valueOf(selected));
        for(Album a: User.getInstance().getAlbums()){
            Log.i("CurrentAlbums", a.toString());
        }

    }
    private void showDeleteItemDialog(){
        Album album = User.getInstance().getAlbums().get(selected);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Delete album")
                .setMessage("Are you sure you want to delete this album?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        User.getInstance().removeAlbum(album);
                        try {
                            User.getInstance().writeApp();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listView.invalidateViews();
                    }

                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }

    public void showAlbum (View view){
        if(selected==-1||selected>=User.getInstance().getAlbums().size()){
            return;
        }
        Log.i("METHOD", "showALBUM");
        Bundle bundle = new Bundle();
        String open = User.getInstance().getAlbums().get(selected).toString();
        bundle.putString("Album",open);
        Intent intent = new Intent(this, PhotoView.class);
        intent.putExtras(bundle);
        startActivity(intent);


    }

    public void searchTag(View view){
        String s = query.getText().toString();
        if(s.trim().isEmpty()){
            Toast.makeText(getApplicationContext(),"Invalid Tag",Toast.LENGTH_SHORT).show();
            return;
        }
        User.getInstance().searchTag(s);
        Album res = User.getInstance().getResult();
        if(res==null||res.getPhotos().size()==0){
            Toast.makeText(getApplicationContext(),"No Results",Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString("Album", " ");
        Intent intent = new Intent(this, PhotoView.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}