package com.example.namho.awssigninui.Model;

import android.content.Context;

import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;

import java.util.UUID;

public class KetNoiAWSIoT {
    private final String END_POINT = "apjxpdfzcw3nh.iot.us-east-2.amazonaws.com";
    private String clientID;
    private final String TOPIC_SUBSCRIBE = "aws/things/tienesp/shadow";
    private final String TOPIC_SUBSCRIBE1 = "subs/tbThang";
    private final String TOPIC_SUBSCRIBE2 = "$aws/things/sonnvb_gateway/shadow/update/accepted";

    private final String TOPIC_PUBLISH = "$aws/things/tienesp/shadow/update/accepted";
    private final String TOPIC_PUBLISH1= "pub/tbThang";
    private final String TOPIC_PUBLISH2= "$aws/things/sonnvb_gateway/shadow/update/accepted";

    Context context;
    AWSIotMqttManager awsIotMqttManager;
    public KetNoiAWSIoT(Context context) {
        this.context = context;
        clientID = UUID.randomUUID().toString();
        awsIotMqttManager = new AWSIotMqttManager(clientID, END_POINT);
    }

    public String getClientID() {
        return clientID;
    }

    public AWSIotMqttManager getAwsIotMqttManager() {
        return awsIotMqttManager;
    }

    public String getTOPIC_SUBSCRIBE() {
        return TOPIC_SUBSCRIBE;
    }

    public String getTOPIC_PUBLISH() {
        return TOPIC_PUBLISH;
    }

    public String getTOPIC_PUBLISH1() {
        return TOPIC_PUBLISH1;
    }
}
