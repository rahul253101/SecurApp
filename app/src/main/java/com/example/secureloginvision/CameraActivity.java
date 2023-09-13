package com.example.secureloginvision;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;

import com.google.common.util.concurrent.ListenableFuture;

    public class CameraActivity extends AppCompatActivity {

        private PreviewView previewView;
        private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
        private TextView textView;

        private ImageView imageId ;
        private static final int pic_id = 123;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_captureimage);
            Log.w("inside onCreate activity ...", "1");

            //  previewView = findViewById(R.id.previewView);
            imageId = findViewById(R.id.imageView);
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(camera_intent);
          // startActivityForResult(camera_intent, 1);
           // ImageCapture imageCapture = new ImageCapture.Builder().setTargetRotation(
                  //  .getDisplay().getRotation()).build();



        }

          //  cameraProviderFuture = ProcessCameraProvider.getInstance(this);
          // textView = findViewById(R.id.orientation);
    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w("inside camera activity ...", "3");
            super.onActivityResult(requestCode, resultCode, data);
            // Match the request 'pic id with requestCode
           // if (requestCode == pic_id) {
                // BitMap is data structure of image file which store the image in memory
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // Set the image in imageview for display
        Log.w("inside bitmap activity ...", "4");

        imageId.setImageBitmap(photo);
           // }
        }

    }
