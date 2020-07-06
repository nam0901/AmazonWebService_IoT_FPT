package com.example.namho.awsuser;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSettings;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.UpdateAttributesHandler;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProvider;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String USER_POOL_ID = "us-east-2_HXZqEWlUr";
    final String CLIENT_ID = "c6s83h1b1mfr1uk71cn5jvvrs";
    final String CLIENT_SECRET = "1uvjnlt1fplgkje4mnjg6emjii84653648lrs1u80mqsslq1kook";
    final Regions REGIONS = Regions.US_EAST_2;

    EditText edtID, edtMatKhau;
    Button btnDangky, btnDangNhap;
        CognitoUserPool cognitoUserPool;

    final int REQUEST_XACNHAN = 113;
    String codeXacNhan = null;

    public static CognitoUser cognitoUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtID = findViewById(R.id.edtID);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangky = findViewById(R.id.btnDangKy);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        cognitoUserPool = new CognitoUserPool(getApplicationContext(), USER_POOL_ID, CLIENT_ID, CLIENT_SECRET, REGIONS);

        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CognitoUserAttributes userAttributes = new CognitoUserAttributes();

                userAttributes.addAttribute("given_name", "namho");
                userAttributes.addAttribute("phone_number", "+84932338125");
                userAttributes.addAttribute("email", "namho2508@gmail.com");


                cognitoUserPool.signUpInBackground(edtID.getText().toString(),edtMatKhau.getText().toString(), userAttributes, null, new SignUpHandler() {
                    @Override
                    public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                        cognitoUser = user;
                        Log.d("DUONGDAN", "des "+cognitoUserCodeDeliveryDetails.getDestination());
                        Log.d("DUONGDAN", "name "+cognitoUserCodeDeliveryDetails.getAttributeName());
                        Log.d("DUONGDAN", "meidum "+cognitoUserCodeDeliveryDetails.getDeliveryMedium());
                        //chua Confirm
                        if(!signUpConfirmationState) {
                            Intent iXacNhan = new Intent(MainActivity.this, ConfirmActivity.class);
                            startActivityForResult(iXacNhan,REQUEST_XACNHAN);

                        }
                        // da Confirm
                        else {
                            Log.d("TESTDANGNHAP", "da confirm");
                        }
                    }
                    @Override
                    public void onFailure(Exception exception) {
                            exception.printStackTrace();
                    }
                });// --Het SignUp
            }
        }); // --Het OnClick dang ky

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cognitoUser == null) {
                   cognitoUser = cognitoUserPool.getUser(edtID.getText().toString());

                }
                cognitoUser.getSessionInBackground(new AuthenticationHandler() {
                    @Override
                    public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                            Log.d("TESTDANGNHAP", "dang nhap thanh cong success");
                            Log.d("TESTDANGNHAPACESS", userSession.getAccessToken().getJWTToken().toString());
                            Log.d("TESTDANGNHAPName",userSession.getUsername().toString());
                            Log.d("TESTDANGNHAPIDDDD", userSession.getIdToken().getJWTToken().toString());
                        ChuyenSangTrangChu();
                    }

                    @Override
                    public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                        Log.d("TESTDANGNHAP", "dang nhap thanh cong get detail");
                        Log.d("TESTDANGNHAP",userId);
                        AuthenticationDetails authDetails = new AuthenticationDetails(userId, edtMatKhau.getText().toString(), null);

                        // Now allow the authentication to continue
                        authenticationContinuation.setAuthenticationDetails(authDetails);
                        authenticationContinuation.continueTask();

                    }

                    @Override
                    public void getMFACode(MultiFactorAuthenticationContinuation continuation) {
                        Log.d("TESTDANGNHAP", "dang nhap thanh cong mfacode");
                        ChuyenSangTrangChu();
                    }

                    @Override
                    public void authenticationChallenge(ChallengeContinuation continuation) {
                        Log.d("TESTDANGNHAP", "dang nhap thanh cong challenge");
                        ChuyenSangTrangChu();
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Log.d("TESTDANGNHAP", "dang nhap thanh cong exception");
                        ChuyenSangTrangChu();
                    }
                });
            }
        }); // --Het OnClick dang nhap


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_XACNHAN:
                if(resultCode == RESULT_OK) {
                    codeXacNhan = data.getStringExtra("code");

                    if(codeXacNhan != null) {
                        cognitoUser.confirmSignUpInBackground(codeXacNhan, false, new GenericHandler() {
                            @Override
                            public void onSuccess() {
                                Log.d("TESTDANGNHAP","dang ky thanh cong");

                            }

                            @Override
                            public void onFailure(Exception exception) {

                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
    }

    private void ChuyenSangTrangChu() {
        Intent iTrangChu = new Intent(MainActivity.this, TrangChuActivity.class);
        startActivity(iTrangChu);
    }
}
