package com.anarres.toolskit.httpclient;


import com.anarres.toolskit.httpclient.async.AsyncBuilder;
import com.anarres.toolskit.httpclient.async.AsyncHttpClient;
import com.anarres.toolskit.httpclient.sync.HttpClient;
import com.anarres.toolskit.httpclient.sync.SyncBuilder;

import java.io.IOException;

public class Https {

    public static HttpClient syncClient =  sync().setDefaultCharset("UTF-8").trustAll().build();
    public static AsyncHttpClient asyncClient =  async().setDefaultCharset("UTF-8").trustAll()
//            .setConnectTimeout(500000)
//            .setSocketTimeout(50000)
//            .setConnectionRequestTimeout(1000000)
            .build().init();

    public static AsyncBuilder async() {
        return new AsyncBuilder();
    }

    public static SyncBuilder sync() {
        return new SyncBuilder();
    }


    public static String asString(Response response) {
        try {
            return response.asString();
        } catch (IOException e) {
            throw new IllegalArgumentException("Response 转 String 失败", e);
        }
    }

}
