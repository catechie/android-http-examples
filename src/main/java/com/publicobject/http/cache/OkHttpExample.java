package com.publicobject.http.cache;

import com.squareup.okhttp.HttpResponseCache;
import com.squareup.okhttp.OkHttpClient;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OkHttpExample {
  final OkHttpClient okHttpClient;
  final HttpResponseCache cache;

  public OkHttpExample() throws IOException {
    File cacheDir = new File(System.getProperty("java.io.tmpdir"), "okhttp-cache");
    cache = new HttpResponseCache(cacheDir, 10L * 1024 * 1024);
    okHttpClient = new OkHttpClient();
    okHttpClient.setResponseCache(cache);
  }

  public void get(String url) throws IOException {
    HttpURLConnection connection = okHttpClient.open(new URL(url));
    printResponse(connection.getInputStream());
  }

  private void printResponse(InputStream in) throws IOException {
    if (true) {
      in.close();
      return;
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    for (String line; (line = reader.readLine()) != null; ) {
      System.out.println(line);
    }
  }

  public static void main(String[] args) throws IOException {
    String url = "http://localhost:8910/rfc2616.txt";
    OkHttpExample example = new OkHttpExample();

    example.get(url);
    System.out.println(example.cache.getHitCount());
    example.get(url);
    System.out.println(example.cache.getHitCount());
  }
}
