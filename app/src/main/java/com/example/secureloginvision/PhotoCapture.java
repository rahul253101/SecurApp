package com.example.secureloginvision;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PhotoCapture extends AppCompatActivity {
    private int REQUEST_CODE_PERMISSIONS = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView imageView;
    PreviewView mPreviewView;
    private Executor executor = Executors.newSingleThreadExecutor();
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_capture);
       // imageView = findViewById(R.id.image_view);
        mPreviewView = findViewById(R.id.preView);
        if (hasCameraPermission()) {
            Log.w("inside success ...", "1");
            enableCamera(mPreviewView);
        } else {
            Log.w("inside permission ...", "2");
            requestPermission();
        }
        // Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivity(camera_intent);
    }

    public void captureImage(View view) {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(camera_intent);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w("inside preview ...", "3");
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        // if (requestCode == pic_id) {
        // BitMap is data structure of image file which store the image in memory
        Bitmap photo = (Bitmap) data.getExtras().get("data");
        // Set the image in imageview for display
      //  imageView.setImageBitmap(photo);
       // imageView.setVisibility(View.VISIBLE);

        // }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkCamerhardware(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(PhotoCapture.this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_IMAGE_CAPTURE
        );
    }

    private void enableCamera(View view) {

        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {

                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    bindPreview(cameraProvider);
                    Log.w("Provider setup ...","6");
                } catch (ExecutionException | InterruptedException e) {
                    Log.w("Exce setup ...","6");

                    // No errors need to be handled for this Future.
                    // This should never be reached.
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Log.w("inside binidind ...", "4");
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
         cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        //preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());
        try {
            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview);
        }catch(Exception e){
            Log.e(TAG,"use case failed",e);
        }


        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();

        ImageCapture.Builder builder = new ImageCapture.Builder();

        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
     /*   HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);

        // Query if extension is available (optional).
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            // Enable the extension if available.
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }*/
        Log.w("inside image capture ...", "6");

        /*final ImageCapture imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
                .build();*/
        /*final  ImageCapture imageCapture =
                new ImageCapture.Builder()
                        .setTargetRotation(view.getDisplay().getRotation())
                        .build();*/
       /* OrientationEventListener orientationEventListener = new OrientationEventListener((Context) this) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation;

                // Monitors orientation values to determine the target rotation value
                if (orientation >= 45 && orientation < 135) {
                    rotation = Surface.ROTATION_270;
                } else if (orientation >= 135 && orientation < 225) {
                    rotation = Surface.ROTATION_180;
                } else if (orientation >= 225 && orientation < 315) {
                    rotation = Surface.ROTATION_90;
                } else {
                    rotation = Surface.ROTATION_0;
                }

                imageCapture.setTargetRotation(rotation);
            }
        };
        orientationEventListener.enable();
       // imageCapture.setFlashMode(ImageCapture.FLASH_MODE_AUTO);
       // ImageReader imageRe;
        // ImageReader imageReader = imageReader.getSurface();
       ///
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageAnalysis, imageCapture);
        Log.w("inside camera ...", "7");
       // camera.getCameraControl().setLinearZoom().get();
        //ViewPort viewPort = ((PreviewView)findViewById(R.id.preView)).getViewPort();

        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
        File file = new File(getBatchDirectoryName(), mDateFormat.format(new Date())+ ".jpg");

        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback () {

            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Log.w("onImageSaved setup ...","6");

                        Toast.makeText(PhotoCapture.this, "Image Saved successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(@NonNull ImageCaptureException error) {
                error.printStackTrace();
            }
        });
        /*preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();
        FaceDetectorOptions realTimeOpts =
                new FaceDetectorOptions.Builder()
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                        .build();*/


    }
    public String getBatchDirectoryName() {

        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
        Log.w("File Path : ",app_folder_path);
        File dir = new File(app_folder_path);
        if (!dir.exists() && !dir.mkdirs()) {

        }

        return app_folder_path;
    }

}




