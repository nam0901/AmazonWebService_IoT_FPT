package com.example.namho.awssigninui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.amazonaws.services.iot.model.InvalidQueryException;
import com.example.namho.awssigninui.Model.KetNoiCognito;

public class QuenMatKhauTenDNActivity extends AppCompatActivity {
    final String LOI = "QUENMKTENDNAC";
    final int REQUEST_CODE = 123;
    EditText edtTenDNQuen;
    TextView txtLoiDN;
    Button btnXNQuenDN;

    KetNoiCognito ketNoiCognito;
    ForgotPasswordContinuation forgotPasswordContinuation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quenmatkhautendn);

        KhoiTaoToolBar();
        KhoiTao();
        GanSuKien();
    }

    private void KhoiTaoToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        TextView txtTieuDe = findViewById(R.id.txtTBTieuDe);
        txtTieuDe.setText(R.string.quen_mat_khau);
    }

    private void KhoiTao() {
        edtTenDNQuen = findViewById(R.id.edtTenDNQuen);
        txtLoiDN = findViewById(R.id.txtLoiTenDN);
        btnXNQuenDN = findViewById(R.id.btnXNQuenDN);

        ketNoiCognito = new KetNoiCognito(QuenMatKhauTenDNActivity.this);
    }

    private void GanSuKien() {
        btnXNQuenDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String tenDangNhap = edtTenDNQuen.getText().toString().trim();
                            if(tenDangNhap.length() == 0) {
                                txtLoiDN.setText("Nhập tên đăng nhập đầy đủ");
                            }
                            CognitoUserPool userPool = ketNoiCognito.getCognitoUserPool();
                            CognitoUser cognitoUser = userPool.getUser(tenDangNhap);
                            cognitoUser.forgotPassword(new ForgotPasswordHandler() {
                                @Override
                                public void onSuccess() {
                                    // đổi pass thành công
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent iQuenMKTenDNToDangNhap = new Intent(QuenMatKhauTenDNActivity.this, DangNhapActitvity.class);
                                            startActivity(iQuenMKTenDNToDangNhap);
                                        }
                                    }, 2700);
                                    finish();
                                }

                                @Override
                                public void getResetCode(ForgotPasswordContinuation continuation) {
                                        forgotPasswordContinuation = continuation;
                                        Intent iQuenMKDNToQuenMK = new Intent(QuenMatKhauTenDNActivity.this, QuenMatKhauActivity.class);
                                        startActivityForResult(iQuenMKDNToQuenMK,REQUEST_CODE);
                                }

                                @Override
                                public void onFailure(Exception exception) {
                                    exception.printStackTrace();
                                }
                            });
                        }
                    }).start();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if(resultCode == RESULT_OK) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String matKhauMoi = data.getStringExtra("matkhaumoi");
                            String xacNhanQuen = data.getStringExtra("xacnhanquen");
                            if(matKhauMoi.length() != 0 && xacNhanQuen.length() != 0) {
                                forgotPasswordContinuation.setPassword(matKhauMoi);
                                forgotPasswordContinuation.setVerificationCode(xacNhanQuen);
                                forgotPasswordContinuation.continueTask();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(QuenMatKhauTenDNActivity.this, "doi mat khau thanh cong", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).start();

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
