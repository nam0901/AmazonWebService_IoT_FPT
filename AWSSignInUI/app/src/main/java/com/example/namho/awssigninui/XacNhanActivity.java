package com.example.namho.awssigninui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.example.namho.awssigninui.Model.KetNoiCognito;

public class XacNhanActivity extends AppCompatActivity {

    EditText edtXacNhan;
    Button btnXacNhan;

    String tenDangNhap;
    KetNoiCognito ketNoiCognitoXacNhan;
    CognitoUserPool cognitoUserPoolXacNhan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xacnhan);
        KhoiTaoToolBar();
        KhoiTao();
        GanSuKien();
    }

    private void KhoiTaoToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        TextView txtTBTieuDe = findViewById(R.id.txtTBTieuDe);
        txtTBTieuDe.setText(R.string.btn_xac_nhan);
    }

    private void KhoiTao() {
        edtXacNhan = findViewById(R.id.edtXacNhan);
        btnXacNhan = findViewById(R.id.btnXacNhan);

        Intent iXacNhan = getIntent();
        tenDangNhap = iXacNhan.getStringExtra("userid");
        ketNoiCognitoXacNhan = new KetNoiCognito(XacNhanActivity.this);
        cognitoUserPoolXacNhan = ketNoiCognitoXacNhan.getCognitoUserPool();
    }

    private void GanSuKien() {
        btnXacNhan.setOnClickListener(XacNhan);
    }

    View.OnClickListener XacNhan = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //lấy user đã đăng ký theo userid đã nằm trong pool mà chưa xác nhận
            if(tenDangNhap != null) {
                CognitoUser userXacNhan = cognitoUserPoolXacNhan.getUser(tenDangNhap);
                String maXacNhan = edtXacNhan.getText().toString().trim();
                // để false thì sẽ xác nhận thất bại khi thuộc tính của 2 người dùng giống nhau ( false = không có cùng attribute)
                boolean forcedAliasCreation = true;
                userXacNhan.confirmSignUpInBackground(maXacNhan, forcedAliasCreation, new GenericHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(XacNhanActivity.this, R.string.dk_thanh_cong, Toast.LENGTH_SHORT).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent iDangNhapTuXacNhan = new Intent(XacNhanActivity.this, DangNhapActitvity.class);
                                startActivity(iDangNhapTuXacNhan);
                            }
                        },2500);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        exception.printStackTrace();
                    }
                });
            }
        }
    };

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
}
