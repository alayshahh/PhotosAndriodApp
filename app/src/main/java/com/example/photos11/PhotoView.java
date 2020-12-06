package com.example.photos11;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.photos11.model.Album;
import com.example.photos11.model.Photo;
import com.example.photos11.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PhotoView extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;
    Album album;
    GridView gridView;
    int selected = -1;
    Button add;
    Button delete;
    //ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        add = findViewById(R.id.add_photo);
        delete = findViewById(R.id.delete_photo);
        String alb = bundle.getString("Album");
        Log.i("Cur ALB", bundle.getString("Album"));
        if(alb.trim().isEmpty()){
            add.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
            album = User.getInstance().getResult();
        }else{
            album = User.getInstance().getAlbum(new Album(alb));
        }
        gridView = findViewById(R.id.gridview);
        ArrayList<Photo> photos = album.getPhotos();

        gridView.setAdapter( new PhotoAdapter(this, R.layout.grid_layout, album.getPhotos() ));
        gridView.setOnItemClickListener((parent, view,pos, id)->{
            selected = pos;
        });
    }

    public void addPhoto(View v){
        Intent intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
        intentGallery.setType("image/*");
        intentGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentGallery.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intentGallery, REQUEST_CODE);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Uri selImg = data.getData();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                this.getContentResolver().takePersistableUriPermission(selImg, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
            Log.i("SEL URI", selImg.toString());
            Photo p = new Photo(selImg);
            Log.i("COMP URI", String.valueOf(selImg.compareTo(p.getPath())));
            if(album.contains(p)){
                Toast.makeText(getApplicationContext(),"Duplicate photo",Toast.LENGTH_SHORT).show();
                return;
            }
            album.addPhoto(p);
            gridView.invalidateViews();

            try {
                User.getInstance().writeApp();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(getApplicationContext(),"Successfully added photo",Toast.LENGTH_SHORT).show();
            //imageView.setImageURI(selImg);

        }

    }

    public void deletePhoto(View view){
        if(selected==-1||selected>=User.getInstance().getAlbums().size()){
            return;
        }
        showDeleteItemDialog();
    }
    private void showDeleteItemDialog(){
        Photo p = album.getPhotos().remove(selected);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete photo")
                .setMessage("Are you sure you want to delete this photo?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        album.removePhoto(p);
                        try {
                            User.getInstance().writeApp();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gridView.invalidateViews();
                    }

                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();

    }


}