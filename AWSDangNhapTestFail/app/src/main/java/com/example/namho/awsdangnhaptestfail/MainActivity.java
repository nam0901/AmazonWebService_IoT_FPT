package com.example.namho.awsdangnhaptestfail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.regions.Regions;

public class MainActivity extends AppCompatActivity {
    final String USER_POOL_ID = "us-east-2_HXZqEWlUr";
    final String CLIENT_ID = "c6s83h1b1mfr1uk71cn5jvvrs";
    final String CLIENT_SECRET = "1uvjnlt1fplgkje4mnjg6emjii84653648lrs1u80mqsslq1kook";
    final Regions REGIONS = Regions.US_EAST_2;

    EditText edDN, edMK;
    Button btnDN;
    CognitoUserPool cognitoUserPool;
    CognitoUser cognitoUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edDN = findViewById(R.id.edtDN);
        edMK = findViewById(R.id.edtMK);
        btnDN = findViewById(R.id.btnDN);

        cognitoUserPool = new CognitoUserPool(getApplicationContext(), USER_POOL_ID, CLIENT_ID, CLIENT_SECRET, REGIONS);
        cognitoUser = cognitoUserPool.getUser(edDN.getText().toString());

        btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cognitoUser.getSessionInBackground(new AuthenticationHandler() {
                    @Override
                    public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                        Toast.makeText(getApplicationContext(), "Dang Nhap Success", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                        Toast.makeText(getApplicationContext(), "Dang Nhap AuthenticationContinuation", Toast.LENGTH_LONG).show();
                        AuthenticationDetails authDetails = new AuthenticationDetails(edDN.getText().toString(), edMK.getText().toString(), null);

                        // Now allow the authentication to continue
                        authenticationContinuation.setAuthenticationDetails(authDetails);
                        authenticationContinuation.continueTask();
                    }

                    @Override
                    public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                        Toast.makeText(getApplicationContext(), "Dang Nhap MultiFactorAuthenticationContinuation", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void authenticationChallenge(ChallengeContinuation continuation) {
                        Toast.makeText(getApplicationContext(), "Dang Nhap authenticationChallenge", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Toast.makeText(getApplicationContext(), "Dang Nhap Exception", Toast.LENGTH_LONG).show();
                        exception.printStackTrace();
                    }
                });
            }
        });

    }
}
