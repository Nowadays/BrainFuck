package com.deodexed.brainfuck;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Deode on 14/09/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements RemoteDatabaseListener, PostResultListener {


    @Override
    public void onExecSQLfinished(String jsondata, String sciptUsed) {

    }

    @Override
    public void onDataUpdated() {

    }

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();

        registerToken(token);


    }

    private void registerToken(String token) {

        HttpPostTask httpPostTask = new HttpPostTask(this,token,null);
        httpPostTask.execute(token);
    }

    @Override
    public void onPostRequestFinished(String jsondata) {

    }
}
