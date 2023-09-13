package com.example.secureloginvision;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, secureLogin;
    TextView tvAuthStatus;
    private BiometricPrompt biometricprompt;
    private BiometricPrompt.PromptInfo promptinfo;
    private Executor executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogin = findViewById(R.id.btnLogin);
        tvAuthStatus = findViewById(R.id.tvAuthStatus);
        executor = ContextCompat.getMainExecutor(this);
        secureLogin = findViewById(R.id.SecureLogin);
        biometricprompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                tvAuthStatus.setText(errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                tvAuthStatus.setText("Successful");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                tvAuthStatus.setText("Authentication Failed");
            }
        });

        promptinfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using Fingerprint or face")
                .setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)
               // .setNegativeButtonText("Use account credential")
               //.setAllowedAuthenticators(BIOMETRIC_WEAK | DEVICE_CREDENTIAL)
                      //  BiometricManager.Authenticators.BIOMETRIC_WEAK)
                .build();

        secureLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (biometricsAvilable()) {
                    tvAuthStatus.setText(" ");
                    biometricprompt.authenticate(promptinfo);
                    startActivity(new Intent(MainActivity.this, PhotoCapture.class));
                    finish();
                    //CaptureImage

               // }
                // biometricsAvilable();


                // startActivity(new Intent(MainActivity.this,Login.class));
                // finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAuthStatus.setText(" ");
                // biometricprompt.authenticate(promptinfo);


                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }

        });


    }

    private Boolean biometricsAvilable() {
        BiometricManager biometricManager = BiometricManager.from(this);
        int isPresent = 1;
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                isPresent =0;
            break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");

            break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
            break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                // startActivityForResult(enrollIntent, REQUEST_CODE);
            break;
        }
        if(isPresent==0)
        return true;
        else
            return false;
    }
}
