package com.example.namho.awssigninui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.example.namho.awssigninui.Model.KetNoiCognito;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DangKyActivity extends AppCompatActivity implements View.OnClickListener{
    final String LOI = "DANGKYAC";
    TextView txtTenDangNhapDk, txtMatKhauDk, txtTenNguoiDungDK, txtEmailDK, txtSoDTDK;
    Button btnDK;

    String tenDangNhapDK, matKhauDK;
    KetNoiCognito ketNoiCognito;
    CognitoUserPool userPool;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);
        KhoiTaoToolbar();
        KhoiTao();
        GanSuKien();
    }

    private void KhoiTao() {
        txtTenDangNhapDk = findViewById(R.id.txtTenDangNhapDK);
        txtMatKhauDk = findViewById(R.id.txtMatKhauDK);
        txtTenNguoiDungDK = findViewById(R.id.txtTenNguoiDungDK);
        txtEmailDK = findViewById(R.id.txtEmailDK);
        txtSoDTDK = findViewById(R.id.txtSoDienThoatDK);
        btnDK = findViewById(R.id.btnDK);

        ketNoiCognito = new KetNoiCognito(DangKyActivity.this);
        userPool = ketNoiCognito.getCognitoUserPool();
    }

    private void GanSuKien() {
        btnDK.setOnClickListener(this);
    }

    private void KhoiTaoToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        TextView txtTBTieuDe = findViewById(R.id.txtTBTieuDe);
        txtTBTieuDe.setText(R.string.btn_dang_ky);
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

    private void DangKyUser() {
        tenDangNhapDK = txtTenDangNhapDk.getText().toString().trim();
        if(tenDangNhapDK.length() == 0 || !isEmailValid(tenDangNhapDK)) {
            txtTenDangNhapDk.requestFocus();
            txtTenDangNhapDk.setHint(R.string.day_du_thong_tin);
            return;
        }
        matKhauDK = txtMatKhauDk.getText().toString().trim();
        if(matKhauDK.length() == 0) {
            txtMatKhauDk.requestFocus();
            txtMatKhauDk.setHint(R.string.day_du_thong_tin);
            return;
        }
        String tenNguoiDungDK = txtTenNguoiDungDK.getText().toString().trim();
        if(tenNguoiDungDK.length() == 0) {
            txtTenNguoiDungDK.requestFocus();
            txtTenNguoiDungDK.setHint(R.string.day_du_thong_tin);
            return;
        }
        String soDTDK = txtSoDTDK.getText().toString().trim();
        if(soDTDK.length() == 0) {
            txtSoDTDK.requestFocus();
            txtSoDTDK.setHint(R.string.day_du_thong_tin);
            return;
        }
        String emailDK = txtEmailDK.getText().toString().trim();
        if(emailDK.length() == 0 || !isEmailValid(emailDK)) {
            txtEmailDK.requestFocus();
            txtEmailDK.setHint(R.string.day_du_thong_tin);
            return;
        }

        CognitoUserAttributes userAttributes = new CognitoUserAttributes();
        userAttributes.addAttribute("given_name", tenNguoiDungDK);
        userAttributes.addAttribute("phone_number", soDTDK);
        userAttributes.addAttribute("email", emailDK);


        userPool.signUpInBackground(tenDangNhapDK, matKhauDK, userAttributes, null,
                new SignUpHandler() {
                    @Override
                    public void onSuccess(CognitoUser user, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                        //Chưa confirm, chuyển sang trang confirm để lấy code confirm gửi qua mail
                        if(!signUpConfirmationState) {

                            Intent iXacNhan = new Intent(DangKyActivity.this, XacNhanActivity.class);
                            iXacNhan.putExtra("userid",tenDangNhapDK);
                            startActivity(iXacNhan);
                        }
                        else {
                            // người dùng đã confirm
                        }
                    }

                    @Override
                    public void onFailure(Exception exception) {
                                Toast.makeText(DangKyActivity.this, R.string.loi_dk,Toast.LENGTH_LONG).show();
                        exception.printStackTrace();
                        Log.d(LOI + "DK", exception.getMessage());
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.btnDK:
                DangKyUser();
                break;
        }
    }


}
