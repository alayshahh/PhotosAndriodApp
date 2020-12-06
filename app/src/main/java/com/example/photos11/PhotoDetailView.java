package com.example.photos11;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photos11.model.Album;
import com.example.photos11.model.Photo;
import com.example.photos11.model.User;

import java.io.IOException;

public class PhotoDetailView extends AppCompatActivity {
    int selected =  -1;
    int numSelected;
    Photo photo;
    ImageView imageView;
    ListView listView;
    TextView textView;
    Album album;
    Button add;
    Button delete;
    Button edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail_view);
        Bundle bundle = getIntent().getExtras();
        String alb = bundle.getString("Album");
        Uri path = Uri.parse(bundle.getString("Photo"));
        numSelected = bundle.getInt("Selected");
        add = findViewById(R.id.add_tag);
        delete = findViewById(R.id.del_tag);
        edit = findViewById(R.id.edit_loc);
        if(alb.trim().isEmpty()){
            add.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            album = User.getInstance().getResult();
        }else album = User.getInstance().getAlbum(new Album(alb));
        photo = album.getPhoto(path);
        imageView = findViewById(R.id.cur_img);
        textView = findViewById(R.id.loc_tag);
        listView = findViewById(R.id.people_tags);
        imageView.setImageURI(path);
        textView.setText(photo.getLocation());
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.album_in_list, photo.getPeople()));
        listView.setOnItemClickListener((parent, view, pos, id)-> selected = pos);

    }

    public void nextImg(View view){
        numSelected++;
        if(numSelected == album.getPhotos().size()){
            numSelected = 0;
        }
        photo = album.getPhotos().get(numSelected);
        resetView();
    }

    public void prevImg(View view){
        numSelected--;
        if(numSelected == -1){
            numSelected = album.getPhotos().size() - 1;
        }
        photo = album.getPhotos().get(numSelected);
        resetView();
    }

    private void resetView(){
        selected = -1;
        imageView.setImageURI(photo.getPath());
        textView.setText(photo.getLocation());
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.album_in_list, photo.getPeople()));
    }

    public void editLocation(View view){
        Context c = this;
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Set Location")
                .setMessage("What is the location?")
                .setView(taskEditText)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        photo.setLocation(task);
                        textView.setText(photo.getLocation());
                        try {
                            User.getInstance().writeApp();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(),"Successfully Set",Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void addPerson(View view){
        showAddItemDialog();
    }

    private void showAddItemDialog() {
        Context c = this;
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a new person")
                .setMessage("Who is the person?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        task=task.trim();
                        if(task.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Invalid name",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(photo.getPeople().contains(task)){
                            Toast.makeText(getApplicationContext(),"Duplicate name",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        photo.addPerson(task);
                        try {
                            User.getInstance().writeApp();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        listView.invalidateViews();
                        Toast.makeText(getApplicationContext(),"Successfully Added",Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }





    public void deletePerson(View view){
        if(selected==-1||selected>=photo.getPeople().size()){
            return;
        }
        showDeleteItemDialog();
    }

    private void showDeleteItemDialog(){
        String person = photo.getPeople().get(selected);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Person")
                .setMessage("Are you sure you want to delete this person?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        photo.getPeople().remove(person);
                        try {
                            User.getInstance().writeApp();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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



}