package com.example.namho.awssigninui.Model;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class KetNoiCognito   {

    final String USER_POOL_ID = "us-east-2_HXZqEWlUr";
    final String CLIENT_ID = "c6s83h1b1mfr1uk71cn5jvvrs";
    final String CLIENT_SECRET = "1uvjnlt1fplgkje4mnjg6emjii84653648lrs1u80mqsslq1kook";
    final Regions REGIONS = Regions.US_EAST_2;

    CognitoUserPool cognitoUserPool;
    Context context;

    public KetNoiCognito(Context context) {
        this.context = context;
        cognitoUserPool = new CognitoUserPool(context, USER_POOL_ID, CLIENT_ID, CLIENT_SECRET, REGIONS);
    }

    public String getUSER_POOL_ID() {
        return USER_POOL_ID;
    }

    public String getCLIENT_ID() {
        return CLIENT_ID;
    }

    public String getCLIENT_SECRET() {
        return CLIENT_SECRET;
    }

    public Regions getREGIONS() {
        return REGIONS;
    }

    public  CognitoUserPool getCognitoUserPool() {
        return cognitoUserPool;
    }


}
