package com.example.namho.awsanalytics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobile.auth.core.IdentityHandler;
import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobile.config.AWSConfiguration;


public class MainActivity extends AppCompatActivity {

     AWSCredentialsProvider credentialsProvider;
     AWSConfiguration configuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ket noi voi AWS
        AWSMobileClient.getInstance().initialize(MainActivity.this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Toast.makeText(MainActivity.this,"Ket noi thanh cong", Toast.LENGTH_LONG).show();
               /* credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
                configuration = AWSMobileClient.getInstance().getConfiguration();

                Log.d("AWS GetStarted", awsStartupResult.toString());
                Log.d("AWS GetStartedProvider", credentialsProvider.toString());
                Log.d("AWS GetStartedConfi",configuration.toString());

                IdentityManager.getDefaultIdentityManager().getUserID(new IdentityHandler() {
                    @Override
                    public void onIdentityId(String identityId) {
                        Log.d("AWS GetStartedID", "Identity ID = " + identityId);

                        // Use IdentityManager#getCachedUserID to
                        //  fetch the locally cached identity id.
                        final String cachedIdentityId =
                                IdentityManager.getDefaultIdentityManager().getCachedUserID();
                        Log.d("AWS GetStartedCached", "Identity ID = " + cachedIdentityId);
                    }

                    @Override
                    public void handleError(Exception exception) {
                        Log.d("YourMainActivity", "Error in retrieving the identity" + exception);
                    }
                });*/

                SignInUI signin = (SignInUI) AWSMobileClient.getInstance().getClient(MainActivity.this, SignInUI.class);
                signin.login(MainActivity.this, Main2Activity.class).execute();
            }
        }).execute();
        // end Ket noi AWS

        /*        //Analytics
        PinpointConfiguration pinpointConfiguration = new PinpointConfiguration(
                this,
                AWSMobileClient.getInstance().getCredentialsProvider(),
                AWSMobileClient.getInstance().getConfiguration());
        pinpointManager = new PinpointManager(pinpointConfiguration);
        pinpointManager.getSessionClient().startSession();
        pinpointManager.getAnalyticsClient().submitEvents();
        Log.d("AWS Analytics", "thanh cong?");
        Log.d("hello", pinpointManager.toString()+pinpointConfiguration.toString());
        */


    }
}
