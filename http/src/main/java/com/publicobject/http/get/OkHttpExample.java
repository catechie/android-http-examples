package com.publicobject.http.get;

import com.squareup.okhttp.OkHttpClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OkHttpExample {
  final OkHttpClient okHttpClient = new OkHttpClient();

  public void get(String url) throws IOException {
    HttpURLConnection connection = okHttpClient.open(new URL(url));
    printResponse(connection.getInputStream());
  }

  private void printResponse(InputStream in) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
    for (String line; (line = reader.readLine()) != null; ) {
      System.out.println(line);
    }
  }

  public static void main(String[] args) throws IOException {
    String url = "http://localhost:8910/rfc2616.txt";
    new OkHttpExample().get(url);
  }
}
