package com.example.tumal.clarifai.model;

import android.arch.persistence.room.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.tumal.clarifai.ClarifiaiApplication;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;

@Entity
public class ClarifaiRequest {

    private static int THUMBNAIL_SIZE = 350;

    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo
    public Date created;
    @ColumnInfo
    public Date done;
    @ColumnInfo
    public String description;
    @ColumnInfo
    public int state;
    @ColumnInfo
    public String imageUri;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] imageThumbnail;
    @ColumnInfo
    public String modelName;

    public void setImageThumbnail(Bitmap imageThumbnailBitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageThumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.imageThumbnail = stream.toByteArray();
    }

    public Bitmap getImageThumbnailBitmap()
    {
        return BitmapFactory.decodeByteArray(imageThumbnail, 0, imageThumbnail.length);
    }

    public Bitmap getImage()
    {
        try {
       /* InputStream input = ClarifiaiApplication.getAppContext().getContentResolver().openInputStream(Uri.parse(this.imageUri));
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        */

        //Bitmap bitmap = BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        Bitmap bitmap =MediaStore.Images.Media.getBitmap(ClarifiaiApplication.getAppContext().getContentResolver(), Uri.parse(imageUri));
       // input.close();
        return bitmap;
        }
        catch (Exception e){
            Log.wtf(e.getMessage(),e);
            return null;
        }
    }

    public static Bitmap createThumbnail(Uri uri){
        try {
            InputStream input = ClarifiaiApplication.getAppContext().getContentResolver().openInputStream(uri);

            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither=true;//optional
            onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();

            if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
                return null;
            }

            int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

            double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
            bitmapOptions.inDither = true; //optional
            bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
            input = ClarifiaiApplication.getAppContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
            input.close();
            return bitmap;
        }
        catch (Exception e){
            Log.wtf(e.getMessage(),e);
            return null;
        }
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }
}

