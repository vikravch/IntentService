package com.example.alex.intentservice;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Alex on 26.11.2015.
 */
public class ImageIntentService extends IntentService {
    File cacheDir;
    public static final String TRANSACTION_DONE="com.example.alex.intentservice.TRANSACTION_DONE";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ImageIntentService() {
        super("ImageIntentService");

    }

    @Override
    public void onCreate(){
        super.onCreate();
        String tmpLocation= Environment.getExternalStorageDirectory().getPath()+"/img_tmp";
        Log.d("myLog","Location - "+tmpLocation);
        cacheDir=new File(tmpLocation);
        if(!cacheDir.exists()){
            cacheDir.mkdirs();
            Log.d("myLog","Directory :"+tmpLocation+" was created");
        }
        Log.d("myLog", "ServiceCreated");

    }
    @Override
    protected void onHandleIntent(Intent intent) {
       String remoteUrl=intent.getExtras().getString("url");
        String location;
        String fileName=remoteUrl.substring(remoteUrl.lastIndexOf("/")+1);
        File tmp = new File(cacheDir.getPath()+"/"+fileName);
        try {
            URL url = new URL(remoteUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(tmp);
            writeStream(inputStream,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            location=tmp.getAbsolutePath();
            notifyFinished(location);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void writeStream(InputStream inputStream, FileOutputStream fileOutputStream) {
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bufferedOutputStream);

        try {
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void notifyFinished(String location){
        Intent intent = new Intent(TRANSACTION_DONE);
        intent.putExtra("Location",location);
        ImageIntentService.this.sendBroadcast(intent);
    }

}
