package com.example.namho.awssigninui;

import android.content.Intent;
import android.net.MailTo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.namho.awssigninui.Model.KetNoiCognito;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DangNhapActitvity extends AppCompatActivity implements View.OnClickListener{

    EditText edtDN, edtMatKhau;
    TextView  txtDangky, txtQuenMatKhau;
    Button btnDangNhap;

    KetNoiCognito ketNoiCognito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        KhoiTaoView();
        SuKien();
    }

    @Override
    protected void onResume() {
        edtDN.setText("");
        edtMatKhau.setText("");
        edtDN.requestFocus();
        super.onResume();
    }

    private void KhoiTaoView() {
        edtDN = findViewById(R.id.edtDN);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        txtDangky = findViewById(R.id.txtDangKy);
        txtQuenMatKhau = findViewById(R.id.txtQuenMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
    }

    private void SuKien() {
        btnDangNhap.setOnClickListener(this);
        txtDangky.setOnClickListener(this);
        txtQuenMatKhau.setOnClickListener(this);
    }



    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    void DNCogintoUP(final String tenDangNhap, final String matKhau) {
        ketNoiCognito = new KetNoiCognito(DangNhapActitvity.this);
        CognitoUserPool cognitoUserPool = ketNoiCognito.getCognitoUserPool();
        final CognitoUser cognitoUser = cognitoUserPool.getUser(tenDangNhap);
        cognitoUser.getSessionInBackground(new AuthenticationHandler() {
            @Override
            public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
                String idtoken = userSession.getIdToken().getJWTToken();
                String accesstoken = userSession.getAccessToken().getJWTToken();
                Intent iDangNhapToTrangChu = new Intent(DangNhapActitvity.this, TrangChuActivity.class);
                iDangNhapToTrangChu.putExtra("userid", cognitoUser.getUserId());
                iDangNhapToTrangChu.putExtra("idtoken", idtoken);
                iDangNhapToTrangChu.putExtra("accesstoken", accesstoken);
                startActivity(iDangNhapToTrangChu);
            }

            @Override
            public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
                // Khi Đăng nhập sẽ vào thằng này để xác nhận id và mật khẩu và sau đó nhảy vào lại thằng onSucess
                AuthenticationDetails authenticationDetails = new AuthenticationDetails(tenDangNhap, matKhau, null);

                authenticationContinuation.setAuthenticationDetails(authenticationDetails);
                authenticationContinuation.continueTask();
            }

            @Override
            public void getMFACode(MultiFactorAuthenticationContinuation continuation) {

            }

            @Override
            public void authenticationChallenge(ChallengeContinuation continuation) {

            }

            @Override
            public void onFailure(Exception exception) {
                exception.printStackTrace();
                Log.d("LOIDANGNHAPAC",exception.getMessage());
                String[] loiMang = exception.getMessage().split("\\.");
                if(loiMang[0].equals("Incorrect username or password")) {
                    Toast.makeText(getApplicationContext(), "Tên đăng nhập hoặc mật khẩu không chính xác",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.btnDangNhap:
                String tenDangNhap = edtDN.getText().toString().trim();
                String matKhau = edtMatKhau.getText().toString().trim();
                if(tenDangNhap.length() > 0 && matKhau.length() > 0) {
                    if(isEmailValid(tenDangNhap)) {
                        // code đăng nhập vào cognito user pool
                        DNCogintoUP(tenDangNhap, matKhau);
                    }
                    else {
                        Toast.makeText(DangNhapActitvity.this, R.string.mail_khong_hop_le, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                        Toast.makeText(DangNhapActitvity.this,R.string.nhap_day_du_ten_mk,Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.txtDangKy:
                Intent iDangky = new Intent(DangNhapActitvity.this, DangKyActivity.class);
                startActivity(iDangky);
                break;

            case R.id.txtQuenMatKhau:
                Intent iDangNhapToQuenMatKhau = new Intent(DangNhapActitvity.this, QuenMatKhauTenDNActivity.class);
                startActivity(iDangNhapToQuenMatKhau);
                break;

            default:
                break;
        }
    }

}
