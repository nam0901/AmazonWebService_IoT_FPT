package com.example.namho.awssignin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.mobile.auth.ui.AuthUIConfiguration;
import com.amazonaws.mobile.auth.ui.SignInUI;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AWSMobileClient.getInstance().initialize(MainActivity.this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Toast.makeText(MainActivity.this,"ket noi", Toast.LENGTH_LONG).show();

            }
        }).execute();
    }

    public void DangNhap(View view) {
        AuthUIConfiguration config =
                new AuthUIConfiguration.Builder()
                        .userPools(true)  // true? show the Email and Password UI
                        .logoResId(R.drawable.default_sign_in_logo) // Change the logo
                        .backgroundColor(R.color.blue)
                        .isBackgroundColorFullScreen(true) // Full screen backgroundColor the backgroundColor full screenff
                        .fontFamily("sans-serif-light") // Apply sans-serif-light as the global font
                        .canCancel(false)
                        .build();
        SignInUI signInUI = (SignInUI) AWSMobileClient.getInstance().getClient(MainActivity.this, SignInUI.class);
        signInUI.login(MainActivity.this, Main2Activity.class).authUIConfiguration(config).execute();

    }
}
