package com.example.a01363207.pucare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

// Loads an image from URL
public class GetImageFromURL extends AsyncTask<String, Void, Bitmap> {
    ImageView icon;

    public GetImageFromURL(ImageView image) {
        this.icon = image;
    }

    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap bmp = null;
        try {
            InputStream is = new java.net.URL(urlDisplay).openStream();
            bmp = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bmp;
    }

    protected void onPostExecute(Bitmap result) {
        icon.setImageBitmap(result);
    }
}
