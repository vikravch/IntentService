package com.example.alex.intentservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ImageIntentService.TRANSACTION_DONE);
        registerReceiver(imageReceiver,intentFilter);



        Intent intent=new Intent(this,ImageIntentService.class);
        intent.putExtra("url","http://cityfinder.esy.es/img/1.jpg");
        startService(intent);

    }

    private BroadcastReceiver imageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String location = intent.getStringExtra("Location");
            if(location==null || location.length()==0){
                Toast.makeText(context,"Картинка не загрузилась",Toast.LENGTH_SHORT).show();
                return;
            }
            File imageFile = new File(location);
            if(!imageFile.exists()){
                Toast.makeText(context,"File not exist!!!",Toast.LENGTH_SHORT).show();
                return;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(location);
            ImageView imageView =(ImageView)findViewById(R.id.main_image);
            imageView.setImageBitmap(bitmap);
        }
    };
}
