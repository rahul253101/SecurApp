package com.example.secureloginvision;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;



public class Login extends AppCompatActivity {
    EditText biometeric,CustomerId,passCode;
    Button btnOkay;
    TextView tvAuthStatus;
    int count = 0;

    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    PreviewView viewFinder ;
    private Camera mCamera;
    private Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnOkay=findViewById(R.id.btnOkay);
        tvAuthStatus=findViewById(R.id.tvAuthStatus);
        CustomerId = findViewById(R.id.CustomerId);
        passCode = findViewById(R.id.passCode);

        btnOkay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //   biometricprompt.authenticate(promptinfo);
                String custId = CustomerId.getText().toString().trim();
                String password = passCode.getText().toString().trim();
                tvAuthStatus.setText(" ");
                if(TextUtils.isEmpty(custId)){
                   // CustomerId.setError("Customer Id is Required.");
                    tvAuthStatus.setText(" Customer Id is Required..... ");
                    return;
                }

               else if(custId.equalsIgnoreCase("123456")
                        || custId.equalsIgnoreCase("345678")){

                }else{
                    //CustomerId.setError("Customer Id is Invalid.");
                    tvAuthStatus.setText(" Customer Id is Invalid..... ");
                    return;
                }

                if(!TextUtils.isEmpty(custId) && TextUtils.isEmpty(password)){

                    tvAuthStatus.setText(" Password is Required..... ");
                    return;
                }

               else if(passCode.getText().equals("345678")){
                    count =0;
                    tvAuthStatus.setText("");
                }else{
                    //CustomerId.setError("Password is Invalid.");

                    count = count +1;
                    if (count >= 3){
                        tvAuthStatus.setText(" Exceeds Password Invalid attempts ..... Capturing Photo ");
                      //  biometeric.setText(" Capturing Photo.... ");
                        if (hasCameraPermission()) {
                            enableCamera();
                        } else {
                            requestPermission();
                        }
                       //startActivity(new Intent(Login.this,CaptureImage.class));
                        return;

                    }else{
                        tvAuthStatus.setText(" Incorrect Password attempts..... "+count);
                    }
                    return;
                }

                if(count==0) {
                    tvAuthStatus.setText(" ");

                    startActivity(new Intent(Login.this, MainActivity.class));
                }
            }
        });
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    private void enableCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

}
