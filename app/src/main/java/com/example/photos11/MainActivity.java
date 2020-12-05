package com.example.photos11;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.health.SystemHealthManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.photos11.model.Album;
import com.example.photos11.model.User;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Context context = this;
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
    }
    public void addAlbum(View view){
        showAddItemDialog();
        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album_in_list, User.getInstance().getAlbums()));
    }

    private void showAddItemDialog() {
        Context c = this;
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a new album")
                .setMessage("What is the album title")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
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
                        String task = String.valueOf(taskEditText.getText());
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
        listView.setAdapter(new ArrayAdapter<Album>(this, R.layout.album_in_list, User.getInstance().getAlbums()));

    }



    public void deleteAlbum(View view){

    }
    public void showAlbum (View view){

    }




}