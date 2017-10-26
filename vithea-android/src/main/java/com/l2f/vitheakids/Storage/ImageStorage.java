package com.l2f.vitheakids.Storage;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import com.l2f.vitheakids.model.Resource;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by silvi on 17/10/2017.
 */

public class ImageStorage extends Application {
    private ConcurrentMap<String, TreeMap<Long, Bitmap >> imgStorage = new ConcurrentHashMap<String, TreeMap<Long, Bitmap >>();

    public void addImage(String className, Long id, Bitmap img){

        TreeMap<Long, Bitmap > images = imgStorage.get(className);
        if(images==null){
            TreeMap<Long, Bitmap > auxImages = new TreeMap<Long,Bitmap>();
            Log.d("idBitmap", Long.toString(id));
            auxImages.put(id,img);
            imgStorage.put(className, auxImages);
        }
        else{
            images.put(id, img);
        }
    }

    public Bitmap getImage(String className, Long id){
            TreeMap<Long,Bitmap> images = imgStorage.get(className);
        return images.get(id);
    }


    //Fixme
    public boolean checkResouces(Resource resource){

            while(this.imgStorage.get(resource.getResourceId()).equals(null));

        return true; //every image needed is in the storage
    }



}
