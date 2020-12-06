package com.example.photos11;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.photos11.model.Photo;

import java.util.ArrayList;

public class PhotoAdapter extends ArrayAdapter<Photo> {
    ArrayList<Photo> photos;
    Context context;
    int resource;



    public PhotoAdapter( Context context, int resource,  ArrayList<Photo> photos) {
        super(context, resource, photos);
        this.context=context;
        this.resource=resource;
        this.photos=photos;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent ){
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(20, 20, 20, 20);
        } else {
            imageView = (ImageView) convertView;
        }
        Photo p = photos.get(position);
        imageView.setImageBitmap(p.getBitMap(context));
        return imageView;
    }
}



