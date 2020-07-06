package com.example.namho.awssigninui;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttMessageDeliveryCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttNewMessageCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import com.amazonaws.regions.Regions;
import com.example.namho.awssigninui.Model.DuLieuGuiDi;
import com.example.namho.awssigninui.Model.DuLieuNhanDuoc;
import com.example.namho.awssigninui.Model.KetNoiAWSIoT;
import com.example.namho.awssigninui.Model.KetNoiCognito;
import com.example.namho.awssigninui.Model.KetNoiCredential;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrangChuActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, AWSIotMqttNewMessageCallback {

    final String LOI = "TESTTRANGCHUAC";
    private final String POOL_ID = "us-east-2:b4b024e0-e509-48d3-8964-5ef5d4f6b0df"; // SignInUp
    private final String POOL_ID2 = "us-east-2:344be957-d0cc-45bd-9917-10d479073cbd"; // tbThang
    private final Regions REGIONS = Regions.US_EAST_2;

    String idToken, accessToken;
    Switch swKetNoi;
    TextView txtTinhTrangKetNoi, txtClientID;
    Button btnSubscribe, btnMoBlue, btnDongBLue, btnMoGreen, btnDongGreen, btnMoRed, btnDongRed, btnMoYellow, btnDongYellow;
    ImageView imgBlue, imgGreen, imgRed, imgYellow;

    CognitoCachingCredentialsProvider credentialsProvider;
    AWSIotMqttManager mqttManager;
    String clientID;
    KetNoiAWSIoT ketNoiAWSIoT;
    KetNoiCredential ketNoiCredential;
    DuLieuGuiDi duLieuGuiDi;
    String userID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu);

        KhoiTaoWidget();
        GanSuKien();
    }

    private void KhoiTaoWidget() {
        swKetNoi = findViewById(R.id.swKetNoi);
        txtTinhTrangKetNoi = findViewById(R.id.txtTinhTrangKetNoi);
        btnSubscribe = findViewById(R.id.btnSubscribe);
        txtClientID = findViewById(R.id.txtClientID);
        imgBlue = findViewById(R.id.imgBlue);
        imgGreen = findViewById(R.id.imgGreen);
        imgRed = findViewById(R.id.imgRed);
        imgYellow = findViewById(R.id.imgYellow);
        btnMoBlue = findViewById(R.id.btnMoBlue);
        btnDongBLue = findViewById(R.id.btnDongBLue);
        btnMoGreen = findViewById(R.id.btnMoGreen);
        btnDongGreen = findViewById(R.id.btnDongGreen);
        btnMoRed = findViewById(R.id.btnMoRed);
        btnDongRed = findViewById(R.id.btnDongRed);
        btnMoYellow = findViewById(R.id.btnMoYellow);
        btnDongYellow = findViewById(R.id.btnDongYellow);

        Intent iTrangChu = getIntent();
        idToken = iTrangChu.getStringExtra("idtoken");
        accessToken = iTrangChu.getStringExtra("accesstoken");
        userID = iTrangChu.getStringExtra("userid");
        /*Log.d(LOI, "idken = " + idToken);
        Log.d(LOI, "accessken = " + accessToken);
        Log.d(LOI, "user = " + userID);*/
        ketNoiCredential = new KetNoiCredential(TrangChuActivity.this);

        credentialsProvider = new CognitoCachingCredentialsProvider(getApplicationContext(), POOL_ID2, REGIONS);

        ketNoiAWSIoT = new KetNoiAWSIoT(TrangChuActivity.this);
        mqttManager = ketNoiAWSIoT.getAwsIotMqttManager();
        clientID = ketNoiAWSIoT.getClientID();

       /* Map<String, String> logins = new HashMap<String, String>();
        logins.put("cognito-idp.us-east-2.amazonaws.com/us-east-2_HXZqEWlUr", idToken);
        credentialsProvider.setLogins(logins);*/


        /*Map<String, String> logins = new HashMap<String, String>();
        logins.put("cognito-idp.us-east-2.amazonaws.com/us-east-2_HXZqEWlUr", idToken);
        credentialsProvider.setLogins(logins);*/

        try {
            duLieuGuiDi = new DuLieuGuiDi();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GanSuKien() {
        swKetNoi.setOnCheckedChangeListener(this);
        btnSubscribe.setOnClickListener(Subscribe);

        btnMoBlue.setOnClickListener(Publish);
        btnDongBLue.setOnClickListener(Publish);

        btnMoGreen.setOnClickListener(Publish);
        btnDongGreen.setOnClickListener(Publish);

        btnMoRed.setOnClickListener(Publish);
        btnDongRed.setOnClickListener(Publish);

        btnMoYellow.setOnClickListener(Publish);
        btnDongYellow.setOnClickListener(Publish);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b) {
            try {
                mqttManager.connect(credentialsProvider, new AWSIotMqttClientStatusCallback() {
                    @Override
                    public void onStatusChanged(final AWSIotMqttClientStatus status, final Throwable throwable) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status == AWSIotMqttClientStatus.Connecting) {
                                    txtTinhTrangKetNoi.setText("Đang kết nối...");

                                } else if (status == AWSIotMqttClientStatus.Connected) {
                                    txtTinhTrangKetNoi.setText("Đã kết nối.");
                                    txtClientID.setText("clientid = " +clientID);

                                } else if (status == AWSIotMqttClientStatus.Reconnecting) {
                                    if (throwable != null) {
                                        throwable.printStackTrace();
                                    }
                                    txtTinhTrangKetNoi.setText("Kết nối lại...");
                                } else if (status == AWSIotMqttClientStatus.ConnectionLost) {
                                    if (throwable != null) {
                                        throwable.printStackTrace();
                                    }
                                    txtTinhTrangKetNoi.setText("Mất kết nối.");
                                } else {
                                    txtTinhTrangKetNoi.setText("Mất kết nối.");
                                }
                            }
                        });

                    }
                });
            }
            catch (Exception ex) {
                ex.printStackTrace();
                txtTinhTrangKetNoi.setText("Mất kết nối.");
            }
        }
        else {
            try {
                mqttManager.disconnect();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtTinhTrangKetNoi.setText("Ngắt kết nối.");
                    }
                });
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    View.OnClickListener Subscribe = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                Toast.makeText(TrangChuActivity.this,"Da nhan",Toast.LENGTH_LONG).show();
                mqttManager.subscribeToTopic(ketNoiAWSIoT.getTOPIC_SUBSCRIBE(), AWSIotMqttQos.QOS0, TrangChuActivity.this);

            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };


    View.OnClickListener Publish = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                int id = view.getId();
                switch(id) {
                    case R.id.btnMoBlue:
                        GuiTinhTrangLed(DuLieuGuiDi.BLUE_VALUE, "On");
                        break;
                    case R.id.btnDongBLue:
                        GuiTinhTrangLed(DuLieuGuiDi.BLUE_VALUE, "Off");
                        break;

                    case R.id.btnMoRed:
                        GuiTinhTrangLed(DuLieuGuiDi.RED_VALUE, "On");
                        break;
                    case R.id.btnDongRed:
                        GuiTinhTrangLed(DuLieuGuiDi.RED_VALUE, "Off");
                        break;

                    case R.id.btnMoGreen:
                        GuiTinhTrangLed(DuLieuGuiDi.GREEN_VALUE, "On");
                        break;
                    case R.id.btnDongGreen:
                        GuiTinhTrangLed(DuLieuGuiDi.GREEN_VALUE, "Off");
                        break;

                    case R.id.btnMoYellow:
                        GuiTinhTrangLed(DuLieuGuiDi.YELLOW_VALUE, "On");
                        break;
                    case R.id.btnDongYellow:
                        GuiTinhTrangLed(DuLieuGuiDi.YELLOW_VALUE, "Off");
                        break;
                }
            } catch (Exception ex) {

            }

        }
    };

    private void GuiTinhTrangLed(String value, String tinhTrang) throws JSONException {
        if(value.equals(DuLieuGuiDi.BLUE_VALUE)) {
            duLieuGuiDi.setBlueValueGui(tinhTrang);
        }else if(value.equals(DuLieuGuiDi.GREEN_VALUE)) {
            duLieuGuiDi.setGreenValueGui(tinhTrang);
        }else if(value.equals(DuLieuGuiDi.RED_VALUE)) {
            duLieuGuiDi.setRedValueGui(tinhTrang);
        }else if(value.equals(DuLieuGuiDi.YELLOW_VALUE)) {
            duLieuGuiDi.setYellowValueGui(tinhTrang);
        }
        duLieuGuiDi.XulyJSGuiDi(value, tinhTrang);
        mqttManager.publishString(duLieuGuiDi.getGuiDi(), ketNoiAWSIoT.getTOPIC_PUBLISH(), AWSIotMqttQos.QOS0);
    }

    @Override
    public void onMessageArrived(final String topic, final byte[] data) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    String msg = new String(data, "UTF-8");
                    //duLieuGuiDi.setGuiDi(msg);
                    /*if(topic.equals(ketNoiAWSIoT.getTOPIC_SUBSCRIBE())) {

                        DuLieuNhanDuoc duLieuNhanDuoc = new DuLieuNhanDuoc(msg);
                        duLieuNhanDuoc.XulyDuLieu(duLieuNhanDuoc.getDesiredJS());

                        if(duLieuNhanDuoc.getBlueValue().equals("Off")) {
                            imgBlue.setImageResource(R.drawable.off);
                        }
                        else if(duLieuNhanDuoc.getBlueValue().equals("On")) {
                            imgBlue.setImageResource(R.drawable.on);
                        }
                        else {
                            Log.d(LOI+"NHANDUOC","Khong dung yeu cau");
                        }

                        if(duLieuNhanDuoc.getGreenValue().equals("Off")) {
                            imgGreen.setImageResource(R.drawable.off);
                        }
                        else if(duLieuNhanDuoc.getGreenValue().equals("On")) {
                            imgGreen.setImageResource(R.drawable.on);
                        }
                        else {
                            Log.d(LOI+"NHANDUOC","Khong dung yeu cau");
                        }

                        if(duLieuNhanDuoc.getRedValue().equals("Off")) {
                            imgRed.setImageResource(R.drawable.off);
                        }
                        else if(duLieuNhanDuoc.getRedValue().equals("On")) {
                            imgRed.setImageResource(R.drawable.on);
                        }
                        else {
                            Log.d(LOI+"NHANDUOC","Khong dung yeu cau");
                        }

                        if(duLieuNhanDuoc.getYellowValue().equals("Off")) {
                            imgYellow.setImageResource(R.drawable.off);
                        }
                        else if(duLieuNhanDuoc.getYellowValue().equals("On")) {
                            imgYellow.setImageResource(R.drawable.on);
                        }
                        else {
                            Log.d(LOI+"NHANDUOC","Khong dung yeu cau");
                        }
                    }*/
                    JSONObject rootJS = new JSONObject(msg);
                    JSONObject stateJS = rootJS.getJSONObject("state");
                    JSONObject reportedJS = stateJS.getJSONObject("reported");

                    String blueValue = reportedJS.getString("Blue_value");

                    if(blueValue.equals("On")) {
                        imgBlue.setImageResource(R.drawable.on);
                    }
                    else if(blueValue.equals("Off")) {
                        imgBlue.setImageResource(R.drawable.off);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOI+"JSon", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        KetNoiCognito ketNoiCognito = new KetNoiCognito(TrangChuActivity.this);
        CognitoUserPool userPool = ketNoiCognito.getCognitoUserPool();
        CognitoUser user = userPool.getUser(userID);
        Toast.makeText(TrangChuActivity.this, user.getUserId(), Toast.LENGTH_LONG).show();
        user.signOut();
        super.onBackPressed();
    }
}
