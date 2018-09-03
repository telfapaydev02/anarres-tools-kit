package com.anarres.toolskit.httpclient;


import com.anarres.toolskit.httpclient.async.AsyncBuilder;
import com.anarres.toolskit.httpclient.async.AsyncHttpClient;
import com.anarres.toolskit.httpclient.sync.HttpClient;
import com.anarres.toolskit.httpclient.sync.SyncBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;

import java.io.IOException;

public class Https {

    public static HttpClient syncClient =  sync().setDefaultCharset("UTF-8").trustAll().build();
    public static AsyncHttpClient asyncClient =  async().setDefaultCharset("UTF-8").trustAll().build().init();

    public static AsyncBuilder async() {
        return new AsyncBuilder();
    }

    public static SyncBuilder sync() {
        return new SyncBuilder();
    }

    public static AsyncBuilder async(HttpAsyncClientBuilder builder) {
        return new AsyncBuilder(builder);
    }

    public static SyncBuilder async(HttpClientBuilder builder) {
        return new SyncBuilder(builder);
    }
}
