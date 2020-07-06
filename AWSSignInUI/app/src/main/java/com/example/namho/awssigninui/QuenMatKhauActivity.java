package com.example.namho.awssigninui;

import android.content.Intent;
import android.os.Bundle;
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
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ForgotPasswordContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.ForgotPasswordHandler;
import com.example.namho.awssigninui.Model.KetNoiCognito;

public class QuenMatKhauActivity extends AppCompatActivity {
    final int REQUEST_CODE = 123;
    final String LOI = "QUENMATKHAUAC";

    EditText edtMKMoi, edtXNQuen;
    TextView txtLoiMK, txtLoiXN;
    Button btnXNQuen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quenmatkhau);

        KhoiTaoToolBar();
        KhoiTao();
        GanSuKien();
    }

    private void KhoiTaoToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView txtTieuDe = findViewById(R.id.txtTBTieuDe);
        txtTieuDe.setText(R.string.quen_mat_khau);

    }

    private void KhoiTao() {
        edtMKMoi = findViewById(R.id.edtMKMoi);
        edtXNQuen = findViewById(R.id.edtXNQuen);
        txtLoiMK = findViewById(R.id.txtLoiMKMoi);
        txtLoiXN = findViewById(R.id.txtLoiXNQuen);
        btnXNQuen = findViewById(R.id.btnXNQuen);
    }

    private void GanSuKien() {
        btnXNQuen.setOnClickListener(XacNhanQuen);
    }

    View.OnClickListener XacNhanQuen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String matKhauMoi = edtMKMoi.getText().toString().trim();
            if(matKhauMoi.length() == 0) {
                txtLoiMK.setText("Nhập mật khẩu mới");
            }
            String xacNhanQuen = edtXNQuen.getText().toString().trim();
            if(xacNhanQuen.length() == 0) {
                txtLoiXN.setText("Nhập mã xác nhận");
            }

            Intent iQuenMKToQuenMKDN = new Intent();
            iQuenMKToQuenMKDN.putExtra("matkhaumoi",matKhauMoi);
            iQuenMKToQuenMKDN.putExtra("xacnhanquen",xacNhanQuen);
            setResult(RESULT_OK, iQuenMKToQuenMKDN);
            finish();
        }
    };


}
