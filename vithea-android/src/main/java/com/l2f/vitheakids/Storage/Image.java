package com.l2f.vitheakids.Storage;

import android.graphics.Bitmap;

/**
 * Created by silvi on 25/10/2017.
 */

public class Image {
    private long id;
    private Bitmap image;

    public Image(long id, Bitmap image){
        this.id= id;
        this.image=image;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
