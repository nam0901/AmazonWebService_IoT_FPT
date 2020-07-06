package com.example.namho.awsuser;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfirmActivity extends AppCompatActivity {
    EditText edtCODE;
    Button btnXacNhan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        edtCODE = findViewById(R.id.edtCODE);
        btnXacNhan = findViewById(R.id.btnXacNhan);

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iCode = new Intent();
                iCode.putExtra("code",edtCODE.getText().toString());
                setResult(RESULT_OK, iCode);
                finish();
            }
        });
    }
}
