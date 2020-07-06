package com.example.namho.awssigninui.Model;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

public class KetNoiCredential {
    private final String POOL_ID = "us-east-2:b4b024e0-e509-48d3-8964-5ef5d4f6b0df"; // SignInUp
    private final String POOL_ID2 = "us-east-2:344be957-d0cc-45bd-9917-10d479073cbd"; // tbThang
    private final Regions REGIONS = Regions.US_EAST_2;

    CognitoCachingCredentialsProvider credentialsProvider;
    Context context;
    public KetNoiCredential(Context context) {
        this.context = context;
        credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                POOL_ID,
                REGIONS);
    }

    public CognitoCachingCredentialsProvider getCredentialsProvider() {
        return credentialsProvider;
    }
}
